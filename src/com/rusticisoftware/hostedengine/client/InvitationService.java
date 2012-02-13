/* Software License Agreement (BSD License)
 * 
 * Copyright (c) 2010-2011, Rustici Software, LLC
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Rustici Software, LLC BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.rusticisoftware.hostedengine.client;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.rusticisoftware.hostedengine.client.datatypes.InvitationInfo;
import com.rusticisoftware.hostedengine.client.datatypes.MailMessageTemplate;
import com.rusticisoftware.hostedengine.client.datatypes.RegistrationResultsPostback;
import com.rusticisoftware.hostedengine.client.datatypes.RegistrationSummary;
import com.rusticisoftware.hostedengine.client.datatypes.UserInvitationStatus;

public class InvitationService
{
    private Configuration configuration = null;
    private Gson gson = new Gson();
    
    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public InvitationService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
    }
    
	public String createInvitation(String courseId, 
		boolean publicInvitation, Integer registrationCap, String addressess,MailMessageTemplate messageTemplate,
		RegistrationResultsPostback resultsPostback, Map<String, String> extendedParameters) throws Exception  {
		
        ServiceRequest request = new ServiceRequest(configuration);
        request.setUsePost(true);
        
        request.getParameters().add("public", publicInvitation);
        request.getParameters().add("courseid", courseId);
        if (registrationCap != null && registrationCap > 0) {
        	request.getParameters().add("registrationCap", registrationCap);
        }
        

        if (resultsPostback != null) {
            request.getParameters().add("resultsPostback", gson.toJson(resultsPostback));
        }
       
        request.getParameters().add("addressess", addressess);
        if (messageTemplate != null) {
        	request.getParameters().add("messageTemplate", gson.toJson(messageTemplate));
        }
        
        if (extendedParameters != null) {
        	for (Entry<String, String> entry : extendedParameters.entrySet()) {
        		request.getParameters().add(entry.getKey(), entry.getValue());
        	}
        }
        return Utils.getNonXmlPayloadFromResponse(request.callService("rustici.invitation.createInvitation"));
	}
	
	public InvitationInfo getInvitationInfo(String invitationId, boolean includeRegistrationSummary) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("invitationId", invitationId);
        request.getParameters().add("detail", includeRegistrationSummary);

        Document response = request.callService("rustici.invitation.getInvitationInfo");
        return parseInvitationInfoElement((Element)response.getElementsByTagName("invitationInfo").item(0));
	}
	
	private InvitationInfo parseInvitationInfoElement(Element invitationInfo) {
		InvitationInfo result = new InvitationInfo();
		result.set_allowLaunch(Boolean.parseBoolean(invitationInfo.getElementsByTagName("allowLaunch").item(0).getTextContent()));
		result.set_allowNewRegistrations(Boolean.parseBoolean(invitationInfo.getElementsByTagName("allowNewRegistrations").item(0).getTextContent()));
		result.set_created(Boolean.parseBoolean(invitationInfo.getElementsByTagName("created").item(0).getTextContent()));
		result.set_message(invitationInfo.getElementsByTagName("body").item(0).getTextContent());
		result.set_id(invitationInfo.getElementsByTagName("id").item(0).getTextContent());
		result.set_Public(Boolean.parseBoolean(invitationInfo.getElementsByTagName("public").item(0).getTextContent()));
		result.set_subject(invitationInfo.getElementsByTagName("subject").item(0).getTextContent());
		result.set_url(invitationInfo.getElementsByTagName("url").item(0).getTextContent());
		result.set_userInvitations(parseUserInvitations((Element)invitationInfo.getElementsByTagName("userInvitations").item(0)));
		
		return result;
	}
	
	private UserInvitationStatus[] parseUserInvitations(Element userInvitations) {
		if (userInvitations == null) {
			return new UserInvitationStatus[0];
		}
		NodeList nl = userInvitations.getElementsByTagName("userInvitation");
		UserInvitationStatus[] userInvitationStatuses = new UserInvitationStatus[nl.getLength()];
		for (int ii = 0; ii < nl.getLength(); ii++) {
			Element userInvitation = (Element) nl.item(ii);
			userInvitationStatuses[ii] = new UserInvitationStatus();
			userInvitationStatuses[ii].set_email(userInvitation.getElementsByTagName("email").item(0).getTextContent());
			userInvitationStatuses[ii].set_isStarted(Boolean.parseBoolean(userInvitation.getElementsByTagName("isStarted").item(0).getTextContent()));
			userInvitationStatuses[ii].set_registrationId(userInvitation.getElementsByTagName("registrationId").item(0).getTextContent());
			userInvitationStatuses[ii].set_url(userInvitation.getElementsByTagName("url").item(0).getTextContent());
			Element regReport = (Element) userInvitation.getElementsByTagName("registrationreport").item(0);
			if (regReport != null) {
				userInvitationStatuses[ii].set_registrationSummary(new RegistrationSummary(regReport));
			}
		}
		return userInvitationStatuses; 
	}

    /// <summary>
    /// creates and sends an invitation. 
	/// NOTE: synchronous, blocks up to timeoutSeconds waiting for invitation to be created, and then the time it takes to send invitations.
    /// </summary>
  	public String createAndSendInvitation(String courseId, 
			boolean publicInvitation, Integer registrationCap, String addressess,MailMessageTemplate messageTemplate,
			RegistrationResultsPostback resultsPostback, Map<String, String> extendedParameters,
			SMTPHelper mailer, int timeoutSeconds)  throws Exception{

  			Calendar endBy = Calendar.getInstance();
  			endBy.add(Calendar.SECOND, timeoutSeconds);
  			int trys = 1;
  			System.out.println("initial timeout ms: " + Calendar.getInstance().compareTo(endBy));
  			// first, create the invitation
			String invitationId = createInvitation(courseId, publicInvitation, registrationCap, addressess, messageTemplate,
					resultsPostback, extendedParameters);
			InvitationInfo invitationInfo = null;
			
			// wait for invitation (and associated registrations if applicable) to be created.
			while ((invitationInfo == null || !invitationInfo.is_created()) && Calendar.getInstance().compareTo(endBy) < 0) {
				int sleepMs = (int) Math.min(trys++ * 2000, endBy.getTime().getTime() - new Date().getTime());
				if (sleepMs > 0) {
					Thread.sleep(sleepMs);
				}
				
				invitationInfo = getInvitationInfo(invitationId, false);
				if (invitationInfo != null) {
					validateInvitationInfo(invitationInfo);
				}
			}
			
			if (invitationInfo == null || !invitationInfo.is_created()) {
				throw new TimeoutException();
			}
		
		sendInvitation(invitationInfo, messageTemplate.getFromAddress(), mailer);
		
		return invitationId;
	}

  	public void sendInvitation(String invitationId, String from, SMTPHelper mailer) throws Exception {
  		InvitationInfo info = getInvitationInfo(invitationId, false);
  		validateInvitationInfo(info);
  		sendInvitation(info, from, mailer);
  	}
  	
  	private void validateInvitationInfo(InvitationInfo info) throws Exception {
		if (info.get_errors() != null && info.get_errors().length > 0) {
			StringBuilder errorString = new StringBuilder();
			errorString.append("There were errors creating the invitation: \n");
			for (String error : info.get_errors()) {
				errorString.append(error + "\n");
			}
			throw new Exception(errorString.toString());
		}
  	}
  	
  	private void sendInvitation(InvitationInfo invitationInfo, String from, SMTPHelper mailer) throws Exception {
		validateInvitationInfo(invitationInfo);
		
		MailMessageTemplate invitationMessage = new MailMessageTemplate();
		invitationMessage.setBody(invitationInfo.get_message());
		invitationMessage.setFromAddress(from);
		invitationMessage.setSubject(invitationInfo.get_subject());
		
		if (invitationInfo.is_Public()){
			throw new Exception("Not implemented");
		}
		else {
			// send invitation mails now that invitation (& registrations) have been created.
			for (UserInvitationStatus userInvitation : invitationInfo.get_userInvitations()) {
				InternetAddress toAddress = new InternetAddress(userInvitation.get_email());
				mailer.Send(buildMessage(mailer, invitationMessage, toAddress, userInvitation.get_url()));
			}
			
			if (invitationInfo.get_userInvitations().length == 0) {
				throw new Exception("Nothing to send!");
			}
		}
  	}
  	
  	
  	private String replaceTokens(String source, InternetAddress userAddress, String url) {
  		String result;
  		if (source == null || source.equals("")) {
  			return "";
  		}
  		String name;
  		name = userAddress.getPersonal();
  		if (name == null || name.equals("")) {
  			name = userAddress.getAddress();
  		}
  		
  		result = source.replace("[USER]", name);
  		result = result.replace("[URL]",url);
  		result = result.replaceAll("\\[URL:(.*)\\]", "<a href='" + url + "'>$1</a>");
  		
  		return result;
   	}
  	
  	// NOTE: name portion of toAddress is used for [USER] replacement, so the caller should make sure it is set
  	private MimeMessage buildMessage(SMTPHelper mailer, MailMessageTemplate template, InternetAddress toAddress, String url) throws MessagingException {
  		MimeMessage msg = new MimeMessage(mailer.getSession());
  		msg.setFrom(new InternetAddress(template.getFromAddress()));
  		msg.addRecipient(RecipientType.TO, toAddress);
  		msg.setContent(replaceTokens(template.getBody(), toAddress, url), "text/html");
  		msg.setSubject(replaceTokens(template.getSubject(), toAddress, url));
  		return msg;
  	}
}