package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is for the purpose of creating a list of DTO instances from a result set using
 * an open connection. The legacy retrieval mechanism (e.g., SpGetContacts) executed a query
 * and then returned the reesult set. However, as soon as the exevute() method returns, the
 * connection is closed by the framework. In many cases this doesn't matter. However, when retrieving
 * a list of items (but not when retrieving a single item), the connection must be open in order to
 * retrieve postgres list column types. There are probably other such cases out there, so this
 * class should be used for all retrievals.
 * @param <T> The dto type
 */
public class DtoListFromSql<T> implements Work {


    private Class<T> dtoType;
    private String sql;
    private Map<String, Object> parameters = null;

    public DtoListFromSql(Class<T> dtoType,
                          String sql,
                          Map<String, Object> parameters) {
        this.dtoType = dtoType;
        this.sql = sql;
        this.parameters = parameters;
    }


    List<T> dtoList = new ArrayList<>();

    public List<T> getDtoList() {
        return dtoList;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        this.dtoList = new ArrayList<>();
        while (resultSet.next()) {
            try {
                T dto = dtoType.newInstance();
                ResultColumnApplicator.applyColumnValues(resultSet, dto);
                dtoList.add(dto);
            } catch (IllegalAccessException e) {
                throw new SQLException(e);
            } catch (InstantiationException e) {
                throw new SQLException(e);
            }
        }

    } // execute()
}
