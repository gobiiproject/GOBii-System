// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiprocess;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Phil on 3/24/2016.
 */
public class SpringContextLoader implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public SpringContextLoader() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/application-config.xml");
    } // ctor

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


}
