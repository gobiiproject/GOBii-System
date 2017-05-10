package edu.cornell.gobii.gdi.wizards.markers;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;
import edu.cornell.gobii.gdi.utils.WizardUtils.TemplateCode;

public class WizardMarkers extends Wizard {

	private static Logger log = Logger.getLogger(WizardMarkers.class.getName());
	private String config;
	private DTOmarkers dto = new DTOmarkers();

	public WizardMarkers(String config, int piID, int projectID, int experimentID, int datasetID){
		setWindowTitle("Marker Data Loading Wizard");
		this.config = config;
		
		dto.setPiID(piID);
		dto.setProjectID(projectID);
		dto.setExperimentID(experimentID);
		dto.setDatasetID(datasetID);
	}

	@Override
	public void addPages() {
		addPage(new Pg1Markers(config, dto, dto.getPiID(), dto.getProjectID(), dto.getExperimentID(), dto.getDatasetID()));
		addPage(new Pg2Markers(config, dto));
		addPage(new Pg3Markers(config, dto));
	}

	@Override
	public boolean performFinish() {
		try{
////			String folder = WizardUtils.generateSourceFolder();
//			String folder = new File(dto.getPreviewDTO().getDirectoryName()).getName();
//			if(folder != null || !folder.isEmpty()){
//				//			if((dto.getFile().getSource() == null || dto.getFile().getSource().isEmpty()) && dto.getFiles().size() > 0){
//				String digesterInputDirectory = ClientContext
//						.getInstance(null, false)
//						.getFileLocationOfCurrenCrop(GobiiFileProcessDir.RAW_USER_FILES);
//				dto.getFile().setSource(digesterInputDirectory+folder);
//				dto.getFile().setCreateSource(false);
//				dto.getFile().setRequireDirectoriesToExist(true);
////			}else if(dto.getFile().getSource() != null && !dto.getFile().getSource().isEmpty()){
////				dto.getFile().setCreateSource(false);
////				dto.getFile().setRequireDirectoriesToExist(true);
//			}else{
//				Utils.log(getShell(), null, log, "Error submitting instruction file", new Exception("No source file(s) specified!"));
//				return false;
//			}
//			String digesterOutputDirectory = ClientContext
//					.getInstance(null, false)
//					.getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);
//			dto.getFile().setDestination(digesterOutputDirectory+folder);

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
				if(!WizardUtils.createMarkerInstructionsFromTemplate(getShell(), instructions, dto, folder)){
					return false;
				}
			}else{
				if(!validate()){
					return false;
				}
				instructions = new LoaderInstructionFilesDTO();
				WizardUtils.createMarkerInstructionsFromDTO(getShell(), instructions, dto, folder, false);
			}

			instructions.setInstructionFileName(folder);

			if(WizardUtils.confirm("Do you want to submit data?")){
				try {
					PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(instructions, GobiiProcessType.CREATE);
					GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> restResource = new GobiiEnvelopeRestResource<>(App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
					PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = restResource.post(LoaderInstructionFilesDTO.class,
							payloadEnvelope);
					if(!Controller.getDTOResponse(this.getShell(), loaderInstructionFileDTOResponseEnvelope.getHeader(), null, true)){
						return false;
					}
//					else{
//						if(!dto.isRemote() && dto.getFiles().size() > 0)
//							WizardUtils.sendFilesToServer(getShell(), dto.getFile().getSource(), dto.getFiles(), dto.getFileExtention());
//					};
				} catch (Exception err) {
					Utils.log(getShell(), null, log, "Error submitting instruction file", err);
					return false;
				}
			}

			WizardUtils.saveTemplate(getShell(), TemplateCode.MKR, instructions);
		}catch(Exception err){
			Utils.log(getShell(), null, log, "Error submitting instruction file", err);
			return false;
		}
		return true;
	}

	private boolean validate(){
		if(dto.getMarkerFields().size() > 0 && dto.getPlatformID() == null){
			Utils.log(getShell(), null, log, "Platform is required for Marker information", new Exception("No Platform specified!"));
			return false;
		}
		if(dto.getDsMarkerFields().size() > 0 && dto.getDatasetID() == null){
			Utils.log(getShell(), null, log, "Dataset is required for DS Marker information", new Exception("No Dataset specified!"));
			return false;
		}
		if(dto.getLgFields().size() > 0 && dto.getMapsetID() == null){
			Utils.log(getShell(), null, log, "Mapset is required for LG information", new Exception("No Mapset specified!"));
			return false;
		}
		if(dto.getLgMarkerFields().size() > 0 && dto.getPlatformID()==null){
			Utils.log(getShell(), null, log, "Platform is required for Marker_LG information", new Exception("No Mapset specified!"));
			return false;
		}
		return true;
	}
}
