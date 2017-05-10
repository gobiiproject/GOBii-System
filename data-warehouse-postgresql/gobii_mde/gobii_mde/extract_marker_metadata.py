#!/usr/bin/env python
'''
	This script extracts marker metadata given a dataset ID.
	Prerequisites:
	Exit Codes: 10-19
	@author kdp44 Kevin Palis

	Extraction types:
	#1 = By dataset, 2 = By Markers, 3 = By Samples

	TODO: Extraction types to constants across IFLs
'''
from __future__ import print_function
import sys
import csv
import traceback
from util.mde_utility import MDEUtility
from db.extract_metadata_manager import ExtractMetadataManager

def main(isVerbose, connectionStr, datasetId, outputFile, allMeta, namesOnly, mapId, includeChrLen, displayMapId, markerList, sampleList, mapsetOutputFile, extractionType, datasetType, markerNames, platformList, piId, projectId, sampleType, sampleNames):
	MAPID_COL_POS = 2
	MARKERNAME_COL_POS_1 = 0
	MARKERNAME_COL_POS_2 = 0
	if isVerbose:
		print("Marker Metadata Output File: ", outputFile)
	exMgr = ExtractMetadataManager(connectionStr)
	try:
		if allMeta:  # deprecated
			exMgr.createAllMarkerMetadataFile(outputFile, datasetId, mapId)
		elif namesOnly:  # deprecated
			exMgr.createMarkerNamesFile(outputFile, datasetId, mapId)
		else:
			if extractionType == 1:  # by dataset
				if isVerbose:
					print("Generating marker metadata by dataset.")
				exMgr.createQCMarkerMetadataFile(outputFile, datasetId, mapId)
			elif extractionType == 2:  # by markers
				if isVerbose:
					print("Generating marker metadata by marker list.")
				if not markerList:
					if isVerbose:
						print("Deriving marker IDs based on the given parameters: markerNames, platformList.")
						#get the marker ids list
					res = exMgr.getMarkerIds(markerNames, platformList)
					if res is None:
						MDEUtility.printError('MarkerNames and PlatformList cannot be both empty.')
						sys.exit(13)
					markerList = [str(i[0]) for i in res]
					if not markerList:
						MDEUtility.printError("Resulting list of marker IDs is empty. Nothing to extract.")
						sys.exit(15)
				exMgr.createQCMarkerMetadataByMarkerList(outputFile, markerList)
				if datasetType is None:
					MDEUtility.printError('Dataset type is required for extraction by marker list.')
					sys.exit(14)
				exMgr.createMarkerPositionsFile(outputFile, markerList, datasetType)  # this generates the pos file - will get affected by the inroduction of filtering by dataset type
			elif extractionType == 3:  # by samples
				if isVerbose:
					print("Generating marker metadata by sample list.")
				if not sampleList:
					if isVerbose:
						print("Deriving dnarun IDs based on the given parameters: piId, projectId, sampleNames + sampleType.")
						#piId, projectId, sampleType, sampleNames
					res = exMgr.getDnarunIds(piId, projectId, sampleType, sampleNames)
					if res is None:
						MDEUtility.printError('No Dnarun IDs fetched. Possible invalid usage. Check your criteria.')
						sys.exit(13)
					sampleList = [str(i[0]) for i in res]
					if not sampleList:
						MDEUtility.printError("Resulting list of Dnarun IDs is empty. Nothing to extract.")
						sys.exit(15)
					if isVerbose:
						print("Deriving marker IDs based on Dnarun IDs (using the dataset_dnarun_idx route).")
					#get the marker ids list based on the derived samples
					res2 = exMgr.getMarkerIdsFromSamples(sampleList, datasetType, platformList)
					if res2 is None:
						MDEUtility.printError('No Marker IDs fetched. Possible invalid usage. Check your criteria.')
						sys.exit(13)
					markerList = [str(i[0]) for i in res2]
					if not markerList:
						MDEUtility.printError("Resulting list of marker IDs is empty. Nothing to extract.")
						sys.exit(15)
				exMgr.createQCMarkerMetadataByMarkerList(outputFile, markerList)
				if datasetType is None:
					MDEUtility.printError('Dataset type is required for extraction by sample list.')
					sys.exit(14)
				exMgr.createMarkerPositionsFile(outputFile, markerList, datasetType)  # this generates the marker.pos file
			else:
				MDEUtility.printError('ERROR: Extraction type is required.')
				sys.exit(12)

		if displayMapId != -1:
			if mapsetOutputFile == '':
				MDEUtility.printError('ERROR: Mapset output file path is not set.')
				sys.exit(11)
			else:
				exMgr.createMapsetFile(mapsetOutputFile, datasetId, displayMapId, markerList, sampleList, extractionType)
			#integrating the mapset info with the marker metadata file:
			#Open marker meta file (markerMeta) and mapset meta file (mapsetMeta) and another file for writing.
			#Scan mapsetMeta for the displayMapId. Stop at the first instance found. These files are ordered accordingly, which saves the algorithm a lot of processing time.
			#For the first row found with the displayMapId, look for the row in markerMeta where markerMeta.marker_name=mapsetMeta.marker_name and append all columns of mapsetMeta to that row of markerMeta.
			#Iterate through the next rows until mapsetMeta.mapset_id!=displayMapId or eof
			with open(mapsetOutputFile, 'r') as mapsetMeta:
				with open(outputFile, 'r') as markerMeta:
					with open(outputFile+'.ext', 'w') as markerMetaExt:
						mapsetReader = csv.reader(mapsetMeta, delimiter='\t')
						markerReader = csv.reader(markerMeta, delimiter='\t')
						markerWriter = csv.writer(markerMetaExt, delimiter='\t')
						headerRow = next(markerReader) + next(mapsetReader)[MAPID_COL_POS+1:]
						markerWriter.writerow(headerRow)
						mapsetRowNum = 0
						mapsetRow = None
						mapsetRowsList = list(mapsetReader)
						totalMapsetRows = len(mapsetRowsList) - 1  # subtract the header
						foundMapId = False
						if isVerbose:
							print("Total mapset rows: %s" % totalMapsetRows)
						for mapsetRow in mapsetReader:
								mapsetRowNum += 1
								if mapsetRow[MAPID_COL_POS] == displayMapId:
									foundMapId = True
									if isVerbose:
										print ('Integrating map data to marker meta file. Found mapId at row %s.' % mapsetRowNum)
									break
						columnsCount = len(mapsetRow[MAPID_COL_POS+1:])
						fillerList = ['' for x in range(columnsCount)]
						if isVerbose:
							print('Mapset Row currently at marker_name=%s' % mapsetRow[MARKERNAME_COL_POS_1])
							print('Total number of columns to append: %s' % columnsCount)
						eomReached = False
						if mapsetRowNum >= totalMapsetRows and not foundMapId:
							eomReached = True
						for markerRow in markerReader:
							if eomReached:
								newRow = markerRow + fillerList
								markerWriter.writerow(newRow)
								continue
							if markerRow[MARKERNAME_COL_POS_2] == mapsetRow[MARKERNAME_COL_POS_1]:
								newRow = markerRow + mapsetRow[MAPID_COL_POS+1:]
								markerWriter.writerow(newRow)
								try:
									mapsetRow = next(mapsetReader)
								except StopIteration as e:
									if isVerbose:
										print ('End of file reached.')
									eomReached = True
									#break
								if mapsetRow[MAPID_COL_POS] != displayMapId:
									eomReached = True
									#break
							else:
								newRow = markerRow + fillerList
								markerWriter.writerow(newRow)
		if includeChrLen:
					exMgr.createChrLenFile(outputFile, datasetId, mapId, markerList, sampleList)
		exMgr.commitTransaction()
		exMgr.closeConnection()
		''' These things don't make sense anymore
		if allMeta:
			print("Created full marker metadata file successfully.")
		elif namesOnly:
			print("Created marker names file successfully.")
		else:
			print("Created minimal marker metadata file successfully.")
		'''
		if isVerbose:
			print("Created marker metadata file successfully.")
		return outputFile, markerList, sampleList
	except Exception as e:
		MDEUtility.printError('Failed to create marker metadata file. Error: %s' % (str(e)))
		exMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)
		sys.exit(10)


#extractionType, datasetType, markerNames, platformList
if __name__ == "__main__":
	if len(sys.argv) < 15:
		print("Please supply the parameters. \nUsage: extract_marker_metadata <db_connection_string> <dataset_id> <output_file_abs_path> <all_meta> <names_only:boolean> <map_id> <includeChrLen:boolean> <displayMapId> <markerList> <sampleList> <mapsetOutputFile> <extractionType> <datasetType> <markerNames> <platformList> <piId> <projectId> <sampleType> <sampleNames>")
		sys.exit(1)
	main(True, str(sys.argv[1]), str(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]), str(sys.argv[5]), str(sys.argv[6]), str(sys.argv[7]), str(sys.argv[8]), str(sys.argv[9]), str(sys.argv[10]), str(sys.argv[11]), str(sys.argv[12]), str(sys.argv[13]), str(sys.argv[14]), str(sys.argv[15]), str(sys.argv[16]), str(sys.argv[17]), str(sys.argv[18]), str(sys.argv[19]))
