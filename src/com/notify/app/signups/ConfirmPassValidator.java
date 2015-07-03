package com.notify.app.signups;
import com.notify.app.R;
import android.content.Context;

public class ConfirmPassValidator extends AbstractValidator {

	/**
	 * The start of the range
	 */
	final String Pass;

	/**
	 * The end of the range
	 */
	final String CPaass;

	/**
	 * The error Id from the string resource
	 */
	private static int mErrorMessage; // Your custom error message

	/**
	 * Default error message if none specified
	 */
	private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_confPass;

	public ConfirmPassValidator(Context c, String pass, String cpass) {
		super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
		mErrorMessage = DEFAULT_ERROR_MESSAGE_RESOURCE;
		this.Pass = pass;
		this.CPaass = cpass;
	}

	/**
	 * @param context
	 * @param start
	 *            of the range
	 * @param end
	 *            of the range
	 * @param errorMessageRes
	 */

	
	@Override
	public String getMessage() {
		return getContext().getString(mErrorMessage);
	}

	/**
	 * Checks is value is between given range
	 * 
	 * @return true if between range; false if outside of range
	 */
	
	@Override
	public boolean isValid(String value) {
		// Message.message(getContext(), value);
		if (value != null) {
			if (value.trim().equals(Pass.trim())) {
//				Message.message(getContext(), value + "  " + Pass);
				return true;
			} else {
				return false;
			}
		} else
			return false;

	}
}