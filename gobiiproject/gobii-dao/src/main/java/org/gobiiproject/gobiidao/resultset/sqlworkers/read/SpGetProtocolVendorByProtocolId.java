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
public class SpGetProtocolVendorByProtocolId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProtocolVendorByProtocolId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select vendor_protocol_id,name from vendor_protocol where protocol_id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer protocolId = (Integer) parameters.get("protocolId");

        preparedStatement.setInt(1, protocolId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
