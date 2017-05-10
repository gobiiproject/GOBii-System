package org.gobiiproject.gobiiprocess.digester.csv;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

public class GobiiProcessedInstruction {

	final String AUTO_INCREMENT_START_VALUE = "0"; // TODO: Need to finalize
	private GobiiLoaderInstruction loaderInstruction;
	private List<GobiiFileColumn> columnList;
	private List<FileLineEntry> fileLine;
	private List<ArrayList<String>> requiredRows;
	private boolean hasCSV_ROW = false;
	private boolean hasCSV_COL = false;
	private boolean hasCSV_BOTH = false;
	private boolean isFirstLine = true;
	
	public GobiiProcessedInstruction(GobiiLoaderInstruction loaderInstruction) {
		this.loaderInstruction = loaderInstruction;
	}

	public GobiiLoaderInstruction getLoaderInstruction() {
		return loaderInstruction;
	}

	public List<GobiiFileColumn> getColumnList() {
		return columnList;
	}

	public List<FileLineEntry> getFileLine() {
		return fileLine;
	}

	public List<ArrayList<String>> getRequiredRows() {
		return requiredRows;
	}
	
	public void addRow(int index, ArrayList<String> rowList ){
		requiredRows.set(index, rowList);
	}

	public boolean hasCSV_ROW() {
		return hasCSV_ROW;
	}

	public boolean hasCSV_COL() {
		return hasCSV_COL;
	}

	public boolean hasCSV_BOTH() {
		return hasCSV_BOTH;
	}

	public boolean isFirstLine() {
		return isFirstLine;
	}

	public void setFirstLine(boolean isFirstLine) {
		this.isFirstLine = isFirstLine;
	}

	/**
	 * Get all the gobiiFileColums info from the instruction into columnList.
	 * Line of data to be written into temp file, is represented by fileLine.
	 * Each column is represented by three consecutive elements in fileLine
	 * gobiiFileColumnType, Value, Index in column list.
	 *
	 */
	public void parseInstruction() {
		int columnNo = 0;
		columnList = loaderInstruction.getGobiiFileColumns();
		fileLine = new ArrayList<FileLineEntry>();
		requiredRows = new ArrayList<ArrayList<String>>();
		for (GobiiFileColumn gobiiFileColumn : columnList) {
			switch (gobiiFileColumn.getGobiiColumnType()) {
			case CONSTANT:
				fileLine.add(new FileLineEntry(GobiiColumnType.CONSTANT, gobiiFileColumn.getConstantValue(), columnNo));
				break;
			case AUTOINCREMENT:
				fileLine.add(new FileLineEntry(GobiiColumnType.AUTOINCREMENT, AUTO_INCREMENT_START_VALUE, columnNo));
				break;
			case CSV_ROW:
				fileLine.add(new FileLineEntry(GobiiColumnType.CSV_ROW, "", columnNo));
				requiredRows.add(new ArrayList<String>());
				hasCSV_ROW = true;
				break;
			case CSV_COLUMN:
				fileLine.add(new FileLineEntry(GobiiColumnType.CSV_COLUMN, "", columnNo));
				hasCSV_COL = true;
				break;
			case CSV_BOTH:
				fileLine.add(new FileLineEntry(GobiiColumnType.CSV_BOTH, "", columnNo));
				hasCSV_BOTH = true;
				break;
			default:
				ErrorLogger.logError("CSVReader", "Unable to deal with file type"+gobiiFileColumn.getGobiiColumnType().name(), new Throwable());
				break;
			}
			columnNo++;
		}
	}
}

class FileLineEntry{
	
	private GobiiColumnType columnType;
	private String value;
	private int columnNo;
	
	public FileLineEntry(GobiiColumnType constantEntry, String constantValue, int columnNo) {
		this.columnType = constantEntry;
		this.value = constantValue;
		this.columnNo = columnNo;
	}

	public GobiiColumnType getColumnType() {
		return columnType;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}

	public int getColumnNo() {
		return columnNo;
	}
}
