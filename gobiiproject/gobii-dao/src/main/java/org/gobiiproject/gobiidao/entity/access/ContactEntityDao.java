// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entity.access;

import org.gobiiproject.gobiidao.entity.core.EntityDao;
import org.gobiiproject.gobiidao.entity.pojos.Contact;

import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public interface ContactEntityDao extends EntityDao<Contact> {
    List<Contact> getContactsByRoleType(String roleType);
}
