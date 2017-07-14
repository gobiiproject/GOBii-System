package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public class SpGetCvItemsByGroupId implements Work{

    private Map<String, Object> parameters = null;

    public SpGetCvItemsByGroupId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *, g.type as group_type " +
                "from cv c join cvgroup g\n" +
                "on (c.cvgroup_id = g.cvgroup_id)\n" +
                "where g.cvgroup_id=?\n" +
                "order by lower(g.name), lower(c.term)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer groupId = (Integer) parameters.get("groupId");

        preparedStatement.setInt(1, groupId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
