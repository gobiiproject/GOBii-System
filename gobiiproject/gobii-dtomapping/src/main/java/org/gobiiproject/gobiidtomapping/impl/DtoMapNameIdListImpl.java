package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for returning lists of NameIdDTO instances for
 * entities that are specified from a DtoMapNameIdParams instance, which may also
 * include a filter. The client of this class will have done any enum and other type conversions
 * so that this class can work with the native Java types in DtoMapNameIdParams. This class employs a
 * map of classes that implement the DtoMapNameIdFetch interface. This approach was taken to make it
 * easy to add support for a new entity type: all you have
 * to do is implement DtoMapNameIdFetch (see the org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds for
 * all the existing classes) and inform the Spring configuration about the new class so that the map in
 * this class gets populated accordingly. (It is necessary to use the injection container so that the
 * classes that implement DtoMapNameIdFetch can inject the Dao classes they need.) At the moment, the
 * Spring configuration is in application-config.xml (in the web application): there the map property is
 * configured for the DtoMapNameIdListImpl.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


    private Map<GobiiEntityNameType, DtoMapNameIdFetch> dtoMapNameIdFetchMap = new HashMap<>();

    public void setDtoMapNameIdFetchMap(Map<GobiiEntityNameType, DtoMapNameIdFetch> dtoMapNameIdFetchMap) {
        this.dtoMapNameIdFetchMap = dtoMapNameIdFetchMap;
    }

    @Override
    public List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (this.dtoMapNameIdFetchMap.containsKey(dtoMapNameIdParams.getEntityType())) {

            DtoMapNameIdFetch dtoMapNameIdFetch = this.dtoMapNameIdFetchMap.get(dtoMapNameIdParams.getEntityType());
            returnVal = dtoMapNameIdFetch.getNameIds(dtoMapNameIdParams);

        } else {
            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "There is no NameIDFetch instance to handle this entity type: " + dtoMapNameIdParams.getEntityType().toString());
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
