#!/usr/bin/env python
'''
	This script extracts marker metadata given a dataset ID.
	Prerequisites:

	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import traceback
from util.mde_utility import MDEUtility
from db.extract_metadata_manager import ExtractMetadataManager

def main(isVerbose, connectionStr, datasetId, outputFile, allMeta, namesOnly):
	if isVerbose:
		print("Getting marker metadata for dataset with ID: %s" % datasetId)
		print("Output File: ", outputFile)
	exMgr = ExtractMetadataManager(connectionStr)
	try:
		if allMeta:
			exMgr.createAllMarkerMetadataFile(outputFile, datasetId)
		elif namesOnly:
			exMgr.createMarkerNamesFile(outputFile, datasetId)
		else:
			exMgr.createMinimalMarkerMetadataFile(outputFile, datasetId)
		exMgr.commitTransaction()
		exMgr.closeConnection()
		if allMeta:
			print("Created full marker metadata file successfully.")
		elif namesOnly:
			print("Created marker names file successfully.")
		else:
			print("Created minimal marker metadata file successfully.")
		return outputFile
	except Exception as e:
		MDEUtility.printError('Failed to create marker metadata file. Error: %s' % (str(e)))
		exMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)

if __name__ == "__main__":
	if len(sys.argv) < 5:
		print("Please supply the parameters. \nUsage: extract_marker_metadata <db_connection_string> <dataset_id> <output_file_abs_path> <all_meta> <names_only>")
		sys.exit()
	main(True, str(sys.argv[1]), str(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]), str(sys.argv[5]))
