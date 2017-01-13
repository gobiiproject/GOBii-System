#!/usr/bin/env python
from __future__ import print_function
import string
import random
import sys
import subprocess

class IFLUtility:
	"""
	This class provides general and common methods for all IFL classes or scripts.
	"""
	@staticmethod
	def generateRandomString(length):
		"""
		This function generates a random alphanumeric string given of a given length.
		:args: length - the length of the random string to generate
		:returns: A connection object - This class also stores it as an instance variable for your convenience.
		"""
		chars = string.ascii_uppercase + string.digits
		return ''.join(random.choice(chars) for _ in range(length))

	@staticmethod
	def printError(*args, **kwargs):
		print(*args, file=sys.stderr, **kwargs)

	@staticmethod
	def getFileLineCount(fname):
		p = subprocess.Popen(['wc', '-l', fname], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
		result, err = p.communicate()
		if p.returncode != 0:
			raise IOError(err)
		return int(result.strip().split()[0])
