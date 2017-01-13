package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetCvTermsByGroup implements Work {
    /**
     * Created by Angel on 4/26/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvTermsByGroup(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select cv_id, term from cv where cv.group= ? order by lower(term)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String groupName = (String) parameters.get("groupName");
        preparedStatement.setString(1, groupName);
        
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
