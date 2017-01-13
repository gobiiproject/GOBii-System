package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class ExtractorInstructionFilesDTO extends DtoMetaData {

    public ExtractorInstructionFilesDTO() {
        super(ProcessType.CREATE);
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

}
