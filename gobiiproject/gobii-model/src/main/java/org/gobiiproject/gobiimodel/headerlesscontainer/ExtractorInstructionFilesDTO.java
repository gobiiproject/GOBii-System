package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class ExtractorInstructionFilesDTO extends DTOBase {


    private String jobId;

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {
        ;
    }

    private List<GobiiExtractorInstruction>  gobiiExtractorInstructions = new ArrayList<>();
    private String instructionFileName = null;

    public List<GobiiExtractorInstruction> getGobiiExtractorInstructions() {
        return gobiiExtractorInstructions;
    }

    public void setGobiiExtractorInstructions(List<GobiiExtractorInstruction> gobiiExtractorInstructions) {
        this.gobiiExtractorInstructions = gobiiExtractorInstructions;
    }

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

}
