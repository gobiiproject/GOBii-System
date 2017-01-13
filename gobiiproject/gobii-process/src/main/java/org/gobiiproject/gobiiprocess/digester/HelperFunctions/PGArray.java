package org.gobiiproject.gobiiprocess.digester.HelperFunctions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * PGArray method converts takes an input and output file and converts a column inside it into an array.
 * @author Yaw Nti'addae
 * @author Josh Lamos-Sweeney - cleanup of file processing, conversion into routine instead of standalone
 */
public class PGArray {

    private int columnIndex = -1;
    private String strColumn;
    private String strMarkerFile;
    private String strOutput;

    /**
     * Converter of a single file column into a json array format for loading into postgres jsonb columns.
     * Takes the 'strColumn' column and converts it to a jsonb array, using any non-alphabetic characters as delimiters
     * @param strMarkerFile Input file
     * @param strOutput Output file
     * @param strColumn name of column to be converted into an array format
     */
    public PGArray(String strMarkerFile, String strOutput, String strColumn){
        this.strMarkerFile = strMarkerFile;
        this.strOutput = strOutput;
        this.strColumn = strColumn;
    }

    public void process(){
        try {
            Scanner input = new Scanner(new File(strMarkerFile));
            String strHeader = input.nextLine();
            String[] arrHeader = strHeader.split("\t");
            for(int i=0; i<arrHeader.length; i++){
                if(arrHeader[i].toUpperCase().equals(strColumn.toUpperCase())){
                    columnIndex = i;
                    break;
                }
            }
            if(columnIndex == -1){
                return;
            }

            File out = new File(strOutput);
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            writer.write(strHeader);
            writer.newLine();
            while(input.hasNextLine()){
                String line = input.nextLine();
                String[] arr = line.split("\t");
                convertToArray(arr, columnIndex);
                writer.write(StringUtils.join(arr, "\t"));
                writer.newLine();
            }
            input.close();
            writer.close();
        } catch (FileNotFoundException err) {
            ErrorLogger.logError("PGArray","Error processing converting column to array file.",err);

        } catch (Exception err) {
            ErrorLogger.logError("PGArray","Error processing converting column to array file.",err);
        }
    }

    private void convertToArray(String[] arr,int colIndex){
        String col = arr[colIndex];
        String newCol = "\"{\"\"" + col.replaceAll("[^A-Za-z\\-]", "\"\",\"\"") + "\"\"}\"";
        arr[colIndex] = newCol;
    }
}
