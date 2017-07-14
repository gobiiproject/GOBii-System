//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.14 at 11:32:33 AM EDT 
//


package edu.cornell.gobii.gdi.main;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.io.FileUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="crop" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="user">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="user_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="user_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="user_fullname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="user_email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="configDir" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="logFile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="server" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "crop",
    "user",
    "configDir",
    "logFile",
    "server"
})
@XmlRootElement(name = "App")
public class App {
	
	public static App INSTANCE = new App();

    @XmlElement(required = true)
    protected String crop;
    @XmlElement(required = true)
    protected App.User user;
    @XmlElement(required = true)
    protected String configDir;
    @XmlElement(required = true)
    protected String logFile;
    @XmlElement(required = true)
    protected String server;

    /**
     * Gets the value of the crop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrop() {
        return crop;
    }

    /**
     * Sets the value of the crop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrop(String value) {
        this.crop = value;
    }

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link App.User }
     *     
     */
    public App.User getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link App.User }
     *     
     */
    public void setUser(App.User value) {
        this.user = value;
    }

    /**
     * Gets the value of the configDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigDir() {
        return configDir;
    }

    /**
     * Sets the value of the configDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigDir(String value) {
        this.configDir = value;
    }

    /**
     * Gets the value of the logFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * Sets the value of the logFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogFile(String value) {
        this.logFile = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServer(String value) {
        this.server = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="user_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="user_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="user_fullname" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="user_email" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "userId",
        "userName",
        "userFullname",
        "userEmail"
    })
    public static class User {

        @XmlElement(name = "user_id")
        protected int userId;
        @XmlElement(name = "user_name", required = true)
        protected String userName;
        @XmlElement(name = "user_fullname", required = true)
        protected String userFullname;
        @XmlElement(name = "user_email", required = true)
        protected String userEmail;

        /**
         * Gets the value of the userId property.
         * 
         */
        public int getUserId() {
            return userId;
        }

        /**
         * Sets the value of the userId property.
         * 
         */
        public void setUserId(int value) {
            this.userId = value;
        }

        /**
         * Gets the value of the userName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUserName() {
            return userName;
        }

        /**
         * Sets the value of the userName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUserName(String value) {
            this.userName = value;
        }

        /**
         * Gets the value of the userFullname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUserFullname() {
            return userFullname;
        }

        /**
         * Sets the value of the userFullname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUserFullname(String value) {
            this.userFullname = value;
        }

        /**
         * Gets the value of the userEmail property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUserEmail() {
            return userEmail;
        }

        /**
         * Sets the value of the userEmail property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUserEmail(String value) {
            this.userEmail = value;
        }
        
        public boolean isValid(){
        	if(userName == null 	|| userName.isEmpty()) return false;
        	if(userFullname == null || userFullname.isEmpty()) return false;
        	if(userEmail == null 	|| userEmail.isEmpty()) return false;
        	return true;
        }
    }
    
    public App(){}
    
    public boolean isValid(){
    	if(user == null) return false;
    	if(!user.isValid()) 	return false;
    	if(crop == null			|| crop.isEmpty()) return false;
    	if(logFile == null		|| logFile.isEmpty()) return false;
    	if(configDir == null	|| configDir.isEmpty()) return false;
    	if(server == null) return false;
    	return true;
    }

    public void load(String xml){
    	File xmlFile = new File(xml);
    	if(!xmlFile.exists()) return;
    	try{
    		JAXBContext jc = JAXBContext.newInstance(App.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			INSTANCE = (App) unmarshaller.unmarshal(xmlFile);
    	}catch (JAXBException e) {
			e.printStackTrace();
		}
    }
    
    public void save(){
    	try {
    		StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(App.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(INSTANCE, writer);
			FileUtils.writeStringToFile(new File(configDir+"/App.xml"), writer.toString());
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
