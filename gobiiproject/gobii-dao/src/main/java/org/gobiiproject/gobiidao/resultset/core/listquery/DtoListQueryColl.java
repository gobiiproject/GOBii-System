package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This colleciton of DtoListquery instances is consuemd by DtoMap classes to retreive lists
 * of DTOs from queries specified in the ListSqlId enumeration. This class's map is popualted
 * in the Spring xml configuraiton file. This is necessary because the DtoListQuery instances
 * require a StoredProcExec instances from the container, becuase it in turn contains
 * a reference to the entity manager. As a result of this mechanism, the DtoListQuery instances
 * cannot be crated with a parameter. This all works perfectly well except that the getList()
 * method in this class causes an unchecked conversion warning in the code that uses it. The
 * client of this class can use the @SuppressWarnings("unchecked") annotation to suppress this warning.
 * The only danger here is that if you use the wrong ListSqlId
 *
 * The code that calls the
 * getLiset() method will get an unchecked cast warning. The
 */
public class DtoListQueryColl {

    public void setListQueriesBySqlId(Map<ListSqlId, DtoListQuery> listQueriesBySqlId) {
        this.listQueriesBySqlId = listQueriesBySqlId;
    }

    public Map<ListSqlId, DtoListQuery> getListQueriesBySqlId() {
        return listQueriesBySqlId;
    }

    private Map<ListSqlId, DtoListQuery> listQueriesBySqlId = new HashMap<>();

    public List getList(ListSqlId listSqlId, Map<String, Object> parameters) throws GobiiDaoException {

        List returnVal;

        DtoListQuery dtoListQuery = listQueriesBySqlId.get(listSqlId);

        if (null != dtoListQuery ) {
            returnVal = dtoListQuery.getDtoList(parameters);
        } else {
            throw new GobiiDaoException("Unknown query id " + listSqlId.toString());
        }

        return returnVal;


    }
}
