package org.gobiiproject.gobiidao.resultset.core;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.jpa.internal.EntityManagerImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class DbMetaData {

    @PersistenceContext
    protected EntityManager em;

    public String getCurrentDbUrl() throws SQLException {

        String returnVal = "";
//        Shared EntityManager proxy for target factory [org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean@48fde1c2]


        HibernateEntityManagerFactory entityManagerFactory = (HibernateEntityManagerFactory) em.getEntityManagerFactory();
        SessionFactoryImpl sessionFactory = (SessionFactoryImpl) entityManagerFactory.getSessionFactory();
        DatasourceConnectionProviderImpl datasourceConnectionProviderImplj = (DatasourceConnectionProviderImpl) sessionFactory.getConnectionProvider();
        DataSource dataSource = datasourceConnectionProviderImplj.getDataSource();



        returnVal = dataSource.getConnection().getMetaData().getURL();

        return returnVal;
    }
}

