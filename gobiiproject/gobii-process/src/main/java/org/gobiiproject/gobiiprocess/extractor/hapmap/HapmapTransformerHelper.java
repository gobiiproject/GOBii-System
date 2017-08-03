package org.gobiiproject.gobiiprocess.extractor.hapmap;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Helper function class for hapmap transformer
 */
class HapmapTransformerHelper {

    /**
     * Reads the header information from marker and extended Marker file.
     *
     * @param scanner     marker/extendedMarker scanner
     * @param fileHeaders List to store marker/extendedMarker headers.
     * @param info        Whether marker/extendedMarker used in logging.
     * @return Status of reading the line.
     */
    static boolean readHeaders(Scanner scanner, List<String> fileHeaders, String info) {
        if (scanner.hasNextLine()) {
            String[] headers = scanner.nextLine().split("\\t", -1);
            ErrorLogger.logInfo("Extractor", headers.length + info + " header columns read");
            for (String header : headers) {
                fileHeaders.add(header.trim());
            }
            return true;
        }
        return false;
    }

    /**
     * Transposing all the sample data with the exception of the dnarun_name column into the output file.
     *
     * @param sampleHeaders Sample file header information.
     * @param sampleData    Sample file.
     * @param fileWriter    hapmap file writer.
     * @throws IOException Error in writing to the output file.
     */
    static int writeSampleInfoOutputFile(List<String> sampleHeaders, TreeMap<Integer, ArrayList<String>> sampleData, FileWriter fileWriter) throws IOException {

        int dnaIndex = -1;
        for (Map.Entry<Integer, ArrayList<String>> entry : sampleData.entrySet()) {
            String header = sampleHeaders.get(entry.getKey());
            if (header.equals(HapmapConstants.DNA_RUN_NAME)) {
                dnaIndex = entry.getKey();
                continue;
            }
           String stringNewLine="#\t\t\t\t\t\t\t\t\t\t"+header+"\t"+StringUtils.join(entry.getValue(), "\t")+System.lineSeparator();
            fileWriter.write(stringNewLine);
        }
        fileWriter.flush();
        return dnaIndex;
    }

    /**
     * Process the sample file.
     * Seperately process the header followed by rest of the file.
     * @param sampleScanner sample scanner.
     * @param sampleHeaders Sample header.
     * @param sampleData Sample data.
     * @return No od lines from sample file processed.
     */
    static int processSampleFile(Scanner sampleScanner, List<String> sampleHeaders, TreeMap<Integer, ArrayList<String>> sampleData) {
        // Read header from sample file(1st Row) and populate sampleHeader and sampleData.
        readSampleHeader(sampleScanner, sampleHeaders, sampleData);

        // Read rest of sample file(except 1st Row) and populate sampleData. Return the no of lines processed.
        return readSampleData(sampleScanner, sampleData);
    }

    /**
     * Reads the sampleFile (first line already read by readSampleHeader).
     *
     * @param sampleScanner File Scanner
     * @param sampleData    Tree-map to store header along with the data corresponding to the header.
     * @return no fo lines read from sampleFile.
     */
    private static int readSampleData(Scanner sampleScanner, TreeMap<Integer, ArrayList<String>> sampleData) {
        int sampleRowsNumber = 0;
        while (sampleScanner.hasNextLine()) {
            String[] sampleRecords = sampleScanner.nextLine().split("\\t", -1);
            for (int index = 0; index < sampleRecords.length; index++) {
                if (!(sampleData.containsKey(index))) {
                    sampleData.put(index, new ArrayList<>());
                }
                sampleData.get(index).add(sampleRecords[index].trim());
            }
            sampleRowsNumber = sampleRowsNumber + 1;
        }
        return sampleRowsNumber;
    }

    /**
     * Reads the first line of sampleFile.
     *
     * @param sampleScanner File Scanner.
     * @param sampleHeaders List to store Header information.
     * @param sampleData    Tree-map to store header along with the data corresponding to the header.
     */
    private static void readSampleHeader(Scanner sampleScanner, List<String> sampleHeaders, TreeMap<Integer, ArrayList<String>> sampleData) {
        String[] headers = sampleScanner.nextLine().split("\\t", -1);
        ErrorLogger.logInfo("Extractor", headers.length + " sample header columns read");
        for (int index = 0; index < headers.length; index++) {
            sampleHeaders.add(headers[index].trim());
            sampleData.put(index, new ArrayList<>());
        }
    }

