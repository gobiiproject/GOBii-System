package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 * Edited by Angel on 4/14/2016.
 */
public class SpGetTableDisplayDetailByDisplayId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetTableDisplayDetailByDisplayId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *" +
                "from display\n" +
                " where display_id = ? \n " +
                " order by lower(table_name), lower(column_name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer displayId = (Integer) parameters.get("displayId");
        preparedStatement.setInt(1, displayId);
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
