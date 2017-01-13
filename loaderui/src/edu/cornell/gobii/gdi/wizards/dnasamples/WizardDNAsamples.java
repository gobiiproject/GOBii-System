package edu.cornell.gobii.gdi.wizards.dnasamples;

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

public class WizardDNAsamples extends Wizard {

	private static Logger log = Logger.getLogger(WizardDNAsamples.class.getName());
	private String config;
	private DTOsamples dto = new DTOsamples();

	public WizardDNAsamples(String config) {
		setWindowTitle("DNA Sample Data Loading Wizard");
		this.config = config;
	}

	@Override
	public void addPages() {
		addPage(new Pg1DNAsamples(config, dto));
		addPage(new Pg2DNAsamples(config, dto));
		addPage(new Pg3DNAsamples(config, dto));
	}

	@Override
	public boolean performFinish() {
		try {
			//// String folder = WizardUtils.generateSourceFolder();
			// String folder = new
			//// File(dto.getPreviewDTO().getDirectoryName()).getName();
			//// if(!dto.isRemote()){
			// if(folder != null || !folder.isEmpty()){
			// // if((dto.getFile().getSource() == null ||
			//// dto.getFile().getSource().isEmpty()) && dto.getFiles().size() >
			//// 0){
			// String digesterInputDirectory = ClientContext
			// .getInstance(null, false)
			// .getFileLocationOfCurrenCrop(GobiiFileProcessDir.RAW_USER_FILES);
			// dto.getFile().setSource(digesterInputDirectory+folder);
			// dto.getFile().setCreateSource(false);
			// dto.getFile().setRequireDirectoriesToExist(true);
			//// }else if(dto.getFile().getSource() != null &&
			//// !dto.getFile().getSource().isEmpty()){
			//// dto.getFile().setCreateSource(false);
			//// dto.getFile().setRequireDirectoriesToExist(true);
			// }else{
			// Utils.log(getShell(), null, log, "Error submitting instruction
			//// file", new Exception("No source file(s) specified!"));
			// return false;
			// }
			//
			//// String folder = new
			//// File(dto.getPreviewDTO().getDirectoryName()).getName();
			//// dto.getFile().setSource(dto.getPreviewDTO().getDirectoryName());
			//// dto.getFile().setCreateSource(false);
			//// dto.getFile().setRequireDirectoriesToExist(true);
			//setDestination
			// String digesterOutputDirectory = ClientContext
			// .getInstance(null, false)
			// .getFileLocationOfCurrenCrop(GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);
			// dto.getFile().setDestination(digesterOutputDirectory+folder);

			String folder = new File(dto.getPreviewDTO().getDirectoryName()).getName();
			if (folder != null || !folder.isEmpty()) {
				WizardUtils.createInstructionsForWizard(folder, dto);
			} else {
				Utils.log(getShell(), null, log, "Error submitting instruction file",
						new Exception("No source file(s) specified!"));
				return false;
			}

			LoaderInstructionFilesDTO instructions = null;
			if (dto.getTemplate() != null) {
				instructions = WizardUtils.loadTemplate(dto.getTemplate());
				if (!WizardUtils.createSampleInstructionsFromTemplate(getShell(), instructions, dto, folder))
					return false;
			} else {
				if (!validate()) {
					return false;
				}
				instructions = new LoaderInstructionFilesDTO();
				WizardUtils.createSampleInstructionsFromDTO(getShell(), instructions, dto, folder, false);
			}
			instructions.setInstructionFileName(folder);

			if (WizardUtils.confirm("Do you want to submit Instructions?")) {
				try {
					PayloadEnvelope<LoaderInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(instructions,
							GobiiProcessType.CREATE);
					GobiiEnvelopeRestResource<LoaderInstructionFilesDTO> restResource = new GobiiEnvelopeRestResource<>(
							App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS));
					PayloadEnvelope<LoaderInstructionFilesDTO> loaderInstructionFileDTOResponseEnvelope = restResource
							.post(LoaderInstructionFilesDTO.class, payloadEnvelope);
					if (!Controller.getDTOResponse(this.getShell(),
							loaderInstructionFileDTOResponseEnvelope.getHeader(), null, true)) {
						return false;
					}
					// else{
					// if(!dto.isRemote() && dto.getFiles().size() > 0)
					// WizardUtils.sendFilesToServer(getShell(),
					// dto.getFile().getSource(), dto.getFiles(),
					// dto.getFileExtention());
					// };
				} catch (Exception err) {
					Utils.log(getShell(), null, log, "Error submitting instruction file", err);
					return false;
				}
			}
			WizardUtils.saveTemplate(getShell(), TemplateCode.DNA, instructions);
		} catch (Exception err) {
			Utils.log(getShell(), null, log, "Error submitting instruction file", err);
			return false;
		}
		return true;
	}

	private boolean validate() {
		if (dto.getSampleFields().size() > 0 && dto.getProjectID() == null) {
			Utils.log(getShell(), null, log, "Project is required for DNA Sample information",
					new Exception("No Project specified!"));
			return false;
		}
		if (dto.getRunFields().size() > 0 && dto.getExperimentID() == null) {
			Utils.log(getShell(), null, log, "Experiment is required forDNA Run information",
					new Exception("No Experiment specified!"));
			return false;
		}
		return true;
	}

}
