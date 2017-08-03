package org.gobiiproject.gobiiprocess.extractor.hapmap;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class that has the scanners required for the Hapmap.
 * Made as a separate class, for ease of understanding and modularity.
 */
class HapmapScanners{

    Scanner markerScanner, sampleScanner, extendedMarkerScanner, genotypeScanner;

    /**
     * Creates scanners for Marker, Sample, ExtendedMarker and Genotype.
     * @param markerFileIn Marker file name.
     * @param sampleFileIn Sample file name.
     * @param extendedMarkerFileIn Extended Marker file name.
     * @param genotypeFileIn Genotype file name.
     * @param errorFile Error file name.
     * @return True if all the scanners are cleared successfull
     * @throws FileNotFoundException
     */

    public boolean createScanners(String markerFileIn, String sampleFileIn, String extendedMarkerFileIn, String genotypeFileIn, String errorFile) throws FileNotFoundException {
        boolean status = true;
        ///////////////////////////
        // marker file (mandatory)
        ////////////////////////////
        File markerFile = new File(markerFileIn);
        if (!validateFile(markerFile, "Marker")) return false;

        ////////////////////////////
        // sample file (mandatory)
        ////////////////////////////
        File sampleFile = new File(sampleFileIn);
        if (!validateFile(sampleFile, "Sample")) return false;


        ////////////////////////////////////////////////////////
        // extended marker file (optional, so existent or not)
        ////////////////////////////////////////////////////////
        File extendedMarkerFile = new File(extendedMarkerFileIn);
        if (!validateFile(extendedMarkerFile, "Extended Marker")) extendedMarkerFile = null;

        //////////////////////////////
        // genotype file (mandatory)
        //////////////////////////////
        File genotypeFile = new File(genotypeFileIn);
        if (!validateFile(genotypeFile, "Genotype")) return false;


        markerScanner = new Scanner(markerFile);
        sampleScanner = new Scanner(sampleFile);
        extendedMarkerScanner = null;
        if (extendedMarkerFile != null && extendedMarkerFile.exists())
            extendedMarkerScanner = new Scanner(extendedMarkerFile);
        genotypeScanner = new Scanner(genotypeFile);

        return status;
    }


    /**
     * Returns true only when the file exits and is a file. Else returns false.
     *
     * @param file      File to be validated.
     * @param fileName  Used for logging.
     * @return status.
     */
    boolean validateFile(File file, String fileName) {
        if (!(file.exists())) {
            ErrorLogger.logInfo("Extractor", fileName + " file not found");
            return false;
        } else {
            if (!(file.isFile())) {
                ErrorLogger.logInfo("Extractor", fileName + "file not correct");
                return false;
            } else
                return true;
        }

    }
}

