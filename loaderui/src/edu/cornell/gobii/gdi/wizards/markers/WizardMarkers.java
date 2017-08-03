package edu.cornell.gobii.gdi.wizards.markers;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
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
	private int piID;
	private int projectID;
	private int experimentID;
	private int datasetID;

	public WizardMarkers(String config, int piID, int projectID, int experimentID, int datasetID){
		setWindowTitle("Marker Data Loading Wizard");
		this.config = config;

		this.piID=piID;
		this.projectID = projectID;
		this.experimentID = experimentID;
		this.datasetID = datasetID;
	}

	@Override
	public void addPages() {
		addPage(new Pg1Markers(config, dto, piID, projectID, experimentID, datasetID));
		addPage(new Pg2Markers(config, dto));
		addPage(new Pg3Markers(config, dto));
	}

	@Override
	public boolean performFinish() {

		try{
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
				        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
				        GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
					GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> restResource = new GobiiEnvelopeRestResource<>(gobiiUriFactory.resourceColl(GobiiServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
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
