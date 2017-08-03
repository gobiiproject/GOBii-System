package edu.cornell.gobii.gdi.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.cornell.gobii.gdi.forms.DlgFileTransfer;
import edu.cornell.gobii.gdi.forms.DlgWizTemplate;
import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.objects.xml.Columns.Column;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.wizards.datasets.DTOdataset;
import edu.cornell.gobii.gdi.wizards.datasets.WizardDataset;
import edu.cornell.gobii.gdi.wizards.dnasamples.DTOsamples;
import edu.cornell.gobii.gdi.wizards.dnasamples.WizardDNAsamples;
import edu.cornell.gobii.gdi.wizards.markers.DTOmarkers;
import edu.cornell.gobii.gdi.wizards.markers.WizardMarkers;

public class WizardUtils {

	private static Logger log = Logger.getLogger(WizardUtils.class.getName());

	public static enum TemplateCode{
		DNA
		, DSS
		, MKR
	}

	public static void entrySetToCombo(Set<Entry<String, String>> entrySet, Combo combo) {
		// TODO Auto-generated method stub
		if(combo.getItemCount()>0) combo.removeAll();
		for (Entry entry : entrySet){ //add contact on list
			combo.add((String) entry.getValue()); //contact name
			combo.setData((String) entry.getValue(), entry.getKey()); // pair name with id
		}
	}

	public static void saveTemplate(Shell shell, TemplateCode code, LoaderInstructionFilesDTO instructions) {
		if(!confirm("Do you want save template?")) return;
		DlgWizTemplate template = new DlgWizTemplate(shell);
		if(template.open() == Window.OK){
			String dir = App.INSTANCE.getConfigDir()+"/templates/";
			File fdir = new File(dir);
			if (!fdir.exists()) {
				try{
					fdir.mkdir();
				} 
				catch(SecurityException se){
					Utils.log(shell, null, log, "Error creating templates folder", se);
					return;
				}        
			}
			String ext = ".json";
			String filename = template.getTemplName();
			filename = (!filename.startsWith(code.name()) ? code+"_" : "") + filename;
			filename += !filename.endsWith(ext) ? ext : "";
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
			BufferedWriter bufferedWriter = null;
			try {
				String instructionsAsJson = objectMapper.writeValueAsString(instructions);
				bufferedWriter = new BufferedWriter(new FileWriter(dir+filename));
				bufferedWriter.write(instructionsAsJson);

			} catch (IOException err) {
				Utils.log(shell, null, log, "Error saving template", err);
			}finally{
				try {
					bufferedWriter.flush();
					bufferedWriter.close();
				} catch (IOException err) {
					Utils.log(shell, null, log, "Error saving template", err);
				}
			}
		}
	}

