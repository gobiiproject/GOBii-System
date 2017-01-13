package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetCvTerms implements Work {
    /**
     * Created by Angel on 4/26/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvTerms() {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select cv_id, term from cv order by lower(term)";
        
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
