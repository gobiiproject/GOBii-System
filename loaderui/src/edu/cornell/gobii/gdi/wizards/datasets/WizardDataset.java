package edu.cornell.gobii.gdi.wizards.datasets;

import java.io.File;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;
import edu.cornell.gobii.gdi.utils.WizardUtils.TemplateCode;
import edu.cornell.gobii.gdi.wizards.dnasamples.Pg2DNAsamples;
import edu.cornell.gobii.gdi.wizards.dnasamples.Pg3DNAsamples;
import edu.cornell.gobii.gdi.wizards.markers.Pg2Markers;
import edu.cornell.gobii.gdi.wizards.markers.Pg3Markers;

public class WizardDataset extends Wizard {
	private static Logger log = Logger.getLogger(WizardDataset.class.getName());
	private String config;
	private DTOdataset dto = new DTOdataset();

	public WizardDataset(String config) {
		setWindowTitle("New Wizard");
		this.config = config;
	}

	@Override
	public void addPages() {
		addPage(new Page1Datasets(config, dto));
		addPage(new Pg2Markers(config, dto.getDtoMarkers()));
		addPage(new Pg3Markers(config, dto.getDtoMarkers()));
		addPage(new Pg2DNAsamples(config, dto.getDtoSamples()));
		addPage(new Pg3DNAsamples(config, dto.getDtoSamples()));
//		addPage(new Page2Datasets());
	}

