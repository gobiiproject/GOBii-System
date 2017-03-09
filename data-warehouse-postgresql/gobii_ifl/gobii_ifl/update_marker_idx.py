#!/usr/bin/env python
'''
	This script updates dataset_marker.marker_idx column in case it wasn't populated in initial load.
	But note that, ideally, that column should be populated via IFL on initial load. So this script
	should not really be used on production systems. This was written for the purpose of fixing the test
	data which was loaded before such columns existed.

	@author kdp44 Kevin Palis
'''
from __future__ import print_function
import sys
import csv
import traceback
from db.update_h5i_manager import UpdateH5iManager
from util.ifl_utility import IFLUtility

def main(isVerbose, connectionStr, iFile, datasetId):

	updateH5iMgr = UpdateH5iManager(connectionStr)
	idx = 0
	try:
		with open(iFile, 'r') as f1:
			reader = csv.reader(f1, delimiter='\t')
			reader.next()
			for marker_id, marker_name, platform_id in reader:
				#if IS_VERBOSE:
				#	print("Updating: %s" % marker_name)
				updateH5iMgr.updateDatasetMarkerH5Index(datasetId, marker_id, idx)
				idx += 1
			updateH5iMgr.commitTransaction()
			updateH5iMgr.closeConnection()
			print("Updated HDF5 marker indices successfully.")
		f1.close()
	except Exception as e:
		IFLUtility.printError('Failed to load %s. Error: %s' % (iFile, str(e)))
		updateH5iMgr.rollbackTransaction()
		traceback.print_exc(file=sys.stderr)

if __name__ == "__main__":
	if len(sys.argv) < 4:
		print("Please supply the parameters. \nUsage: update_marker_idx <db_connection_str> <list_of_markers_file> <dataset_id>")
		sys.exit()
	connectionStr = str(sys.argv[1])
	iFile = str(sys.argv[2])
	#dupMappingFile = str(sys.argv[2])
	datasetId = str(sys.argv[3])
	main(True, connectionStr, iFile, datasetId)
