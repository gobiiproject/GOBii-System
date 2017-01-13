#!/usr/bin/env python
'''
	This script extracts sample metadata given a dataset ID.
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
		print("Getting sample metadata for dataset with ID: %s" % datasetId)
		print("Output File: ", outputFile)
	exMgr = ExtractMetadataManager(connectionStr)
	try:
		#exMgr.createAllSampleMetadataFile(outputFile, datasetId)
		''' as per Liz, apparently we need all the sample meta and so the minimum = all, but I'm keeping this here in case it changes.'''
		if allMeta:
			exMgr.createAllSampleMetadataFile(outputFile, datasetId)
		elif namesOnly:
			exMgr.createDnarunNamesFile(outputFile, datasetId)
		else:
			#exMgr.createMinimalSampleMetadataFile(outputFile, datasetId)
			exMgr.createAllSampleMetadataFile(outputFile, datasetId)
		exMgr.commitTransaction()
		exMgr.closeConnection()
		#print("Created full sample metadata file successfully.")
		if allMeta:
			print("Created full sample metadata file successfully.")
		elif namesOnly:
			print("Created DNARun names file successfully.")
		else:
			#print("Created minimal sample metadata file successfully.")
			print("Created full sample metadata file successfully.")
		return outputFile
	except Exception as e:
		MDEUtility.printError('Failed to create sample metadata file. Error: %s' % (str(e)))
		exMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)

if __name__ == "__main__":
	if len(sys.argv) < 5:
		print("Please supply the parameters. \nUsage: extract_sample_metadata <db_connection_string> <dataset_id> <output_file_abs_path> <all_meta> <names_only>")
		sys.exit()
	main(True, str(sys.argv[1]), str(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]), str(sys.argv[5]))
