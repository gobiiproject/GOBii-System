package org.gobiiproject.gobiidao.entity.core.impl;

import org.gobiiproject.gobiidao.entity.core.EntityDao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Phil on 3/29/2016.
 * Based on: http://www.codeproject.com/Articles/251166/The-Generic-DAO-pattern-in-Java-with-Spring-3-and
 */
public abstract class DaoImplHibernate<T> implements EntityDao<T> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> type;

    public DaoImplHibernate() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

//    @Transactional
//    @Override
//    public long countAll(final Map<String, Object> params) {
//
//        final StringBuffer queryString = new StringBuffer(
//                "SELECT count(o) from ");
//
//        queryString.append(type.getSimpleName()).append(" o ");
//        queryString.append(this.getQueryClauses(params, null));
//
//
//        final Query query = this.em.createQuery(queryString.toString());
//
//        return (Long) query.getSingleResult();
//
//    }

    @Transactional
    @Override
    public T create(final T t) {
        this.em.persist(t);
        return t;
    }

    @Transactional
    @Override
    public void delete(final Object id) {
        this.em.remove(this.em.getReference(type, id));
    }

    @Transactional
    @Override
    public T find(final Object id) {
        return (T) this.em.find(type, id);
    }

    @Transactional
    @Override
    public T update(final T t) {
        return this.em.merge(t);
    }
}

