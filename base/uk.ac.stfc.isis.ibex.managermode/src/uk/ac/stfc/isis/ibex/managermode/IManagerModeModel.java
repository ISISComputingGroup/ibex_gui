package uk.ac.stfc.isis.ibex.managermode;

import java.io.IOException;

import javax.security.auth.login.FailedLoginException;

public interface IManagerModeModel {
	
	public void authenticate(String password) throws FailedLoginException;
	
	public void deauthenticate();
	
	public boolean isAuthenticated() throws IOException;
	
}