package com.rusticisoftware.hostedengine.client;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class SMTPHelper {
	private Properties _props;
	
	public SMTPHelper(String host, int port, String user, String password, boolean secureConnection)
	{
		_props = new Properties();
		_props.put("mail.smtp.host", host);
        _props.put("mail.smtp.user", user);
        _props.put("mail.smtp.password", password);
        _props.put("mail.smtp.port", Integer.toString(port));
        _props.put("mail.smtp.starttls.enable", Boolean.toString(secureConnection));
        _props.put("mail.smtp.auth", "true");
	}
	
	public Session getSession() {
		return Session.getDefaultInstance(_props);
	}
	
	public void Send(MimeMessage msg) throws Exception {
        Session mailsession = getSession();

        Transport trans = mailsession.getTransport("smtp");
                
        trans.connect(_props.getProperty("mail.smtp.host"),_props.getProperty("mail.smtp.user"),_props.getProperty("mail.smtp.password"));
        try
        {
	        trans.sendMessage(msg, msg.getAllRecipients());
        }
        finally {
        	trans.close();
        }
	}	
}
