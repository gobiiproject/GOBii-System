package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiTableType;
import java.util.*;

public class InstructionFileValidator {

    private final String NAME = "name";
    private final String MARKER_NAME = "marker_name";
    private final String LINKAGE_GROUP_NAME = "linkage_group_name";
    private final String STOP = "stop";
    private final String START = "start";
    private final String EXTERNAL_CODE = "external_code";
    private final String DNA_SAMPLE_NAME = "dnasample_name";
    private final String DNA_RUN_NAME = "dnarun_name";
    private final String MATRIX = "matrix";

    private static String statusMessage = null;

    private List<GobiiLoaderInstruction> instructionList;
    private HashMap<TableEntryKey, TableEntryValue> instructionMap = new HashMap<>();

    public InstructionFileValidator(List<GobiiLoaderInstruction> instructionList) {
        this.instructionList = instructionList;
        statusMessage = null;
    }


    /**
     * Processes the instruction file. Takes the list of instructions and created a hashmap out of it.
     * Hashmap
     * Key: hash(table, gobiiFileColumn_NAME) if column is of AutoIncrement or Constant we dont make an entry.
     * Value: Object with required fields. Currently rCoord and cCoord.
     */
    public void processInstructionFile() {

        // Care about CSV_ROW, CSV_COL, CSV_BOTH only
        // Add more types as they are supported by GOBII.
        Set<GobiiColumnType> requiredColumnTypes = new HashSet<>();
        requiredColumnTypes.add(GobiiColumnType.CSV_BOTH);
        requiredColumnTypes.add(GobiiColumnType.CSV_ROW);
        requiredColumnTypes.add(GobiiColumnType.CSV_COLUMN);


        for (GobiiLoaderInstruction instruction : instructionList) {
            for (GobiiFileColumn fileColumn : instruction.getGobiiFileColumns()) {
                if (requiredColumnTypes.contains(fileColumn.getGobiiColumnType())) {
                    TableEntryKey entryKey = new TableEntryKey(instruction.getTable(), fileColumn.getName());
                    TableEntryValue entryValue = new TableEntryValue(fileColumn.getName(), fileColumn.getrCoord(), fileColumn.getcCoord());
                    instructionMap.put(entryKey, entryValue);
                }
            }
        }
    }

