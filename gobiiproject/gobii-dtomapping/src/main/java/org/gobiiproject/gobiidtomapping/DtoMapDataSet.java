package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapDataSet {

    List<DataSetDTO> getDataSets() throws GobiiDtoMappingException;

    DataSetDTO getDataSetDetails(Integer projectId) throws GobiiDtoMappingException;

    DataSetDTO createDataSet(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;

    DataSetDTO replaceDataSet(Integer projectId, DataSetDTO dataSetDTO) throws GobiiDtoMappingException;

}
