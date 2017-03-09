#!/usr/bin/env python
'''
	This script extracts sample metadata given a dataset ID.
	Prerequisites:
	Exit codes: 20-29
	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import traceback
from util.mde_utility import MDEUtility
from db.extract_metadata_manager import ExtractMetadataManager

def main(isVerbose, connectionStr, datasetId, outputFile, allMeta, namesOnly, markerList, sampleList):
	if isVerbose:
		print("Sample Metadata Output File: ", outputFile)
	exMgr = ExtractMetadataManager(connectionStr)
	try:
		if allMeta:
			exMgr.createAllSampleMetadataFile(outputFile, datasetId)
		elif namesOnly:
			exMgr.createDnarunNamesFile(outputFile, datasetId)
		else:
			if markerList:
				if isVerbose:
					print("Generating sample metadata by marker list.")
				exMgr.createSampleQCMetadataByMarkerList(outputFile, markerList)
			elif sampleList:
				if isVerbose:
					print("Generating sample metadata by sample list.")
					print("!!!Not yet implemented. Skipping...")
			else:
				if isVerbose:
					print("Generating sample metadata by datasetID.")
				exMgr.createSampleQCMetadataFile(outputFile, datasetId)
		exMgr.commitTransaction()
		exMgr.closeConnection()
		''' These don't make sense anymore. Requirements keep changing, I may need to reorganize.
		if allMeta:
			print("Created full sample metadata file successfully.")
		elif namesOnly:
			print("Created DNARun names file successfully.")
		else:
			#print("Created minimal sample metadata file successfully.")
			print("Created full sample metadata file successfully.")
		'''
		if isVerbose:
			print("Created sample metadata file successfully.")
		return outputFile
	except Exception as e:
		MDEUtility.printError('Failed to create sample metadata file. Error: %s' % (str(e)))
		exMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)
		sys.exit(20)

if __name__ == "__main__":
	if len(sys.argv) < 5:
		print("Please supply the parameters. \nUsage: extract_sample_metadata <db_connection_string> <dataset_id> <output_file_abs_path> <all_meta> <names_only> <markerList> <sampleList>")
		sys.exit(1)
	main(True, str(sys.argv[1]), str(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]), str(sys.argv[5]), str(sys.argv[6]), str(sys.argv[7]))
