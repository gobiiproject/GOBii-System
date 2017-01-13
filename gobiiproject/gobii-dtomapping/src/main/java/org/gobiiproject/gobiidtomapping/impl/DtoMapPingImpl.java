package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsPingDao;
import org.gobiiproject.gobiidao.resultset.core.DbMetaData;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapPingImpl implements DtoMapPing {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPingImpl.class);

    @Autowired
    DbMetaData dbMetaData;

    @Autowired
    RsPingDao rsPingDao;

    @Override
    public PingDTO getPings(PingDTO pingDTO) {

        PingDTO returnVal = pingDTO;

        try {

            List<String> pingResponses = new ArrayList<>();

            String newPingMessage = LineUtils.wrapLine("Mapping layer responded");
            pingResponses.add(newPingMessage);

            Map<String, String> dbInfo = rsPingDao.getPingResponses(pingDTO.getDbMetaData());
            for (Map.Entry<String, String> entry : dbInfo.entrySet()) {

                String currentPingMessage = LineUtils.wrapLine(entry.getKey()
                        + ": "
                        + entry.getValue());

                pingResponses.add(currentPingMessage);
            }

            returnVal.setPingResponses(pingResponses);

        } catch (Exception e) {

            // Ok. So, if we got an exception here, it's possible that when we called
            // rsPingDao.getPingResponses(), JPA was unable to open a connection. Note that
            // rsPingDao.getPingResponses() is transaction-decorated and intercepted, such that
            // code in in that method is never even executed -- the framework is trying to open
            // connection before even invoking the method itself. The problem is that the exception
            // thrown by JPA does not tell us which database it was we were trying to open a connection
            // to. In theory, DbMetaData should be able to just call DataSource.getConnection()  in
            // order to tell us the url of the db. In practice, calling DataSource.getConnection() tries
            // to open a connection as well (this makes sense -- why give someone a dead connection?).
            // But, the good news is that DbMetaData tries to get the connection data in such a way
            // that the when the exception is thrown, it does specify the connection information about
            // the connection. So, here we go . . .

            String dbUrl = null;
            try {
                dbUrl = dbMetaData.getCurrentDbUrl();

            } catch (SQLException sqlException) {

                returnVal.getDtoHeaderResponse().addException(sqlException);
            }

            // in case we did succeed in getting the url the normal way
            Exception exception = null;
            if (null != dbUrl) {
                exception = new Exception("Error with db url: " + dbUrl, e);
            } else {
                exception = e;
            }

            returnVal.getDtoHeaderResponse().addException(exception);

            LOGGER.error("Gobii Maping Error", exception);
        }


        return returnVal;

    } // getPings
}
