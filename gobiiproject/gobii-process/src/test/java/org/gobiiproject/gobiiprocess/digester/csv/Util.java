package org.gobiiproject.gobiiprocess.digester.csv;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import com.google.common.annotations.VisibleForTesting;

class Util {

	/**
	 * 
	 * @param tempFolderLocation Temporary Folder.
	 * @param tableName table name used to identify the file.
	 * @param expectedOutputFolderLocation Expected output file.
	 */
	static void validateResult(String tempFolderLocation, String tableName, String expectedOutputFolderLocation) throws IOException {
		BufferedReader actualOutputReader = new BufferedReader(new FileReader(tempFolderLocation + "\\dest" + "\\digest." + tableName));
		BufferedReader expectedOutputReader = new BufferedReader(new FileReader(expectedOutputFolderLocation + "\\" + tableName + ".txt"));
		String actualFileRow, expectedFileRow;
		while (((actualFileRow = actualOutputReader.readLine()) != null )&&((expectedFileRow = expectedOutputReader.readLine()) != null )) {
			assertEquals("Mismatch in output. \n Expected:" + expectedFileRow + "\n Actual     :" + actualFileRow, expectedFileRow, actualFileRow);
		}
		actualOutputReader.close();
		expectedOutputReader.close();
	}
	
	/**
	 * Creates gobiiFIleColumn for CSV_ROW with sub-column
	 * @param rCoord
	 * @param cCoord
	 * @return GobiiFileColumn
	 */
	 
	static GobiiFileColumn createGobiiCSV_SUB(Integer rCoord, Integer cCoord) {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_ROW);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvRow");
		fileColumn.setSubcolumn(true);
		fileColumn.setSubcolumnDelimiter("_");
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;

	}

	/**
	 * Creates gobiiFIleColumn for CSV_BOTH
	 * @param rCoord
	 * @param cCoord
	 * @return GobiiFileColumn
	 */
	@VisibleForTesting
	static GobiiFileColumn createGobiiCSV_BOTH(Integer rCoord, Integer cCoord) {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_BOTH);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvBoth");
		fileColumn.setSubcolumn(false);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;

	}


	/**
	 * Creates gobiiFileColumn for autoIncrement
	 * @return GobiiFileColumn
	 */
	@VisibleForTesting
	static GobiiFileColumn createGobiiAutoIncrementColumn() {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.AUTOINCREMENT);
		fileColumn.setName("AutoIncrement");
		fileColumn.setSubcolumn(false);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;

	}

	/**
	 * Creates gobiiFileColumn for csv_row type.
	 * @param rCoord
	 * @param cCoord
	 * @return GobiiFileColumn
	 */
	@VisibleForTesting
	static GobiiFileColumn createGobiiCSV_ROW(Integer rCoord, Integer cCoord) {

		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_ROW);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvRow");
		fileColumn.setSubcolumn(false);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;
	}
	
	/**
	 * Creates gobiiFileColumn for csv_col type.
	 * @param rCoord
	 * @param cCoord
	 * @return GobiiFileColumn
	 */
	@VisibleForTesting
	static GobiiFileColumn createGobiiCSV_COL(Integer rCoord, Integer cCoord) {

		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvCol");
		fileColumn.setSubcolumn(false);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;
	}

	/**
	 * Creates a gobiiFileColumn for constant type.
	 * @param constantValue Used for creating Constant.
	 * @return GobiiFileColumn
	 */
	@VisibleForTesting
	static GobiiFileColumn createGobiiConstantColumn(String constantValue) {

		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
		fileColumn.setName("constantName");
		fileColumn.setConstantValue(constantValue);
		fileColumn.setSubcolumn(false);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		return fileColumn;
	}
	
	/**
	 * Sets the gobiiFile in the instruction. This is common across all the test
	 * cases. Refer
	 * {@code org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile.java}
	 * 
	 */
	@VisibleForTesting
	static void createAndSetGobiiFile(GobiiLoaderInstruction instruction, String tempFolderLocation) {
		GobiiFile gobiiFile = new GobiiFile();
		gobiiFile.setSource(tempFolderLocation + "/src");
		gobiiFile.setDestination(tempFolderLocation + "/dest");
		gobiiFile.setDelimiter("\\t");
		gobiiFile.setGobiiFileType(GobiiFileType.GENERIC);
		gobiiFile.setCreateSource(false);
		gobiiFile.setRequireDirectoriesToExist(false);
		instruction.setGobiiFile(gobiiFile);
	}
	
	/**
	 * Deletes the directory pointed by path.
	 * @param path Path to deletion directory.
	 * @return Status.
	 */
	@VisibleForTesting
	static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
}
