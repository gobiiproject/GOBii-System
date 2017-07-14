#!/usr/bin/env python
'''
	This module offers the following functionalities:
		1. Extraction by Dataset
		2. Extraction by Markers
		3. Extraction by Samples

	Exit Codes: 2-9
'''
from __future__ import print_function
import sys
import getopt
import extract_marker_metadata
import extract_sample_metadata
import extract_project_metadata
from util.mde_utility import MDEUtility

def main(argv):
		#TODO: Create a constant class when there's time, probably post-V1
		EXTRACTION_TYPES = [1, 2, 3]
		SAMPLE_TYPES = [1, 2, 3]

		verbose = False
		connectionStr = ""
		markerOutputFile = ""
		sampleOutputFile = ""
		datasetId = -1
		projectOutputFile = ""
		allMeta = False
		namesOnly = False
		mapId = -1
		includeChrLen = False
		displayMap = -1
		markerListFile = ""
		sampleListFile = ""
		mapsetOutputFile = ""
		markerNamesFile = ""
		sampleNamesFile = ""
		datasetType = -1
		platformList = []
		piId = -1
		projectId = -1
		#1 = By dataset, 2 = By Markers, 3 = By Samples
		extractionType = -1
		#1 = Germplasm Names, 2 = External Codes, 3 = DnaSample Names
		sampleType = -1
		exitCode = 0
		#PARSE PARAMETERS/ARGUMENTS
		try:
			opts, args = getopt.getopt(argv, "hc:m:s:d:p:avnM:lD:x:y:b:X:P:t:Y:", ["connectionString=", "markerOutputFile=", "sampleOutputFile=", "datasetId=", "projectOutputFile=", "all", "verbose", "namesOnly", "map=", "includeChrLen", "displayMap=", "markerList=", "sampleList=", "mapsetOutputFile=", "extractByMarkers", "markerNames=", "platformList=", "datasetType=", "extractByDataset", "piId=", "projectId=", "sampleType=", "sampleNames=", "extractBySamples"])
			#print (opts, args)
			if len(args) < 2 and len(opts) < 2:
				printUsageHelp(2)
		except getopt.GetoptError as e:
			MDEUtility.printError("Error parsing parameters: %s" % str(e))
			printUsageHelp(9)
		for opt, arg in opts:
			if opt == '-h':
				printUsageHelp(1)
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
			elif opt in ("-n", "--namesOnly"):
				namesOnly = True
			elif opt in ("-M", "--map"):
				mapId = arg
			elif opt in ("-l", "--includeChrLen"):
				includeChrLen = True
			elif opt in ("-D", "--displayMap"):
				displayMap = arg
			elif opt in ("-x", "--markerList"):
				markerListFile = arg
			elif opt in ("-y", "--sampleList"):
				sampleListFile = arg
			elif opt in ("-b", "--mapsetOutputFile"):
				mapsetOutputFile = arg
			elif opt in ("--extractBySamples"):
				extractionType = 3
			elif opt in ("--extractByMarkers"):
				extractionType = 2
			elif opt in ("--extractByDataset"):
				extractionType = 1
			elif opt in ("-X", "--markerNames"):
				markerNamesFile = arg
			elif opt in ("-P", "--platformList"):
				try:
					platformList = arg.split(",")
				except Exception as e:
					MDEUtility.printError("Invalid platformList format. Only comma-delimited ID list is accepted. Error: %s" % str(e))
					exitCode = 6
					sys.exit(exitCode)
			elif opt in ("t", "--datasetType"):
				datasetType = arg
			elif opt in ("--piId"):
				piId = arg
			elif opt in ("--projectId"):
				projectId = arg
			elif opt in ("--sampleType"):
				sampleType = int(arg)
			elif opt in ("-Y", "--sampleNames"):
				sampleNamesFile = arg

		#VALIDATIONS
		if connectionStr == "" or markerOutputFile == "" or sampleOutputFile == "":
			MDEUtility.printError("Invalid usage. All of the following parameters are required: connectionStr, markerOutputFile, and sampleOutputFile.")
			printUsageHelp(2)
		if extractionType not in EXTRACTION_TYPES:
			MDEUtility.printError("Invalid usage. Invalid extraction type.")
			printUsageHelp(2)
		if extractionType == 1:
			if datasetId < 1:
				MDEUtility.printError("Invalid usage. Extraction by dataset requires a dataset ID.")
				printUsageHelp(6)
		elif extractionType == 2:
			if datasetType < 1 or (markerNamesFile == "" and platformList == ""):
				MDEUtility.printError("Invalid usage. Extraction by marker list requires a dataset type and at least one of: markerNamesFile and platformList.")
				printUsageHelp(6)
		elif extractionType == 3:
			if datasetType < 1:
				MDEUtility.printError("Invalid usage. Extraction by samples list requires a dataset type.")
				printUsageHelp(8)
			if piId < 1 and projectId < 1 and sampleNamesFile == "":
				MDEUtility.printError("Invalid usage. Extraction by samples list requires at least one of the following: PI, Project, Samples List.")
				printUsageHelp(8)
			if sampleNamesFile != "" and sampleType not in SAMPLE_TYPES:
				MDEUtility.printError("Invalid usage. Providing a sample names list requires a sample type: 1 = Germplasm Names, 2 = External Codes, 3 = DnaSample Names.")
				printUsageHelp(8)
		if verbose:
			print("Opts: ", opts)
		markerList = []
		sampleList = []
		markerNames = []
		sampleNames = []
		#PREPARE PARAMETERS
		#convert file contents to lists
		if markerListFile != "":
				markerList = [line.strip() for line in open(markerListFile, 'r')]
		if sampleListFile != "":
				sampleList = [line.strip() for line in open(sampleListFile, 'r')]
		if markerNamesFile != "":
				markerNames = [line.strip() for line in open(markerNamesFile, 'r')]
		if sampleNamesFile != "":
				sampleNames = [line.strip() for line in open(sampleNamesFile, 'r')]

		#Do the Dew
		#rn = False
		#if connectionStr != "" and markerOutputFile != "":
		try:
			#if verbose:
			#	print("Generating marker metadata file...")
			mFile, markerList, sampleList = extract_marker_metadata.main(verbose, connectionStr, datasetId, markerOutputFile, allMeta, namesOnly, mapId, includeChrLen, displayMap, markerList, sampleList, mapsetOutputFile, extractionType, datasetType, markerNames, platformList, piId, projectId, sampleType, sampleNames)
			if extractionType == 2 and not markerList:
				MDEUtility.printError("Resulting list of marker IDs is empty. Nothing to extract.")
				sys.exit(7)
		except Exception as e1:
			MDEUtility.printError("Extraction of marker metadata failed. Error: %s" % (str(e1)))
			exitCode = 3
		#rn = True
		#if connectionStr != "" and sampleOutputFile != "":
		try:
			#if verbose:
			#	print("Generating sample metadata file...")
			extract_sample_metadata.main(verbose, connectionStr, datasetId, sampleOutputFile, allMeta, namesOnly, markerList, sampleList, extractionType, datasetType)
		except Exception as e:
			MDEUtility.printError("Extraction of sample metadata failed. Error: %s" % str(e))
			exitCode = 4
		#rn = True
		if projectOutputFile != "":
			try:
				#project metadata is only relevant to extraction by datasetID ---- OR IS IT? GOTTA ASK PEOPLE IF WE WANT THIS FANCY
				if extractionType == 1:
					if verbose:
						print("Generating project metadata file...")
					extract_project_metadata.main(verbose, connectionStr, datasetId, projectOutputFile, allMeta)
			except Exception as e:
				MDEUtility.printError("Error: %s" % str(e))
				exitCode = 5
			#rn = True
		#if not rn:
		#	print("At least one of -m, -s, or -p is required for the extractor to run.")
		#	printUsageHelp(2)

		sys.exit(exitCode)
		#cleanup

