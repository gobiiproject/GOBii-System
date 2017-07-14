/**
 * 
 */
package org.gobiiproject.gobiiprocess;

import java.util.Hashtable;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author smr337
 *
 */
public class UserAuthentication {
	
	private String domain;
	private String ldap;
	private String searchBase;
	private String bindDN;
	private String bindPass;
	private String port;
	private String searchFilter;
	private LdapContext ctx;
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * @return the ldap
	 */
	public String getLdap() {
		return ldap;
	}
	/**
	 * @param ldap the ldap to set
	 */
	public void setLdap(String ldap) {
		this.ldap = ldap;
	}
	/**
	 * @return the searchBase
	 */
	public String getSearchBase() {
		return searchBase;
	}
	/**
	 * @param searchBase the searchBase to set
	 */
	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}
	/**
	 * @return the bindDN
	 */
	public String getBindDN() {
		return bindDN;
	}
	/**
	 * @param bindDN the bindDN to set
	 */
	public void setBindDN(String bindDN) {
		this.bindDN = bindDN;
	}
	/**
	 * @return the bindPass
	 */
	public String getBindPass() {
		return bindPass;
	}
	/**
	 * @param bindPass the bindPass to set
	 */
	public void setBindPass(String bindPass) {
		this.bindPass = bindPass;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the searchFilter
	 */
	public String getSearchFilter() {
		return searchFilter;
	}
	/**
	 * @param searchFilter the searchFilter to set
	 */
	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}
	
	/**
	 * Authenticate the user against the provided 
	 * 
	 * @param user
	 * @param pass
	 * @return true if user/pwd combination is correct. Otherwise false.
	 * 
	 */
	public boolean authenticate(String user, String pass){

		boolean validUser = true;
		
		Hashtable<String, String> env = new Hashtable<String, String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, ldap);
	     
	    ctx = null;
	    try{
	    	ctx = new InitialLdapContext(env, null);
	    	env.put(Context.SECURITY_PRINCIPAL,user);
	    	env.put(Context.SECURITY_CREDENTIALS, pass);
	    	//binding user would verify with the host
	    	ctx = new InitialLdapContext(env,null);
	    	
	    }catch (NamingException e){
	    	System.err.println("Authentication Error:- "+e.getMessage());
	    	validUser = false;
	    }
		
		return validUser;
	}
	
	public static void main(String[] args) {
		
		final String HOST = "host";
		final String PORT = "port";
		final String DN = "DN";
		final String BIND_PASS = "bindPass";
		final String SEARCH_BASE = "searchBase";
		final String USER = "user";
		final String PASSWORD = "password";
		
		String user=null,password=null;
		
		UserAuthentication auth = new UserAuthentication();
		
		
		Options options = new Options();
		Option host = new Option("h", HOST, true, "ldap URL of the host.Must be in form of ldap://myldap.host.");
		host.setRequired(true);
		options.addOption(host);
		
		Option port = new Option("p", PORT, true, "port for the ldap URL.");
		port.setRequired(true);
		options.addOption(port);
		
		Option searchBase = new Option("b", SEARCH_BASE, true, "User's distinguished name." );
		searchBase.setRequired(true);
		options.addOption(searchBase);
		
		Option authUser = new Option("u", USER, true, "User to authenticate." );
		authUser.setRequired(true);
		options.addOption(authUser);

		options
				.addOption("D", DN, true, "Bind User's distinguished name. Optional.") 
				.addOption("P", BIND_PASS, true, "Bind User's password. Optional.") 				
				.addOption("c", PASSWORD, true, "Users's password. Would be prompted for password in absence of this switch." );
				
		
		CommandLineParser parser = new DefaultParser();
        
		try{
			CommandLine cmd = parser.parse( options, args );
            if(cmd.hasOption(HOST))  auth.setLdap( cmd.getOptionValue(HOST) );
            if(cmd.hasOption(PORT))  auth.setPort( cmd.getOptionValue(PORT) );
            if(cmd.hasOption(DN))  auth.setBindDN(cmd.getOptionValue(DN) );
            if(cmd.hasOption(BIND_PASS))  auth.setBindPass( cmd.getOptionValue(BIND_PASS) );
            if(cmd.hasOption(SEARCH_BASE))  auth.setSearchBase( cmd.getOptionValue(SEARCH_BASE) );
            if (cmd.hasOption(USER))  user = cmd.getOptionValue(USER)  ;
            if (cmd.hasOption(PASSWORD))  password =  cmd.getOptionValue(PASSWORD) ; 
            else{ 
            	
            	password = new String(System.console().readPassword("Password:-"));
            }

            System.out.println("Host:- "+auth.getLdap());
            System.out.println("port:- "+auth.getPort());         
            System.out.println("searchBase:- "+auth.getSearchBase());
            System.out.println("User:-"+user);
            
            System.out.println( "User/pwd combination is :-" + auth.authenticate(user, password) );            
            
                
		}catch(org.apache.commons.cli.ParseException exp ) {
			new HelpFormatter().printHelp("java -cp <gobiiconfig.jar> org.gobiiproject.gobiiprocess.UserAuthentication",options);
            System.exit(1);
		}

		
	    
	}
}
