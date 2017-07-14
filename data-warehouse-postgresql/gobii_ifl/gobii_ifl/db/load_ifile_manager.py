#!/usr/bin/env python
from __future__ import print_function

from connection_manager import ConnectionManager
from foreign_data_manager import ForeignDataManager

class LoadIfileManager:
	WORK_MEM = 10240

	def __init__(self, connectionStr):
		self.connMgr = ConnectionManager()
		self.conn = self.connMgr.connectToDatabase(connectionStr)
		self.cur = self.conn.cursor()
		self.fdm = ForeignDataManager()
		self.cur.execute("set work_mem to %s", (self.WORK_MEM,))
		#print("Load IFile Manager Initialized.")

	def dropForeignTable(self, fdwTableName):
		self.cur.execute("drop foreign table if exists "+fdwTableName+";")
		#print("drop foreign table if exists "+fdwTableName+";")

	def createForeignTable(self, iFile, fTableName):
		header, fdwScript = self.fdm.generateFDWScript(iFile, fTableName)
		self.cur.execute(fdwScript)
		return header

	def createFileWithoutDuplicatesV1(self, outputFilePath, noDupsSql):
		copyStmt = "copy ("+noDupsSql+") to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(copyStmt)

	def createFileWithoutDuplicates(self, outputFilePath, noDupsSql):
		copyStmt = "copy ("+noDupsSql+") to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			#let's try 20MB buffer size for a start, default was 8MB
			self.cur.copy_expert(copyStmt, outputFile, 20480)
		outputFile.close()

	def loadDataV1(self, tableName, header, fileToLoad, primaryKeyColumnName):
		loadSql = "copy "+tableName+" ("+(",".join(header))+")"+" from '"+fileToLoad+"' with delimiter E'\\t' csv header;"
		#print("loadSql = "+loadSql)
		self.cur.execute(loadSql)
		self.updateSerialSequence(tableName, primaryKeyColumnName)

	def loadData(self, tableName, header, fileToLoad, primaryKeyColumnName):
		loadSql = "copy "+tableName+" ("+(",".join(header))+")"+" from STDIN with delimiter E'\\t' csv header;"
		rowsLoaded = 0
		with open(fileToLoad, 'r') as f:
			self.cur.copy_expert(loadSql, f, 20480)
			rowsLoaded = self.cur.rowcount
			#print("Rows loaded = %s" % self.cur.rowcount)
		f.close()
		self.updateSerialSequence(tableName, primaryKeyColumnName)
		return rowsLoaded

	def upsertKVPFromForeignTable(self, fTableName, sourceKey, sourceValue, targetTable, targetId, targetJsonb):
		kvpSql = "select * from upsertKVPFromForeignTable('"+fTableName.lower()+"', '"+sourceKey+"', '"+sourceValue+"', '"+targetTable+"', '"+targetId+"', '"+targetJsonb+"');"
		#print ("kvpSQL: %s" % kvpSql)
		self.cur.execute(kvpSql)
		rowsLoaded = self.cur.fetchone()
		if rowsLoaded is not None:
			return rowsLoaded[0]
		else:
			return rowsLoaded

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