def printUsageHelp(eCode):
	print ("gobii_mde.py --extractByDataset -c <connectionString> -m <markerOutputFile> -s <sampleOutputFile> -p <projectOutputFile> -d <dataset_id> -D <mapset_id>")
	print ("\t-h = Usage help")
	print ("\t-c or --connectionString = Database connection string (RFC 3986 URI).\n\t\tFormat: postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...]")
	print ("\t--extractByDataset = Instructs the MDE to do extraction by dataset ID.")
	print ("\t--extractByMarkers = Instructs the MDE to do extraction by marker list.")
	print ("\t--extractBySamples = Instructs the MDE to do extraction by samples list.")
	print ("\t-m or --markerOutputFile = The marker metadata output file. This should be an absolute path.")
	print ("\t-s or --sampleOutputFile = The sample metadata output file. This should be an absolute path.")
	print ("\t-b or --mapsetOutputFile = The mapset metadata output file. This should be an absolute path.")
	print ("\t-p or --projectOutputFile = The project metadata output file. This should be an absolute path.")
	print ("\t-d or --datasetId = The dataset ID of which marker metadata will be extracted from. This should be a valid integer ID.")
	print ("\t-M or --map = This FILTERS the result by mapset, ie. get only the markers in the specified mapset. This is useful if a dataset contains markers that belongs to multiple maps.")
	print ("\t-l or --includeChrLen = Generates a file that lists all the chromosomes (or any linkage groups) that appear on the markers list, along with their lengths. Filename is the same as the marker file but appended with .chr.")
	print ("\t-D or --displayMap = This creates two files: one with the marker metadata appended with the map data of the selected mapset (marker_filename.ext) and a mapset metadata file. The mapset metadata filename is fetched from the value passed to  the -b (--mapsetOutputFile) parameter.")
	print ("\t-x or --markerList = Supplies the file containing a list of marker_ids, newline-delimited.")
	print ("\t-y or --sampleList = Supplies the file containing a list of dnarun_ids, newline-delimited.")
	print ("\t-X or --markerNames = Supplies the file containing a list of marker names, newline-delimited.")
	print ("\t-Y or --sampleNames = Supplies the file containing a list of sample names, newline-delimited. Sample names can be any of the following: germplasm_name, external_code, or dnasample_name. The type is set by --sampleType")
	print ("\t--datasetType = Filters the data by the type of dataset. This should be a valid dataset type ID, otherwise no results will be returned. This is only used for --extractionByMarkers and --extractionBySamples")
	print ("\t--platformList = Comma-delimited string of platform IDs to filter --extractionByMarkers.")
	print ("\t--sampleType = Tells the MDE what kind of sample names are in the file passed to --sampleNames. Valid values: 1 = germplasm_name, 2 = external_code, 3 = dnasample_name.")
	print ("\t--projectId = Filters by project for extraction by samples. This can also be used independently to pull all samples in a given project.")
	print ("\t--piId = Filters by PI contact for extraction by samples. This can also be used independently to pull all samples under a given PI.")
	print ("\t-v or --verbose = Print the status of the MDE in more detail.")
	#---------------------------
	print ("\nDEPRECATED:")
	print("\t-a or --all = With the QC fields being way more than the number of fields that were initially being generated by this option, this got deprecated.")
	print ("\t-n or --namesOnly = This was originally requested to link postgres and monet. But this is not being used and the business logic of the MDEs changed significantly, leaving this behind.")
	sys.exit(eCode)


if __name__ == "__main__":
	main(sys.argv[1:])
