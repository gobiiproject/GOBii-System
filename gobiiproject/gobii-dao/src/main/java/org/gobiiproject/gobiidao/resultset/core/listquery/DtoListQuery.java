package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQuery<T> {

    Logger LOGGER = LoggerFactory.getLogger(DtoListQuery.class);

    private ListSqlId listSqlId;
    private Class<T> dtoType;
    private StoredProcExec storedProcExec;

    public DtoListQuery(StoredProcExec storedProcExec,
                        Class<T> dtoType,
                        ListSqlId listSqlId) {

        this.storedProcExec = storedProcExec;
        this.dtoType = dtoType;
        this.listSqlId = listSqlId;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> getDtoList(Map<String, Object> parameters) throws GobiiException {

        List<T> returnVal;

        try {

            String sql = listSqlId.getSql();
            DtoListFromSql<T> dtoListFromSql = new DtoListFromSql<>(dtoType, sql, parameters);
            this.storedProcExec.doWithConnection(dtoListFromSql);
            returnVal = dtoListFromSql.getDtoList();

        }catch(SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    } // getDtoList()
}
