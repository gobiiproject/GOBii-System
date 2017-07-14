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

	def dropForeignTable(self, fdwTableName):
		self.cur.execute("drop foreign table if exists "+fdwTableName)

	def createForeignTable(self, iFile, fTableName):
		header, fdwScript = self.fdm.generateFDWScript(iFile, fTableName)
		self.cur.execute(fdwScript)
		return header

	def createFileWithoutDuplicates(self, outputFilePath, noDupsSql):
		copyStmt = "copy ("+noDupsSql+") to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(copyStmt, outputFile, 20480)
		outputFile.close()

	def createAllMarkerMetadataFile(self, outputFilePath, datasetId, mapId):
		sql = ""
		if mapId == -1:
			sql = "copy (select * from getAllMarkerMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		else:
			sql = "copy (select * from getAllMarkerMetadataByDatasetAndMap("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		#print (sql)
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createMarkerNamesFile(self, outputFilePath, datasetId, mapId):
		sql = ""
		if mapId == -1:
			sql = "copy (select * from getMarkerNamesByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		else:
			sql = "copy (select * from getMarkerNamesByDatasetAndMap("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createMinimalMarkerMetadataFile(self, outputFilePath, datasetId, mapId):
		sql = ""
		if mapId == -1:
			sql = "copy (select * from getMinimalMarkerMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		else:
			sql = "copy (select * from getMinimalMarkerMetadataByDatasetAndMap("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createQCMarkerMetadataFile(self, outputFilePath, datasetId, mapId):
		sql = "copy (select * from getMarkerQCMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		#TODO: in case we offer the feature to filter by mapId, we'll need to create a version of getMarkerQCMetadataByDataset that filters by mapId as well. And add the conditional expressions as in createAllMarkerMetadataFile
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createQCMarkerMetadataByMarkerList(self, outputFilePath, markerList):
		sql = "copy (select * from getMarkerQCMetadataByMarkerList('{"+(','.join(markerList))+"}')) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createChrLenFile(self, outputFilePath, datasetId, mapId, markerList, sampleList):
		sql = ""
		outputFilePath = outputFilePath+".chr"
		if markerList:
			sql = "copy (select * from getAllChrLenByMarkerList('{"+(','.join(markerList))+"}')) to STDOUT with delimiter E'\\t'"+" csv header;"
		elif sampleList:
			print("Not yet implemented")
			#with the decision on doing an extraction by marker list in the background of an extraction by samples request, this is now an unreachable condition. Code cleanup pending.
			return
		else:
			if mapId == -1:
				sql = "copy (select * from getAllChrLenByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
			else:
				sql = "copy (select * from getAllChrLenByDatasetAndMap("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createAllSampleMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getAllSampleMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createDnarunNamesFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getDnarunNamesByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createMinimalSampleMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getMinimalSampleMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createAllProjectMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getAllProjectMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def getMarkerMapsetInfoByDataset(self, outputFilePath, datasetId, mapId):
		#rename this function
		'''
			For the given datasetId & mapId this funtion would output all markers in dataset and  only the given mapset info.
		'''
		outputFilePath = outputFilePath+".mapset"
		sql = "copy (select * from getMarkerMapsetInfoByDataset("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createSampleQCMetadataFile(self, outputFilePath, datasetId):
		sql = "copy (select * from getSampleQCMetadataByDataset("+datasetId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createSampleQCMetadataByMarkerList(self, outputFilePath, markerList, datasetType):
		#print(self.cur.mogrify("copy (select * from getSampleQCMetadataByMarkerList('{"+(','.join(markerList))+"}',"+datasetType+")) to STDOUT with delimiter E'\\t'"+" csv header;"))
		sql = "copy (select * from getSampleQCMetadataByMarkerList('{"+(','.join(markerList))+"}',"+datasetType+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createSampleQCMetadataBySampleList(self, outputFilePath, sampleList, datasetType):
		sql = "copy (select * from getSampleQCMetadataBySampleList('{"+(','.join(sampleList))+"}',"+datasetType+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def getMarkerAllMapsetInfoByDataset(self, outputFilePath, datasetId, mapId):
		'''
			For the given datasetId & mapId this funtion would output all markers in dataset and mapsets associated with them.Markers with multiple mapsets will be repeated.
		'''
		outputFilePath = outputFilePath+".mapset"
		sql = "copy (select * from getMarkerAllMapsetInfoByDataset("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createMapsetFile(self, outputFilePath, datasetId, mapId, markerList, sampleList, extractionType):
		#outputFilePath = outputFilePath+".mapset"
		sql = ""
		if extractionType == 2 or extractionType == 3:
			sql = "copy (select * from getMarkerMapsetInfoByMarkerList('{"+(','.join(markerList))+"}')) to STDOUT with delimiter E'\\t'"+" csv header;"
		elif extractionType == 1:
			sql = "copy (select * from getMarkerAllMapsetInfoByDataset("+datasetId+","+mapId+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createMarkerPositionsFile(self, outputFilePath, markerList, datasetType):
		outputFilePath = outputFilePath+".pos"
		sql = "copy (select * from getMatrixPosOfMarkers('{"+(','.join(markerList))+"}',"+datasetType+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def createSamplePositionsFile(self, outputFilePath, sampleList, datasetType):
		outputFilePath = outputFilePath+".pos"
		sql = "copy (select * from getMatrixPosOfSamples('{"+(','.join(sampleList))+"}',"+datasetType+")) to STDOUT with delimiter E'\\t'"+" csv header;"
		with open(outputFilePath, 'w') as outputFile:
			self.cur.copy_expert(sql, outputFile, 20480)
		outputFile.close()

	def getMarkerIds(self, markerNames, platformList):
		#print("Generating marker ids...")
		if markerNames and platformList:
			#print(self.cur.mogrify("select marker_id from getMarkerIdsByMarkerNamesAndPlatformList(%s, %s)", ("{"+(','.join(markerNames))+"}", "{"+(','.join(platformList))+"}")))
			self.cur.execute("select marker_id from getMarkerIdsByMarkerNamesAndPlatformList(%s, %s)", ("{"+(','.join(markerNames))+"}", "{"+(','.join(platformList))+"}"))
		elif markerNames and not platformList:
			#print(self.cur.mogrify("select marker_id from getMarkerIdsByMarkerNames(%s)", ("{"+(','.join(markerNames))+"}",)))
			self.cur.execute("select marker_id from getMarkerIdsByMarkerNames(%s)", ("{"+(','.join(markerNames))+"}",))
		elif platformList and not markerNames:
			#print(self.cur.mogrify("select marker_id from getMarkerIdsByPlatformList(%s)", ("{"+(','.join(platformList))+"}",)))
			self.cur.execute("select marker_id from getMarkerIdsByPlatformList(%s)", ("{"+(','.join(platformList))+"}",))
		else:  # both params are null
			return None
		res = self.cur.fetchall()
		return res

	def getDnarunIds(self, piId, projectId, sampleType, sampleNames):
		print("Deriving Dnarun IDs...")
		if sampleNames and sampleType > 0:
			#1 = Germplasm Names, 2 = External Codes, 3 = DnaSample Names
			if sampleType == 1:
				if projectId > 0:
					print("...based on Germplasm Names AND projectID")
					self.cur.execute("select dnarun_id from getDnarunIdsByGermplasmNamesAndProject(%s, %s)", ("{"+(','.join(sampleNames))+"}", projectId))
				elif piId > 0:
					print("...based on Germplasm Names AND piId")
					self.cur.execute("select dnarun_id from getDnarunIdsByGermplasmNamesAndPI(%s, %s)", ("{"+(','.join(sampleNames))+"}", piId))
				else:
					print("...based on Germplasm Names")
					self.cur.execute("select dnarun_id from getDnarunIdsByGermplasmNames(%s)", ("{"+(','.join(sampleNames))+"}",))
			elif sampleType == 2:
				if projectId > 0:
					print("...based on External Codes AND projectID")
					self.cur.execute("select dnarun_id from getDnarunIdsByExternalCodesAndProject(%s, %s)", ("{"+(','.join(sampleNames))+"}", projectId))
				elif piId > 0:
					print("...based on External Codes AND piId")
					self.cur.execute("select dnarun_id from getDnarunIdsByExternalCodesAndPI(%s, %s)", ("{"+(','.join(sampleNames))+"}", piId))
				else:
					print("...based on External Codes")
					self.cur.execute("select dnarun_id from getDnarunIdsByExternalCodes(%s)", ("{"+(','.join(sampleNames))+"}",))
			elif sampleType == 3:
				if projectId > 0:
					print("...based on Dnasample Names AND projectID")
					self.cur.execute("select dnarun_id from getDnarunIdsByDnasampleNamesAndProject(%s, %s)", ("{"+(','.join(sampleNames))+"}", projectId))
				elif piId > 0:
					print("...based on Dnasample Names AND piId")
					self.cur.execute("select dnarun_id from getDnarunIdsByDnasampleNamesAndPI(%s, %s)", ("{"+(','.join(sampleNames))+"}", piId))
				else:
					print("...based on Dnasample Names")
					#print(self.cur.mogrify("select dnarun_id from getDnarunIdsByDnasampleNames(%s)", ("{"+(','.join(sampleNames))+"}",)))
					self.cur.execute("select dnarun_id from getDnarunIdsByDnasampleNames(%s)", ("{"+(','.join(sampleNames))+"}",))
			else:
				print("Invalid usage.")
				return None
		elif projectId > 0:
			print("...based on projectID")
			self.cur.execute("select dnarun_id from getDnarunIdsByProject(%s)", (projectId,))
		elif piId > 0:
			print("...based on PI")
			self.cur.execute("select dnarun_id from getDnarunIdsByPI(%s)", (piId,))
		else:
			print("Invalid usage.")
			return None
		res = self.cur.fetchall()
		return res

	#datasetType = required, sampleList = required, platformList = optional
	def getMarkerIdsFromSamples(self, sampleList, datasetType, platformList):
		if sampleList and platformList:
			self.cur.execute("select marker_id from getMarkerIdsBySamplesPlatformsAndDatasetType(%s, %s, %s)", ("{"+(','.join(sampleList))+"}", "{"+(','.join(platformList))+"}", datasetType))
		elif sampleList and not platformList:
			self.cur.execute("select marker_id from getMarkerIdsBySamplesAndDatasetType(%s, %s)", ("{"+(','.join(sampleList))+"}", datasetType))
		else:  # invalid usage
			return None
		res = self.cur.fetchall()
		return res

	def commitTransaction(self):
		self.conn.commit()

	def rollbackTransaction(self):
		self.conn.rollback()

	def closeConnection(self):
		self.connMgr.disconnectFromDatabase()
