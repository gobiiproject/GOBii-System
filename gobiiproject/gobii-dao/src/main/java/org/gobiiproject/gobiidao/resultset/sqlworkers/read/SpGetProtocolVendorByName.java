package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
/**
 * Created by Phil on 12/26/2016.
 */

/**
 * Created by Phil on 4/11/2016.
 */
public class SpGetProtocolVendorByName implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProtocolVendorByName(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select * \n" +
                "from organization org \n " +
                "join vendor_protocol vp on (org.organization_id=vp.vendor_id) \n" +
                "where lower(vp.name) = lower(?) ";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String vendorProtocolName = (String) parameters.get("vendorProtocolName");

        preparedStatement.setString(1, vendorProtocolName);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}

