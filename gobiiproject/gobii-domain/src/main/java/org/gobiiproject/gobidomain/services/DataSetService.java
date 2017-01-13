package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DataSetService {

    DataSetDTO createDataSet(DataSetDTO dataSetDTO) throws GobiiDomainException;
    DataSetDTO replaceDataSet(Integer dataSetId, DataSetDTO dataSetDTO) throws GobiiDomainException;
    List<DataSetDTO> getDataSets() throws GobiiDomainException;
    DataSetDTO getDataSetById(Integer dataSetId);

}