	public static LoaderInstructionFilesDTO loadTemplate(String filename){
		LoaderInstructionFilesDTO instructions = null;
		File file = new File(App.INSTANCE.getConfigDir()+"/templates/"+filename);
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
			instructions = objectMapper.readValue(fileInputStream, LoaderInstructionFilesDTO.class);
		} catch (IOException err) {
			Utils.log(log, "Error loading template", err);
			return null;
		}
		return instructions;
	}

	public static boolean confirm(String message){
		return MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Confirmation", message);
	}

	public static void CreateMarkerWizard(Shell shell, String config, int piID, int projectID, int experimentID, int datasetID) {
		// TODO Auto-generated method stub
		WizardDialog wizardDialog = new WizardDialog(shell, new WizardMarkers(config, piID, projectID, experimentID, datasetID));
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}

	public static void createDNASampleWizard(Shell shell, String config, int piID, int projectID, int experimentID) {
		// TODO Auto-generated method stub
		WizardDialog wizardDialog = new WizardDialog(shell, new WizardDNAsamples(config, piID, projectID, experimentID));
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}

	public static void CreateDatasetWizard(Shell shell, String config, int piID, int projectID, int experimentID, int datasetID) {
		// TODO Auto-generated method stub
		WizardDialog wizardDialog = new WizardDialog(shell, new WizardDataset(config, piID, projectID, experimentID, datasetID));
		if (wizardDialog.open() == Window.OK) {
			System.out.println("Ok pressed");
		} else {
			System.out.println("Cancel pressed");
		}
	}

	public static String generateSourceFolder(){
		String folder = null;
		try{
			DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			folder = "data_"+df.format(new Date())+"_"+App.INSTANCE.getUser().getUserName();
		}catch(Exception err){
			Utils.log(log, "Error generating Source Folder", err);
		}
		return folder;
	}

	private static GobiiFileColumn copyColumn(GobiiFileColumn column){
		GobiiFileColumn col = new GobiiFileColumn();
		col.setCCoord(column.getcCoord());
		col.setConstantValue(column.getConstantValue());
		col.setDataSetType(column.getDataSetType());
		col.setFilterFrom(column.getFilterFrom());
		col.setFilterTo(column.getFilterTo());
		col.setGobiiColumnType(column.getGobiiColumnType());
		col.setIndex(column.getIndex());
		col.setMetaDataId(column.getMetaDataId());
		col.setName(column.getName());
		col.setRCoord(column.getrCoord());
		col.setSubcolumn(column.isSubcolumn());
		col.setSubcolumnDelimiter(column.getSubcolumnDelimiter());
		return col;
	}

	public static void setMarkerPropInstructions(LoaderInstructionFilesDTO instructions, DTOmarkers dto, final GobiiFileColumn colId, String name, String propName, HashMap<String, GobiiFileColumn> props, boolean isDataset) throws Exception{
		if(colId != null && props.size() > 0){
			GobiiLoaderInstruction instProp = new GobiiLoaderInstruction();

			GobiiFileColumn col = copyColumn(colId);
			col.setName(name);
			instProp.getGobiiFileColumns().add(col);

			// add platform_id
			GobiiFileColumn colPlatform = new GobiiFileColumn();
			colPlatform.setName("platform_id");
			colPlatform.setGobiiColumnType(GobiiColumnType.CONSTANT);
			colPlatform.setConstantValue(dto.getPlatformID().toString());
			instProp.getGobiiFileColumns().add(colPlatform);

			instProp.setContactEmail(App.INSTANCE.getUser().getUserEmail());
			instProp.setContactId(App.INSTANCE.getUser().getUserId());
			instProp.setDataSetId(dto.getDatasetID());
			instProp.setQcCheck(dto.isQcCheck());
			instProp.setTable(propName);
			instProp.setGobiiFile(dto.getFile());

			addJSONprops(instProp, props, dto, isDataset);
			/*// add open parenthesis
			GobiiFileColumn colOpenParenthesis = new GobiiFileColumn();
			colOpenParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
			colOpenParenthesis.setName("props");
			colOpenParenthesis.setConstantValue("\"{");
			instProp.getGobiiFileColumns().add(colOpenParenthesis);
			int count = 0;
			for(Entry<String, GobiiFileColumn> entry : props.entrySet()){
				count++;
				GobiiFileColumn colValue = entry.getValue();
				if(colValue == null) continue;
				// set key column
				GobiiFileColumn colKey = new GobiiFileColumn();
				colKey.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colKey.setName("key"+count);
				colKey.setConstantValue("\"\""+entry.getKey()+"\"\":\"\"");
				colKey.setSubcolumn(true);
				colKey.setSubcolumnDelimiter("");
				//set value column
				colValue.setName("value"+count);
				if(isDataset){
					if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
						colValue.setRCoord(dto.getrCoord());
					}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
						colValue.setCCoord(dto.getcCoord());
					}
				}else{
					if(dto.getcCoord() > -1) colValue.setCCoord(dto.getcCoord());
					if(dto.getrCoord() > -1) colValue.setRCoord(dto.getrCoord());
				}
				colValue.setGobiiColumnType(dto.getColumnType());
				colValue.setSubcolumn(true);
				colValue.setSubcolumnDelimiter("");
				// set close quotes
				GobiiFileColumn colEnd = new GobiiFileColumn();
				colEnd.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colEnd.setName("end");
				colEnd.setConstantValue("\"\"");
				colEnd.setSubcolumn(true);
				colEnd.setSubcolumnDelimiter("");
				// add comma
				GobiiFileColumn colComma = new GobiiFileColumn();
				colComma.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colComma.setName("comma");
				colComma.setConstantValue(",");
				colComma.setSubcolumn(true);
				colComma.setSubcolumnDelimiter("");
				// add columns to instruction object
				instProp.getGobiiFileColumns().add(colKey);
				instProp.getGobiiFileColumns().add(colValue);
				instProp.getGobiiFileColumns().add(colEnd);
				instProp.getGobiiFileColumns().add(colComma);
			}
			instProp.getGobiiFileColumns().remove(instProp.getGobiiFileColumns().size()-1);
			// add close parenthesis
			GobiiFileColumn colCloseParenthesis = new GobiiFileColumn();
			colCloseParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
			colCloseParenthesis.setName("close");
			colCloseParenthesis.setConstantValue("}\"");
			colCloseParenthesis.setSubcolumn(true);
			colCloseParenthesis.setSubcolumnDelimiter("");
			instProp.getGobiiFileColumns().add(colCloseParenthesis);*/
			// instruction to set
			instructions.getGobiiLoaderInstructions().add(instProp);
		}
	}

	public static boolean createDatasetInstructionsFromTemplate(Shell shell, LoaderInstructionFilesDTO instructions, DTOdataset dto, String folder){
		GobiiFilePropNameId gobiiFilePropNameId = new GobiiFilePropNameId();
		try{
			for(GobiiLoaderInstruction instruction : instructions.getGobiiLoaderInstructions()){
				Utils.setDSInstructionFileDetails(instruction, dto);
				instruction.setQcCheck(dto.isQcCheck());
				instruction.setGobiiFile(dto.getFile());
				instruction.setDataSetId(dto.getDatasetID());
				instruction.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instruction.setContactId(App.INSTANCE.getUser().getUserId());
				instruction.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());


				if(instruction.getTable().equals("dataset_marker") || instruction.getTable().equals("dataset_dnarun")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("experiment_id")){
							if(dto.getExperimentID() == null){
								Utils.log(shell, null, log, "Experiment is required", null);
								return false;
							}
							column.setConstantValue(dto.getExperimentID().toString());
						}
						if(column.getName().equals("dataset_id")){
							if(dto.getDatasetID() == null){
								Utils.log(shell, null, log, "Dataset is required", null);
								return false;
							}
							column.setConstantValue(dto.getDatasetID().toString());
						}
					}
				}
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error creating instruction file", err);
			return false;
		}
		return true;
	}

	public static boolean createMarkerInstructionsFromTemplate(Shell shell, LoaderInstructionFilesDTO instructions, DTOmarkers dto, String folder){
		try{
			for(GobiiLoaderInstruction instruction : instructions.getGobiiLoaderInstructions()){
				Utils.setMarkerInstructionFileDetails(instruction, dto);
				instruction.setQcCheck(dto.isQcCheck());
				instruction.setGobiiFile(dto.getFile());
				instruction.setDataSetId(dto.getDatasetID());
				instruction.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instruction.setContactId(App.INSTANCE.getUser().getUserId());
				instruction.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());
				if(instruction.getTable().equals("marker")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("platform_id")){
							if(dto.getPlatformID() == null){
								Utils.log(shell, null, log, "Platform is required", null);
								return false;
							}
							column.setConstantValue(dto.getPlatformID().toString());
							break;
						}
					}
				}else if(instruction.getTable().equals("linkage_group")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("map_id")){
							if(dto.getMapsetID() == null){
								Utils.log(shell, null, log, "Mapset is required", null);
								return false;
							}
							column.setConstantValue(dto.getMapsetID().toString());
							break;
						}
					}
				}else if(instruction.getTable().equals("marker_linkage_group")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("platform_id")){
							if(dto.getPlatformID() == null){
								Utils.log(shell, null, log, "Platform is required", null);
								return false;
							}
							column.setConstantValue(dto.getPlatformID().toString());
						}
						if(column.getName().equals("map_id")){
							if(dto.getMapsetID() == null){
								Utils.log(shell,  null,  log, "Mapset is required", null);
								return false;
							}
							column.setConstantValue(dto.getMapsetID().toString());
						}
					}
				}else if(instruction.getTable().equals("marker_prop")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("platform_id")){
							if(dto.getPlatformID() == null){
								Utils.log(shell, null, log, "Platform is required", null);
								return false;
							}
							column.setConstantValue(dto.getPlatformID().toString());
							break;
						}
					}
				}
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error creating instruction file", err);
			return false;
		}
		return true;
	}

	public static boolean createMarkerInstructionsFromDTO(Shell shell, LoaderInstructionFilesDTO instructions, DTOmarkers dto, String folder, boolean isDataset){
		try{
			String crop = GobiiClientContext.getInstance(null, false).getCurrentClientCropType();
			// create status column
			GobiiFileColumn colStatus = new GobiiFileColumn();
			colStatus.setName("status");
			colStatus.setGobiiColumnType(GobiiColumnType.CONSTANT);
			String status_id = "0";
			for (NameIdDTO entry : Controller.getCVByGroup("status")){
				String key = entry.getId().toString();
				String term = entry.getName();
				if(term.toLowerCase().equals("new")){
					status_id = key;
					break;
				}
			}
			colStatus.setConstantValue(status_id);

			if(dto.getMarkerFields().size() > 0){
				GobiiLoaderInstruction instMarker = new GobiiLoaderInstruction();
				instMarker.setQcCheck(dto.isQcCheck());
				instMarker.setDataSetId(dto.getDatasetID());

				Utils.setMarkerInstructionFileDetails(instMarker, dto);
				instMarker.setGobiiFile(dto.getFile());
				instMarker.setTable("marker");

				GobiiFileColumn colPlatform = new GobiiFileColumn();
				colPlatform.setName("platform_id");
				colPlatform.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colPlatform.setConstantValue(dto.getPlatformID().toString());
				instMarker.getGobiiFileColumns().add(colPlatform);

				for(Entry<String, GobiiFileColumn> entry : dto.getMarkerFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}

					//					if(column.getName().equals("alts")){
					//						// add open parenthesis
					//						GobiiFileColumn colOpenParenthesis = new GobiiFileColumn();
					//						colOpenParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
					//						colOpenParenthesis.setName("alts");
					//						colOpenParenthesis.setConstantValue("{\"");
					//						instMarker.getGobiiFileColumns().add(colOpenParenthesis);
					//						
					//						column.setName("alts1");
					//						column.setSubcolumn(true);
					//						column.setSubcolumnDelimiter("");
					//						column.setGobiiColumnType(dto.getColumnType());
					//						instMarker.getGobiiFileColumns().add(column);
					//						
					//						// add close parenthesis
					//						GobiiFileColumn colCloseParenthesis = new GobiiFileColumn();
					//						colCloseParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
					//						colCloseParenthesis.setName("alts2");
					//						colCloseParenthesis.setConstantValue("\"}");
					//						colCloseParenthesis.setSubcolumn(true);
					//						colCloseParenthesis.setSubcolumnDelimiter("");
					//						instMarker.getGobiiFileColumns().add(colCloseParenthesis);
					//					}else{
					column.setGobiiColumnType(dto.getColumnType());
					instMarker.getGobiiFileColumns().add(column);
					addSubColumn(instMarker, column.getName(), dto.getMarkerFields(), dto, isDataset);
					//					}
				}

				instMarker.getGobiiFileColumns().add(colStatus);
				instMarker.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instMarker.setContactId(App.INSTANCE.getUser().getUserId());
				instMarker.setGobiiCropType(crop);
				instructions.getGobiiLoaderInstructions().add(instMarker);
				setMarkerPropInstructions(instructions, dto, dto.getMarkerFields().get("name"), "marker_name", "marker_prop", dto.getMarkerPropFields(), isDataset);
			}

			if(dto.getLgFields().size() > 0){
				GobiiLoaderInstruction instLG = new GobiiLoaderInstruction();
				Utils.setMarkerInstructionFileDetails(instLG, dto);

				instLG.setQcCheck(dto.isQcCheck());
				instLG.setTable("linkage_group");
				instLG.setGobiiFile(dto.getFile());
				instLG.setGobiiFile(dto.getFile());
				instLG.setDataSetId(dto.getDatasetID());

				GobiiFileColumn colMap = new GobiiFileColumn();
				colMap.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colMap.setConstantValue(dto.getMapsetID().toString());
				colMap.setName("map_id");
				instLG.getGobiiFileColumns().add(colMap);
				for(Entry<String, GobiiFileColumn> entry : dto.getLgFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}
					column.setGobiiColumnType(dto.getColumnType());
					instLG.getGobiiFileColumns().add(column);
					addSubColumn(instLG, column.getName(), dto.getSubLgFields(), dto, isDataset);
				}
				if(!dto.getLgFields().containsKey("start")){
					//				if(!dto.getLgFields().entrySet().contains("start")){
					GobiiFileColumn colStart = new GobiiFileColumn();
					colStart.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colStart.setConstantValue(dto.getMapsetID().toString());
					colStart.setName("start");
					colStart.setConstantValue("0");
					instLG.getGobiiFileColumns().add(colStart);
				}
				if(!dto.getLgFields().containsKey("stop")){
					//				if(!dto.getLgFields().entrySet().contains("stop")){
					GobiiFileColumn colStop = new GobiiFileColumn();
					colStop.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colStop.setConstantValue(dto.getMapsetID().toString());
					colStop.setName("stop");
					colStop.setConstantValue("0");
					instLG.getGobiiFileColumns().add(colStop);
				}

				instLG.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instLG.setContactId(App.INSTANCE.getUser().getUserId());
				instLG.setGobiiCropType(crop);
				instructions.getGobiiLoaderInstructions().add(instLG);
			}

			if(dto.getLgMarkerFields().size() > 0){
				GobiiLoaderInstruction instLGmarker = new GobiiLoaderInstruction();
				Utils.setMarkerInstructionFileDetails(instLGmarker, dto);

				instLGmarker.setDataSetId(dto.getDatasetID());
				instLGmarker.setQcCheck(dto.isQcCheck());
				instLGmarker.setTable("marker_linkage_group");
				instLGmarker.setGobiiFile(dto.getFile());
				instLGmarker.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instLGmarker.setContactId(App.INSTANCE.getUser().getUserId());
				instLGmarker.setGobiiCropType(crop);

				// add platform_id
				GobiiFileColumn colPlatform = new GobiiFileColumn();
				colPlatform.setName("platform_id");
				colPlatform.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colPlatform.setConstantValue(dto.getPlatformID().toString());
				instLGmarker.getGobiiFileColumns().add(colPlatform);

				GobiiFileColumn colMap = new GobiiFileColumn();
				colMap.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colMap.setConstantValue(dto.getMapsetID().toString());
				colMap.setName("map_id");
				instLGmarker.getGobiiFileColumns().add(colMap);

				for(Entry<String, GobiiFileColumn> entry : dto.getLgMarkerFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}
					column.setGobiiColumnType(dto.getColumnType());
					instLGmarker.getGobiiFileColumns().add(column);
					addSubColumn(instLGmarker, column.getName(), dto.getSubLgMarkerFields(), dto, isDataset);
				}

				instructions.getGobiiLoaderInstructions().add(instLGmarker);
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error creating instruction file", err);
			return false;
		}
		return true;
	}

	public static void setSamplePropInstructions(Shell shell, LoaderInstructionFilesDTO instructions, DTOsamples dto, GobiiFileColumn colId, String name, String propName, HashMap<String, GobiiFileColumn> props, boolean isDataset) throws Exception{
		if(colId != null && props.size() > 0){
			GobiiLoaderInstruction instProp = new GobiiLoaderInstruction();
			instProp.setQcCheck(dto.isQcCheck());
			instProp.setDataSetId(dto.getDatasetID());
			instProp.setTable(propName);
			instProp.setGobiiFile(dto.getFile());
			instProp.setContactEmail(App.INSTANCE.getUser().getUserEmail());
			instProp.setContactId(App.INSTANCE.getUser().getUserId());

			GobiiFileColumn col = copyColumn(colId);
			col.setName(name);
			instProp.getGobiiFileColumns().add(col);
			addSubColumn(instProp, col.getName(), dto.getSubSampleFields(), dto, isDataset);

			// add needed fileds
			if(propName.equals("dnasample_prop")){
				GobiiFileColumn colProject = new GobiiFileColumn();
				colProject.setName("project_id");
				colProject.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colProject.setConstantValue(dto.getProjectID().toString());

				GobiiFileColumn colExtCode = dto.getSampleFields().get("external_code");
				GobiiFileColumn colNum = dto.getSampleFields().get("num");
				if(colExtCode == null || colNum == null){
					Utils.log(shell, null, log, "external_code and num are required for Germplasm prop", null);
					return;
				}
				instProp.getGobiiFileColumns().add(colExtCode);
				addSubColumn(instProp, colExtCode.getName(), dto.getSubSampleFields(), dto, isDataset);

				instProp.getGobiiFileColumns().add(colNum);
				addSubColumn(instProp, colNum.getName(), dto.getSubSampleFields(), dto, isDataset);
				instProp.getGobiiFileColumns().add(colProject);

			}else if(propName.equals("dnarun_prop")){
				GobiiFileColumn colExp = new GobiiFileColumn();
				colExp.setName("experiment_id");
				colExp.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colExp.setConstantValue(dto.getExperimentID().toString());
				instProp.getGobiiFileColumns().add(colExp);
			}
			addJSONprops(instProp, props, dto, isDataset);
			//			// add open parenthesis
			//			GobiiFileColumn colOpenParenthesis = new GobiiFileColumn();
			//			colOpenParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
			//			colOpenParenthesis.setName("props");
			//			colOpenParenthesis.setConstantValue("\"{");
			//			instProp.getGobiiFileColumns().add(colOpenParenthesis);
			//			int count = 0;
			//			for(Entry<String, GobiiFileColumn> entry : props.entrySet()){
			//				count++;
			//				GobiiFileColumn colValue = entry.getValue();
			//				if(colValue == null) continue;
			//				// set key column
			//				GobiiFileColumn colKey = new GobiiFileColumn();
			//				colKey.setGobiiColumnType(GobiiColumnType.CONSTANT);
			//				colKey.setName("key"+count);
			////				colKey.setConstantValue("\"\""+entry.getKey()+"\"\":\"\"");
			//				colKey.setConstantValue(entry.getKey()+":");
			//				colKey.setSubcolumn(true);
			//				colKey.setSubcolumnDelimiter("");
			//				//set value column
			//				colValue.setName("value"+count);
			//				if(isDataset){
			//					if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
			//						colValue.setRCoord(dto.getrCoord());
			//					}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
			//						colValue.setCCoord(dto.getcCoord());
			//					}
			//				}else{
			//					if(dto.getcCoord() > -1) colValue.setCCoord(dto.getcCoord());
			//					if(dto.getrCoord() > -1) colValue.setRCoord(dto.getrCoord());
			//				}
			//				colValue.setGobiiColumnType(dto.getColumnType());
			//				colValue.setSubcolumn(true);
			//				colValue.setSubcolumnDelimiter("");
			////				// set close quotes
			////				GobiiFileColumn colEnd = new GobiiFileColumn();
			////				colEnd.setGobiiColumnType(GobiiColumnType.CONSTANT);
			////				colEnd.setName("end");
			////				colEnd.setConstantValue("\"\"");
			////				colEnd.setSubcolumn(true);
			////				colEnd.setSubcolumnDelimiter("");
			//				// add comma
			//				GobiiFileColumn colComma = new GobiiFileColumn();
			//				colComma.setGobiiColumnType(GobiiColumnType.CONSTANT);
			//				colComma.setName("comma");
			//				colComma.setConstantValue(",");
			//				colComma.setSubcolumn(true);
			//				colComma.setSubcolumnDelimiter("");
			//				// add columns to instruction object
			//				instProp.getGobiiFileColumns().add(colKey);
			//				instProp.getGobiiFileColumns().add(colValue);
			////				instProp.getGobiiFileColumns().add(colEnd);
			//				instProp.getGobiiFileColumns().add(colComma);
			//			}
			//			instProp.getGobiiFileColumns().remove(instProp.getGobiiFileColumns().size()-1);
			//			// add close parenthesis
			//			GobiiFileColumn colCloseParenthesis = new GobiiFileColumn();
			//			colCloseParenthesis.setGobiiColumnType(GobiiColumnType.CONSTANT);
			//			colCloseParenthesis.setName("close");
			//			colCloseParenthesis.setConstantValue("}\"");
			//			colCloseParenthesis.setSubcolumn(true);
			//			colCloseParenthesis.setSubcolumnDelimiter("");
			//			instProp.getGobiiFileColumns().add(colCloseParenthesis);
			// instruction to set
			instructions.getGobiiLoaderInstructions().add(instProp);
		}
	}

	public static boolean createSampleInstructionsFromTemplate(Shell shell, LoaderInstructionFilesDTO instructions, DTOsamples dto, String folder){
		try{
			for(GobiiLoaderInstruction instruction : instructions.getGobiiLoaderInstructions()){
				Utils.setSampleInstructionFileDetails(instruction, dto);

				instruction.setQcCheck(dto.isQcCheck());
				instruction.setGobiiFile(dto.getFile());
				instruction.setDataSetId(dto.getDatasetID());
				instruction.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instruction.setContactId(App.INSTANCE.getUser().getUserId());
				instruction.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());


				if(instruction.getTable().equals("dnasample")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("project_id")){
							if(dto.getProjectID() == null){
								Utils.log(shell, null, log, "Project is required", null);
								return false;
							}
							column.setConstantValue(dto.getProjectID().toString());
							break;
						}
					}
				}else if(instruction.getTable().equals("dnarun")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("project_id")){
							if(dto.getProjectID() == null){
								Utils.log(shell, null, log, "Project is required", null);
								return false;
							}
							column.setConstantValue(dto.getProjectID().toString());
						}
						if(column.getName().equals("experiment_id")){
							if(dto.getExperimentID() == null){
								Utils.log(shell, null, log, "Experiment is required", null);
								return false;
							}
							column.setConstantValue(dto.getExperimentID().toString());
						}
					}
				}else if(instruction.getTable().equals("dnasample_prop")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("project_id")){
							if(dto.getProjectID() == null){
								Utils.log(shell, null, log, "Project is required", null);
								return false;
							}
							column.setConstantValue(dto.getProjectID().toString());
						}
						break;
					}
				}else if(instruction.getTable().equals("dnarun_prop")){
					for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
						if(column.getName().equals("experiment_id")){
							if(dto.getExperimentID() == null){
								Utils.log(shell, null, log, "Experiment is required", null);
								return false;
							}
							column.setConstantValue(dto.getExperimentID().toString());
							break;
						}
					}
				}
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error creating instruction file", err);
			return false;
		}
		return true;
	}

	public static boolean createSampleInstructionsFromDTO(Shell shell, LoaderInstructionFilesDTO instructions, DTOsamples dto, String folder, boolean isDataset){
		try{
			// create status column
			GobiiFileColumn colStatus = new GobiiFileColumn();
			colStatus.setName("status");
			colStatus.setGobiiColumnType(GobiiColumnType.CONSTANT);
			String status_id = "0";
			for (NameIdDTO entry : Controller.getCVByGroup("status")){
				String key =  entry.getId().toString();
				String term = entry.getName();
				if(term.toLowerCase().equals("new")){
					status_id = key;
					break;
				}
			}
			colStatus.setConstantValue(status_id);

			if(dto.getGermplasmFields().size() > 0){
				GobiiLoaderInstruction instGermplasm = new GobiiLoaderInstruction();

				Utils.setSampleInstructionFileDetails(instGermplasm, dto);
				instGermplasm.setQcCheck(dto.isQcCheck());
				instGermplasm.setTable("germplasm");
				instGermplasm.setGobiiFile(dto.getFile());
				instGermplasm.setDataSetId(dto.getDatasetID());
				instGermplasm.getGobiiFileColumns().add(colStatus);
				instGermplasm.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instGermplasm.setContactId(App.INSTANCE.getUser().getUserId());
				instGermplasm.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());

				for(Entry<String, GobiiFileColumn> entry : dto.getGermplasmFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}
					column.setGobiiColumnType(dto.getColumnType());
					instGermplasm.getGobiiFileColumns().add(column);
					addSubColumn(instGermplasm, column.getName(), dto.getSubGermplasmFields(), dto, isDataset);
					//					if(dto.getSubGermplasmFields().containsKey("sub_"+column.getName())){
					//						GobiiFileColumn subColumn = dto.getSubGermplasmFields().get(column.getName());
					//						subColumn.setGobiiColumnType(dto.getColumnType());
					//						instGermplasm.getGobiiFileColumns().add(subColumn);
					//					}
				}
				instructions.getGobiiLoaderInstructions().add(instGermplasm);
				setSamplePropInstructions(shell, instructions, dto, dto.getGermplasmFields().get("external_code"), "external_code", "germplasm_prop", dto.getGermplasmPropFields(), isDataset);
			}

			if(dto.getSampleFields().size() > 0){
				GobiiLoaderInstruction instSample = new GobiiLoaderInstruction();
				Utils.setSampleInstructionFileDetails(instSample, dto);

				instSample.setTable("dnasample");
				instSample.setGobiiFile(dto.getFile());
				instSample.setDataSetId(dto.getDatasetID());
				instSample.setQcCheck(dto.isQcCheck());
				instSample.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instSample.setContactId(App.INSTANCE.getUser().getUserId());
				instSample.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());
				
				GobiiFileColumn colProject = new GobiiFileColumn();
				colProject.setName("project_id");
				colProject.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colProject.setConstantValue(dto.getProjectID().toString());
				instSample.getGobiiFileColumns().add(colProject);

				for(Entry<String, GobiiFileColumn> entry : dto.getSampleFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}
					column.setGobiiColumnType(dto.getColumnType());
					instSample.getGobiiFileColumns().add(column);
					
					addSubColumn(instSample, column.getName(), dto.getSubSampleFields(), dto, isDataset);
					//					if(dto.getSubSampleFields().containsKey("sub_"+column.getName())){
					//						GobiiFileColumn subColumn = dto.getSubSampleFields().get("sub_"+column.getName());
					//						subColumn.setGobiiColumnType(dto.getColumnType());
					//						instSample.getGobiiFileColumns().add(subColumn);
					//					}
				}
				instSample.getGobiiFileColumns().add(colStatus);
				
				instructions.getGobiiLoaderInstructions().add(instSample);
				setSamplePropInstructions(shell, instructions, dto, dto.getSampleFields().get("name"), "dnasample_name", "dnasample_prop", dto.getSamplePropFields(), isDataset);
			}

			if(dto.getRunFields().size() > 0){
				GobiiLoaderInstruction instRun = new GobiiLoaderInstruction();
				Utils.setSampleInstructionFileDetails(instRun, dto);
				
				instRun.setQcCheck(dto.isQcCheck());
				instRun.setTable("dnarun");
				instRun.setGobiiFile(dto.getFile());
				instRun.setDataSetId(dto.getDatasetID());
				instRun.setContactEmail(App.INSTANCE.getUser().getUserEmail());
				instRun.setContactId(App.INSTANCE.getUser().getUserId());
				instRun.setGobiiCropType(GobiiClientContext.getInstance(null, false).getCurrentClientCropType());
				// add project_id
				
				GobiiFileColumn colProject = new GobiiFileColumn();
				colProject.setName("project_id");
				colProject.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colProject.setConstantValue(dto.getProjectID().toString());
				instRun.getGobiiFileColumns().add(colProject);
				
				// add experiment_id
				GobiiFileColumn colExperiment = new GobiiFileColumn();
				colExperiment.setName("experiment_id");
				colExperiment.setGobiiColumnType(GobiiColumnType.CONSTANT);
				colExperiment.setConstantValue(dto.getExperimentID().toString());
				instRun.getGobiiFileColumns().add(colExperiment);
				
				for(Entry<String, GobiiFileColumn> entry : dto.getRunFields().entrySet()){
					GobiiFileColumn column = entry.getValue();
					if(column == null) continue;
					if(isDataset){
						if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getrCoord());
						}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getcCoord());
						}
					}else{
						if(dto.getcCoord() > -1) column.setCCoord(dto.getcCoord());
						if(dto.getrCoord() > -1) column.setRCoord(dto.getrCoord());
					}
					column.setGobiiColumnType(dto.getColumnType());
					instRun.getGobiiFileColumns().add(column);
					addSubColumn(instRun, column.getName(), dto.getSubRunFields(), dto, isDataset);
				}
				
				instructions.getGobiiLoaderInstructions().add(instRun);
				setSamplePropInstructions(shell, instructions, dto, dto.getRunFields().get("name"), "dnarun_name", "dnarun_prop", dto.getRunPropFields(), isDataset);
			}
		}catch(Exception err){
			Utils.log(shell, null, log, "Error submitting instruction file", err);
			return false;
		}
		return true;
	}

	public static void sendFilesToServer(Shell parent, String destination, List<String> files, String ext){
		DlgFileTransfer dlg = new DlgFileTransfer(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, destination, files, ext);
		dlg.open();
	}

	public static void removeFiles(Table table, List<String> files){
		for(TableItem item : table.getItems()){
			if(item.getChecked()){
				files.remove(item.getText());
				item.dispose();
			}
		}
	}

	public static void getTemplateFiles(TemplateCode code, Combo combo){
		combo.removeAll();
		File[] files = new File(App.INSTANCE.getConfigDir()+"/templates/").listFiles();
		if(files == null) return;
		for(File file : files){
			String filename = file.getName();
			if(code == null || filename.startsWith(code.name())){
				combo.add(filename);
			}
		}
	}

	public static void populateExperimentInformation(Shell shell, int experimentId, Combo cbPlatform, Combo cbVendorProtocol){
		Integer vendorProtocolId = null;
		Integer platformId = null;

		// get experiment details
		ExperimentDTO experimentDTO = null;
		try {
			RestUri experimentsUri =  GobiiClientContext.getInstance(null, false).getUriFactory()
					.resourceByUriIdParam(GobiiServiceRequestId.URL_EXPERIMENTS);
			experimentsUri.setParamValue("id", Integer.toString(experimentId));
			GobiiEnvelopeRestResource<ExperimentDTO> restResourceForExperiments = new GobiiEnvelopeRestResource<>(experimentsUri);
			PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
					.get(ExperimentDTO.class);

			if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)){
				experimentDTO = resultEnvelope.getPayload().getData().get(0);
				vendorProtocolId = experimentDTO.getVendorProtocolId();
			}
		} catch (Exception e) {
			Utils.log(shell, null, log, "Error retrieving Experiemnts", e);
		}
	}

	public static void populateDatasetInformation(Shell shell, int datasetId, Text text, DTOdataset dto){
		try{
			RestUri projectsUri =  GobiiClientContext.getInstance(null, false).getUriFactory().resourceByUriIdParam(GobiiServiceRequestId.URL_DATASETS);
			projectsUri.setParamValue("id", Integer.toString(datasetId));
			GobiiEnvelopeRestResource<DataSetDTO> restResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
			PayloadEnvelope<DataSetDTO> resultEnvelope = restResourceForProjects
					.get(DataSetDTO.class);

			if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)){
				DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);
				Integer typeId = dataSetDTOResponse.getTypeId();
				String typeName = FormUtils.getNameById(Controller.getCVByGroup("dataset_type"), typeId);
				text.setText(typeName);
				dto.setDatasetTypeID(typeId);
				dto.setDatasetType(typeName.toUpperCase());
			}
		}catch(Exception err){
			text.setText(" ");
			Utils.log(shell, null, log, "Error retrieving Dataset details.\n\n Please use the Analysis Dataset form to specify a type for this dataset.", err);
		}
	}

	public static LoaderFilePreviewDTO previewData(Shell shell, boolean remote, String folder, List<String> files, String fileExtention){

		LoaderFilePreviewDTO loaderFilePreviewDTO = null;
		if(!remote){
			loaderFilePreviewDTO = sendFilesToServer(shell, files, fileExtention);
			if(loaderFilePreviewDTO==null || loaderFilePreviewDTO.getDirectoryName() == null){
				return null;
			}else{
				folder = new File(loaderFilePreviewDTO.getDirectoryName()).getName();
			}
		}

		RestUri previewURI;
		GobiiEnvelopeRestResource<LoaderFilePreviewDTO> restResource;
		PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope;
		try {
			previewURI = GobiiClientContext
					.getInstance(null,false)
					.getUriFactory()
					.fileLoaderPreview();

			previewURI.setParamValue("directoryName", folder);
			previewURI.setParamValue("fileFormat", fileExtention);
			restResource = new GobiiEnvelopeRestResource<>(previewURI);
			resultEnvelope = restResource.get(LoaderFilePreviewDTO.class);

			if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)) {
				loaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
			}

		} catch (Exception err) {
			Utils.log(shell, null, log, "Error previewing data", err);
			Controller.showException(shell,null,err);
		}
		return loaderFilePreviewDTO;
	}

	private static LoaderFilePreviewDTO sendFilesToServer(Shell shell, List<String> files, String fileExtention){
		LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO(); 
		File dir = new File(WizardUtils.generateSourceFolder());

		RestUri previewTestUri;
		try {
			previewTestUri = GobiiClientContext
					.getInstance(null,false)
					.getUriFactory()
					.resourceByUriIdParam(GobiiServiceRequestId.URL_FILE_LOAD);
			previewTestUri.setParamValue("id", dir.toString());
			GobiiEnvelopeRestResource<LoaderFilePreviewDTO> restResource = new GobiiEnvelopeRestResource<>(previewTestUri);
			PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.put(LoaderFilePreviewDTO.class,
					new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));

			if(!Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)) {
				return null; 
			}else{
				loaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
			}
			WizardUtils.sendFilesToServer(shell, loaderFilePreviewDTO.getDirectoryName(), files, fileExtention);
			return loaderFilePreviewDTO;
		} catch (Exception err) {
			Utils.log(shell, null, log, "Error creating folder on server", err);
			System.out.println(err.getMessage());
			return null;
		}
	}

	public static void createInstructionsForWizard(String folder, WizardDTO dto)throws Exception{
		String digesterInputDirectory = GobiiClientContext
				.getInstance(null, false)
				.getFileLocationOfCurrenCrop(GobiiFileProcessDir.RAW_USER_FILES);
		dto.getFile().setSource(digesterInputDirectory+folder);
		dto.getFile().setCreateSource(false);
		dto.getFile().setRequireDirectoriesToExist(false);

		String digesterOutputDirectory = GobiiClientContext
				.getInstance(null, false)
				.getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);
		dto.getFile().setDestination(digesterOutputDirectory+folder);
	}

	private static void addSubColumn(GobiiLoaderInstruction instruction, String colName, HashMap<String, GobiiFileColumn> subColumns, WizardDTO dto, boolean isDataset){
		String subName = "sub_"+colName;
		if(subColumns.containsKey(subName)){
			GobiiFileColumn subColumn = subColumns.get(subName);
			subColumn.setName(subName);
			subColumn.setGobiiColumnType(dto.getColumnType());
			if(isDataset){
				if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
					subColumn.setRCoord(dto.getrCoord());
				}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
					subColumn.setCCoord(dto.getcCoord());
				}
			}else{
				if(dto.getcCoord() > -1) subColumn.setCCoord(dto.getcCoord());
				if(dto.getrCoord() > -1) subColumn.setRCoord(dto.getrCoord());
			}
			instruction.getGobiiFileColumns().add(subColumn);
		}
	}

	private static void addJSONprops(GobiiLoaderInstruction instProp, HashMap<String, GobiiFileColumn> props, WizardDTO dto, boolean isDataset){
		int count = 0;
		for(Entry<String, GobiiFileColumn> entry : props.entrySet()){
			GobiiFileColumn colValue = entry.getValue();
			if(colValue == null) continue;
			count++;
			String keyname = count == 1 ? "props" : "key"+count;
			boolean isSub = count == 1 ? false : true;
			// set key column
			GobiiFileColumn colKey = new GobiiFileColumn();
			colKey.setGobiiColumnType(GobiiColumnType.CONSTANT);
			colKey.setName(keyname);
			colKey.setConstantValue(entry.getKey()+":");
			colKey.setSubcolumn(isSub);
			colKey.setSubcolumnDelimiter("");
			//set value column
			colValue.setName("value"+count);
			if(isDataset){
				if(dto.getColumnType() == GobiiColumnType.CSV_COLUMN){
					colValue.setRCoord(dto.getrCoord());
				}else if(dto.getColumnType() == GobiiColumnType.CSV_ROW ){
					colValue.setCCoord(dto.getcCoord());
				}
			}else{
				if(dto.getcCoord() > -1) colValue.setCCoord(dto.getcCoord());
				if(dto.getrCoord() > -1) colValue.setRCoord(dto.getrCoord());
			}
			colValue.setGobiiColumnType(dto.getColumnType());
			colValue.setSubcolumn(true);
			colValue.setSubcolumnDelimiter("");
			// add comma
			GobiiFileColumn colComma = new GobiiFileColumn();
			colComma.setGobiiColumnType(GobiiColumnType.CONSTANT);
			colComma.setName("comma"+count);
			colComma.setConstantValue(",");
			colComma.setSubcolumn(true);
			colComma.setSubcolumnDelimiter("");
			// add columns to instruction object
			instProp.getGobiiFileColumns().add(colKey);
			instProp.getGobiiFileColumns().add(colValue);
			instProp.getGobiiFileColumns().add(colComma);
		}
		instProp.getGobiiFileColumns().remove(instProp.getGobiiFileColumns().size()-1);
	}

	public static void populatePlatformText(Shell shell, int platformId, Text textPlatform) {
		// TODO Auto-generated method stub
		PayloadEnvelope<PlatformDTO> resultEnvelope = Controller.getPlatformDetails(platformId);
		if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)){
			PlatformDTO platformDTO = resultEnvelope.getPayload().getData().get(0);
			textPlatform.setText(platformDTO.getPlatformName());
		}
	}
}
