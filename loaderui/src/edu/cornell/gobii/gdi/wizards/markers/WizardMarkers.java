package edu.cornell.gobii.gdi.wizards.markers;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestFileLoadInstructions;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileLocationType;

import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;
import edu.cornell.gobii.gdi.utils.WizardUtils.TemplateCode;

public class WizardMarkers extends Wizard {
	
	private static Logger log = Logger.getLogger(WizardMarkers.class.getName());
	private String config;
	private DTOmarkers dto = new DTOmarkers();

	public WizardMarkers(String config){
		setWindowTitle("Marker Data Loading Wizard");
		this.config = config;
	}
	
	public WizardMarkers(String config, Integer projectID, Integer platformID, Integer experiemtnID) {
		setWindowTitle("Marker Data Loading Wizard");
		this.config = config;
	}

	@Override
	public void addPages() {
		addPage(new Pg1Markers(config, dto));
		addPage(new Pg2Markers(config, dto));
		addPage(new Pg3Markers(config, dto));
	}

	@Override
	public boolean performFinish() {
		try{
			String folder = WizardUtils.generateSourceFolder();
			if(!dto.isRemote()){
//			if((dto.getFile().getSource() == null || dto.getFile().getSource().isEmpty()) && dto.getFiles().size() > 0){
				String digesterInputDirectory = ClientContext
		                .getInstance(null, false)
		                .getFileLocationOfCurrenCrop(GobiiFileLocationType.RAWUSER_FILES);
				dto.getFile().setSource(digesterInputDirectory+folder);
				dto.getFile().setCreateSource(true);
			}else if(dto.getFile().getSource() != null && !dto.getFile().getSource().isEmpty()){
				dto.getFile().setCreateSource(false);
				dto.getFile().setRequireDirectoriesToExist(true);
			}else{
				Utils.log(getShell(), null, log, "Error submitting instruction file", new Exception("No source file(s) specified!"));
				return false;
			}
			String digesterOutputDirectory = ClientContext
	                .getInstance(null, false)
	                .getFileLocationOfCurrenCrop(GobiiFileLocationType.INTERMEDIATE_FILES);
			dto.getFile().setDestination(digesterOutputDirectory+folder);
			
			
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
				DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
				try {
					LoaderInstructionFilesDTO loaderInstructionFilesDTOResponse = dtoRequestFileLoadInstructions.process(instructions);
					boolean successful = Controller.getDTOResponse(this.getShell(), loaderInstructionFilesDTOResponse, null);
					if(!successful){
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
