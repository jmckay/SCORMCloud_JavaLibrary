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
    
	public String createInvitation(String courseId, boolean publicInvitation, boolean send, 
			String addresses, String emailSubject, String emailBody, String creatingUserEmail, Integer registrationCap,
			String postbackUrl, String authType, String urlName, String urlPass, String resultsFormat, 
			boolean async, Map<String, String> extendedParameters) throws Exception  {
		
        ServiceRequest request = new ServiceRequest(configuration);
        request.setUsePost(true);
        
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("public", publicInvitation);
        
        request.getParameters().add("send", send);
        
        if (registrationCap != null && registrationCap > 0) {
        	request.getParameters().add("registrationCap", registrationCap);
        }
        if (addresses != null) {
            request.getParameters().add("addresses", addresses);
        }
        if (emailSubject != null) {
            request.getParameters().add("emailSubject", emailSubject);
        }
        if (emailBody != null) {
            request.getParameters().add("emailBody", emailBody);
        }
        if (creatingUserEmail != null) {
            request.getParameters().add("creatingUserEmail", creatingUserEmail);
        }
        
        if (postbackUrl != null) {
            request.getParameters().add("postbackUrl", postbackUrl);
        }
        if (authType != null) {
            request.getParameters().add("authType", authType);
        }
        if (urlName != null) {
            request.getParameters().add("urlName", urlName);
        }
        if (urlPass != null) {
            request.getParameters().add("urlPass", urlPass);
        }
        if (resultsFormat != null) {
            request.getParameters().add("resultsFormat", resultsFormat);
        }
        
        if (extendedParameters != null) {
        	for (Entry<String, String> entry : extendedParameters.entrySet()) {
        		request.getParameters().add(entry.getKey(), entry.getValue());
        	}
        }
        if (async){
        	return Utils.getNonXmlPayloadFromResponse(request.callService("rustici.invitation.createInvitationAsync"));
        } else {
        	return Utils.getNonXmlPayloadFromResponse(request.callService("rustici.invitation.createInvitation"));
        }
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

    
  	
  	
  	
  	
}