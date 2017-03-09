#!/usr/bin/env python
from __future__ import print_function
import psycopg2

class ConnectionManager:
	"""
	This class manages the database connection and initializes connection and cursor objects.
	You'll have to manually change the connection credentials here.
	"""

	#def __init__(self):
	#print("Database Manager Initialized.")

	def connectToDatabase(self, connectionStr):
		"""
		Start a database connection using the connection string
		:args: connectionStr - this is a RFC 3986 URI (postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...])
		:returns: A connection object - This class also stores it as an instance variable for your convenience.
		"""
		#self.conn = psycopg2.connect(database=self.db_name, user=self.db_user, password=self.db_pass, host=self.db_host, port=self.db_port)
		#postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...]
		#self.conn = psycopg2.connect("postgresql://loaderusr:loaderusr@:5432/gobii_rice2")
		self.conn = psycopg2.connect(connectionStr)
		return self.conn

	def disconnectFromDatabase(self):
		"""
		End a database connection.
		"""
		self.conn.close()
