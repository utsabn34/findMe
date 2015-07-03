package com.notify.app.signups;
import com.notify.app.R;
import android.content.Context;
import android.text.TextUtils;
public class NotEmptyValidator extends AbstractValidator {

    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_empty;

    public NotEmptyValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public NotEmptyValidator(Context c, int errorMessage) {
        super(c, errorMessage);
    }

    
	@Override
    public boolean isValid(String text) {
        return !TextUtils.isEmpty(text);
    }
}
