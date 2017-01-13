package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DataSetService {

    DataSetDTO processDataSet(DataSetDTO dataSetDTO);

}
