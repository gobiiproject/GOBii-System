package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetAnalysisNamesByTypeId implements Work {
    /**
     * Created by Angel on 4/26/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetAnalysisNamesByTypeId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select analysis_id, name from analysis where analysis.type_id= ?  order by lower(name) ";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        int typeId = (int) parameters.get("typeId");
        preparedStatement.setInt(1, typeId);
        
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
