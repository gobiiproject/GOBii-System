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
public class SpGetAnalysisDetailsByAnalysisId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetAnalysisDetailsByAnalysisId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\ta.analysis_id,\n" +
                "\ta.\"name\",\n" +
                "\ta.description,\n" +
                "\ta.type_id,\n" +
                "\ta.program,\n" +
                "\ta.programversion,\n" +
                "\ta.algorithm,\n" +
                "\ta.sourcename,\n" +
                "\ta.sourceversion,\n" +
                "\ta.sourceuri,\n" +
                "\ta.reference_id,\n" +
                "\ta.status,\n" +
                "\ta.timeexecuted\n" +
                "from analysis a \n" +
                "where analysis_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("analysisId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
