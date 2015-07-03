package com.notify.app.signups;

import android.content.Context;

import com.notify.app.R;

public class ASingleValidator extends AbstractValidator {

	/**
	 * The start of the range
	 */
	

	/**
	 * The end of the range
	 */
	String text="";

	/**
	 * The error Id from the string resource
	 */
	private static int mErrorMessage; // Your custom error message

	/**
	 * Default error message if none specified
	 */
	private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_confPass;

	public ASingleValidator(Context c, String text) {
		super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
		mErrorMessage = DEFAULT_ERROR_MESSAGE_RESOURCE;
		this.text = text;
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
		//return getContext().getString(mErrorMessage);
		return "Username cannot have spaces.";
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
			if (value.trim().split(" ").length == 1) {
//				Message.message(getContext(), value + "  " + Pass);
				return true;
			} else {
				return false;
			}
		} else
			return false;

	}
}