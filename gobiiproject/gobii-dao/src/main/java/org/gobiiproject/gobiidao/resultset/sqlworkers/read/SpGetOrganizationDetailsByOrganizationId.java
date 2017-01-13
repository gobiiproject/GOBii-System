package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpGetOrganizationDetailsByOrganizationId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetOrganizationDetailsByOrganizationId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select r.organization_id,\n" +
                "r.name,\n" +
                "r.address,\n" +
                "r.website,\n"+
                "r.created_by,\n" +
                "r.created_date,\n" +
                "r.modified_by,\n" +
                "r.modified_date\n" +
                "from organization r\n" +
                "where organization_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("organizationId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
