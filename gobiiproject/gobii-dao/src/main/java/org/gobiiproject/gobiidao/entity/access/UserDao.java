// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entity.access;

import org.gobiiproject.gobiimodel.entity.User;
import org.springframework.stereotype.Component;


/**
 * This is domain DAO to entityaccess users. Kinda fake here.
 */
@Component
public interface UserDao {

     User getByLogin(String login);
}