	@Override
	public boolean performFinish() {
		try{
//			String folder = WizardUtils.generateSourceFolder();
//			if(!dto.isRemote()){
////			if((dto.getFile().getSource() == null || dto.gz`etFile().getSource().isEmpty()) && dto.getFiles().size() > 0){
//				String digesterInputDirectory = ClientContext
//		                .getInstance(null,false)
//		                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.RAW_USER_FILES);
//				dto.getFile().setSource(digesterInputDirectory+folder);
//				dto.getFile().setCreateSource(true);
//			}else if(dto.getFile().getSource() != null && !dto.getFile().getSource().isEmpty()){
//				
//				dto.getFile().setCreateSource(false);
//				dto.getFile().setRequireDirectoriesToExist(true);
//			}else{
//				Utils.log(getShell(), null, log, "Error submitting instruction file", new Exception("No source file(s) specified!"));
//				return false;
//			}
//			String digesterOutputDirectory = ClientContext
//	                .getInstance(null, false)
//	                .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES );
//			dto.getFile().setDestination(digesterOutputDirectory+folder);
//			dto.getDtoMarkers().setFile(dto.getFile());
//			dto.getDtoSamples().setFile(dto.getFile());
			
			String folder = new File(dto.getPreviewDTO().getDirectoryName()).getName();
			if(folder != null || !folder.isEmpty()){
				WizardUtils.createInstructionsForWizard(folder, dto);
			}else{
				Utils.log(getShell(), null, log, "Error submitting instruction file", new Exception("No source file(s) specified!"));
				return false;
			}
			
			LoaderInstructionFilesDTO instructions = null;
			if(dto.getTemplate() != null){
				instructions = WizardUtils.loadTemplate(dto.getTemplate());
				if(!WizardUtils.createMarkerInstructionsFromTemplate(getShell(), instructions, dto.getDtoMarkers(), folder)){
					return false;
				}
				if(!WizardUtils.createSampleInstructionsFromTemplate(getShell(), instructions, dto.getDtoSamples(), folder)){
					return false;
				}
				if(!WizardUtils.createDatasetInstructionsFromTemplate(getShell(), instructions, dto, folder));
			}else{
				if(!validate()){
					return false;
				}
				instructions = new LoaderInstructionFilesDTO();

				// set marker inforation
				// GSD-55: if you don't set the marker file's file here, the marker file's file is there, 
				// 		   but has null values. This is because WizardUtils.createInstructionsForWizard() 
				//         sets the values for the DTOdataset's file, but does not set those of the DTOdataset
				//         marker. Ideally, this should be done in createInstructionsForWizard(). However, 
				//         I have no idea what the implications of that kind of change would be. 
				dto.getDtoMarkers().setFile(dto.getFile());
				
				WizardUtils.createMarkerInstructionsFromDTO(getShell(), instructions, dto.getDtoMarkers(), folder, true);
				if(dto.getDtoMarkers().getDsMarkerFields().size() > 0){
					GobiiLoaderInstruction instDSmarker = new GobiiLoaderInstruction();
					Utils.setDSInstructionFileDetails(instDSmarker, dto);
					instDSmarker.setTable("dataset_marker");
					instDSmarker.setGobiiFile(dto.getFile());
					instDSmarker.setGobiiFile(dto.getFile());
					// add platform_id
					GobiiFileColumn colPlatform = new GobiiFileColumn();
					colPlatform.setName("platform_id");
					colPlatform.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colPlatform.setConstantValue(dto.getPlatformID().toString());
					instDSmarker.getGobiiFileColumns().add(colPlatform);
					// add dataset_id
					GobiiFileColumn colDS = new GobiiFileColumn();
					colDS.setName("dataset_id");
					colDS.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colDS.setConstantValue(dto.getDatasetID().toString());
					instDSmarker.getGobiiFileColumns().add(colDS);
					for(Entry<String, GobiiFileColumn> entry : dto.getDtoMarkers().getDsMarkerFields().entrySet()){
						GobiiFileColumn column = entry.getValue();
						if(column == null) continue;
						column.setGobiiColumnType(dto.getDtoMarkers().getColumnType());
						if(dto.getDtoMarkers().getColumnType() == GobiiColumnType.CSV_COLUMN){
							column.setRCoord(dto.getDtoMarkers().getrCoord());
						}else if(dto.getDtoMarkers().getColumnType() == GobiiColumnType.CSV_ROW ){
							column.setCCoord(dto.getDtoMarkers().getcCoord());
						}
						instDSmarker.getGobiiFileColumns().add(column);
					}
					GobiiFileColumn colIdx = new GobiiFileColumn();
					colIdx.setName("marker_idx");
					colIdx.setGobiiColumnType(GobiiColumnType.AUTOINCREMENT);
					instDSmarker.getGobiiFileColumns().add(colIdx);
					instDSmarker.setDataSetId(dto.getDatasetID());

					instDSmarker.setContactEmail(App.INSTANCE.getUser().getUserEmail());
					instDSmarker.setContactId(App.INSTANCE.getUser().getUserId());
					instDSmarker.setGobiiCropType(ClientContext.getInstance(null, false).getCurrentClientCropType());
					instructions.getGobiiLoaderInstructions().add(instDSmarker);
				}

				// set dna sample information
				dto.getDtoSamples().setFile(dto.getFile());	// See comment above in regard to GSD-55
				WizardUtils.createSampleInstructionsFromDTO(getShell(), instructions, dto.getDtoSamples(), folder, true);
				if(dto.getDtoSamples().getRunFields().size() > 0){
					GobiiLoaderInstruction instDSrun = new GobiiLoaderInstruction();
					Utils.setDSInstructionFileDetails(instDSrun, dto);
					instDSrun.setTable("dataset_dnarun");
					instDSrun.setGobiiFile(dto.getFile());
					// add platform_id
					GobiiFileColumn colExperiment = new GobiiFileColumn();
					colExperiment.setName("experiment_id");
					colExperiment.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colExperiment.setConstantValue(dto.getExperimentID().toString());
					instDSrun.getGobiiFileColumns().add(colExperiment);
					// add dataset_id
					GobiiFileColumn colDS = new GobiiFileColumn();
					colDS.setName("dataset_id");
					colDS.setGobiiColumnType(GobiiColumnType.CONSTANT);
					colDS.setConstantValue(dto.getDatasetID().toString());
					instDSrun.getGobiiFileColumns().add(colDS);
					for(Entry<String, GobiiFileColumn> entry : dto.getDtoSamples().getRunFields().entrySet()){
						GobiiFileColumn column = entry.getValue();
						if(column == null) continue;
						if(entry.getKey().equals("name")){
							column.setName("dnarun_name");
							column.setGobiiColumnType(dto.getDtoSamples().getColumnType());
							if(dto.getDtoSamples().getColumnType() == GobiiColumnType.CSV_COLUMN){
								column.setRCoord(dto.getDtoSamples().getrCoord());
							}else if(dto.getDtoSamples().getColumnType() == GobiiColumnType.CSV_ROW ){
								column.setCCoord(dto.getDtoSamples().getcCoord());
							}
							instDSrun.getGobiiFileColumns().add(column);
							break;
						}else{
							continue;
						}
					}
					GobiiFileColumn colIdx = new GobiiFileColumn();
					colIdx.setName("dnarun_idx");
					colIdx.setGobiiColumnType(GobiiColumnType.AUTOINCREMENT);
					instDSrun.getGobiiFileColumns().add(colIdx);
					instDSrun.setDataSetId(dto.getDatasetID());
					instDSrun.setContactEmail(App.INSTANCE.getUser().getUserEmail());
					instDSrun.setContactId(App.INSTANCE.getUser().getUserId());
					instDSrun.setGobiiCropType(ClientContext.getInstance(null, false).getCurrentClientCropType());
					instructions.getGobiiLoaderInstructions().add(instDSrun);
					// remove DNArun if needed
					boolean hasSample = false;
					boolean hasNum = false;
					GobiiLoaderInstruction inst = null;
					for(GobiiLoaderInstruction instruction : instructions.getGobiiLoaderInstructions()){
						if(instruction.getTable().equals("dnarun")){
							inst = instruction;
							for(GobiiFileColumn column : instruction.getGobiiFileColumns()){
								if(column.getName().equals("dnasample_name")){
									hasSample = true;
								}else if(column.getName().equals("num")){
									hasNum = true;
								}
							}
							break;
						}
					}
					if(!hasSample && !hasNum && inst != null){
						instructions.getGobiiLoaderInstructions().remove(inst);
					}
				}

				if(dto.getcCoord() > -1 && dto.getrCoord() > -1){
					GobiiLoaderInstruction instDataset = new GobiiLoaderInstruction();
					instDataset.setQcCheck(dto.isQcCheck());
					Utils.setDSInstructionFileDetails(instDataset, dto);
					instDataset.setTable("matrix");
					instDataset.setGobiiFile(dto.getFile());
					GobiiFileColumn colDataset = new GobiiFileColumn();
					colDataset.setName("matrix");
					colDataset.setGobiiColumnType(GobiiColumnType.CSV_BOTH);
					colDataset.setDataSetOrientationType(dto.getOrientation());
					colDataset.setCCoord(dto.getcCoord());
					colDataset.setRCoord(dto.getrCoord());
					colDataset.setDataSetType(DataSetType.valueOf(dto.getDatasetType().toUpperCase()));
					instDataset.getGobiiFileColumns().add(colDataset);
					instDataset.setDataSetId(dto.getDatasetID());
					instDataset.setContactEmail(App.INSTANCE.getUser().getUserEmail());
					instDataset.setContactId(App.INSTANCE.getUser().getUserId());
					instDataset.setGobiiCropType(ClientContext.getInstance(null, false).getCurrentClientCropType());
					instructions.getGobiiLoaderInstructions().add(instDataset);
				}
			}
			instructions.setInstructionFileName(folder);
			if(WizardUtils.confirm("Do you want to submit Instructions?")){
				try {
					PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(instructions, GobiiProcessType.CREATE);
					GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> restResource = new GobiiEnvelopeRestResource<>(App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
					PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = restResource.post(LoaderInstructionFilesDTO.class,
							payloadEnvelope);
					if(!Controller.getDTOResponse(this.getShell(), loaderInstructionFileDTOResponseEnvelope.getHeader(), null, true)){
						return false;
					}else{
						if(!dto.isRemote() && dto.getFiles().size() > 0)
							WizardUtils.sendFilesToServer(getShell(), dto.getFile().getSource(), dto.getFiles(), dto.getFileExtention());
					};
				} catch (Exception err) {
					Utils.log(getShell(), null, log, "Error submitting instruction file", err);
					return false;
				}
			}
			WizardUtils.saveTemplate(getShell(), TemplateCode.DSS, instructions);
		}catch(Exception err){
			System.out.println(err.getMessage());
			Utils.log(getShell(), null, log, "Error submitting instruction file", err);
			return false;
		}
		return true;
	}

	private boolean validate(){
		if(dto.getDatasetID() == null){
			Utils.log(getShell(), null, log, "Dataset is required information", new Exception("No Dataset specified!"));
			return false;
		}
		if(dto.getDatasetTypeID() == null){
			Utils.log(getShell(), null, log, "Dataset Type is required information", new Exception("No Dataset Type specified!"));
			return false;
		}
		if(dto.getDtoMarkers().getMarkerFields().size() > 0 && dto.getPlatformID() == null){
			Utils.log(getShell(), null, log, "Platform is required for Marker information", new Exception("No Platform specified!"));
			return false;
		}
		if(dto.getDtoMarkers().getDsMarkerFields().size() > 0 && dto.getDatasetID() == null){
			Utils.log(getShell(), null, log, "Dataset is required for DS Marker information", new Exception("No Dataset specified!"));
			return false;
		}
		if(dto.getDtoMarkers().getDsMarkerFields().size() > 0 && dto.getPlatformID() == null){
			Utils.log(getShell(), null, log, "Platform is required for DS Marker information", new Exception("No Platform specified!"));
			return false;
		}
		if(dto.getDtoMarkers().getLgFields().size() > 0 && dto.getMapsetID() == null){
			Utils.log(getShell(), null, log, "Mapset is required for LG information", new Exception("No Mapset specified!"));
			return false;
		}
		if(dto.getDtoMarkers().getLgMarkerFields().size() > 0 && dto.getPlatformID()==null){
			Utils.log(getShell(), null, log, "Platform is required for Marker_LG information", new Exception("No Mapset specified!"));
			return false;
		}
		if(dto.getDtoSamples().getSampleFields().size() > 0 && dto.getProjectID() == null){
			Utils.log(getShell(), null, log, "Project is required for DNA Sample information", new Exception("No Project specified!"));
			return false;
		}
		if(dto.getDtoSamples().getRunFields().size() > 0 && dto.getExperimentID() == null){
			Utils.log(getShell(), null, log, "Experiment is required forDNA Run information", new Exception("No Experiment specified!"));
			return false;
		}
		return true;
	}
}
