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
public class SpGetPlatformNamesByTypeId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetPlatformNamesByTypeId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select platform_id, name from platform where type_id = ? order by lower(name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("typeId"));

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
