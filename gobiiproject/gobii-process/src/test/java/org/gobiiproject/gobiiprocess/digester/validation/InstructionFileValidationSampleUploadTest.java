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
public class InstructionFileValidationSampleUploadTest {

	private final String NAME = "name";
	private final String EXTERNAL_CODE = "external_code";
	private final String DNA_SAMPLE_NAME = "dnasample_name";
	private final String DNA_RUN_NAME = "dnarun_name";

	private String rCoord = "1", cCoord = "2";

	/**
	 * Test case for digest.germplasm (external_code) == digest.dnasample (external_code)
	 */
	@Test
	public void testGermplasm2DNASample() {
		String []params = {GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord, GobiiTableType.DNASAMPLE, EXTERNAL_CODE, rCoord, cCoord};
		assertNull(callValidateSampleUpload(params));
	}

	/**
	 * Test case for digest.germplasm (external_code) == digest.dnasample (external_code)
	 */
	@Test
	public void testGermplasm2DNASample_Failure() {
		String []params = {GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord, GobiiTableType.DNASAMPLE, EXTERNAL_CODE, rCoord+1, cCoord+1};
		assertNotNull(callValidateSampleUpload(params));
	}

	/**
	 * Test case for digest.dnasample (name) == digest.dnarun (dnasample_name)
	 */
	@Test
	public void testDNASample2DNARUN() {
		String []params = {GobiiTableType.DNASAMPLE, NAME, rCoord, cCoord, GobiiTableType.DNARUN, DNA_SAMPLE_NAME, rCoord, cCoord};
		assertNull(callValidateSampleUpload(params));
	}

	/**
	 * Test case for digest.dnasample (name) == digest.dnarun (dnasample_name)
	 */
	@Test
	public void testDNASample2DNARUN_Failure() {
		String []params = {GobiiTableType.DNASAMPLE, NAME, rCoord, cCoord, GobiiTableType.DNARUN, DNA_SAMPLE_NAME, rCoord+1,	cCoord};
		assertNotNull(callValidateSampleUpload(params));
	}

	/*
	 * Tests case for digest.germplasm_prop upload only if digest.germplasm exists
	 */
	@Test
	public void testGermplasmProp2GermplasmExist_Failure() {
		String []params = {GobiiTableType.GERMPLASM_PROP, NAME, rCoord, cCoord};
		assertNotNull(callValidateSampleUpload(params));
	}
	
	/**
	 * Test case for digest.germplasm_prop (external_code) == digest.germplasm (external_code)
	 */
	@Test
	public void testGermplasmProp2Germplasm() {
		String []params = {GobiiTableType.GERMPLASM_PROP, EXTERNAL_CODE, rCoord, cCoord, GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord};
		assertNull(callValidateSampleUpload(params));
	}

	/**
	 * Test case for digest.germplasm_prop (external_code) == digest.germplasm (external_code)
	 */
	@Test
	public void testGermplasmProp2Germplasm_Failure() {
		String []params = {GobiiTableType.GERMPLASM_PROP, EXTERNAL_CODE, rCoord, cCoord, GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord+1, cCoord+1};
		assertNotNull(callValidateSampleUpload(params));
	}
	
	/*
	 * Tests case for digest.dnasample_prop upload only if digest.dnasample exists
	 */
	@Test
	public void testDNASampleProp2DNASampleExist_Failure() {
		String []params = {GobiiTableType.DNASAMPLE_PROP, NAME, rCoord, cCoord};
		assertNotNull(callValidateSampleUpload(params));
	}

	/*
	 * Tests case for digest.dnasample_prop (dnasample_name) == digest.dnasample (name)
	 */
	@Test
	public void testDNASampleProp2DNASample() {
		String []params = {GobiiTableType.DNASAMPLE_PROP, DNA_SAMPLE_NAME, rCoord, cCoord, GobiiTableType.DNASAMPLE, NAME, rCoord, cCoord};
		assertNull(callValidateSampleUpload(params));
	}

	/**
	 * Test case for digest.dnasample_prop (dnasample_name) == digest.dnasample (name)
	 */
	@Test
	public void testDNASampleProp2DNASample_Failure() {
		String []params = {GobiiTableType.DNASAMPLE_PROP, DNA_SAMPLE_NAME, rCoord, cCoord, GobiiTableType.DNASAMPLE, NAME, rCoord+1, cCoord+1};
		assertNotNull(callValidateSampleUpload(params));
	}
	
	/*
	 * Tests case for digest.dnarun_prop upload only if digest.dnarun file exists
	 */
	@Test
	public void testDNARunProp2DNARunExist_Failure() {
		String []params = {GobiiTableType.DNARUN_PROP, NAME, rCoord, cCoord};
		assertNotNull(callValidateSampleUpload(params));
	}
	
	/*
	 * Tests case for digest.dnarun_prop (dnarun_name) == digest.dnarun (name)
	 */
	@Test
	public void testDNARunProp2DNARun() {
		String []params = {GobiiTableType.DNARUN_PROP, DNA_RUN_NAME, rCoord, cCoord, GobiiTableType.DNARUN, NAME, rCoord, cCoord};
		assertNull(callValidateSampleUpload(params));
	}

	/*
	 * Tests case for digest.dnarun_prop (dnarun_name) == digest.dnarun (name)
	 */
	@Test
	public void testDNARunProp2DNARun_Failure() {
		String []params = {GobiiTableType.DNARUN_PROP, DNA_RUN_NAME, rCoord, cCoord, GobiiTableType.DNARUN, NAME, rCoord+1, cCoord+1};
		assertNotNull(callValidateSampleUpload(params));
	}
	
	@VisibleForTesting
	private String callValidateSampleUpload(String[] params) {
		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();	
		for(int i= 0; i < params.length; i= i+4){
			instructionList.add(InstructionFileValidationUtil.createInstruction(params[i+0], params[i+1], Integer.parseInt(params[i+2]), Integer.parseInt(params[i+3])));
		}
		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		return instructionFileValidator.validateSampleUpload();
	}
}