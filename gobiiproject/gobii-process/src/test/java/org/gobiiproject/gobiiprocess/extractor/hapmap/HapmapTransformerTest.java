package org.gobiiproject.gobiiprocess.extractor.hapmap;

import static org.junit.Assert.*;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;


public class HapmapTransformerTest {

    /**
     * Validates Hapmap Transformer without Extended Marker FIle
     */
    @Test
    public void TestHapmapTransformerWithoutExtendedMarkerFile(){
        String sampleFile = "src/test/resources/hapmap_sample.file";
        String markerFile = "src/test/resources/hapmap_marker.file";
        String genotypeFile = "src/test/resources/hapmap_genotype.file";
        String outFile = "src/test/resources/hapmap_marker_out.file";
        String expectedOutFile = "src/test/resources/hapmap_marker_out_benchmark.file";

        HapmapTransformer hapmapTransformer = new HapmapTransformer();
        ErrorLogger.logDebug("GobiiExtractor", "Executing Hapmap Generation");
        boolean success;
        try {
            success = hapmapTransformer.generateFile(markerFile,sampleFile, "", genotypeFile, outFile, "");
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
        success = validateResult(outFile, expectedOutFile);
        assertTrue(success);
    }

    /**
     * Validates Hapmap Transformer with Extended Marker FIle
     */
    @Test
    public void TestHapmapTransformerWithExtendedMarkerFile(){
        String sampleFile = "src/test/resources/hapmap_sample.file";
        String markerFile = "src/test/resources/hapmap_marker.file";
        String genotypeFile = "src/test/resources/hapmap_genotype.file";
        String extendedMarkerFile = "src/test/resources/hapmap_Extended_marker.file";
        String outFile = "src/test/resources/hapmap_extended_out.file";
        String expectedOutFile = "src/test/resources/hapmap_extended_out_benchmark.file";

        HapmapTransformer hapmapTransformer = new HapmapTransformer();
        ErrorLogger.logDebug("GobiiExtractor", "Executing Hapmap Generation");
        boolean success;
        try {
            success = hapmapTransformer.generateFile(markerFile,sampleFile, extendedMarkerFile, genotypeFile,outFile, "");
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
        success = validateResult(outFile, expectedOutFile);
        assertTrue(success);
    }

    /**
     *
     * @param actualFileName Actual output file
     * @param expectedFileName expected output file
     */
    private static boolean validateResult(String actualFileName, String expectedFileName){
       boolean success = true;
        try {
            BufferedReader actualOutputReader = new BufferedReader(new FileReader(actualFileName));
            BufferedReader expectedOutputReader = new BufferedReader(new FileReader(expectedFileName));
            String actualFileRow, expectedFileRow;
            while (((actualFileRow = actualOutputReader.readLine()) != null )&&((expectedFileRow = expectedOutputReader.readLine()) != null )) {
                assertEquals("Mismatch in output. \n Expected:" + expectedFileRow + "\n Actual     :" + actualFileRow, expectedFileRow, actualFileRow);
            }
            actualOutputReader.close();
            expectedOutputReader.close();
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
}
