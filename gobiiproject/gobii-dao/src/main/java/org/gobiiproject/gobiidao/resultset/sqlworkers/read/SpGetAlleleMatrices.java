package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by VCalaminos on 6/14/2017.
 */
public class SpGetAlleleMatrices implements Work {

    public SpGetAlleleMatrices() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }


    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "e.project_id,\n" +
                "e.name || d.name as name,\n" +
                "d.dataset_id as matrixdbid,\n" +
                "e.experiment_id,\n" +
                "e.name as experiment_name,\n" +
                "d.name as dataset_name,\n" +
                "d.callinganalysis_id,\n" +
                "e.code,\n" +
                "e.data_file\n"+
                "from\n" +
                "experiment e\n" +
                "join\n" +
                "dataset d\n" +
                "on\n" +
                "d.experiment_id = e.experiment_id\n" +
                "order by e.experiment_id, e.project_id";


        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

    }

}
