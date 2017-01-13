package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetMapsetDetailsByMapsetId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetMapsetDetailsByMapsetId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "\tmapset_id,\n" +
                "\tname,\n" +
                "\tcode,\n" +
                "\tdescription,\n" +
                "\treference_id,\n" +
                "\ttype_id,\n" +
                "\tcreated_by,\n" +
                "\tcreated_date,\n" +
                "\tmodified_by,\n" +
                "\tmodified_date,\n" +
                "\tstatus\n" +
                "from\n" +
                "\tmapset\n" +
                "where\n" +
                "\tmapset_id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("mapSetId"));
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
