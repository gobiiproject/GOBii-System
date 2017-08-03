package org.gobiiproject.gobiiprocess.digester.vcf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * Created by AVB on 1/5/2017.
 *
 * Bares all the matrix data by rejecting anything after the ":" character inclusive and
 * creates a file of bimatrix data in the "bimatrix" file by using
 * the following rules:
 * a) the (bare) matrix cell must be X/Y where X, Y can be 0 or 1 or . so there are 9 possibilities: 0/0, 0/1, 0/., 1/0, 1/1, 1/., ./0, ./1, and ./.
 * b) 0 from the matrix cell -> the cell from the same row in the .mref matrix but in the first column "ref"
 * c) 1 from the matrix cell -> the cell from the same row in the .mref matrix but in the second column "alt"
 * d) . from the matrix cell -> N
 **/
public class VCFTransformer {
	/**
	 * Creates a new VCFTransformer using the path arguments of the ".mref" and "matrix" files to capture all their data
	 * as well as the path argument of the new "bimatrix" file to dump the transformed VCF data
	 */
	public VCFTransformer(String mrefFilePath, String matrixFilePath, String bimatrixFilePath) {
		String mrefLine,matrixLine;
		try {
			BufferedReader mrefFileBufferedReader = new BufferedReader(new FileReader(new File(mrefFilePath)));
			// Omitting the .mref header
			mrefFileBufferedReader.readLine();
			BufferedReader matrixFileBufferedReader = new BufferedReader(new FileReader(new File(matrixFilePath)));
			// Omitting the matrix header
			matrixFileBufferedReader.readLine();
			BufferedWriter bimatrixFileBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(bimatrixFilePath))));
			bimatrixFileBufferedWriter.write("bi matrix");
			bimatrixFileBufferedWriter.newLine();
			while (((mrefLine = mrefFileBufferedReader.readLine()) != null) &&
				   ((matrixLine = matrixFileBufferedReader.readLine()) != null)) {
				String[] mrefLineRawData = mrefLine.trim().toUpperCase().split("\\s+");
				String[] mrefAltData = mrefLineRawData[1].split("[^A-Za-z\\-]+");//Split on non-alphabetic, may be jsonified already
				int offset=0;//offset for first alt being blank (happens with this regex and split (freaking split)
				if(mrefAltData[0].equals(""))offset=1;
				String mrefLineData[]=new String[mrefAltData.length+1-offset];//List of 1 + alts length
				mrefLineData[0]=mrefLineRawData[0];
				System.arraycopy(mrefAltData,0+offset,mrefLineData,1,mrefAltData.length-offset);

				String[] matrixLineData = matrixLine.trim().split("\\s+");
				int columnsNumber = matrixLineData.length;
				for (int i = 0; i < columnsNumber; i++) {
					matrixLineData[i] = matrixLineData[i].split(":")[0];
					String[] terms = matrixLineData[i].split("/");
					if (terms.length != 2) {
						mrefFileBufferedReader.close();
						matrixFileBufferedReader.close();
						bimatrixFileBufferedWriter.flush();
						bimatrixFileBufferedWriter.close();
						ErrorLogger.logError("VCFTransformer", "Incorrect number of alleles: " + matrixLineData[i]
								+ "\n Line "+ matrixLine, new Exception());
						return;
					}
					String bimatrixCell = "";
					for (int k = 0; k < 2; k++) {
						switch (terms[k]) {
							case "0": case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9":
								int value=Integer.parseInt(terms[k]);
								if(k >= mrefLineData.length) {
									throw new IOException("Referenced alternate number is greater than alt list length: " + terms[k] + " > " + mrefLineData.length);
								}
								bimatrixCell = bimatrixCell + mrefLineData[value];
								break;
							case ".":
								bimatrixCell = bimatrixCell + "N";
								break;
							default: 
								mrefFileBufferedReader.close();
								matrixFileBufferedReader.close();
								bimatrixFileBufferedWriter.flush();
								bimatrixFileBufferedWriter.close();
								ErrorLogger.logError("VCFTransformer", "Unsupported alternate: " + terms[k], new Exception());
								return;
						}
					}
					bimatrixFileBufferedWriter.write(bimatrixCell);
					bimatrixFileBufferedWriter.write("\t");
				}
				bimatrixFileBufferedWriter.newLine();
			}
			mrefFileBufferedReader.close();
			matrixFileBufferedReader.close();
			bimatrixFileBufferedWriter.flush();
			bimatrixFileBufferedWriter.close();
		} catch (IOException e) {
			ErrorLogger.logError("VCFTransformer", "File access error", e);
		}
	}

    /**
     * Generates a marker reference file from a marker file
     * If input is name ref alt blah blah
     * output is ref alt
     * @param markerFile marker file
     * @param outFile
     */
    public static void generateMarkerReference(String markerFile, String outFile, String errorPath) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(markerFile));
        String[] headers = br.readLine().split("\\s+");
        br.close();
        String ref="ref",alt="alt";
        int refPos=-1;
        int altPos=-1;
        for(int i=0;i<headers.length;i++){
            if(headers[i].contains(ref)){
                refPos=i+1;break;//cut is 1 based
            }
        }
        for(int i=0;i<headers.length;i++){
            if(headers[i].contains(alt)){
                altPos=i+1;break;//cut is 1 based
            }

        }
        if((refPos==-1)||(altPos==-1)){
            ErrorLogger.logError("VCFTransformer","Could not find one of Ref or Alt in file: "+markerFile);
        }

        HelperFunctions.tryExec("cut -f"+refPos+","+altPos+ " "+markerFile,outFile,errorPath);
    }
}
