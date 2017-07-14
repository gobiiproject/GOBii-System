#!/usr/bin/env python
from __future__ import print_function
import csv

class ForeignDataManager:
	"""
	This class handles the FDW and creation of SQL commands for external data
	"""
	def __init__(self):
		self.delim = "\t"
		self.fdwServer = "idatafilesrvr"

	def generateFDWScript(self, inputFile, fdwTableName):
		"""
		for the given fileName:
			- generate the fdw table script
			- generate the bulk insert script
		"""
		fdwOptions = " SERVER %s OPTIONS (FILENAME '%s', format 'csv', delimiter E'%s', header 'true', NULL '\N');" % (self.fdwServer, inputFile, self.delim)
		header = self.getHeader(inputFile)
		#print("header: ", header)
		numOfcol = len(header)
		fdwScript = 'CREATE FOREIGN TABLE '+fdwTableName+' ( '
		for i, column in enumerate(header):
			if i + 1 == numOfcol:
				fdwScript += column + " text "
			else:
				fdwScript += column + " text, "
		fdwScript += ') ' + fdwOptions
		#print("fdwScript: ", fdwScript)
		return header, fdwScript

	def getHeader(self, inputFile):
		'''
		for the given data fileName:
		- get the header ( column names)
		'''
		dataFile = open(inputFile, 'r')
		reader = csv.reader(dataFile, delimiter=self.delim)
		header = reader.next()
		dataFile.close()
		return header
