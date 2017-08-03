#!/usr/bin/env python
from __future__ import print_function

from connection_manager import ConnectionManager
from foreign_data_manager import ForeignDataManager

class ExtractMetadataManager:

	def __init__(self, connectionStr):
		self.connMgr = ConnectionManager()
		self.conn = self.connMgr.connectToDatabase(connectionStr)
		self.cur = self.conn.cursor()
		self.fdm = ForeignDataManager()
		#print("Preprocess IFile Manager Initialized.")

	def dropForeignTable(self, fdwTableName):
		self.cur.execute("drop foreign table if exists "+fdwTableName)

	def createForeignTable(self, iFile, fTableName):
		header, fdwScript = self.fdm.generateFDWScript(iFile, fTableName)
		self.cur.execute(fdwScript)
		return header

	def createAllMarkerMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getAllMarkerMetadataByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def createMarkerNamesFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getMarkerNamesByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		self.cur.execute(sql)

	def createMinimalMarkerMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getMinimalMarkerMetadataByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def createAllSampleMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getAllSampleMetadataByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def createDnarunNamesFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getDnarunNamesByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def createMinimalSampleMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getMinimalSampleMetadataByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def createAllProjectMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getAllProjectMetadataByDataset("+datasetId+")) to '"+outputFilePath+"' with delimiter E'\\t'"+" csv header;"
		#print("copyStmt = "+copyStmt)
		self.cur.execute(sql)

	def commitTransaction(self):
		self.conn.commit()

	def rollbackTransaction(self):
		self.conn.rollback()

	def closeConnection(self):
		self.connMgr.disconnectFromDatabase()
