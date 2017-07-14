package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/7/2016.
 * Modified by AVB on 9/29/2016.
 */
public class SpGetMapSetNames implements Work {

    public SpGetMapSetNames() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "SELECT\n" +
                "\tmapset_id,\n" +
                "\tname\n" +
                "FROM\n" +
                "\tmapset\n" +
                "ORDER BY LOWER(name);";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
    }
}
