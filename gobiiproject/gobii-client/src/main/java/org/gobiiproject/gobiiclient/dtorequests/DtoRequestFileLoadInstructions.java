// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestFileLoadInstructions {


    public LoaderInstructionFilesDTO process(LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws Exception {

        return new DtoRequestProcessor<LoaderInstructionFilesDTO>().process(loaderInstructionFilesDTO,
                LoaderInstructionFilesDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_FILE_LOAD_INSTRUCTIONS);
    }
}
