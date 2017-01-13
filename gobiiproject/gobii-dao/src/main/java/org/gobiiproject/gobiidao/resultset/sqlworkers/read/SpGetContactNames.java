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
public class SpGetContactNames implements Work {

    public SpGetContactNames() {

    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String sql = "SELECT c.contact_id,\n" +
                "\t\t\t\tc.lastname,\n" +
                "\t\t\t\tc.firstname\n" +
                "\t\tfrom contact c\n" +
                "\t\torder by lastname";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
