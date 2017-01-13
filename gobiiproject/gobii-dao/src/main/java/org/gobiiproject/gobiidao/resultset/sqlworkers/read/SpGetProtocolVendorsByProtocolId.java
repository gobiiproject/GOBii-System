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
public class SpGetProtocolVendorsByProtocolId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProtocolVendorsByProtocolId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select o.* ,vp.*\n" +
                "from organization o\n" +
                "join vendor_protocol vp on (o.organization_id=vp.vendor_id)\n" +
                "where vp.protocol_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer protocolId = (Integer) parameters.get("protocolId");

        preparedStatement.setInt(1, protocolId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
