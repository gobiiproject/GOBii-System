package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class LoaderInstructionFilesDTO extends DtoMetaData {

    public LoaderInstructionFilesDTO() {
        super(ProcessType.CREATE);
    }

    private List<GobiiLoaderInstruction> gobiiLoaderInstructions = new ArrayList<>();
    private String instructionFileName = null;

    public List<GobiiLoaderInstruction> getGobiiLoaderInstructions() {
        return gobiiLoaderInstructions;
    }

    public void setGobiiLoaderInstructions(List<GobiiLoaderInstruction> gobiiLoaderInstructions) {
        this.gobiiLoaderInstructions = gobiiLoaderInstructions;
    }

    public String getInstructionFileName() {
        return instructionFileName;
    }

    public void setInstructionFileName(String instructionFileName) {
        this.instructionFileName = instructionFileName;
    }

}
