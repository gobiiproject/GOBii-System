package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetProjecttNamesByContactId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetProjecttNamesByContactId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select p.project_id, \n" +
                "\t\t\t\t\tp.name \n" +
                "\t\t\tfrom project p\n" +
                "\t\t\twhere p.pi_contact= ? ";
        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("contactId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
