package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapDataSet {

    DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO  createDataset(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO  updateDataset(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;

}
