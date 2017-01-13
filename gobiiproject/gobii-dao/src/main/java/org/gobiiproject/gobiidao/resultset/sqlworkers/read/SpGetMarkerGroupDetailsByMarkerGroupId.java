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
public class SpGetMarkerGroupDetailsByMarkerGroupId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetMarkerGroupDetailsByMarkerGroupId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select * " +
                "from marker_group\n" +
                "where marker_group_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("markerGroupId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
