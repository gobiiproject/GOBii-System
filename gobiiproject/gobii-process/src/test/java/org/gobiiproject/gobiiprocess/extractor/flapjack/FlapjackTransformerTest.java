package org.gobiiproject.gobiiprocess.extractor.flapjack;

import static org.junit.Assert.*;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HelperFunctions.class)
public class FlapjackTransformerTest {

	/**
	 * Extended File.
	 */

	@Test
	public void testGenerateMapFileExtended() {

		PowerMockito.mockStatic(HelperFunctions.class);
		PowerMockito.when(HelperFunctions.tryExec("echo # fjFile = MAP", "tempDir" + "map.header", "errorFile"))
				.thenReturn(true);
		PowerMockito
				.when(HelperFunctions.tryExec("tail -n +2 " + "tempDir" + "tmp", "tempDir" + "map.body", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(
				HelperFunctions.tryExec("cut -f" + "1,28,31" + " " + "markerFile", "tempDir" + "tmp", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cat " + "tempDir" + "map.header " + "" + "" + "tempDir" + "map.body",
				"outFile", "errorFile")).thenReturn(true);

		assertEquals(FlapjackTransformer.generateMapFile("markerFile", "sampleFile", "chrLengthFile", "tempDir",
				"outFile", "errorFile", true), true);
	}

	/**
	 * Not an Extended File.
	 */

	@Test
	public void testGenerateMapFileNoChrLenghtAndNotExtended() {

		PowerMockito.mockStatic(HelperFunctions.class);
		PowerMockito.when(HelperFunctions.tryExec("echo # fjFile = MAP", "tempDir" + "map.header", "errorFile"))
				.thenReturn(true);
		PowerMockito
				.when(HelperFunctions.tryExec("tail -n +2 " + "tempDir" + "tmp", "tempDir" + "map.body", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cut -f" + "1" + " " + "markerFile", "tempDir" + "tmp", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cat " + "tempDir" + "map.header " + "" + "" + "tempDir" + "map.body",
				"outFile", "errorFile")).thenReturn(true);

		assertEquals(FlapjackTransformer.generateMapFile("markerFile", "sampleFile", "chrLengthFile", "tempDir",
				"outFile", "errorFile", false), true);
	}

	@Test
	public void testGenerateGenotypeFile() {

		String tempFile = "tempDir" + "tmp";
		String markerList = "tempDir" + "genotype.markerList";
		String inverseMarkerList = "tempDir" + "genotype.markerIList";

		PowerMockito.mockStatic(HelperFunctions.class);
		PowerMockito.when(HelperFunctions.tryExec("echo # fjFile = GENOTYPE", "tempDir" + "map.response", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("echo ", "tempDir" + "blank.file", "errorFile")).thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cut -f1 " + "markerFile", tempFile, "errorFile")).thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("tail -n +2 " + tempFile, markerList, "errorFile")).thenReturn(true);
		PowerMockito
				.when(HelperFunctions.tryExec("cat " + "tempDir" + "blank.file " + markerList, tempFile, "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("tr '\\n' '\\t'", inverseMarkerList, "errorFile", tempFile))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cut -f1 " + "sampleFile", tempFile, "errorFile")).thenReturn(true);
		PowerMockito
				.when(HelperFunctions.tryExec("tail -n +2 " + tempFile, "tempDir" + "genotype.sampleList", "errorFile"))
				.thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("paste " + "tempDir" + "genotype.sampleList " + "genotypeFile",
				"tempDir" + "sample.matrix", "errorFile")).thenReturn(true);
		PowerMockito.when(HelperFunctions.tryExec("cat " + "tempDir" + "map.response " + inverseMarkerList + " "
				+ "tempDir" + "blank.file " + "tempDir" + "sample.matrix", "outFile", "errorFile")).thenReturn(true);

		assertEquals(FlapjackTransformer.generateGenotypeFile("markerFile", "sampleFile", "genotypeFile", "tempDir",
				"outFile", "errorFile"), true);

	}

}
