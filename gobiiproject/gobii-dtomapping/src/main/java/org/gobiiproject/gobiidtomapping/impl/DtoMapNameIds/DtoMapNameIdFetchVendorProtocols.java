package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsProtocolDao;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIdFetch;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdFetchVendorProtocols implements DtoMapNameIdFetch {

    @Autowired
    private RsProtocolDao rsProtocolDao = null;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchVendorProtocols.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.VENDORS_PROTOCOLS;
    }


    private List<NameIdDTO> makeListFromResultSet(ResultSet resultSet) {
        List<NameIdDTO> returnVal = new ArrayList<>();

        NameIdDTO nameIdDTO;
        try {
            while (resultSet.next()) {
                nameIdDTO = new NameIdDTO();
                nameIdDTO.setId(resultSet.getInt("vendor_protocol_id"));
                nameIdDTO.setName(resultSet.getString("name"));
                returnVal.add(nameIdDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }

    private List<NameIdDTO> getNameIdListForVendorProtcolByProtocolId(Integer protocolId) throws GobiiException {

        List<NameIdDTO> returnVal;

        ResultSet resultSet = rsProtocolDao.getVendorsProtocolNamesByProtocolId(protocolId);

        returnVal = this.makeListFromResultSet(resultSet);


        return returnVal;
    }



    private List<NameIdDTO> getProtocolNames() throws GobiiException {

        List<NameIdDTO> returnVal;

        try {

            ResultSet resultSet = rsProtocolDao.getVendorProtocolNames();

            returnVal = this.makeListFromResultSet(resultSet);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (GobiiFilterType.NONE == dtoMapNameIdParams.getGobiiFilterType()) {
            returnVal = this.getProtocolNames();
        } else if (GobiiFilterType.BYTYPEID == dtoMapNameIdParams.getGobiiFilterType()) {

            returnVal = this.getNameIdListForVendorProtcolByProtocolId(dtoMapNameIdParams.getFilterValueAsInteger());

        } else {

            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Unsupported filter type for "
                            + this.getEntityTypeName().toString().toLowerCase()
                            + ": " + dtoMapNameIdParams.getGobiiFilterType());
        }

        return returnVal;
    }
}
