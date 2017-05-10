package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 2/19/2017.
 */
public class SpCvGroupById implements Work {


    private Map<String, Object> parameters = null;

    public SpCvGroupById(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select cvgroup_id,name,definition,type from cvgroup where cvgroup_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer groupId = (Integer) parameters.get("groupId");

        preparedStatement.setInt(1, groupId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
