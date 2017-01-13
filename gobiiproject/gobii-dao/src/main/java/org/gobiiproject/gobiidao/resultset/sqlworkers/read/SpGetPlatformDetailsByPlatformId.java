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
public class SpGetPlatformDetailsByPlatformId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetPlatformDetailsByPlatformId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select platform_id,\n" +
                "name,\n" +
                "code,\n" +
                "vendor_id,\n" +
                "description,\n" +
                "created_by,\n" +
                "created_date,\n" +
                "modified_by,\n" +
                "modified_date,\n" +
                "status,\n" +
                "type_id\n" +
                "from platform \n" +
                "where platform_id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("platformId"));
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
