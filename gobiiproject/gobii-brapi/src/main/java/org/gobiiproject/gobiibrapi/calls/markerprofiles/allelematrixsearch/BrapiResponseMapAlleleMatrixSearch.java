package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrixSearch {

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService;

    public BrapiMetaData search(String crop, String matrixDbId) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        Integer dataSetId = Integer.parseInt(matrixDbId);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTO = new ExtractorInstructionFilesDTO();
        GobiiExtractorInstruction gobiiExtractorInstruction = new GobiiExtractorInstruction();
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setDataSet(new GobiiFilePropNameId(dataSetId, null));
        gobiiExtractorInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        gobiiExtractorInstruction.setContactId(1);
        extractorInstructionFilesDTO.getGobiiExtractorInstructions().add(gobiiExtractorInstruction);

        String jobId = DateUtils.makeDateIdString();
        extractorInstructionFilesDTO.setInstructionFileName(jobId);


        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .createInstruction(crop, extractorInstructionFilesDTO);

        brapiMetaData.addStatusMessage("asynchid", extractorInstructionFilesDTONew.getJobId());

        return brapiMetaData;
    }

    public BrapiMetaData getStatus(String crop, String jobId, HttpServletRequest request) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .getStatus(crop, jobId);


        String brapiAsynchStatus = null;
        if( ( extractorInstructionFilesDTONew
                .getGobiiExtractorInstructions().size() > 0 ) &&
                (extractorInstructionFilesDTONew
                        .getGobiiExtractorInstructions().get(0).getDataSetExtracts().size() > 0 ) ) {

            GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTONew
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts()
                    .get(0);

            GobiiJobStatus gobiiJobStatus = gobiiDataSetExtract
                    .getGobiiJobStatus();

            switch (gobiiJobStatus) {

                case FAILED:
                    brapiAsynchStatus = "FAILED";
                    break;

                case STARTED:
                    brapiAsynchStatus = "PENDING";
                    break;

                case COMPLETED:
                    brapiAsynchStatus = "FINISHED";
                    break;

                case IN_PROGRESS:
                    brapiAsynchStatus = "INPROCESS";
                    break;

            }


            // this is only for test purposes!!! -- it should
            if (gobiiJobStatus.equals(GobiiJobStatus.COMPLETED)) {

                try {

                    String extractDirectory = gobiiDataSetExtract.getExtractDestinationDirectory();
                    File extractDirectoryFile = new File(extractDirectory);
                    if( extractDirectoryFile.exists() ) {

                        File[] extractedFiles = extractDirectoryFile.listFiles();
                        for(Integer idx = 0; idx < extractedFiles.length; idx++ ) {

                            File currentFile = extractedFiles[idx];

                            // first make the http link
                            RestUri restUri = new GobiiUriFactory(request.getServerName(),
                                    request.getServerPort(),
                                    request.getContextPath(),
                                    GobiiControllerType.GOBII)
                                    .resourceColl(GobiiServiceRequestId.URL_FILES)
                                    .addUriParam("gobiiJobId",jobId)
                                    .addUriParam("destinationType", GobiiFileProcessDir.EXTRACTOR_OUTPUT.toString().toLowerCase())
                                    .addQueryParam("fileName", currentFile.getName());

                            String fileUri = restUri.makeUrlComplete();
                            brapiMetaData.getDatafiles().add(fileUri);

                            // now the absolute path to the file
                            String filePath = FilenameUtils.normalize(currentFile.getAbsolutePath());
                            brapiMetaData.getDatafiles().add(filePath);
                        }

                    } else {
                        brapiMetaData.addStatusMessage("error", "The extract directory does not exist: " + extractDirectory);
                    }


                } catch (Exception e) {
                    brapiMetaData.addStatusMessage("Exception", e.getMessage());
                }
            }

        } else {
            brapiMetaData.addStatusMessage("error", "There are not extractor instructions for job : " + jobId);
        }

        brapiMetaData.addStatusMessage("asynchstatus", brapiAsynchStatus);

        return brapiMetaData;
    }

}
