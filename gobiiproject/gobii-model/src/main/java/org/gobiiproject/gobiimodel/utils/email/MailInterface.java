package org.gobiiproject.gobiimodel.utils.email;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.security.Decrypter;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart; 

public class MailInterface {
	
	private String host;
	private String port;
	private String user;
	private String password;
	private String protocol;
	private MailMessage message = new MailMessage();
	
	
	public MailInterface(ConfigSettings config){
		host = config.getEmailSvrDomain();
		port = config.getEmailServerPort().toString();
		user = config.getEmailSvrUser();
		password = config.getEmailSvrPassword();
		protocol = config.getEmailSvrType().toLowerCase();
	}
	
	public String getHost(){
		return host;
	}
	
	public String getPort(){
		return port;
	}
	
	public String getUser(){
		return user;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getProtocol(){
		return protocol;
	}
	
	public MailMessage newMessage(){
		return this.message;
	}
	
	public void send(MailMessage message) throws Exception{
		if(message.getUser()==null || message.getUser().equals(""))return;
		
		String username = this.getUser();
		String password = this.getPassword();
		String protocol = this.getProtocol();
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.starttls.required", "true");
		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.host", this.getHost());
		props.setProperty("mail.smtp.port", this.getPort());
		props.setProperty("mail.host", this.getHost());
		props.setProperty("mail.port", this.getPort());
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);
		


		Session mailSession = Session.getInstance(props,
				new javax.mail.Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication(){
						return new PasswordAuthentication(username, password);
					}
				});
		Transport transport = mailSession.getTransport(protocol);
		
		MimeMessage mimeMessage = new MimeMessage(mailSession);
		mimeMessage.setFrom(new InternetAddress(username));
		mimeMessage.setSubject(message.getSubject());
		
		MimeMultipart multipart = new MimeMultipart("related");
		BodyPart messageBodyPart = new MimeBodyPart();
		String htmlContent = message.getHeader() + message.getBody() + message.getFooter();
		messageBodyPart.setContent(htmlContent, "text/html");
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		DataSource fds = new URLDataSource(message.getImg());
		messageBodyPart.setDataHandler(new DataHandler(fds));
		messageBodyPart.setHeader("Content-ID", "<image>");
		multipart.addBodyPart(messageBodyPart);
		mimeMessage.setContent(multipart);
		mimeMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(message.getUser()));
		ErrorLogger.logDebug("Mail Interface","Recipient.TO => "+message.getUser());
		transport.connect(username, password);
		transport.sendMessage(mimeMessage,
				mimeMessage.getRecipients(Message.RecipientType.TO));
		ErrorLogger.logDebug("Mail Interface","Sending To => "+mimeMessage.getRecipients(Message.RecipientType.TO));
		transport.close();
		ErrorLogger.logInfo("Mail Interface","Email sent");
	}
	
}