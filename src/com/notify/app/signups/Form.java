package com.notify.app.signups;


/**
 * Form Validation Class
 * <p/>
 * Immediately, utonly works with EditText
 * 
 * @author Utsab Neupane
 * @version 1.0
 */
public class Form {

	private List<AbstractValidate> mValidates = new ArrayList<AbstractValidate>();

	/**
	 * Function adding Validates to our form
	 * 
	 * @param validate
	 *            {@link AbstractValidate} Validate to add
	 */
	public void addValidates(AbstractValidate validate) {
		mValidates.add(validate);
	}

	/**
	 * Function removing Validates from our form
	 * 
	 * @param validate
	 *            {@link AbstractValidate} Validate to remove
	 * @return validate that was removed from the form
	 */
	public boolean removeValidates(AbstractValidate validate) {
		if (mValidates != null && !mValidates.isEmpty()) {
			return mValidates.remove(validate);
		}
		return false;
	}

	/**
	 * Called to validate our form. If an error is found, it will be displayed in the corresponding field.
	 * 
	 * @return boolean true if the form is valid, otherwise false
	 */
	public boolean validate() {
		boolean formValid = true;
		for (AbstractValidate validate : mValidates) {
			formValid = formValid & validate.isValid(); // Use & in order to evaluate both side of the operation.
		}
		return formValid;
	}

	/**
	 * Closes the error popup of the text view. A little useless due to ability to just call source.setError(null), but added anyways
	 * 
	 * @param sourceTextView
	 * @author Satish Gupta
	 */
	public void closeError(TextView sourceTextView) {
		for (AbstractValidate av : mValidates) {
			Validate v = (Validate) av;
			if (v.getSource().equals(sourceTextView))
				v.getSource().setError(null);
		}
	}

	/**
	 * Closes all error pop ups that were created by validator
	 * 
	 * @author Satish Gupta
	 */
	public void closeAllErrors() {
		for (AbstractValidate av : mValidates) {
			Validate v = (Validate) av;
			v.getSource().setError(null);
		}
	}
}
