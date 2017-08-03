package org.gobiiproject.gobiiprocess.digester.validation;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiTableType;
import com.google.common.annotations.VisibleForTesting;

/**
 * Unit test for InstructionFileValidationTest.
 */
public class InstructionFileValidationUtil {

	private final static String MARKER_NAME = "marker_name";
	private final static String LINKAGE_GROUP_NAME = "linkage_group_name";
	private final static String STOP = "stop";
	private final static String START = "start";

	@VisibleForTesting
	static GobiiLoaderInstruction createInstruction(String tableName, String nameValue, int rCoord, int cCoord) {
		if(tableName.equals(GobiiTableType.MARKER_LINKAGE_GROUP)){
			return createlgMarkerInstruction(tableName, nameValue, rCoord, cCoord);
		}
		else{
			GobiiLoaderInstruction markerInstruction = new GobiiLoaderInstruction();
			markerInstruction.setTable(tableName);
			List<GobiiFileColumn> linkageGroupGobiiColumns = new ArrayList<>();
			linkageGroupGobiiColumns.add(createGobiiColumn(nameValue, rCoord, cCoord));
			markerInstruction.setGobiiFileColumns(linkageGroupGobiiColumns);
			return markerInstruction;
		}
	}

	/*
	 * Helper method to create lgMarkerInstruction.
	 * Must contain MARKER_NAME, LINKAGE_GROUP_NAME, START, STOP
	 * Ignores nameValue parameter. Just there for uniformity
	 */
	@VisibleForTesting
	static GobiiLoaderInstruction createlgMarkerInstruction(String tableName, String nameValue,int rCoord, int cCoord) {
		GobiiLoaderInstruction lgMarkerInstruction = new GobiiLoaderInstruction();
		lgMarkerInstruction.setTable(tableName);
		List<GobiiFileColumn> lgMarkerGobiiColumns = new ArrayList<>();
		lgMarkerGobiiColumns.add(createGobiiColumn(MARKER_NAME, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(LINKAGE_GROUP_NAME, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(START, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(STOP, rCoord, cCoord));
		lgMarkerInstruction.setGobiiFileColumns(lgMarkerGobiiColumns);
		return lgMarkerInstruction;
	}

	@VisibleForTesting
	static GobiiFileColumn createGobiiColumn(String name, Integer rCoord, Integer cCoord) {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_ROW);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName(name);
		return fileColumn;
	}


}