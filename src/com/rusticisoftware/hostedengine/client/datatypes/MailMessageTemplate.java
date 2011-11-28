package com.rusticisoftware.hostedengine.client.datatypes;

public class MailMessageTemplate {
	private String subject;
	private String fromAddress;
	private String bodyHTML;
	private String bodyText;

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setBodyHTML(String bodyHTML) {
		this.bodyHTML = bodyHTML;
	}
	public String getBodyHTML() {
		return bodyHTML;
	}
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	public String getBodyText() {
		return bodyText;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubject() {
		return subject;
	}
}
