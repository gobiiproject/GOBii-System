#!/usr/bin/env python
from __future__ import print_function

from connection_manager import ConnectionManager
from foreign_data_manager import ForeignDataManager

class LoadIfileManager:

	def __init__(self, connectionStr):
		self.connMgr = ConnectionManager()
		self.conn = self.connMgr.connectToDatabase(connectionStr)
		self.cur = self.conn.cursor()
		self.fdm = ForeignDataManager()
		#print("Load IFile Manager Initialized.")

	def dropForeignTable(self, fdwTableName):
		self.cur.execute("drop foreign table if exists "+fdwTableName+";")
		#print("drop foreign table if exists "+fdwTableName+";")

	def createForeignTable(self, iFile, fTableName):
		header, fdwScript = self.fdm.generateFDWScript(iFile, fTableName)
		self.cur.execute(fdwScript)
		return header

	def createFileWithDerivedIds(self, outputFilePath, derivedIdSql):
		copyStmt = "copy ("+derivedIdSql+") to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(copyStmt)

	def createFileWithoutDuplicates(self, outputFilePath, noDupsSql):
		copyStmt = "copy ("+noDupsSql+") to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(copyStmt)

	def loadData(self, tableName, header, fileToLoad, primaryKeyColumnName):
		loadSql = "copy "+tableName+" ("+(",".join(header))+")"+" from '"+fileToLoad+"' with delimiter E'\\t' csv header;"
		#print("loadSql = "+loadSql)
		self.updateSerialSequence(tableName, primaryKeyColumnName)
		self.cur.execute(loadSql)

	def updateSerialSequence(self, tableName, primaryKeyColumnName):
		updateSeqSql = "SELECT pg_catalog.setval(pg_get_serial_sequence('"+tableName+"', '"+primaryKeyColumnName+"'), MAX("+primaryKeyColumnName+")) FROM "+tableName+";"
		#print("updateSeqSql = "+updateSeqSql)
		self.cur.execute(updateSeqSql)

	def commitTransaction(self):
		self.conn.commit()

	def rollbackTransaction(self):
		self.conn.rollback()

	def closeConnection(self):
		self.connMgr.disconnectFromDatabase()
