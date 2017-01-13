package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetContactsByRoleName implements Work {

    private Map<String,Object> parameters = null;
    public SpGetContactsByRoleName(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String sql = "SELECT c.contact_id,\n" +
                "\t\t\t\t\tc.lastname,\n" +
                "\t\t\t\t\tc.firstname,\n" +
                "\t\t\t\t\tc.code,\n" +
                "\t\t\t\t\tc.email,\n" +
                "\t\t\t\t\tc.organization_id,\n" +
                "\t\t\t\t\tr.role_id,\n" +
                "\t\t\t\t\tr.role_name,\n" +
                "\t\t\t\t\tr.role_code\n" +
                "\t\t\tfrom contact c\n" +
                "\t\t\tjoin role r on (r.role_id=ANY(c.roles))\n" +
                "\t\t\twhere r.role_name= ? order by lastname";
        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setString(1, (String) parameters.get("roleName"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
