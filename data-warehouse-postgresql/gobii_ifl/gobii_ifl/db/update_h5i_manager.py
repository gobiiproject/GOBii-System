#!/usr/bin/env python
from __future__ import print_function

from connection_manager import ConnectionManager

class UpdateH5iManager:

	def __init__(self, connectionStr):
		self.connMgr = ConnectionManager()
		self.conn = self.connMgr.connectToDatabase(connectionStr)
		self.cur = self.conn.cursor()

	def updateDatasetMarkerH5Index(self, datasetId, markerId, idx):
		print ("Updating %s:%s:%s ..." % (datasetId, markerId, idx))
		updateStmt = "update dataset_marker set marker_idx=%s where dataset_id=%s and marker_id=%s;"
		self.cur.execute(updateStmt, (idx, datasetId, markerId))

	def updateDatasetDnarunH5Index(self, datasetId, sampleId, idx):
		print ("Updating %s:%s:%s ..." % (datasetId, sampleId, idx))
		updateStmt = "update dataset_dnarun set dnarun_idx=%s where dataset_id=%s and dnarun_id=%s;"
		self.cur.execute(updateStmt, (idx, datasetId, sampleId))

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