    /**
     * Reads Marker and Genotype files line by line and writes to the output hapmap file.
     * @param hapmapScanners Object containing all the scanners required.
     * @param fileWriter Hapmap file writer.
     * @param markerHeaders Marker file Headers
     * @param extendedMarkerHeaders Extended Marker File Headers
     * @param newMarkerHeadersLine New Marker Header Line
     * @return no of lines written to the file.
     * @throws IOException Issue with I/O.
     */
    static int writeMarkerAndGenoTypeInfo(HapmapScanners hapmapScanners, FileWriter fileWriter, List<String> markerHeaders, List<String> extendedMarkerHeaders, String[] newMarkerHeadersLine) throws IOException {
        int processedBothRowsNumber = 0;
        StringBuilder stringBuilderNewLine;
        System.out.println("sup");
        MarkerAndExtendedMarkerIndex markerAndExtendedMarkerIndex = new MarkerAndExtendedMarkerIndex(markerHeaders, extendedMarkerHeaders);

        while (hapmapScanners.markerScanner.hasNextLine() && hapmapScanners.genotypeScanner.hasNextLine()) {
            // Writing the marker (and extended marker if existent) data line(s)
            // in alignment to the new marker headers into the current line.
            // All the other new marker header columns not matched are left blank.
            String[] markerLineParts = hapmapScanners.markerScanner.nextLine().split("\\t", -1);
            String[] extendedMarkerLineParts = null;
            if (hapmapScanners.extendedMarkerScanner != null) {
                if (hapmapScanners.extendedMarkerScanner.hasNextLine()) {
                    extendedMarkerLineParts = hapmapScanners.extendedMarkerScanner.nextLine().split("\\t", -1);
                }
            }
            stringBuilderNewLine = new StringBuilder();
            for (String newMarkerHeader : newMarkerHeadersLine) {
                // Old marker header to include data from marker line
                if (markerHeaders.contains(newMarkerHeader)) {
                    stringBuilderNewLine.append(markerLineParts[markerHeaders.indexOf(newMarkerHeader)]);
                }
                // New marker header to include data from extended marker line and if no then marker line
                else {
                    switch (newMarkerHeader) {
                        case HapmapConstants.RS:
                            if (extendedMarkerLineParts != null) {
                                stringBuilderNewLine.append(extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerNameIndex()]);
                            } else {
                                stringBuilderNewLine.append(markerLineParts[markerAndExtendedMarkerIndex.getMarkerNameIndex()]);
                            }
                            break;
                        case HapmapConstants.ALLELES:
                            String ref;
                            String alt;
                            if (extendedMarkerLineParts != null) {
                                ref = extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerRefHeaderIndex()];
                                alt = extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerAltsHeaderIndex()];
                            } else {
                                ref = markerLineParts[markerAndExtendedMarkerIndex.getMarkerRefHeaderIndex()];
                                alt = markerLineParts[markerAndExtendedMarkerIndex.getMarkerAltsHeaderIndex()];
                            }
                            if ((!(ref.equals(""))) && (!(alt.equals("")))) {
                                stringBuilderNewLine.append(ref);
                                stringBuilderNewLine.append("/");
                                stringBuilderNewLine.append(alt);
                            }
                            break;
                        case HapmapConstants.CHROM:
                            if (extendedMarkerLineParts != null) {
                                stringBuilderNewLine.append(extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerLinkageGroupNameHeaderIndex()]);
                            }
                            break;
                        case HapmapConstants.POS:
                            if (extendedMarkerLineParts != null) {
                                stringBuilderNewLine.append(extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerLinkageGroupStartHeaderIndex()]);
                            }
                            break;
                        case HapmapConstants.STRAND:
                            if (extendedMarkerLineParts != null) {
                                stringBuilderNewLine.append(extendedMarkerLineParts[markerAndExtendedMarkerIndex.getExtendedMarkerStrandHeaderIndex()]);
                            } else {
                                stringBuilderNewLine.append(markerLineParts[markerAndExtendedMarkerIndex.getMarkerStrandHeaderIndex()]);
                            }
                            break;
                        case HapmapConstants.ASSEMBLY:
                            break;
                        case HapmapConstants.CENTER:
                            break;
                        case HapmapConstants.PROT_LSID:
                            break;
                        case HapmapConstants.ASSAY_LSID:
                            break;
                        case HapmapConstants.PANEL_LSID:
                            break;
                        case HapmapConstants.QC_CODE:
                            break;
                        default:
                    }
                }
                stringBuilderNewLine.append("\t");
            }

            // Writing the genotype line into the current line,
            // and then writing the current line into the output file
            stringBuilderNewLine.append(hapmapScanners.genotypeScanner.nextLine());
            stringBuilderNewLine.append(System.lineSeparator());
            fileWriter.write(stringBuilderNewLine.toString());
            processedBothRowsNumber = processedBothRowsNumber + 1;
        }
        return processedBothRowsNumber;
    }

}

