package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetCvGroups implements Work {

    private Map<String, Object> parameters = null;

    public SpGetCvGroups() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select distinct on (lower(cv.group)) lower(cv.group), cv.cv_id  from cv order by lower(cv.group)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
