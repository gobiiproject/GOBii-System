package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;
import java.util.Map;
import java.sql.ResultSet;
/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface RsProtocolDao {

    ResultSet getProtocolDetailsByProtocolId(Integer protocolId) throws GobiiDaoException;
    Integer createProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    void updateProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    Integer createVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    ResultSet getProtocolNames() throws GobiiDaoException;
    ResultSet getVendorProtocolNames() throws GobiiDaoException;
    ResultSet getVendorsByProtocolId(Integer protocolId) throws GobiiDaoException;
    ResultSet getVendorsProtocolNamesByProtocolId(Integer protocolId) throws GobiiDaoException;
    ResultSet getProtocolNamesByPlatformId(Integer platformId) throws GobiiDaoException;
    ResultSet getVendorByProtocolVendorName(Map<String, Object> parameters) throws GobiiDaoException;
    ResultSet getVendorByProtocolByCompoundIds(Integer protocolId, Integer vendorId) throws GobiiDaoException;
    ResultSet getVendorProtocolsForVendor(Integer organizationId) throws GobiiDaoException;
    ResultSet getVendorProtocolsForProtocol(Integer protocolId) throws GobiiDaoException;
    ResultSet getVendorProtocolForVendorProtocolId(Integer vendorProtocolId) throws GobiiDaoException;
    ResultSet getProtocolDetailsByExperimentId(Integer experimentId) throws GobiiDaoException;
    void updateVendorProtocol(Map<String,Object> parameters) throws GobiiDaoException;
}
