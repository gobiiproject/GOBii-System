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
public class SpGetDatasetNames implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDatasetNames() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select dataset_id, name from dataset";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
