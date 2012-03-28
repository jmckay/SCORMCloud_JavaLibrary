package com.rusticisoftware.hostedengine.client.datatypes;

public class InvitationInfo {
	private String _id;
	private String[] _errors;
	private boolean _public;
	private boolean _allowNewRegistrations;
	private boolean _allowLaunch;
	private UserInvitationStatus[] _userInvitations;
	private String _url;
	private boolean _created;
	private String _message;
	private String _subject;

	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_id() {
		return _id;
	}
	public void set_errors(String[] _errors) {
		this._errors = _errors;
	}
	public String[] get_errors() {
		return _errors;
	}
	public void set_Public(boolean _isPublic) {
		this._public = _isPublic;
	}
	public boolean is_Public() {
		return _public;
	}
	public void set_allowNewRegistrations(boolean _allowNewRegistrations) {
		this._allowNewRegistrations = _allowNewRegistrations;
	}
	public boolean is_allowNewRegistrations() {
		return _allowNewRegistrations;
	}
	public void set_allowLaunch(boolean _allowLaunch) {
		this._allowLaunch = _allowLaunch;
	}
	public boolean is_allowLaunch() {
		return _allowLaunch;
	}
	public void set_userInvitations(UserInvitationStatus[] _userInvitations) {
		this._userInvitations = _userInvitations;
	}
	public UserInvitationStatus[] get_userInvitations() {
		return _userInvitations;
	}
	public void set_url(String _url) {
		this._url = _url;
	}
	public String get_url() {
		return _url;
	}
	public void set_created(boolean _created) {
		this._created = _created;
	}
	public boolean is_created() {
		return _created;
	}
	public void set_message(String _message) {
		this._message = _message;
	}
	public String get_message() {
		return _message;
	}
	public void set_subject(String _subject) {
		this._subject = _subject;
	}
	public String get_subject() {
		return _subject;
	}
}