    public String validateMarkerUpload() {

        /*
            Validation Rule:
                digest.marker (name) == digest.lg_marker (marker_name)
         */
        if(!validateColumn(GobiiTableType.MARKER, NAME, GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME)){
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.lg_marker (lg_name) == digest.linkage_group (name)
         */
        if (!validateColumn(GobiiTableType.MARKER_LINKAGE_GROUP, LINKAGE_GROUP_NAME, GobiiTableType.LINKAGE_GROUP, NAME)) {
            return statusMessage;
        }
        /*
            Validation Rule:
                digest.marker_prop(marker_name) == digest.marker (name)
         */
        if (!validateColumn(GobiiTableType.MARKER_PROP, MARKER_NAME, GobiiTableType.MARKER, NAME)) {
            return statusMessage;
        }

         /*
            Validation Rule:
                digest.marker_prop upload only if digest.marker exist
         */
        //Assumption
        // Marker table will always have a column for name.
        // Marker_prop table will always have a columns for name.
        if (!validateTableExist(GobiiTableType.MARKER_PROP, NAME, GobiiTableType.MARKER, NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                if digest.marker_linkage_group exists, then file must contain all columns lg_name, marker_name, start and stop
         */
        // If one of columns lg_name, marker_name, start and stop exist all other should exist
        if (instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME)) || instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, LINKAGE_GROUP_NAME)) || instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, START)) || instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, STOP))) {
            if (!(instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, MARKER_NAME)) && instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, LINKAGE_GROUP_NAME)) && instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, START)) && instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER_LINKAGE_GROUP, STOP)))) {
                statusMessage = "Marker_linkage_group exists but doesn't contain all columns lg_name, marker_name, start and stop";
                return statusMessage;

            }
        }

        return statusMessage;
    }

    private boolean validateTableExist(String table1, String column1, String table2, String column2) {
        if (instructionMap.containsKey(new TableEntryKey(table1, column1))) {
            if (!instructionMap.containsKey(new TableEntryKey(table2, column2))) {
                statusMessage = table1 + " exists without " + table2;
                return false;
            }
        }
        return true;
    }

    private boolean validateColumn(String table1, String column1, String table2, String column2) {
        if (instructionMap.containsKey(new TableEntryKey(table1, column1)) && instructionMap.containsKey(new TableEntryKey(table2, column2))) {
            // If both exist then lets compare else skip the test.
            TableEntryValue entry1 = instructionMap.get(new TableEntryKey(table1, column1));
            TableEntryValue entry2 = instructionMap.get(new TableEntryKey(table2, column2));
            if (!(entry1.getrCoord() == entry2.getrCoord() && entry1.getcCoord() == entry2.getcCoord())) {
                // At-least one of the two comparisons fails.
                statusMessage = "Mismatch between " + table1 + "(" + column1 + ") and " + table2 + "(" + column2 + ")";
                return false;
            }
        }
        return true;
    }

    public String validateSampleUpload() {
        /*
            Validation Rule:
                digest.germplasm (external_code) == digest.dnasample (external_code)
         */
        if (!validateColumn(GobiiTableType.GERMPLASM, EXTERNAL_CODE, GobiiTableType.DNASAMPLE, EXTERNAL_CODE)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.dnasample (name) == digest.dnarun (dnasample_name)
        */
        if (!validateColumn(GobiiTableType.DNASAMPLE, NAME, GobiiTableType.DNARUN, DNA_SAMPLE_NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.germplasm_prop upload only if digest.germplasm exists
        */
        //Assumption
        // Germplasm table will always have a column for name.
        // Germplasm_prop table will always have a columns for name.
        if (!validateTableExist(GobiiTableType.GERMPLASM_PROP, NAME, GobiiTableType.GERMPLASM, NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.germplasm_prop (external_code) == digest.germplasm (external_code)
        */
        if (!validateColumn(GobiiTableType.GERMPLASM_PROP, EXTERNAL_CODE, GobiiTableType.GERMPLASM, EXTERNAL_CODE)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.dnasample_prop upload only if digest.dnasample exists
        */
        //Assumption
        // DnaSample table will always have a column for name.
        // DnaSample_prop table will always have a columns for name.
        if (!validateTableExist(GobiiTableType.DNASAMPLE_PROP, NAME, GobiiTableType.DNASAMPLE, NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.dnasample_prop (dnasample_name) == digest.dnasample (name)
        */
        if (!validateColumn(GobiiTableType.DNASAMPLE_PROP, DNA_SAMPLE_NAME, GobiiTableType.DNASAMPLE, NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.dnarun_prop upload only if digest.dnarun file exists
        */
        //Assumption
        // DnaRun table will always have a column for name.
        // DnaRun_prop table will always have a columns for name.
        if (!validateTableExist(GobiiTableType.DNARUN_PROP, NAME, GobiiTableType.DNARUN, NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                digest.dnarun_prop (dnarun_name) == digest.dnarun (name)
        */
        if (!validateColumn(GobiiTableType.DNARUN_PROP, DNA_RUN_NAME, GobiiTableType.DNARUN, NAME)) {
            return statusMessage;
        }

        return statusMessage;
    }

    public String validate() {

        /*
            Validation Rule:
                (IF digest.matrix EXISTS)
                    digest.dataset_marker exists
        */
        //Assumption
        // Matrix table will always have a column for matrix.
        // dataset_marker table will always have a columns for marker_name.
        if (!validateTableExist(GobiiTableType.MATRIX, MATRIX, GobiiTableType.DATASET_MARKER, MARKER_NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                (IF digest.matrix EXISTS)
                    digest.dataset_dnarun exists

        */
        //Assumption
        // Matrix table will always have a column for matrix.
        // dataset_dnarun table will always have a columns for marker_name.
        if (!validateTableExist(GobiiTableType.MATRIX, MATRIX, GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME)) {
            return statusMessage;
        }

        /*
            Validation Rule:
                    if digest.marker file exists
                    digest.marker (name) == digest.dataset_marker (marker_name)
        */
        //Assumption
        // MARKER table will always have a column for name.
        if (instructionMap.containsKey(new TableEntryKey(GobiiTableType.MARKER, NAME))) {
            if (!validateColumn(GobiiTableType.MARKER, NAME, GobiiTableType.DATASET_MARKER, MARKER_NAME)) {
                return statusMessage;
            }
        }

        /*
            Validation Rule:
                    if digest.dnarun file exists
                     digest.dnarun (name) == digest.dataSet_dnarun (dnarun_name)
        */
        //Assumption
        // DNARUN table will always have a column for name.
        if (instructionMap.containsKey(new TableEntryKey(GobiiTableType.DNARUN, NAME))) {
            if (!validateColumn(GobiiTableType.DNARUN, NAME, GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME)) {
                return statusMessage;
            }
        }

        return statusMessage;
    }
}

class TableEntryValue {
    private String name;
    private int rCoord;
    private int cCoord;

    TableEntryValue(String name, int rCoord, int cCoord) {
        this.name = name;
        this.rCoord = rCoord;
        this.cCoord = cCoord;
    }

    public String getName() {
        return name;
    }

    int getrCoord() {
        return rCoord;
    }

    int getcCoord() {
        return cCoord;
    }
}

/*
    Key for table entry for the hashmap.
 */
class TableEntryKey {
    private String table, columnName;

    TableEntryKey(String table, String column) {
        this.table = table;
        this.columnName = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableEntryKey that = (TableEntryKey) o;
        if (table != null ? !table.equals(that.table) : that.table != null) return false;
        return columnName != null ? columnName.equals(that.columnName) : that.columnName == null;
    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        return result;
    }
}