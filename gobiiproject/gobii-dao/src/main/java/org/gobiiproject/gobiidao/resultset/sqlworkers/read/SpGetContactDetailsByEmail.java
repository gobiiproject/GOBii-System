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
public class SpGetContactDetailsByEmail implements Work {

    private Map<String,Object> parameters = null;
    public SpGetContactDetailsByEmail(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select c.contact_id,\n" +
                "c.lastname,\n" +
                "c.firstname,\n" +
                "c.code,\n" +
                "c.email,\n" +
                "c.roles,\n" +
                "c.organization_id,\n" +
                "c.created_by,\n" +
                "c.created_date,\n" +
                "c.modified_by,\n" +
                "c.modified_date,\n" +
                "c.username\n"+
                " from contact c\n" +
                " where email=?\n";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setString(1, (String) parameters.get("email"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
