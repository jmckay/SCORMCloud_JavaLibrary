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

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rusticisoftware.hostedengine.client.datatypes.MailMessageTemplate;
import com.rusticisoftware.hostedengine.client.datatypes.RegistrationResultsPostback;

import com.google.gson.Gson;

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
		boolean publicInvitation, String addressess,MailMessageTemplate messageTemplate,
		RegistrationResultsPostback resultsPostback, Map<String, String> extendedParameters) throws Exception {
		
        ServiceRequest request = new ServiceRequest(configuration);
        request.setUsePost(true);
        
        request.getParameters().add("public", publicInvitation);
        request.getParameters().add("courseid", courseId);
        

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
}