package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpGetReferenceDetailsByReferenceId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetReferenceDetailsByReferenceId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select r.reference_id,\n" +
                "r.name,\n" +
                "r.version,\n" +
                "r.link,\n" +
                "r.file_path\n" +
                "from reference r\n" +
                "where reference_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("referenceId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
