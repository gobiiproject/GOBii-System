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
public class SpGetDatasetDetailsByDataSetId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetDatasetDetailsByDataSetId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "\tds.dataset_id,\n" +
                "\tds.name,\n" +
                "\tds.experiment_id,\n" +
                "\tds.callinganalysis_id,\n" +
                "\tds.analyses,\n" +
                "\tds.data_table,\n" +
                "\tds.data_file,\n" +
                "\tds.quality_table,\n" +
                "\tds.quality_file,\n" +
//                "\tds.scores,\n" +
                "\tds.created_by,\n" +
                "\tds.created_date,\n" +
                "\tds.modified_by,\n" +
                "\tds.modified_date,\n" +
                "\tds.status, \n" +
                "\tds.type_id \n" +
                "from dataset ds \n" +
                "where dataset_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
