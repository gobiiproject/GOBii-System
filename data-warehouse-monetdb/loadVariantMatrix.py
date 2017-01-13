#!/usr/bin/env python

'''
	This module loads the Intermediate Files  for variant calls into a MonetDB table.
	Table is named same as file/dataset name.
	If the same dataset is loaded, the existing table would be droped and reloaded.
	
	@author raza
'''
import sys
import subprocess
import csv
from os.path import basename
from os.path import splitext
from os.path import dirname
from os.path import join
from dbconnector import MonetDBManager


# reads the file and return the values as array
def getIDs(fileName,skipHeader):

	dataFile = csv.reader(open(fileName, 'r'))
	nameCol = []
	#skip header
	if skipHeader:		
		next(dataFile)
	for row in dataFile:
		#first col
		nameCol.append(row[0])
   	
	return nameCol

#creates the DDL for monet table 
def monetdbTableSQL(dnarunId,datasetName):

	ddlStmt = "CREATE TABLE "+datasetName+ " ( "
	'''
		changing to marker_name as the current MDE's output is marker name instead of marker id
	'''
#	ddlStmt+= "marker_id integer"
	ddlStmt+= "marker_name text"
	for dnarunid in dnarunId:	
		ddlStmt+= ",S_"+dnarunid+" text "  

	ddlStmt+= " )"
	return ddlStmt


#creates the copy DML for loading into monetdb
def monetdbCopyDML(dnarunId,tableName,variantFile):
	prefix = "S_"
	dmlStmt = "COPY INTO " + tableName
	dmlStmt += " FROM  '"+variantFile+"' USING DELIMITERS '\\t','\\n' "
	return dmlStmt

if len(sys.argv) < 10:
	print "Please supply the parameters. \nUsage:python loadVariantMatrix.py <DatasetName> <fileName.matrix> <fileName.marker_id> <fileName.dnarun_id> <hostname> <port> <dbuser> <dbpass> <dbname>" 
	print "Data files must have absolute path."		
	sys.exit(1)

#dataset name
datasetName = str(sys.argv[1]).upper()

#TODO error handling if no files present
variantFile = str(sys.argv[2])
markerFile = str(sys.argv[3])
dnarunFile = str(sys.argv[4])

# db connections
hostname = str(sys.argv[5])
portNumber = str(sys.argv[6])
dbUser = str(sys.argv[7])
dbPass = str(sys.argv[8])
dbName = str(sys.argv[9])




#rudimentry check
if(not variantFile.endswith(".matrix") or not markerFile.endswith(".marker_id") or not dnarunFile.endswith(".dnarun_id") ):
	print "Data Files are not correct."
	print "Data Files must have correct extensions. <fileName.matrix> <fileName.marker_id> <fileName.dnarun_id>"
	sys.exit(1)


#get dnaruns IDs
dnaruns = getIDs(dnarunFile,True)

fullName = join(dirname(variantFile),datasetName+".monetmp")

#print matrixFileName,fullName
print fullName

mddl = monetdbTableSQL(dnaruns,datasetName)


#system  call 
with open(fullName, "w") as matrixFile:
	subprocess.call(["paste", markerFile, variantFile],stdout=matrixFile)

mdml = monetdbCopyDML(dnaruns,datasetName,fullName)

print mddl
print mdml


#monetdb connection
mdMgr = MonetDBManager(dbUser,dbPass,dbName,hostname,portNumber)
mdCon = mdMgr.connectToDatabase()
mdCurs= mdCon.cursor()


try:

	#create monetDB table 
	#check if table already exist
	mdCurs.execute("select name from tables where upper(name) = '%s'" % datasetName)
	tableExist = mdCurs.fetchone()
	print tableExist
	#essentially reloading the dataset by droping the table and reloading
	if tableExist is not None:
		print "Droped existing table:", datasetName
		mdCurs.execute("Drop  table %s " % datasetName )

	mdCurs.execute(mddl)
		
	mdCurs.execute(mdml)
	mdCon.commit()
	print "All Done!!!"
except:
	print "MonetDB Unexpected error:", sys.exc_info()
	mdCon.rollback()


mdCon.close()

	
