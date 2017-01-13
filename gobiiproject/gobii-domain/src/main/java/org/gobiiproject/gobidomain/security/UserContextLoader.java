// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.security;


import org.gobiiproject.gobiidao.entity.access.UserDao;
import org.gobiiproject.gobiimodel.entity.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class UserContextLoader implements ApplicationContextAware {

    private ApplicationContext applicationContext;



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //We need this in case we use this class in a context that was not an autowired
    //bean thingy (like whenyou you use @ContextConfiguraiton in unit testing)
    private void verifyContext() {
        if (null == applicationContext) {
            applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/application-config.xml");
        }
    }//verifyContext()

    //Test comment
    public void loadUser(String userName) {

        verifyContext();

//        InMemoryUserDetailsManager userDetailsService = applicationContext.getBean(InMemoryUserDetailsManager.class);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        UserDao userDao =((UserDao) applicationContext.getBean(UserDao.class));
        User user = userDao.getByLogin(userName);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for( String currentRoleName : user.getRoles())
        {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(currentRoleName);
            grantedAuthorities.add(grantedAuthority);
        }

        Authentication authToken = new UsernamePasswordAuthenticationToken(user.getLogin(), user.getLogin(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }//loadUser

//    public ResourceService getResourceService(String userName) {
//        verifyContext();
//        loadUser(userName);
//        return ((ResourceService) applicationContext.getBean("resourceService"));
//    }
//
//    public ResourceDTO getResourceDTO(String userName) {
//        verifyContext();
//        loadUser(userName);
//        return ((ResourceDTO) applicationContext.getBean("resourceDTO"));
//    }

}// UserContextLoader
