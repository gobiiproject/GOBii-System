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
public class SpUserCvGroupByName implements Work {


    private Map<String, Object> parameters = null;

    public SpUserCvGroupByName(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select cvgroup_id,name,definition,type from cvgroup where lower(name) = ? and type= 2";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        String groupName =  parameters.get("groupName").toString().toLowerCase();

        preparedStatement.setString(1, groupName);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
