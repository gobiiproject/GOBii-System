#!/usr/bin/env python
import psycopg2
import monetdb.sql

#connection for Postgres db. Not currently used.

class PostgresDBManager:

	def __init__(self,db_user,db_pass,db_name,db_host=None,db_port=None):
		if db_host is not None:		
			self.db_host = db_host
		else:	
			self.db_host = "localhost"
		self.db_port = db_port
		self.db_user = db_user
		self.db_pass = db_pass
		self.db_name = db_name

		print "Postgres Manager Initialized."

	def connectToDatabase(self):
		self.conn = psycopg2.connect(database=self.db_name, user=self.db_user, password=self.db_pass, host=self.db_host, port=self.db_port)
		self.cur = self.conn.cursor()
		return self.conn

#using the python monetdb lib 11.19.3.2
# consolidate with db package
class MonetDBManager:

	def __init__(self,db_user,db_pass,db_name,db_host=None,db_port=None):
		if db_host is not None:		
			self.db_host = db_host
		else:	
			self.db_host = "localhost"
		
		if db_port is not None:		
			self.db_port = db_port
		else:	
			self.db_port = "50000"

	
		self.db_user = db_user
		self.db_pass = db_pass
		self.db_name = db_name

		print "MonetDB Manager params set."

	def connectToDatabase(self):
		
		self.conn = monetdb.sql.connect(username=self.db_user, password=self.db_pass, hostname=self.db_host, database=self.db_name,autocommit=False)		
		self.cur = self.conn.cursor()
		return self.conn

