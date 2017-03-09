#!/usr/bin/env python
'''
Tests/Steps:
1. Copy the data files (ie. codominant/data/*) to the target server's gobii_bundle file directory (ie. <bundle>/crops/<crop_name>/files/)
2. Modify the marker instruction file with the correct parameter values, then copy the file (ie. codominant/instruction/m_test.json) to the target server's gobii_bundle instruction dir (ie. <bundle>/crops/<cropname>/loader/instructions)
3. Wait for N minutes (where N=cron job interval)
4. Check the target server's gobii_bundle done directory (ie. <bundle>/crops/<cropname>/loader/done) if the instruction file has been moved
5. Run a query against the DB to check that the file really loaded
'''
from __future__ import print_function
import unittest
import xmlrunner
import sys
import subprocess
import time
from db.connection_manager import ConnectionManager
from os.path import basename
import csv


class GLoadingTest(unittest.TestCase):
	DB_CONN = 'postgresql://appuser:appuser@localhost:5432/test'
	FS_HOST = 'localhost'
	FS_USERNAME = 'gadm'
	FS_PASSWORD = 'dummypass'
	CROP_PATH = '/storage1/gobii_sys_int/gobii_bundle/crops/dev'
	#LOADING_INSTRUCTION_PATH = '/storage1/gobii_sys_int/gobii_bundle/crops/dev/loader/instructions'
	MARKER_INPUT_FILE = 'codominant/data/codominant_markers.txt'
	MARKER_INSTRUCTION_FILE = 'codominant/instruction/m_test.json.template'
	SAMPLE_INPUT_FILE = 'codominant/data/codominant_samples.csv'
	SAMPLE_INSTRUCTION_FILE = 'codominant/instruction/s_test.json.template'
	MARKER_FILE_TARGET_DIR = ''
	MARKER_OUTPUT_TARGET_DIR = ''
	SAMPLE_FILE_TARGET_DIR = ''
	SAMPLE_OUTPUT_TARGET_DIR = ''
	CRONS_INTERVAL = '5'  # in minutes
	FILE_AGE = '5'
	PROCESSING_TIME = '1'
	conMgr = None
	conn = None
	cur = None

	@classmethod
	def setUpClass(self):
		try:
			self.connMgr = ConnectionManager()
			self.conn = self.connMgr.connectToDatabase(self.DB_CONN)
			self.cur = self.conn.cursor()
			#self.fdm = ForeignDataManager()
		except Exception as e1:
			print('Failed to connect to the database. Please check connection string. Cause: %s' % e1)
		try:
			#print('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' mkdir -p '+self.MARKER_FILE_TARGET_DIR)
			retCode = subprocess.call('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' mkdir -p '+self.MARKER_FILE_TARGET_DIR+' '+self.SAMPLE_FILE_TARGET_DIR+' '+self.MARKER_OUTPUT_TARGET_DIR+' '+self.SAMPLE_OUTPUT_TARGET_DIR, shell=True)
		except OSError as e:
			print('Failed to create target directories in server. Retcode: %s Cause: %s' % (retCode, e))

	@classmethod
	def tearDownClass(self):
		try:
			self.connMgr.disconnectFromDatabase()
		except Exception as e1:
			print('Failed to disconnect from the database. Please check connection string. Cause: %s' % e1)

	def test_1_create_marker_instruction_file(self):
		try:
			markerInstructionFile = self.MARKER_INSTRUCTION_FILE.replace('.template', '')
			with open(self.MARKER_INSTRUCTION_FILE, "r") as fin:
				with open(markerInstructionFile, "w") as fout:
					for line in fin:
						line = line.replace('SOURCE_replace_me_I_am_a_temporary_string', self.MARKER_FILE_TARGET_DIR)
						line = line.replace('DESTINATION_replace_me_I_am_a_temporary_string', self.MARKER_OUTPUT_TARGET_DIR)
						fout.write(line)
			self.assertTrue(True)
		except Exception:
			self.assertTrue(False)

	'''def test_create_target_path_in_gobii_server(self):
		try:
			#print('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' mkdir -p '+self.MARKER_FILE_TARGET_DIR)
			retCode = subprocess.call('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' mkdir -p '+self.MARKER_FILE_TARGET_DIR+' '+self.SAMPLE_FILE_TARGET_DIR+' '+self.MARKER_OUTPUT_TARGET_DIR+' '+self.SAMPLE_OUTPUT_TARGET_DIR, shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to create target directories in server. Cause: %s' % e)'''

	def test_1_create_sample_instruction_file(self):
		try:
			sampleInstructionFile = self.SAMPLE_INSTRUCTION_FILE.replace('.template', '')
			with open(self.SAMPLE_INSTRUCTION_FILE, "r") as fin:
				with open(sampleInstructionFile, "w") as fout:
					for line in fin:
						line = line.replace('SOURCE_replace_me_I_am_a_temporary_string', self.SAMPLE_FILE_TARGET_DIR)
						line = line.replace('DESTINATION_replace_me_I_am_a_temporary_string', self.SAMPLE_OUTPUT_TARGET_DIR)
						fout.write(line)
			#self.assertTrue(True)
		except Exception:
			self.fail('Failed to create sample instruction file.')

	def test_2_upload_marker_data_file(self):
		try:
			retCode = subprocess.call('scp '+self.MARKER_INPUT_FILE+' '+self.FS_USERNAME+'@'+self.FS_HOST+':'+self.MARKER_FILE_TARGET_DIR, shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to upload marker data file. Cause: %s' % e)

	def test_2_upload_sample_data_file(self):
		try:
			retCode = subprocess.call('scp '+self.SAMPLE_INPUT_FILE+' '+self.FS_USERNAME+'@'+self.FS_HOST+':'+self.SAMPLE_FILE_TARGET_DIR, shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to upload sample data file. Cause: %s' % e)

	def test_3_upload_marker_instruction_file(self):
		try:
			retCode = subprocess.call('scp '+self.MARKER_INSTRUCTION_FILE.replace('.template', '')+' '+self.FS_USERNAME+'@'+self.FS_HOST+':'+self.CROP_PATH+'/loader/instructions', shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to upload marker instruction file. Cause: %s' % e)

	def test_3_upload_sample_instruction_file(self):
		try:
			retCode = subprocess.call('scp '+self.SAMPLE_INSTRUCTION_FILE.replace('.template', '')+' '+self.FS_USERNAME+'@'+self.FS_HOST+':'+self.CROP_PATH+'/loader/instructions', shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to upload sample instruction file. Cause: %s' % e)

	def test_4_check_if_digester_consumed_marker_file(self):
		try:
			#print('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' test -f '+self.CROP_PATH+'/loader/done/'+basename(self.MARKER_INSTRUCTION_FILE+'.new'))
			time.sleep((int(self.CRONS_INTERVAL) * 60) + (int(self.FILE_AGE) * 60) + int(self.PROCESSING_TIME))
			retCode = subprocess.call('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' test -f '+self.CROP_PATH+'/loader/done/'+basename(self.MARKER_INSTRUCTION_FILE.replace('.template', '')), shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to check finished marker instruction file. Cause: %s' % e)

	def test_4_check_if_digester_consumed_sample_file(self):
		try:
			#time.sleep(int(self.CRONS_INTERVAL) * 2.3)
			retCode = subprocess.call('ssh '+self.FS_USERNAME+'@'+self.FS_HOST+' test -f '+self.CROP_PATH+'/loader/done/'+basename(self.SAMPLE_INSTRUCTION_FILE.replace('.template', '')), shell=True)
			self.assertEqual(retCode, 0)
		except OSError as e:
			self.fail('Failed to check finished sample instruction file. Cause: %s' % e)

def test_5_check_if_markers_loaded(self):
		#get all marker_names and store it in a list -- note that we're only checking via names as the DB will have no markers when the integration test runs anyway. If in the future, that assumption will no longer hold, add the whole criteria here: right now it's marker_name + platfrom_id
		with open(self.MARKER_INPUT_FILE, "r") as markerFile:
			next(markerFile)
			markerReader = csv.reader(markerFile, delimiter='\t')
			markerList = [i[0] for i in markerReader]
			#print ('markers count: ', len(markerList))
			#print ('markers: ', ','.join(markerList))
			#self.cur.mogrify("select count(*) from marker where name in %s;", (tuple(markerList),))
			self.cur.execute("select count(*) from marker where name in %s;", (tuple(markerList),))
			markerCount = self.cur.fetchone()
			self.assertEqual(markerCount[0], len(markerList), msg='Row counts did not match. Either the file did not load correctly or it did not load at all.')

def test_5_check_if_germplasm_loaded(self):
		with open(self.SAMPLE_INPUT_FILE, "r") as sampleFile:
			next(sampleFile)
			sampleReader = csv.reader(sampleFile, delimiter='\t')
			sampleList = [i[1] for i in sampleReader]
			self.cur.execute("select count(*) from germplasm where external_code in %s;", (tuple(sampleList),))
			count = self.cur.fetchone()
			self.assertEqual(count[0], len(sampleList), msg='Row counts did not match. Either the file did not load correctly or it did not load at all.')

def test_5_check_if_dnasample_loaded(self):
		with open(self.SAMPLE_INPUT_FILE, "r") as sampleFile:
			next(sampleFile)
			sampleReader = csv.reader(sampleFile, delimiter='\t')
			sampleList = [i[2] for i in sampleReader]
			self.cur.execute("select count(*) from dnasample where name in %s;", (tuple(sampleList),))
			count = self.cur.fetchone()
			self.assertEqual(count[0], len(sampleList), msg='Row counts did not match. Either the file did not load correctly or it did not load at all.')

def test_5_check_if_dnarun_loaded(self):
		with open(self.SAMPLE_INPUT_FILE, "r") as sampleFile:
			next(sampleFile)
			sampleReader = csv.reader(sampleFile, delimiter='\t')
			sampleList = [i[5] for i in sampleReader]
			self.cur.execute("select count(*) from dnarun where name in %s;", (tuple(sampleList),))
			count = self.cur.fetchone()
			self.assertEqual(count[0], len(sampleList), msg='Row counts did not match. Either the file did not load correctly or it did not load at all.')


if __name__ == '__main__':
	if len(sys.argv) < 11:
		print("Please supply the parameters. \nUsage: gobii_loading_test <db_connection_string> <fs_host> <fs_username> <fs_password> <crop_path> <marker_input_file> <marker_instruction_file> <sample_input_file> <sample_instruction_file> <marker_file_target_dir> <marker_output_target_dir> <sample_file_target_dir> <sample_output_target_dir> <crons_interval:minutes> <file_age:minutes> <processing_time:seconds>")
		sys.exit(1)
	else:
		GLoadingTest.PROCESSING_TIME = str(sys.argv.pop())
		GLoadingTest.FILE_AGE = str(sys.argv.pop())
		GLoadingTest.CRONS_INTERVAL = str(sys.argv.pop())
		GLoadingTest.SAMPLE_OUTPUT_TARGET_DIR = str(sys.argv.pop())
		GLoadingTest.SAMPLE_FILE_TARGET_DIR = str(sys.argv.pop())
		GLoadingTest.MARKER_OUTPUT_TARGET_DIR = str(sys.argv.pop())
		GLoadingTest.MARKER_FILE_TARGET_DIR = str(sys.argv.pop())
		GLoadingTest.SAMPLE_INSTRUCTION_FILE = str(sys.argv.pop())
		GLoadingTest.SAMPLE_INPUT_FILE = str(sys.argv.pop())
		GLoadingTest.MARKER_INSTRUCTION_FILE = str(sys.argv.pop())
		GLoadingTest.MARKER_INPUT_FILE = str(sys.argv.pop())
		GLoadingTest.CROP_PATH = str(sys.argv.pop())
		GLoadingTest.FS_PASSWORD = str(sys.argv.pop())
		GLoadingTest.FS_USERNAME = str(sys.argv.pop())
		GLoadingTest.FS_HOST = str(sys.argv.pop())
		GLoadingTest.DB_CONN = str(sys.argv.pop())
		#print('\n '.join("%s: %s" % item for item in vars(GLoadingTest).items()))
	#unittest.main()
	#unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
	with open('test-reports/loading_test_results.xml', 'wb') as output:
		unittest.main(testRunner=xmlrunner.XMLTestRunner(output=output))
