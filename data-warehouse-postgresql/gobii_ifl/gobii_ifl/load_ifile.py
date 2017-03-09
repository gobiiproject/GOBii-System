#!/usr/bin/env python
'''
	This script loads an intermediate file (Digester output) directly to its corresponding table in the GOBII schema.
	But before doing the actual bulk load, this will create a foreign table via the Foreign Data Wrapper and remove duplicates.
	The 'unique' rows are then piped to a file for the bulk loader
	to work on. Determining duplicates is done with the help of a mapping file (ex. marker.dupmap) which
	details the condition for a particular row to be a duplicate. For example, if marker.dupmap has:

	FILE_COLUMN_NAME 		TABLE_COLUMN_NAME 	COLUMN_DATA_TYPE
	-------------------------------------------------------------------
	name 								name 								text
	ref_allele					ref 								text

	This tells the script to use the following criteria for duplicates:
	If name column in file is equal to the value of marker.name AND ref_allele column in file is equal to the value of marker.ref
	column, then that row is a duplicate. The third column is the data type of the column to be compared, this basically just cast the column
	via ::<column_type>. The script will then NOT include that in the file for bulk loading. The .dupmap file
	can have an arbitrary number of criteria, just note that the comparison will always be an exact match.

	More information: http://cbsugobii05.tc.cornell.edu:6084/display/TD/PostgreSQL+IFL
	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import csv
import traceback
from os.path import basename
from os.path import splitext
from db.load_ifile_manager import LoadIfileManager
from pkg_resources import resource_stream
from util.ifl_utility import IFLUtility

def main(isVerbose, connectionStr, iFile, outputPath):
	IS_VERBOSE = isVerbose
	SUFFIX_LEN = 8

	#if IS_VERBOSE:
	#print("arguments: %s" % str(sys.argv))

	outputFile = outputPath+"nodups_"+basename(iFile)
	#print("splitext: ", splitext(basename(iFile)))
	tableName = splitext(basename(iFile))[1][1:]
	randomStr = IFLUtility.generateRandomString(SUFFIX_LEN)
	rowsLoaded = 0
	exitCode = 0
	isKVP = False

	fTableName = "ft_" + tableName + "_" + randomStr
	if IS_VERBOSE:
		print("Table Name:", tableName)
		print("Output File: ", outputFile)
		print("Getting information from mapping file: ", tableName+'.dupmap')
		#print(resource_listdir('res.map', ''))
		#print(resource_string('res.map', tableName+'.dupmap'))

	dupMappingFile = resource_stream('res.map', tableName+'.dupmap')
	kvpMapFile = resource_stream('res.map', 'kvp.map')
	kvpReader = csv.reader(kvpMapFile, delimiter='\t')
	kvpTablesList = [i[0] for i in kvpReader]
	kvpMapFile.seek(0)
	if tableName in kvpTablesList:
		isKVP = True
	if IS_VERBOSE and isKVP:
		print ("Detected a KVP file...")
	#instantiating this initializes a database connection
	loadMgr = LoadIfileManager(connectionStr)

	loadMgr.dropForeignTable(fTableName)
	header = loadMgr.createForeignTable(iFile, fTableName)
	loadMgr.commitTransaction()
	if IS_VERBOSE:
		print("Foreign table %s created and populated." % fTableName)
	selectStr = ""
	joinStr = ""
	fromStr = fTableName
	conditionStr = ""
	for fColumn in header:
		if selectStr == "":
			selectStr += fTableName+"."+fColumn
		else:
			selectStr += ", "+fTableName+"."+fColumn

	try:
		if not isKVP:
			reader = csv.reader(dupMappingFile, delimiter='\t')
			for row in reader:
				if row[0].startswith("#"):
					continue
				file_column_name, table_column_name, data_type = row
				if IS_VERBOSE:
					print("\nProcessing column: %s" % file_column_name)
				if(joinStr == ""):
					joinStr += fTableName+"."+file_column_name+"::"+data_type+"="+tableName+"."+table_column_name
				else:
					joinStr += " and "+fTableName+"."+file_column_name+"::"+data_type+"="+tableName+"."+table_column_name
				if(conditionStr == ""):
					conditionStr += tableName+"."+table_column_name+" is null"
				else:
					conditionStr += " and "+tableName+"."+table_column_name+" is null"
			dupMappingFile.close()
			joinSql = "select "+selectStr+" from "+fromStr+" left join "+tableName+" on "+joinStr+" where "+conditionStr
			if IS_VERBOSE:
				print ("\njoinSql: %s \n" % joinSql)
			#ppMgr.createFileWithDerivedIds(outputFile, deriveIdSql)
			loadMgr.createFileWithoutDuplicates(outputFile, joinSql)
			if IS_VERBOSE:
				print("Removed duplicates successfully.")
			#primary key column assumed to be tablename_id --> needs to be configurable(?) (would've been better if everything's just 'id' as usual!)
			rowsLoaded = loadMgr.loadData(tableName, header, outputFile, tableName+"_id")
		else:  # KVP file
			for kvpRow in kvpReader:
				if kvpRow[0].startswith("#") or kvpRow[0] != tableName:
					continue
				#print ("Found row: %s" % kvpRow)
				if IS_VERBOSE:
					print ("Upserting for %s..." % kvpRow[0])
				source_table, source_key_column, source_value_column, target_table, target_id_col, target_jsonb_col = kvpRow
				rowsLoaded = loadMgr.upsertKVPFromForeignTable(fTableName, source_key_column, source_value_column, target_table, target_id_col, target_jsonb_col)

		loadMgr.dropForeignTable(fTableName)
		loadMgr.commitTransaction()
		loadMgr.closeConnection()
		if IS_VERBOSE:
			print("Loaded %s successfully.\nRows loaded/upserted = %s" % (iFile, rowsLoaded))
		print(rowsLoaded)
		return outputFile, exitCode
	except Exception as e:
		IFLUtility.printError('Failed to load %s. Error: %s' % (iFile, str(e)))
		loadMgr.rollbackTransaction()
		exitCode = 6
		traceback.print_exc(file=sys.stderr)
		return outputFile, exitCode


if __name__ == "__main__":
	if len(sys.argv) < 4:
		print("Please supply the parameters. \nUsage: load_ifile <db_connection_str> <intermediate_file> <output_directory_path>")
		sys.exit(1)
	connectionStr = str(sys.argv[1])
	iFile = str(sys.argv[2])
	#dupMappingFile = str(sys.argv[2])
	outputPath = str(sys.argv[3])
	main(True, connectionStr, iFile, outputPath)
