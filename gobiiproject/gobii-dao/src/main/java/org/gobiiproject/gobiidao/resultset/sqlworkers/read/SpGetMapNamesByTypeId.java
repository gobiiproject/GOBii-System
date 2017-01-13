package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetMapNamesByTypeId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetMapNamesByTypeId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select ms.mapset_id, ms.name from mapset ms where ms.type_id = ? order by lower(name)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer typeId = (Integer) parameters.get("typeId");
        preparedStatement.setInt(1, typeId);
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
