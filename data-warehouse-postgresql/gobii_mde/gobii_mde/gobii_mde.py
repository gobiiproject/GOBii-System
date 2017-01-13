#!/usr/bin/env python

from __future__ import print_function
import sys
import getopt
import extract_marker_metadata
import extract_sample_metadata
import extract_project_metadata
from util.mde_utility import MDEUtility

def main(argv):
		verbose = False
		connectionStr = ""
		markerOutputFile = ""
		sampleOutputFile = ""
		datasetId = ""
		projectOutputFile = ""
		allMeta = False
		namesOnly = False
		#print("Args count: ", len(argv))
		try:
			opts, args = getopt.getopt(argv, "hc:m:s:d:p:avn", ["connectionString=", "markerOutputFile=", "sampleOutputFile=", "datasetId=", "projectOutputFile=", "all", "verbose", "namesonly"])
			#print (opts, args)
			if len(args) < 2 and len(opts) < 2:
				printUsageHelp()
		except getopt.GetoptError:
			printUsageHelp()
			sys.exit(2)
		for opt, arg in opts:
			if opt == '-h':
				printUsageHelp()
			elif opt in ("-c", "--connectionString"):
				connectionStr = arg
			elif opt in ("-m", "--markerOutputFile"):
				markerOutputFile = arg
			elif opt in ("-s", "--sampleOutputFile"):
				sampleOutputFile = arg
			elif opt in ("-d", "--datasetId"):
				datasetId = arg
			elif opt in ("-p", "--projectOutputFile"):
				projectOutputFile = arg
			elif opt in ("-a", "--all"):
				allMeta = True
			elif opt in ("-v", "--verbose"):
				verbose = True
			elif opt in ("-n", "--namesonly"):
				namesOnly = True

		#if verbose:
		#print("Opts: ", opts)
		rn = False
		if datasetId.isdigit():
			if connectionStr != "" and markerOutputFile != "":
				try:
					if verbose:
						print("Generating marker metadata file...")
					extract_marker_metadata.main(verbose, connectionStr, datasetId, markerOutputFile, allMeta, namesOnly)
				except Exception as e1:
					MDEUtility.printError("Error: %s" % (str(e1)))
				rn = True
			if connectionStr != "" and sampleOutputFile != "":
				try:
					if verbose:
						print("Generating sample metadata file...")
					extract_sample_metadata.main(verbose, connectionStr, datasetId, sampleOutputFile, allMeta, namesOnly)
				except Exception as e:
					MDEUtility.printError("Error: %s" % str(e))
				rn = True
			if connectionStr != "" and projectOutputFile != "":
				try:
					if verbose:
						print("Generating project metadata file...")
					extract_project_metadata.main(verbose, connectionStr, datasetId, projectOutputFile, allMeta)
				except Exception as e:
					MDEUtility.printError("Error: %s" % str(e))
				rn = True
			if not rn:
				print("At least one of -m, -s, or -p is required for the extractor to run.")
				printUsageHelp()
		else:
			MDEUtility.printError("The supplied dataset ID is not valid.")
		#cleanup

def checkDataIntegrity(iFile, pFile, verbose):
	iCount = MDEUtility.getFileLineCount(iFile)
	pCount = MDEUtility.getFileLineCount(pFile)
	#print ("Input file line count: %i" % iCount)
	#print ("Ppd file line count: %i" % pCount)
	if iCount == pCount:
		return True
	else:
		if verbose:
			print ("Mismatch: input_file=%s preprocessed_file=%s" % (iCount, pCount))
		return False

def printUsageHelp():
	print ("gobii_mde.py -c <connectionString> -m <markerOutputFile> -s <sampleOutputFile> -p <projectOutputFile> -d <dataset_id> -a -v -n")
	print ("\t-h = Usage help")
	print ("\t-c or --connectionString = Database connection string (RFC 3986 URI).\n\t\tFormat: postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...]")
	print ("\t-m or --markerOutputFile = The marker metadata output file. This should be an absolute path.")
	print ("\t-s or --sampleOutputFile = The sample metadata output file. This should be an absolute path.")
	print ("\t-p or --projectOutputFile = The project metadata output file. This should be an absolute path.")
	print ("\t-d or --datasetId = The dataset ID of which marker metadata will be extracted from. This should be a valid integer ID.")
	print ("\t-a or --all = Get all metadata information available, regardless if they are relevant to HMP, Flapjack, etc. formats.")
	print ("\t-v or --verbose = Print the status of the MDE in more detail.")
	print ("\t-n or --namesonly = Generate only names metadata. This flag is ignored if -a / --all is set.")
	sys.exit()

if __name__ == "__main__":
	main(sys.argv[1:])
