// ************************************************************************
// (c) 2016 GOBii DataSet
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;

public class DtoRequestDataSet {


    public DataSetDTO process(DataSetDTO dataSetDTO) throws Exception {

        return new DtoRequestProcessor<DataSetDTO>().process(dataSetDTO,
                DataSetDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_DATASET);

    } // getPing()

}
