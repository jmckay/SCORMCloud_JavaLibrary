package com.rusticisoftware.hostedengine.client.datatypes;

public class MailMessageTemplate {
	private String subject;
	private String fromAddress;
	private String body;

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBody() {
		return body;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}
}
