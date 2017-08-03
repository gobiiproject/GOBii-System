package org.gobiiproject.gobiiprocess.digester.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiTableType;
import org.gobiiproject.gobiimodel.utils.*;
import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;

/**
 * Unit test for InstructionFileValidationTest.
 */
public class InstructionFileValidationTest {

	private final String NAME = "name";
	private final String MARKER_NAME = "marker_name";
	private final String MATRIX = "matrix";
    private final String DNA_RUN_NAME = "dnarun_name";

	private String rCoord = "1", cCoord = "2";

	/**
	 * Test case for (IF digest.matrix EXISTS) 
	 * 					digest.dataset_marker exists
	 * 					digest.dataset_dnarun exists
	 */
	@Test
	public void testMatrix2DatasetMarkerANDDatasetDNARunExist() {
		String []params = {GobiiTableType.MATRIX, MATRIX, rCoord, cCoord, GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord, cCoord, GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME,rCoord, cCoord};
		assertNull(callValidate(params));
	}

	/**
	 * Test case for (IF digest.matrix EXISTS) digest.dataset_marker exists
	 */
	@Test
	public void testMatrix2DatasetMarkerANDDatasetDNARunExist_Failure() {
		String []params = {GobiiTableType.MATRIX, MATRIX, rCoord, cCoord};
		assertNotNull(callValidate(params));
	}

	/**
	 * Test case for 
                    if digest.marker file exists
                    digest.marker (name) == digest.dataset_marker (marker_name)
      */
	@Test
	public void testMarker2DatasetMarkerExist() {
		String []params = {GobiiTableType.MARKER, NAME, rCoord, cCoord,GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord, cCoord};
		assertNull(callValidate(params));
	}

	/**
	 * Test case for (IF digest.matrix EXISTS) digest.dataset_marker exists
	 */
	@Test
	public void testMarker2DatasetMarkerExist_Failure() {
		String []params = {GobiiTableType.MARKER, NAME, rCoord, cCoord,GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord+1, cCoord};
		assertNotNull(callValidate(params));
	}

	/**
	 * Test case for 
                   if digest.dnarun exists
   						digest.dnarun (name) == digest.ds_dnarun (dnarun_name)
     */
	@Test
	public void testDNARun2DatasetDNARunExist() {
		String []params = {GobiiTableType.DNARUN, NAME, rCoord, cCoord,GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME, rCoord, cCoord};
		assertNull(callValidate(params));
	}
	
	/**
	 * Test case for 
                   if digest.dnarun exists
   						digest.dnarun (name) == digest.ds_dnarun (dnarun_name)
     */
	@Test
	public void testDNARun2DatasetDNARunExist_Failure() {
		String []params = {GobiiTableType.DNARUN, NAME, rCoord, cCoord,GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME, rCoord+1, cCoord};
		assertNotNull(callValidate(params));
	}

	@VisibleForTesting
	private String callValidate(String[] params) {
		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();	
		for(int i= 0; i < params.length; i= i+4){
			instructionList.add(InstructionFileValidationUtil.createInstruction(params[i+0], params[i+1], Integer.parseInt(params[i+2]), Integer.parseInt(params[i+3])));
		}
		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		return instructionFileValidator.validate();
	}

	}