package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/11/2016.
 */
public class SpGetProjectByNameAndPIContact implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProjectByNameAndPIContact(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select * \n" +
                "\tfrom project\n" +
                "\twhere name = ? \n" +
                "\tand pi_contact= ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer piContactId = (Integer) parameters.get("piContactId");
        String projectName = (String) parameters.get("projectName");

        preparedStatement.setString(1,projectName);
        preparedStatement.setInt(2, piContactId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
