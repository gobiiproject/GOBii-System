package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsMapset extends SpDef {

    public SpInsMapset() {

        super("{call createmapset(?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("code", String.class).setNullable(false);
        this.addParamDef("description", String.class).setNullable(true);
        this.addParamDef("referenceId", Integer.class).setNullable(true);
        this.addParamDef("mapType", Integer.class).setNullable(false);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);

    } // ctor

//    public Integer projectId = null;
//    public Integer getEntityIdId() {
//        return projectId;
//    }


//    public void execute(Connection dbConnection) throws SQLException {
//
//        CallableStatement callableStatement = dbConnection.prepareCall("{call createproject(?,?,?,?,?,?,?,?,?)}");
//
//        callableStatement.setString(1,(String) parameters.get("projectName"));
//        callableStatement.setString(2,(String) parameters.get("projectCode"));
//        callableStatement.setString(3,(String) parameters.get("projectDescription"));
//        callableStatement.setInt(4,(Integer) parameters.get("piContact"));
//        callableStatement.setInt(5,(Integer) parameters.get("createdBy"));
//        callableStatement.setDate(6,(Date) parameters.get("createdDate"));
//        callableStatement.setInt(7,(Integer) parameters.get("modifiedby"));
//        callableStatement.setDate(8,(Date) parameters.get("modifiedDate"));
//        callableStatement.setInt(9,(Integer) parameters.get("projectStatus"));
//
//        callableStatement.registerOutParameter(10, Types.INTEGER);
//
//        projectId = callableStatement.executeUpdate();

//    } // execute()
}
