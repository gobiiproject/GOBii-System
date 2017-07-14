package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class SpGetProtocolDetailsByProtocolId implements Work {

    private Map<String, Object> parameters = null;
    public SpGetProtocolDetailsByProtocolId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet(){ return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *" +
                " from protocol" +
                " where protocol_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("protocolId"));
        resultSet = preparedStatement.executeQuery();
    }

}
