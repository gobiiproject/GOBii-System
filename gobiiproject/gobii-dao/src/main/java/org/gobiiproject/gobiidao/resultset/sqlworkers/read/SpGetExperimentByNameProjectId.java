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
public class SpGetExperimentByNameProjectId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetExperimentByNameProjectId(Map<String, Object> parameters) {
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
                "\twhere e.name= ?  \n" +
                "\t\t\tAND e.project_id= ?\n";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String experimentName = (String) parameters.get("experimentName");
        Integer projectId = (Integer) parameters.get("projectId");

        preparedStatement.setString(1,experimentName);
        preparedStatement.setInt(2, projectId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
