package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetPropertiesForProject implements Work {

    private Map<String,Object> parameters = null;
    public SpGetPropertiesForProject(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String sql = "select * from getallpropertiesofproject(?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("projectId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
