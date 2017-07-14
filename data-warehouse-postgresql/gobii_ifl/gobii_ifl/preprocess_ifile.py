#!/usr/bin/env python
'''
	This script preprocesses intermediate file (Digester output) to convert names/text columns to their
	corresponding database table IDs. This is done with the help of a mapping file (ex. marker.nmap) which
	details the name mapping. For example:

	FILE_COL_TO_MATCH 	COL_ALIAS 		TABLE_NAME 		TABLE_COL_TO_MATCH 	ID_COLUMN 			TABLE_ALIAS
	-------------------------------------------------------------------------------------------------------------
	species_name				species_id		cv						term								cv_id						cv1
	type_name						type_id				cv						term								cv_id						cv2

	This tells the preprocess script that for column 'species_name', find its ID in the database table 'cv'
	using the criteria: species_name = cv.term column. Then in the result file, change the column name to the
	col_alias 'species_id' for it to map directly to the species_id column of the germplasm table. The table_alias
	value is necessary in cases when the table name repeats - ie. another column 'type_id' maps to the same table 'cv'.
	For other cases, you don't need to specify this, but will need to leave a placeholder (trailing tab).

	Another example:

	reference_name			reference_id	reference			name								reference_id
	strand_name					strand_id			cv						term								cv_id

	Which means, for the 'strand_name' column in the file, find its ID in the database table 'cv'
	using the criteria: strand_name = cv.term column. Then in the result file, change the column name to the col_alias
	which is 'strand_id' for it to map directly to the strand_id column of the marker table.

	More information: http://cbsugobii05.tc.cornell.edu:6084/display/TD/PostgreSQL+IFL
	Prerequisites:
	ERROR Codes: 20 series
	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import csv
import traceback
import itertools
from os.path import basename
from os.path import splitext
from pkg_resources import resource_stream
from db.preprocess_ifile_manager import PreprocessIfileManager
from util.ifl_utility import IFLUtility

def main(isVerbose, connectionStr, iFile, outputPath):
	IS_VERBOSE = isVerbose
	SUFFIX_LEN = 8
	PROPS_COL_NAME = 'props'
	PROPERTY_ID = 'property_id'
	PROPERTY_VALUE = 'property_value'
	DEBUG_LEVEL = 0
	#if IS_VERBOSE:
	#print("arguments: %s" % str(sys.argv))

	outputFile = outputPath+"ppd_"+basename(iFile)
	longPropFilename = outputPath+"long_"+basename(iFile)
	exitCode = 0
	isKVP = False
	isProp = False
	#print("splitext: ", splitext(basename(iFile)))
	tableName = splitext(basename(iFile))[1][1:]
	randomStr = IFLUtility.generateRandomString(SUFFIX_LEN)
	fTableName = "f_" + tableName + "_" + randomStr
	if IS_VERBOSE:
		print("Table Name:", tableName)
		print("Output File: ", outputFile)
		print("Getting information from mapping file: ", tableName+'.nmap')
		#print(resource_listdir('res.map', ''))
		#print(resource_string('res.map', tableName+'.nmap'))
	isProp = tableName.endswith('_prop')
	nameMappingFile = resource_stream('res.map', tableName+'.nmap')
	kvpMapFile = resource_stream('res.map', 'kvp.map')
	kvpReader = csv.reader(kvpMapFile, delimiter='\t')
	kvpTablesList = [i[0] for i in kvpReader]
	kvpMapFile.seek(0)
	#print ("kvpTablesList: %s" % kvpTablesList)
	if tableName in kvpTablesList:
		isKVP = True
	if IS_VERBOSE and isKVP:
		print ("Detected a KVP file...")

	if isProp:
		#format the file to LONG
		if IS_VERBOSE:
			print ("Detected a property file. Converting file to long-format...")
		with open(iFile, 'r') as propFile:
			with open(longPropFilename, 'w') as longPropFile:
				propReader = csv.reader(propFile, delimiter='\t')
				propWriter = csv.writer(longPropFile, delimiter='\t')
				atHeader = True
				propColPos = 0
				for row in propReader:
					longRow = []
					if atHeader:
						try:
							propColPos = row.index(PROPS_COL_NAME)
							if IS_VERBOSE and DEBUG_LEVEL > 0:
								print ("Got column position of property at %s" % propColPos)
							#write header of long-format file as: <all other columns> property_id property_value
							for col in row:
									if col != PROPS_COL_NAME:
										longRow.append(col)
							longRow.append(PROPERTY_ID)
							longRow.append(PROPERTY_VALUE)
							propWriter.writerow(longRow)
							atHeader = False
						except Exception as e:
							IFLUtility.printError('\nFailed to preprocess %s. No property column in file. Error: %s' % (iFile, str(e)))
							exitCode = 20
							traceback.print_exc(file=sys.stderr)
							return outputFile, exitCode
					else:
						#print ("Parsing...%s" % row[propColPos])
						try:
							for idx, col in enumerate(row):
								if idx != propColPos:
									longRow.append(col)
							propKVPs = row[propColPos].split(",")
							for propKVP in propKVPs:
								parsedRow = []
								prop = propKVP.split(":")
								if len(prop) < 2:
									if IS_VERBOSE:
										print ("Property %s is not a KVP. Skipping..." % prop)
										continue
								parsedRow.append(prop[0])
								parsedRow.append(prop[1])
								propWriter.writerow(longRow+parsedRow)
						except Exception as e:
							IFLUtility.printError('\nFailed to preprocess %s. Cannot parse properties. Error: %s' % (iFile, str(e)))
							exitCode = 21
							traceback.print_exc(file=sys.stderr)
							return outputFile, exitCode
	#instantiating this initializes a database connection
	ppMgr = PreprocessIfileManager(connectionStr)

	ppMgr.dropForeignTable(fTableName)
	header = ""
	if isProp:
		header = ppMgr.createForeignTable(longPropFilename, fTableName)
	else:
		header = ppMgr.createForeignTable(iFile, fTableName)
	ppMgr.commitTransaction()
	if IS_VERBOSE:
		print("Foreign table %s created and populated." % fTableName)
	selectStr = ""
	conditionStr = ""
	fromStr = fTableName
	#gets a list of column names for the table we're loading data to
	targetTableColumnList = [i[0] for i in ppMgr.getColumnListOfTable(tableName)]
	if IS_VERBOSE and not isKVP:
		print("Got targetTableColumnList = %s" % targetTableColumnList)
	try:
		reader = csv.reader(nameMappingFile, delimiter='\t')
		mappedColList = [i[0].split(",")[0] for i in reader]
		nameMappingFile.seek(0)
		#if IS_VERBOSE:
		#	print("mappedColList: %s" % mappedColList)
		if not isKVP:
			for fColumn in header:
				if tableName == "mh5i" or tableName == "sh5i":
					if selectStr == "":
						selectStr += fTableName+"."+fColumn
					else:
						selectStr += ", "+fTableName+"."+fColumn
				else:
					#only include the column in the select statement IF it is a column of the target table OR it is a column that will be converted (ex. marker_name -> marker_id)
					if fColumn in targetTableColumnList or fColumn in mappedColList:
						if selectStr == "":
							selectStr += fTableName+"."+fColumn
						else:
							selectStr += ", "+fTableName+"."+fColumn
		else:
			#selectStr = fTableName+".*" -- was initially thinking of just this, but changed my mind for a more consistent pattern. Keeping this here til I'm done.
			for fColumn in header:
				#For KVP files, include all columns
				if selectStr == "":
					selectStr += fTableName+"."+fColumn
				else:
					selectStr += ", "+fTableName+"."+fColumn
		#print("Current selectStr=%s" % selectStr)
		for row in reader:
			#if IS_VERBOSE:
			#	print("Processing column(s): FILECOLS: %s | TABLECOLS: %s" % (file_column_names, table_columns))
			#provide a commenting mechanism so mapping files can be more readable
			if row[0].startswith("#"):
				continue
			file_column_names, column_alias, table_name, table_columns, id_column, table_alias = row
			fileColumns = file_column_names.split(",")
			tableColumns = table_columns.split(",")
			mainFileCol = ""
			#print("File Columns: %s \nTable Columns: %s" % (fileColumns, tableColumns))
			for fileCol, tableCol in itertools.izip(fileColumns, tableColumns):
				if IS_VERBOSE:
					print("\nProcessing column mapping file.%s = %s.%s" % (fileCol, table_name, tableCol))
				if fileCol not in header:
					if IS_VERBOSE:
						print("Column is not present in input file. Skipping...")
					continue
				if mainFileCol == "":
					mainFileCol = fileCol
				colType = ppMgr.getTypeOfColumn(table_name, tableCol)
				cond = table_name+"."+tableCol+"="+fTableName+"."+fileCol
				if colType != 'text':
					cond += "::"+colType
				if(conditionStr == ""):
					conditionStr += cond
				else:
					conditionStr += " and "+cond
			#print ("\nmainFileCol=%s" % mainFileCol)
			if mainFileCol != "":
				#if IS_VERBOSE:
				#	print("Current selectStr=%s \n %s will be replaced by %s" % (selectStr, fTableName+"."+mainFileCol, table_name+"."+id_column+" as "+column_alias))
				selectStr = selectStr.replace(fTableName+"."+mainFileCol, table_name+"."+id_column+" as "+column_alias)
				if table_alias is not None and table_alias.strip() != '':
					selectStr = selectStr.replace(table_name+".", table_alias+".")
					fromStr += ", "+table_name+" as "+table_alias
					conditionStr = conditionStr.replace(table_name+".", table_alias+".")
				else:
					fromStr += ", "+table_name
		nameMappingFile.close()
		deriveIdSql = "select "+selectStr+" from "+fromStr
		if conditionStr.strip() != "":
			deriveIdSql += " where "+conditionStr
		if IS_VERBOSE:
			print ("\nderiveIdSql: %s \n" % deriveIdSql)
		ppMgr.createFileWithDerivedIds(outputFile, deriveIdSql)
		ppMgr.dropForeignTable(fTableName)
		ppMgr.commitTransaction()
		ppMgr.closeConnection()
		if IS_VERBOSE:
			print("\nPreprocessed %s successfully.\n" % iFile)
		return outputFile, exitCode
	except Exception as e:
		IFLUtility.printError('\nFailed to preprocess %s. Error: %s' % (iFile, str(e)))
		ppMgr.rollbackTransaction()
		exitCode = 22
		traceback.print_exc(file=sys.stderr)
		return outputFile, exitCode


if __name__ == "__main__":
	if len(sys.argv) < 4:
		print("Please supply the parameters. \nUsage: preprocess_ifile <db_connection_string> <intermediate_file> <output_directory_path>")
		sys.exit(1)
	connectionStr = str(sys.argv[1])
	iFile = str(sys.argv[2])
	outputPath = str(sys.argv[3])
	main(True, connectionStr, iFile, outputPath)
