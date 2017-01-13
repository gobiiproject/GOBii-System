package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.gobiiproject.gobiidao.resultset.core.DbMetaData;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetDbConnectionMetaData implements Work {

    @Autowired
    DbMetaData dbMetaData;


    private Map<String, Object> parameters = null;

    public SpGetDbConnectionMetaData(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    private String getDbUrl(Connection connection) throws SQLException {

        return connection
                .getMetaData()
                .getURL();
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        parameters.put("url", getDbUrl(dbConnection));


    } // execute()
}
