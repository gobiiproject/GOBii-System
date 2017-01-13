// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************

package org.gobiiproject.gobiidao.entity.access.impl.db;


import org.gobiiproject.gobiidao.entity.access.ContactEntityDao;
import org.gobiiproject.gobiidao.entity.core.impl.DaoImplHibernate;
import org.gobiiproject.gobiidao.entity.pojos.Contact;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public class ContactEntityDaoImplHibernate extends DaoImplHibernate<Contact> implements ContactEntityDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Contact> getContactsByRoleType(String roleType) {

//********************** BEGIN STUFF THAT WORKS *******************************************
        // The following works with the entity class exactly as it was created by hbm2java
//        String query = "SELECT c.contact_id ,c.lastname ,c.firstname ,c.code ,c.email, null as \"roles\", c.created_by,c.created_date, c.modified_by, c.modified_date from contact c";
//        List<Contact> contactsFromNativeQuery = em.createNativeQuery(query, Contact.class).getResultList();


        // Note: there is no mapping here -- all you get is an object with a dump of all the columns returned
//        String query = "SELECT c.contact_id as \"contact_id\",c.lastname as \"lastname\",c.firstname as \"firstname\",c.code as \"code\",c.email as \"email\",r.role_id as \"role_id\",r.role_name as \"role_name\",r.role_code as \"role_code\" from contact c join role r on (r.role_id=ANY(c.roles))";
//        List<Object[]> results = this.em.createNativeQuery(query).getResultList();

//********************** END STUFF THAT WORKS *******************************************


        Session session = (Session) em.getDelegate();

        session.doWork(new Work() {
            @Override
            public void execute(Connection dbConnection) throws SQLException {
                String getDBUSERCursorSql = "{call get_contacts_by_type(?,?)}";
                CallableStatement callableStatement = dbConnection.prepareCall(getDBUSERCursorSql);
                callableStatement.setString(1, "PI");
                callableStatement.registerOutParameter(2, Types.OTHER);


// execute getDBUSERCursor store procedure
                callableStatement.executeUpdate();

// get cursor and cast it to ResultSet
                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
                while(resultSet.next()) {
                    String lastName = resultSet.getString("lastname");
                    String roleName = resultSet.getString("role_name");
                    String foo = "";
                }
            }
        });
        String temp = "";


//********************** BEGIN STUFF THAT DOES NOT WORK  *******************************************
        // REF_CURSOR has to be the "first" parameter
        // The other parameters are after
        // the index is 1 based
        // But now the problem is that it says No dialect mapping for JDBC type 111
        // I believe this may have to do with the array type of the roles column
        // doesn't pass the stored procedure boundary even if you use the "null as roles" idiom
//        StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("get_contacts");
//        storedProcedureQuery.registerStoredProcedureParameter(1, Object.class, ParameterMode.REF_CURSOR);
//        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
//        storedProcedureQuery.setParameter(2, "foo");
//        List<Contact> contactsFromSp = (List<Contact>) storedProcedureQuery.getResultList();
//        List<Object> contactsFromSp =  storedProcedureQuery.getResultList();

        // Named queries qre not recognized. I am wondering whether the scan feature of the entity manager
        // configuration does not work. Maybe it only works if you use a persistence.xml?
        // List<Contact> contacts = (List<Contact>) this.em.createNamedStoredProcedureQuery("getContacts").getResultList();

        // The mapping causes errors, but the lone query works
//        String query = "SELECT c.contact_id ,c.lastname ,c.firstname ,c.email, c.code , r.role_id, r.role_name, r.role_code from contact c join role r on (r.role_id=ANY(c.roles))";
//        List<Object[]> results = this.em.createNativeQuery(query, "ContactRoleMapping").getResultList();

//********************** END STUFF THAT DOES NOT WORK  *******************************************

        //Next step: Move that select to a stored proc to see if _that_ will work


//        StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("get_contacts_by_type");
//// set parameters
//        storedProcedureQuery.registerStoredProcedureParameter("_role_name", String.class, ParameterMode.IN);
//        //storedProcedureQuery.registerStoredProcedureParameter("tax", REF_CURSOR, ParameterMode.OUT);
//        storedProcedureQuery.setParameter("_role_name", roleType);
// execute SP
//        storedProcedureQuery.execute();

//        List<Object[]> results = storedProcedureQuery.getResultList();

//        results.stream().forEach((record) -> {
//            Contact contact = (Contact)record[0];
//            Role role = (Role)record[1];
//            // do something useful
//        });

        return null;
    }
}
