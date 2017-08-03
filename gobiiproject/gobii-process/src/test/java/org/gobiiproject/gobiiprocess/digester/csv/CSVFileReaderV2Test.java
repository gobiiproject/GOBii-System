package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit test for CSVFileReaderV2Test. Approach: A temporary folder is created
 * which contains all input file. Instruction file is created with required
 * fields. Using CSVFileReaderV2 input file is read and stored in output file
 * after processing. Generated o/p file is verified that it is as expected.
 */
public class CSVFileReaderV2Test {

	private static String tempFolderLocation, resourceDestFolderLocation;

	@ClassRule
	public static TemporaryFolder tempFolder = new TemporaryFolder();

	@BeforeClass
	public static void setUp() throws IOException {
		File srcFolder;
		srcFolder = tempFolder.newFolder("src");
		tempFolder.newFolder("dest");
		tempFolderLocation = tempFolder.getRoot().getPath();
		File resourceDest = new File("src/test/resources");
		resourceDestFolderLocation = resourceDest.getAbsolutePath();
		File resourceSource = new File("src/test/resources/input.txt");
		File dest= new File(srcFolder.getAbsolutePath()+"\\input.txt"); 
		Files.copy(resourceSource.toPath(), dest.toPath());
	}

	/**
	 * According to JUnit no exception is thrown when temp folder is not
	 * deleted. This method ensures that temp folder is always deleted.
	 */
	@AfterClass
	public static void checkAndCleanTempFile() {
		Util.deleteDirectory(new File(tempFolderLocation));
	}

	/**
	 * Test case for multiple_CSV_ROW
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testMultipleCSV_ROW() throws IOException, InterruptedException {

		String table = "multipleCSV_ROW";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
		gobiiColumns.add(Util.createGobiiCSV_ROW(0, 0));
		gobiiColumns.add(Util.createGobiiCSV_ROW(1, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2();
		csvReader.processCSV(instruction);

		Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
	}

	@Test
	public void testMultipleCSV_COL() throws IOException, InterruptedException {
		String table = "multipleCSV_COL";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
		gobiiColumns.add(Util.createGobiiCSV_COL(0, 0));
		gobiiColumns.add(Util.createGobiiCSV_COL(0, 1));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2();
		csvReader.processCSV(instruction);

		Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
	}

	@Test
	public void testCSV_BOTH() throws IOException, InterruptedException {
		String table = "multipleCSV_BOTH";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
		gobiiColumns.add(Util.createGobiiCSV_BOTH(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2();
		csvReader.processCSV(instruction);

		Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
	}

	@Test
	public void testSubColumn() throws InterruptedException, IOException {
		String table = "CSVSubColumn";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
		gobiiColumns.add(Util.createGobiiCSV_SUB(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2();
		csvReader.processCSV(instruction);

		Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
	}

	@Test
	public void testMultipleFilesCSV_ROW() throws IOException, InterruptedException {
		String table = "multiFileCSV_ROW";
		File file2 = new File(tempFolderLocation + "\\src" + "\\file2.txt");
		String data[] = { "marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
						  "dommarker1	1	0	1	1" };
		BufferedWriter srcFileWriter = new BufferedWriter(new FileWriter(file2));
		srcFileWriter.write(data[0] + "\n");
		srcFileWriter.write(data[1] + "\n");
		srcFileWriter.close();

		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
		gobiiColumns.add(Util.createGobiiCSV_ROW(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2();
		csvReader.processCSV(instruction);

		Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
		file2.delete();
	}

}