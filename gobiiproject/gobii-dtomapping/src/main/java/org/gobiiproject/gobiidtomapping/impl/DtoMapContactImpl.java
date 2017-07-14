package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.pojos.Contact;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class DtoMapContactImpl implements DtoMapContact {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapContactImpl.class);


    @Autowired
    private RsContactDao rsContactDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;


    @SuppressWarnings("unchecked")
    @Override
    public List<ContactDTO> getContacts() throws GobiiDtoMappingException {

        List<ContactDTO> returnVal = new ArrayList<ContactDTO>();


        returnVal = (List<ContactDTO>) dtoListQueryColl.getList(ListSqlId.QUERY_ID_CONTACT_ALL, null);


        return returnVal;
    }

    @Transactional
    @Override
    public ContactDTO getContactDetails(Integer contactId) throws GobiiDtoMappingException {

        ContactDTO returnVal = new ContactDTO();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByContactId(contactId);

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet


        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    public ContactDTO getContactByUserName(String username) throws GobiiDtoMappingException {

        ContactDTO returnVal = new ContactDTO();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByUsername(username);

            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }

        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Transactional
    @Override
    public ContactDTO getContactByEmail(String email) throws GobiiDtoMappingException {

        ContactDTO returnVal = new ContactDTO();

        try {

            ResultSet resultSet = rsContactDao.getContactDetailsByEmail(email);

            if (resultSet.next()) {

                // apply contact values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            } // iterate resultSet


        } catch (SQLException e) {
            LOGGER.error("error retrieving analysis names", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Transactional
    @Override
    public ContactDTO createContact(ContactDTO contactDTO) throws GobiiDtoMappingException {

        ContactDTO returnVal = contactDTO;


        if (LineUtils.isNullOrEmpty(contactDTO.getEmail())) {
            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.MISSING_REQUIRED_VALUE, "email address is null");
        } else {
            ContactDTO contactForEmail = this.getContactByEmail(contactDTO.getEmail());
            if ((contactForEmail.getContactId() != null) && (contactForEmail.getContactId() > 0)) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_ALREADY_EXISTS,
                        "A contact with email address " + contactDTO.getEmail() + " already exists");
            }
        }

        if (!LineUtils.isNullOrEmpty(contactDTO.getUserName())) {

            ContactDTO contactForUserName = this.getContactByUserName(contactDTO.getUserName());
            if ((contactForUserName.getContactId() != null) && (contactForUserName.getContactId() > 0)) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_ALREADY_EXISTS,
                        "A contact with userName " + contactDTO.getUserName() + " already exists");
            }
        }


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        Integer contactId = rsContactDao.createContact(parameters);
        returnVal.setContactId(contactId);

        return returnVal;
    }

    @Transactional
    @Override
    public ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws GobiiDtoMappingException {

        ContactDTO returnVal = contactDTO;


        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("contactId", contactId);
        rsContactDao.updateContact(parameters);

        return returnVal;
    }
}
