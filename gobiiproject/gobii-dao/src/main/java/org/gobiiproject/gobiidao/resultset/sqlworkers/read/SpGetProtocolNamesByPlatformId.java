package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/11/2016.
 */
public class SpGetProtocolNamesByPlatformId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProtocolNamesByPlatformId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select name,protocol_id from protocol where platform_id= ?;";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer protocolId = (Integer) parameters.get("platformId");

        preparedStatement.setInt(1, protocolId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
