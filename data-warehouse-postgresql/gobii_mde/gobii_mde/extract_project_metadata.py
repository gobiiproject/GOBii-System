#!/usr/bin/env python
'''
	This script extracts project metadata given a dataset ID.
	Prerequisites:

	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import traceback
from util.mde_utility import MDEUtility
from db.extract_metadata_manager import ExtractMetadataManager

def main(isVerbose, connectionStr, datasetId, outputFile, allMeta):
	if isVerbose:
		print("Getting project metadata for dataset with ID: %s" % datasetId)
		print("Output File: ", outputFile)
	exMgr = ExtractMetadataManager(connectionStr)
	try:
		exMgr.createAllProjectMetadataFile(outputFile, datasetId)
		''' as per Liz, apparently we need all the project meta and so the minimum = all, but I'm keeping this here in case it changes.
		if allMeta:
			exMgr.createAllSampleMetadataFile(outputFile, datasetId)
		else:
			exMgr.createMinimalSampleMetadataFile(outputFile, datasetId)
		'''
		exMgr.commitTransaction()
		exMgr.closeConnection()
		print("Created full project metadata file successfully.")
		'''
		if allMeta:
			print("Created full project metadata file successfully.")
		else:
			print("Created minimal project metadata file successfully.")
		'''
		return outputFile
	except Exception as e:
		MDEUtility.printError('Failed to create project metadata file. Error: %s' % (str(e)))
		exMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)
		sys.exit(8)
if __name__ == "__main__":
	if len(sys.argv) < 5:
		print("Please supply the parameters. \nUsage: extract_project_metadata <db_connection_string> <dataset_id> <output_file_abs_path> <all_meta>")
		sys.exit(1)
	main(True, str(sys.argv[1]), str(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]))
