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
public class SpGetManifestDetailsByManifestId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetManifestDetailsByManifestId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select r.manifest_id,\n" +
                "r.name,\n" +
                "r.code,\n" +
                "r.file_path,\n"+
                "r.created_by,\n" +
                "r.created_date,\n" +
                "r.modified_by,\n" +
                "r.modified_date\n" +
                "from manifest r\n" +
                "where manifest_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("manifestId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
