#!/usr/bin/env python
from __future__ import print_function

from connection_manager import ConnectionManager
from foreign_data_manager import ForeignDataManager

class PreprocessIfileManager:
	WORK_MEM = 10240

	def __init__(self, connectionStr):
		self.connMgr = ConnectionManager()
		self.conn = self.connMgr.connectToDatabase(connectionStr)
		self.cur = self.conn.cursor()
		self.fdm = ForeignDataManager()
		self.cur.execute("set work_mem to %s", (self.WORK_MEM,))
		#print("Preprocess IFile Manager Initialized.")

	def getCvIdOfTerm(self, term):
		self.cur.execute("select cv_id from cv where lower(term)=%s", (term.lower(),))
		cv_id = self.cur.fetchone()
		if cv_id is not None:
			return cv_id[0]
		else:
			return cv_id

	def getCvIdOfGroupAndTerm(self, group, term):
		self.cur.execute("select cv_id from cv where lower(\"group\")=%s and lower(term)=%s", (group.lower(), term.lower()))
		cv_id = self.cur.fetchone()
		if cv_id is not None:
			return cv_id[0]
		else:
			return cv_id

	#select data_type from information_schema.columns where table_name='marker' and column_name='platform_id';
	def getTypeOfColumn(self, table, column):
		self.cur.execute("select data_type from information_schema.columns where table_name=%s and column_name=%s", (table, column))
		res = self.cur.fetchone()
		if res is not None:
			return res[0]
		else:
			return res

	def getColumnListOfTable(self, table):
		self.cur.execute("select column_name from information_schema.columns where table_name = %s", (table,))
		res = self.cur.fetchall()
		return res

	def dropForeignTable(self, fdwTableName):
		self.cur.execute("drop foreign table if exists "+fdwTableName)

	def createForeignTable(self, iFile, fTableName):
		header, fdwScript = self.fdm.generateFDWScript(iFile, fTableName)
		#print("fdwScript: %s" % fdwScript)
		self.cur.execute(fdwScript)
		return header

	def createFileWithDerivedIdsV1(self, outputFilePath, derivedIdSql):
		copyStmt = "copy ("+derivedIdSql+") to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(copyStmt)

	def createFileWithDerivedIds(self, outputFilePath, derivedIdSql):
		copyStmt = "copy ("+derivedIdSql+") to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			#let's try 20MB buffer size for a start, default was 8MB
			self.cur.copy_expert(copyStmt, outputFile, 20480)
		outputFile.close()

	def commitTransaction(self):
		self.conn.commit()

	def rollbackTransaction(self):
		self.conn.rollback()

	def closeConnection(self):
		self.connMgr.disconnectFromDatabase()
