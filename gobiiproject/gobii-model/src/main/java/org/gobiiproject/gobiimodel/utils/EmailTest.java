package org.gobiiproject.gobiimodel.utils;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailTest {
	 private static void sendEmail(String jobName, String fileLocation,boolean success,String errorLogLoc, String host,String port, String emailAddress,String fromUser,String password,String username, String protocol) throws Exception{
		 	if(emailAddress==null || emailAddress.equals(""))return;
		     Properties props = new Properties();
		     props.setProperty("mail.smtp.auth", "true");
				props.setProperty("mail.smtp.starttls.enable", "true");
			//	props.setProperty("mail.smtp.starttls.required", "true");
		     props.setProperty("mail.transport.protocol", protocol);
		     props.setProperty("mail.smtp.host", host);
		     props.setProperty("mail.smtp.port", port);
		     props.setProperty("mail.host", host);
		     props.setProperty("mail.port", port);
		     props.setProperty("mail.user", username);
		     props.setProperty("mail.password", password);
		    // props.put("mail.smtp.auth", "true");
				
		     Session mailSession = Session.getInstance(props,
						  new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password);
							}
						  });
		     Transport transport = mailSession.getTransport(protocol);
		     MimeMessage message = new MimeMessage(mailSession);
		     message.setFrom(new InternetAddress(fromUser));
		     String subject=jobName+(success?" Complete":" Failed");
				message.setSubject(subject);
				String content=subject+"\n";
				if(success && fileLocation!=null)content+="Your file is available at "+fileLocation;
				if(!success && errorLogLoc!=null)content="An error log may be available at " + errorLogLoc;
		     message.setContent(content, "text/plain");
		     message.addRecipient(Message.RecipientType.TO,
		          new InternetAddress(emailAddress));
		     transport.connect(username,password);
		     transport.sendMessage(message,
		         message.getRecipients(Message.RecipientType.TO));
		     transport.close();
		     System.out.println("Success?");
	 }
}
