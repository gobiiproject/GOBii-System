package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.List;

/**
 * Created by Phil on 10/16/2016.
 */
public interface DtoMapNameIdFetch {

    GobiiEntityNameType getEntityTypeName() throws GobiiException;
    List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException;
}
