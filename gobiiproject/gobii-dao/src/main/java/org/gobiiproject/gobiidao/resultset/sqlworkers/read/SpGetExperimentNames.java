package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/29/2016.
 */
public class SpGetExperimentNames implements Work {

    private Map<String, Object> parameters = null;

    public SpGetExperimentNames() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select experiment_id, name from experiment order by lower(name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
