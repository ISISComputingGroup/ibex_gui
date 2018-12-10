package uk.ac.stfc.isis.ibex.managermode;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.security.auth.login.FailedLoginException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * A client-side authentication model, providing temporary authentication
 * for certain client-side actions (such as saving layouts).
 */
public class ClientManagerModeModel implements IManagerModeModel {

	private boolean isAuthenticated = false;
	private final PasswordHasher passwordHasher;
	
	public ClientManagerModeModel() {
		passwordHasher = new PasswordHasher();
	}
	
	@Override
	public void authenticate(String password) throws FailedLoginException {
		boolean isValid = false;
		try {
			isValid = passwordHasher.isCorrectPassword(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
			throw new FailedLoginException("Internal IBEX error - please see the log.");
		}
		
		if (isValid) {
			this.isAuthenticated = true;
		} else {
			throw new FailedLoginException("The provided password was incorrect.");
		}
	}

	@Override
	public void deauthenticate() {
		this.isAuthenticated = false;
	}

	@Override
	public boolean isAuthenticated() throws IOException {
		return this.isAuthenticated;
	}

}
