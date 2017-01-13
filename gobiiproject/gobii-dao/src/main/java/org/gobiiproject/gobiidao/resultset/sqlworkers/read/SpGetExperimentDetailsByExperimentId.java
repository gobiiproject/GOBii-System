package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/11/2016.
 */
public class SpGetExperimentDetailsByExperimentId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetExperimentDetailsByExperimentId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select e.*\n" +
                "from experiment e\n" +
                "where experiment_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer experimentId = (Integer) parameters.get("experimentId");
        preparedStatement.setInt(1, experimentId);
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
