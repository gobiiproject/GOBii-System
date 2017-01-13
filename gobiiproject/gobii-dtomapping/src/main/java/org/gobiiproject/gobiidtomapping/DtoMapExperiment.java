package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;

/**
 * Created by Angel on 4/19/2016.
 */
public interface DtoMapExperiment {
    ExperimentDTO getExperiment( ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
    ExperimentDTO createExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
    ExperimentDTO updateExperiment(ExperimentDTO experimentDTO) throws GobiiDtoMappingException;

}
