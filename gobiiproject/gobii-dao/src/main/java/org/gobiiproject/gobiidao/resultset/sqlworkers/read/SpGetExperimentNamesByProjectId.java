package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetExperimentNamesByProjectId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetExperimentNamesByProjectId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select e.experiment_id, \n" +
                "e.name\n" +
                "from experiment e\n" +
                "where e.project_id= ? order by lower(name)";
        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("projectId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
