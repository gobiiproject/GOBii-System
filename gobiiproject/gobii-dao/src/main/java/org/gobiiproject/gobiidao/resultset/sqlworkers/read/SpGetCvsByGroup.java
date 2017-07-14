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
public class SpGetCvsByGroup implements Work {
    /**
     * Created by Angel on 4/26/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvsByGroup(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select cv.*, g.type as group_type from cv join cvgroup g on (cv.cvgroup_id=g.cvgroup_id) where lower(g.name)= ? order by lower(term)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String groupName = (String) parameters.get("groupName");
        preparedStatement.setString(1, groupName.toLowerCase());
        
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
