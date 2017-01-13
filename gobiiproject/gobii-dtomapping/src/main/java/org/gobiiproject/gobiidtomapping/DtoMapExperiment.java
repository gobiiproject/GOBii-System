package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;

import java.util.List;

/**
 * Created by Angel on 4/19/2016.
 */
public interface DtoMapExperiment {

    List<ExperimentDTO> getExperiments() throws GobiiDtoMappingException;
    ExperimentDTO getExperimentDetails(Integer experimentId) throws GobiiDtoMappingException;
    ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
    ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDtoMappingException;

}
