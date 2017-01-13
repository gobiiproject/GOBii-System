package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetDisplayNamesForTable implements Work {

    private Map<String,Object> parameters = null;
    public SpGetDisplayNamesForTable(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select table_name,\n" +
                "display_id,\n" +
                "column_name,\n" +
                "display_name\n" +
                "rank\n" +
                "from display\n" +
                "where LOWER(table_name) = ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        String tableName = parameters.get("tableName").toString().toLowerCase();
        preparedStatement.setString(1, tableName);
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
