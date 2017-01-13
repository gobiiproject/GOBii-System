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
public class SpGetDatasetNamesByExperimentId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetDatasetNamesByExperimentId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select dataset_id, name\n" +
                "from dataset\n" +
                "where experiment_id=? ";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("experimentId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
