package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapContactImpl implements DtoMapContact {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapContactImpl.class);


    @Autowired
    private RsContactDao rsContactDao;

    @Transactional
    @Override
    public ContactDTO getContactDetails(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ContactDTO returnVal = contactDTO;

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByContactId(contactDTO.getContactId());

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) throws GobiiDtoMappingException {
        ContactDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(contactDTO);
            Integer contactId = rsContactDao.createContact(parameters);
            returnVal.setContactId(contactId);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;
    }

    @Override
    public ContactDTO updateContact(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ContactDTO returnVal = contactDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsContactDao.updateContact(parameters);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }

        return returnVal;
    }
}
