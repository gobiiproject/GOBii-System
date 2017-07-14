#!/usr/bin/env python
'''
	Dedup the preprocessed file ( generated from nmap resource) based on the column entries in dupmap resource.
	This dedup is against the data within the preprocessed file. The duplicates against the database is checked while loading records into the table.
	@author venice
'''
from __future__ import print_function
import sys
import csv
import traceback
import itertools
import pandas as pd
from os.path import basename
from os.path import splitext
from pkg_resources import resource_stream
from util.ifl_utility import IFLUtility

def main(isVerbose,preprocessedFile,outputPath, tableName):
	IS_VERBOSE =isVerbose
	
	outputFile = outputPath+"ddp_"+basename(preprocessedFile)
	longPreProcFile = outputPath+"long_"+basename(preprocessedFile)
	exitCode = 0
	if IS_VERBOSE:
		print("PreprocessedFile: ", preprocessedFile)
		print("Table Name: ", tableName)
		print("Output File: ", outputFile)
		print("Getting info from dupmap file: ", tableName+'.dupmap')
	dupMapFile = resource_stream('res.map',tableName+'.dupmap')
	## get columns from dupmap
	dreader = csv.reader(dupMapFile,delimiter='\t')
	dupMapColList = [i[0].split(",")[0] for i in dreader if not i[0].startswith("#")]
	
	## read preprocessed file 
	## create pandas Data Frame
	data = pd.read_table(preprocessedFile)

	for col in dupMapColList:
		if col in data.columns:
			if IS_VERBOSE:
				print("Column %s in preprocessed file." % col)
			continue
		else:
                        ## exit if one of cols specified is not in preprocessed file
                	IFLUtility.printError('\nFailed to deduplicate %s. \nColumn %s does not exist in %s.' % (preprocessedFile,col,preprocessedFile))
                        exitCode = 21
                        traceback.print_exc(file=sys.stderr)
			return outputFile, exitCode

	data = data.drop_duplicates(subset=dupMapColList, keep='first')	
	data.to_csv(outputFile,sep='\t', line_terminator='\n',index=False)	
	if IS_VERBOSE:
		print('Preprocessed file %s with %i rows.' % (preprocessedFile,IFLUtility.getFileLineCount(preprocessedFile)))
		print('Deduplicated output written to: %s with %i rows.' % (outputFile,IFLUtility.getFileLineCount(outputFile)) ) 


	return outputFile, exitCode

if __name__ == "__main__":
	#if len(sys.argv) < 3:
	#	print("Please supply the parameters. \nUsage: deduplicate_ifile <preprocessed_file> <output_directory_path> <tableName>")
	#	sys.exit(1)
	try:
		preprocessedFile = str(sys.argv[1])
		outputPath = str(sys.argv[2])
		tableName = str(sys.argv[3])
		main(True, preprocessedFile, outputPath,tableName)
	except IOError as err:
		print("Cannot open %s" % sys.argv[1])
		sys.exit(1)
	except IndexError:
		print("\nPlease supply the correct parameters. \nUsage: deduplicate_ifile <preprocessed_file> <output_directory_path> <tableName>\n")
		sys.exit(1)
		
