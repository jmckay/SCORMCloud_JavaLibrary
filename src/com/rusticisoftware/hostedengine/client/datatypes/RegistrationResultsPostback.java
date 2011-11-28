package com.rusticisoftware.hostedengine.client.datatypes;

import com.rusticisoftware.hostedengine.client.datatypes.Enums.RegistrationResultsAuthType;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.RegistrationResultsFormat;

public class RegistrationResultsPostback {
	private String resultsPostbackUrl;
	private RegistrationResultsAuthType authType;
	private String postBackLoginName;
	private String postBackLoginPassword;
	private RegistrationResultsFormat resultsFormat;

	public void setResultsPostbackUrl(String resultsPostbackUrl) {
		this.resultsPostbackUrl = resultsPostbackUrl;
	}
	public String getResultsPostbackUrl() {
		return resultsPostbackUrl;
	}
	public void setAuthType(RegistrationResultsAuthType authType) {
		this.authType = authType;
	}
	public RegistrationResultsAuthType getAuthType() {
		return authType;
	}
	public void setPostBackLoginName(String postBackLoginName) {
		this.postBackLoginName = postBackLoginName;
	}
	public String getPostBackLoginName() {
		return postBackLoginName;
	}
	public void setPostBackLoginPassword(String postBackLoginPassword) {
		this.postBackLoginPassword = postBackLoginPassword;
	}
	public String getPostBackLoginPassword() {
		return postBackLoginPassword;
	}
	public void setResultsFormat(RegistrationResultsFormat resultsFormat) {
		this.resultsFormat = resultsFormat;
	}
	public RegistrationResultsFormat getResultsFormat() {
		return resultsFormat;
	}
}
