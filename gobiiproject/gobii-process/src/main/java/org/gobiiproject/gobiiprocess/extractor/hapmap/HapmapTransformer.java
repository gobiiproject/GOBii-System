package org.gobiiproject.gobiiprocess.extractor.hapmap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;

public class HapmapTransformer {

	private static Integer dnaIndex = -1;

	public boolean generateFile(String markerFileIn,
								String sampleFileIn,
								String mapFileIn,
								String genotypeFileIn,
								String outFile,
								String errorFile) throws IOException {
		File markerFile = new File(markerFileIn);
		if (!(markerFile.exists())) {
			ErrorLogger.logError("Extractor","Marker file not found", errorFile);
			return false;
		}
		File sampleFile = new File(sampleFileIn);
		if (!(sampleFile.exists())) {
			ErrorLogger.logError("Extractor","Sample file not found", errorFile);
			return false;
		}
		File mapFile = null;
		if (mapFileIn != null) {
			mapFile = new File(mapFileIn);
			if (!(mapFile.exists())) {
				ErrorLogger.logError("Extractor","Map file not found", errorFile);
				return false;
			}
		}
		File genotypeFile = new File(genotypeFileIn);
		if (!(genotypeFile.exists())) {
			ErrorLogger.logError("Extractor","Genotype file not found", errorFile);
			return false;
		}
		Scanner markerScanner = new Scanner(markerFile);
		Scanner sampleScanner = new Scanner(sampleFile);
		Scanner mapScanner = new Scanner(mapFile);
		Scanner genotypeScanner = new Scanner(genotypeFile);

		if (sampleScanner.hasNext()) {
			List<String> sampleHeaders = new ArrayList<>();
			TreeMap<Integer, ArrayList<String>> sampleData = new TreeMap<>();
			String[] headers = sampleScanner.next().split("\\t");
			for (int index = 0; index < headers.length; index++) {
				sampleHeaders.add(headers[index].trim());
				sampleData.put(index, new ArrayList<String>());
			}
			while (sampleScanner.hasNextLine()) {
				String[] sampleRecords = sampleScanner.nextLine().split("\\t");
				for (int index = 0; index < sampleRecords.length; index++) {
					if (!(sampleData.containsKey(index))) {
						sampleData.put(index, new ArrayList<String>());
					}
					sampleData.get(index).add(sampleRecords[index].trim());
				}
			}
			sampleScanner.close();
			try {
				File out = new File(outFile);
				rmIfExist(outFile);
				FileWriter fileWriter = new FileWriter(out);

				// Transposing all the sample data with the exception of
				// the dnarun_name column into the output file
				for (Map.Entry<Integer, ArrayList<String>> entry : sampleData.entrySet()) {
					String header = sampleHeaders.get(entry.getKey());
					if (header.equals("dnarun_name")) {
						dnaIndex = entry.getKey();
						continue;
					}
					StringBuilder stringBuilderNewLine = new StringBuilder("#\t\t\t\t\t\t\t\t\t\t");
					stringBuilderNewLine.append(header);
					stringBuilderNewLine.append("\t");
					stringBuilderNewLine.append(StringUtils.join(entry.getValue(), "\t"));
					stringBuilderNewLine.append(System.lineSeparator());
					fileWriter.write(stringBuilderNewLine.toString());
				}
				fileWriter.flush();

				List<String> markerHeaders = new ArrayList<String>();
				if (markerScanner.hasNext()) {
					headers = markerScanner.next().split("\\t");
					for (int index = 0; index < headers.length; index++) {
						markerHeaders.add(headers[index].trim());
					}
				}
				else {
					ErrorLogger.logError("Extractor","Marker data file empty", errorFile);
					return false;
				}

				// Writing the new marker header into the current line
				String[] newMarkerHeadersLine = new String[]{
						"marker_name",
						"alleles",
						"chrom",
						"pos",
						"strand",
						"assembly#",
						"center",
						"protLSID",
						"assayLSID",
						"panelLSID",
						"QCcode"};
				StringBuilder stringBuilderNewLine = new StringBuilder(StringUtils.join(newMarkerHeadersLine, "\t"));

				// Writing the map header into the current line if the map file exists
				if(mapScanner != null) {
					if (mapScanner.hasNext()) {
						String mapHeadersLine = mapScanner.next();
						stringBuilderNewLine.append(mapHeadersLine + "\t");
						headers = mapHeadersLine.split("\\t");
						List<String> mapHeaders = new ArrayList<>();
						for (int index = 0; index < headers.length; index++) {
							mapHeaders.add(headers[index].trim());
						}
					}
					else {
						ErrorLogger.logInfo("Extractor","Map data file empty");
					}
				}

				// Transposing and writing the dnarun_name column into the current line,
				// and then writing the current line into the output file
				stringBuilderNewLine.append(StringUtils.join(sampleData.get(dnaIndex), "\t"));
				stringBuilderNewLine.append(System.lineSeparator());
				fileWriter.write(stringBuilderNewLine.toString());

				while(markerScanner.hasNextLine() && genotypeScanner.hasNextLine()){
					// Writing the marker data line in alignment to the marker headers
					// into the current line.
					// All the other (new) marker header columns not used are left blank.
					String markerLine = markerScanner.nextLine();
					String[] markerLineParts = markerLine.split("\\t");
					stringBuilderNewLine = new StringBuilder();
					for (String newMarkerHeader : newMarkerHeadersLine) {
						if (markerHeaders.contains(newMarkerHeader))
						{
							stringBuilderNewLine.append(markerLineParts[markerHeaders.indexOf(newMarkerHeader)]);
						}
						stringBuilderNewLine.append("\t");
					}

					// Writing the genotype line into the current line,
					// and then writing the current line into the output file
					stringBuilderNewLine.append(genotypeScanner.nextLine());
					stringBuilderNewLine.append(System.lineSeparator());
					fileWriter.write(stringBuilderNewLine.toString());
				}

				fileWriter.close();
				markerScanner.close();
				mapScanner.close();
				genotypeScanner.close();
			} catch (IOException e) {
				ErrorLogger.logError("Extractor","Error writing " + outFile + "." + System.lineSeparator() + System.lineSeparator() + "Reason: " + e.getMessage(), errorFile);
				return false;
			}
		}
		else {
			ErrorLogger.logError("Extractor","Sample data file empty", errorFile);
			return false;
		}

		return true;
	}
}
