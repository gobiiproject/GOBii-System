package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 10/16/2016.
 */
public class DtoMapNameIdFetchContacts implements DtoMapNameIdFetch {

    @Autowired
    private RsContactDao rsContactDao = null;


    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdFetchContacts.class);


    @Override
    public GobiiEntityNameType getEntityTypeName() throws GobiiException {
        return GobiiEntityNameType.CONTACTS;
    }


    private NameIdDTO makeNameIdDto(ResultSet resultSet) throws SQLException {

        NameIdDTO returnVal = new NameIdDTO();
        returnVal.setId(resultSet.getInt("contact_id"));
        String lastName = resultSet.getString("lastname");
        String firstName = resultSet.getString("firstname");
        String name = lastName + ", " + firstName;
        returnVal.setName(name);
        return returnVal;
    }

    private List<NameIdDTO> getNameIdListForContactsByRoleName(String roleName) throws GobiiException {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsContactDao.getContactNamesForRoleName(roleName);
            List<NameIdDTO> listDTO = new ArrayList<>();

            while (resultSet.next()) {
                returnVal.add(this.makeNameIdDto(resultSet));
            }


        } catch (SQLException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return (returnVal);

    } // getNameIdListForContactsByRoleName()

    private List<NameIdDTO> getNameIdListForAllContacts() {

        List<NameIdDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsContactDao.getAllContactNames();

            while (resultSet.next()) {
                returnVal.add(this.makeNameIdDto(resultSet));
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);

        }

        return (returnVal);

    } // getNameIdListForContactsByRoleName()


    @Override
    public List<NameIdDTO> getNameIds(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException {

        List<NameIdDTO> returnVal;

        if (GobiiFilterType.NONE == dtoMapNameIdParams.getGobiiFilterType()) {
            returnVal = this.getNameIdListForAllContacts();
        } else {

            if (GobiiFilterType.BYTYPENAME == dtoMapNameIdParams.getGobiiFilterType()) {

                returnVal = this.getNameIdListForContactsByRoleName(dtoMapNameIdParams.getFilterValueAsString());

            } else {

                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "Unsupported filter type for "
                                + this.getEntityTypeName().toString().toLowerCase()
                                + ": " + dtoMapNameIdParams.getGobiiFilterType());
            }
        }

        return returnVal;
    }

}
