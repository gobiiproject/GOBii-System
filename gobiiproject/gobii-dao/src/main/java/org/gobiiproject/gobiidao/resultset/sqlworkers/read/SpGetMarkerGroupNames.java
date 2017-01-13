package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetMarkerGroupNames implements Work {

    private Map<String, Object> parameters = null;

    public SpGetMarkerGroupNames() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select marker_group_id, name from marker_group order by lower(name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
