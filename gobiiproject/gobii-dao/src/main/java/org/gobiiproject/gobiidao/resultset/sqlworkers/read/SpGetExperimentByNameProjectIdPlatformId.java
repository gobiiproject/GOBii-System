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
public class SpGetExperimentByNameProjectIdPlatformId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetExperimentByNameProjectIdPlatformId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select p.name \"platform_name\",e.*\n" +
                "from experiment e\n" +
                "join platform p on (e.platform_id=p.platform_id)\n" +
                "\twhere e.name= ?  \n" +
                "\t\t\tAND e.project_id= ?\n" +
                "\t\t\tand e.platform_id = ?\n ";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String experimentName = (String) parameters.get("experimentName");
        Integer projectId = (Integer) parameters.get("projectId");
        Integer platformId = (Integer) parameters.get("platformId");

        preparedStatement.setString(1,experimentName);
        preparedStatement.setInt(2, projectId);
        preparedStatement.setInt(3, platformId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
