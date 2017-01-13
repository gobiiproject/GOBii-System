package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/7/2016.
 * Edited by Angel on 4/14/2016.
 */
public class SpGetTableDisplayDetailByDisplayId implements Work {

    public SpGetTableDisplayDetailByDisplayId() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *" +
                "from display\n" +
                " order by lower(table_name), lower(column_name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
//        String tableName = parameters.get("tableName").toString().toLowerCase();
//        preparedStatement.setString(1, tableName);
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
