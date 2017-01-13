package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 4/7/2016.
 */
public interface RsContactDao {

    ResultSet getAllContactNames() throws GobiiDaoException;
    ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException;
    ResultSet getContactDetailsByContactId(Integer contact) throws GobiiDaoException;
    Integer createContact(Map<String,Object> parameters) throws GobiiDaoException;
    void updateContact(Map<String,Object> parameters) throws GobiiDaoException;
}
