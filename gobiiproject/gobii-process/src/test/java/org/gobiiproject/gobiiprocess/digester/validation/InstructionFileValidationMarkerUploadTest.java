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
public class InstructionFileValidationMarkerUploadTest {

	private final String NAME = "name";
	private final String MARKER_NAME = "marker_name";

	private String rCoord = "1", cCoord = "2";

	/**
	 * Test case for digest.marker (name) == digest.lg_marker (marker_name)
	 */
	@Test
	public void testMarker2lgMarker() {
		String []params = {GobiiTableType.MARKER,NAME, rCoord, cCoord, GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME, rCoord, cCoord};
		assertNull(callValidateMarkerUpload(params));
	}


	/**
	 * Test case for digest.marker (name) == digest.lg_marker (marker_name)
	 */
	@Test
	public void testMarker2lgMarker_Failure() {
		String []params = {GobiiTableType.MARKER, NAME, rCoord, cCoord, GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME, rCoord+1, cCoord+1};
		assertNotNull(callValidateMarkerUpload(params));
	}
	
	/**
	 * Test case for digest.lg_marker (lg_name) == digest.linkage_group (name)
	 */
	@Test
	public void testlgMarker2linkageGroup() {
		String []params = {GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME, rCoord, cCoord, GobiiTableType.LINKAGE_GROUP, NAME, rCoord, cCoord};
		assertNull(callValidateMarkerUpload(params));
	}

	/**
	 * Test case for digest.lg_marker (lg_name) == digest.linkage_group (name)
	 */
	@Test
	public void testlgMarker2linkageGroup_Failure() {
		String []params = {GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME, rCoord, cCoord, GobiiTableType.LINKAGE_GROUP, NAME, rCoord+1, cCoord+1};
		assertNotNull(callValidateMarkerUpload(params));
	}
	
	/**
	 * Test case for digest.marker_prop (marker_name) == digest.marker (name)
	 */
	@Test
	public void testMarkerProp2Marker() {
		String []params = {GobiiTableType.MARKER_PROP, MARKER_NAME, rCoord, cCoord, GobiiTableType.MARKER, NAME, rCoord, cCoord};
		assertNull(callValidateMarkerUpload(params));
	}

	/**
	 * Test case for digest.marker_prop (marker_name) == digest.marker (name)
	 */
	@Test
	public void testMarkerProp2Marker_Failure() {

		String []params = {GobiiTableType.MARKER_PROP, MARKER_NAME, rCoord, cCoord, GobiiTableType.MARKER, NAME, rCoord+1, cCoord+1};
		assertNotNull(callValidateMarkerUpload(params));
	}
	
	/*
	 * Tests case for digest.marker_prop upload only if digest.marker exist
	 */
	@Test
	public void testMarkerProp2MarkerExist() {
		String []params = {GobiiTableType.MARKER_PROP, NAME, rCoord, cCoord, GobiiTableType.MARKER, NAME, rCoord, cCoord};
		assertNull(callValidateMarkerUpload(params));
	}

	/*
	 * Tests case for digest.marker_prop upload only if digest.marker exist
	 */
	@Test
	public void testMarkerProp2MarkerExist_Failure() {
		String []params = {GobiiTableType.MARKER_PROP, NAME, rCoord, cCoord};
		assertNotNull(callValidateMarkerUpload(params));
	}
	
	/*
	 * Test case for if digest.marker_linkage_group exists, then file must contain all columns lg_name, marker_name, start and stop
	 */
	@Test
	public void testMarkerLinkageGroup() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		GobiiLoaderInstruction lgMarkerInstruction = InstructionFileValidationUtil.createlgMarkerInstruction(GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME, Integer.parseInt(rCoord), Integer.parseInt(cCoord));
		lgMarkerInstruction.getGobiiFileColumns().remove(0);
		instructionList.add(lgMarkerInstruction);

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
	}

	@VisibleForTesting
	private String callValidateMarkerUpload(String[] params) {
		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();	
		for(int i= 0; i < params.length; i= i+4){
			instructionList.add(InstructionFileValidationUtil.createInstruction(params[i+0], params[i+1], Integer.parseInt(params[i+2]), Integer.parseInt(params[i+3])));
		}
		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		return instructionFileValidator.validateMarkerUpload();
	}
}