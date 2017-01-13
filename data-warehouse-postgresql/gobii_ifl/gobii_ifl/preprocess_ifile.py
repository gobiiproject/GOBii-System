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

	Row 2 basically says, for the 'strand_name' column in the file, find its ID in the database table 'cv'
	using the criteria: strand_name = cv.term column. Then in the result file, change the column name to the col_alias
	which is 'strand_id' for it to map directly to the strand_id column of the marker table.

	Prerequisites:

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

	#if IS_VERBOSE:
	#print("arguments: %s" % str(sys.argv))

	outputFile = outputPath+"ppd_"+basename(iFile)

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

	nameMappingFile = resource_stream('res.map', tableName+'.nmap')
	#sys.exit()
	#instantiating this initializes a database connection
	ppMgr = PreprocessIfileManager(connectionStr)

	ppMgr.dropForeignTable(fTableName)
	header = ppMgr.createForeignTable(iFile, fTableName)
	ppMgr.commitTransaction()
	if IS_VERBOSE:
		print("Foreign table %s created and populated." % fTableName)
	selectStr = ""
	conditionStr = ""
	fromStr = fTableName
	targetTableColumnList = [i[0] for i in ppMgr.getColumnListOfTable(tableName)]
	if IS_VERBOSE:
		print("Got targetTableColumnList = %s" % targetTableColumnList)
	try:
		reader = csv.reader(nameMappingFile, delimiter='\t')
		mappedColList = [i[0].split(",")[0] for i in reader]
		nameMappingFile.seek(0)
		if IS_VERBOSE:
			print("mappedColList: %s" % mappedColList)
		for fColumn in header:
			if tableName == "mh5i" or tableName == "sh5i":
				if selectStr == "":
					selectStr += fTableName+"."+fColumn
				else:
					selectStr += ", "+fTableName+"."+fColumn
			else:
				if fColumn in targetTableColumnList or fColumn in mappedColList:
					if selectStr == "":
						selectStr += fTableName+"."+fColumn
					else:
						selectStr += ", "+fTableName+"."+fColumn
			#print("Current selectStr=%s" % selectStr)
		for file_column_names, column_alias, table_name, table_columns, id_column, table_alias in reader:
			#if IS_VERBOSE:
			#	print("Processing column(s): FILECOLS: %s | TABLECOLS: %s" % (file_column_names, table_columns))
			fileColumns = file_column_names.split(",")
			tableColumns = table_columns.split(",")
			mainFileCol = ""
			#print("File Columns: %s \nTable Columns: %s" % (fileColumns, tableColumns))
			for fileCol, tableCol in itertools.izip(fileColumns, tableColumns):
				if IS_VERBOSE:
					print("Processing column mapping %s = %s" % (fileCol, tableCol))
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
			if mainFileCol != "":
				#print("Current selectStr=%s will be replaced: %s BY %s" % (selectStr, fTableName+"."+mainFileCol, table_name+"."+id_column+" as "+column_alias))
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
			print ("deriveIdSql: "+deriveIdSql)
		ppMgr.createFileWithDerivedIds(outputFile, deriveIdSql)
		ppMgr.dropForeignTable(fTableName)
		ppMgr.commitTransaction()
		ppMgr.closeConnection()
		print("Preprocessed %s successfully." % iFile)
		return outputFile
	except Exception as e:
		IFLUtility.printError('Failed to preprocess %s. Error: %s' % (iFile, str(e)))
		ppMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)

if __name__ == "__main__":
	if len(sys.argv) < 4:
		print("Please supply the parameters. \nUsage: preprocess_ifile <db_connection_string> <intermediate_file> <output_directory_path>")
		sys.exit(1)
	connectionStr = str(sys.argv[1])
	iFile = str(sys.argv[2])
	outputPath = str(sys.argv[3])
	main(True, connectionStr, iFile, outputPath)