/**
 * Helper class to get the index required from marker and extendedMarker.
 */
class MarkerAndExtendedMarkerIndex {
    private List<String> markerHeaders;
    private List<String> extendedMarkerHeaders;
    private int extendedMarkerNameIndex;
    private int markerNameIndex;
    private int extendedMarkerRefHeaderIndex;
    private int markerRefHeaderIndex;
    private int extendedMarkerAltsHeaderIndex;
    private int markerAltsHeaderIndex;
    private int extendedMarkerLinkageGroupNameHeaderIndex;
    private int extendedMarkerLinkageGroupStartHeaderIndex;
    private int extendedMarkerStrandHeaderIndex;
    private int markerStrandHeaderIndex;

    private final String MARKER_NAME =  "marker_name";
    private final String MARKER_REF = "marker_ref";
    private final String MARKER_ALTS = "marker_alts";
    private final String LINKAGE_GROUP_NAME = "linkage_group_name";
    private final String MARKER_LINKAGE_GROUP_START = "marker_linkage_group_start";
    private final String MARKER_STRAND = "marker_strand";

    MarkerAndExtendedMarkerIndex(List<String> markerHeaders, List<String> extendedMarkerHeaders) {
        this.markerHeaders = markerHeaders;
        this.extendedMarkerHeaders = extendedMarkerHeaders;
        parse();
    }

    int getExtendedMarkerNameIndex() {
        return extendedMarkerNameIndex;
    }

    int getMarkerNameIndex() {
        return markerNameIndex;
    }

    int getExtendedMarkerRefHeaderIndex() {
        return extendedMarkerRefHeaderIndex;
    }

    int getMarkerRefHeaderIndex() {
        return markerRefHeaderIndex;
    }

    int getExtendedMarkerAltsHeaderIndex() {
        return extendedMarkerAltsHeaderIndex;
    }

    int getMarkerAltsHeaderIndex() {
        return markerAltsHeaderIndex;
    }

    int getExtendedMarkerLinkageGroupNameHeaderIndex() {
        return extendedMarkerLinkageGroupNameHeaderIndex;
    }

    int getExtendedMarkerLinkageGroupStartHeaderIndex() {
        return extendedMarkerLinkageGroupStartHeaderIndex;
    }

    int getExtendedMarkerStrandHeaderIndex() {
        return extendedMarkerStrandHeaderIndex;
    }

    int getMarkerStrandHeaderIndex() {
        return markerStrandHeaderIndex;
    }

    private void parse() {
        extendedMarkerNameIndex = extendedMarkerHeaders.indexOf(MARKER_NAME);
        markerNameIndex = markerHeaders.indexOf(MARKER_NAME);

        extendedMarkerRefHeaderIndex = extendedMarkerHeaders.indexOf(MARKER_REF);
        markerRefHeaderIndex = markerHeaders.indexOf(MARKER_REF);

        extendedMarkerAltsHeaderIndex = extendedMarkerHeaders.indexOf(MARKER_ALTS);
        markerAltsHeaderIndex = markerHeaders.indexOf(MARKER_ALTS);

        extendedMarkerLinkageGroupNameHeaderIndex = extendedMarkerHeaders.indexOf(LINKAGE_GROUP_NAME);
        // The "marker.file" file does not contain the "linkage_group_name" column header

        extendedMarkerLinkageGroupStartHeaderIndex = extendedMarkerHeaders.indexOf(MARKER_LINKAGE_GROUP_START);
        // The "marker.file" file does not contain the "marker_linkage_group_start" column header

        extendedMarkerStrandHeaderIndex = extendedMarkerHeaders.indexOf(MARKER_STRAND);
        markerStrandHeaderIndex = markerHeaders.indexOf(MARKER_STRAND);
    }
}

