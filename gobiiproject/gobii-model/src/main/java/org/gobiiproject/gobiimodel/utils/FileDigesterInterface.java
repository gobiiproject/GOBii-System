package org.gobiiproject.gobiimodel.utils;

import java.util.List;
import java.util.Map;

/**
 * An interface to request a file dump for the data. Solves our basic needs for data loading, but not much else.
 * 
 * Takes as input, locations to move the various files, inluding:
 * VariantCall, Marker, Sample, SampleData, MarkerData, VariantCallData
 * 
 * @author jdl232
 */
public interface FileDigesterInterface {
	/**
	 * Generates output files based on input file and parameters.
	 * Returns a list of filenames for output instruction file to use.
	 * @param outFilename The base filename to be saved to. For example, if outFilename is 
	 * "FixedData", files would be in the format "FixedData.samples" "FixedData.IUPAC", "FixedData.csv"
	 * @return List of output filenames, or null if there was a failure
	 */
	public List<String> saveFiles(String outFilename);
}
