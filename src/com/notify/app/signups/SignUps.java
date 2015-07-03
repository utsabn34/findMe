package com.notify.app.signups;

import com.notify.app.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.notify.app.adapters.Message;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.crouton.Configuration;
import com.notify.app.crouton.Crouton;
import com.notify.app.crouton.Style;
import com.notify.app.dialogs.FinalSignUp;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import eu.janmuller.android.simplecropimage.CropImage;

public class SignUps extends SherlockFragmentActivity implements
		OnClickListener, OnItemSelectedListener {
	Button signUps, gallery, camera;
	Context con;
	FragmentManager manager;
	public static Bitmap bitmap2 = null;
	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
	private File mFileTemp;
	String country = "", year = "", day = "", month = "";
	private static final Style INFINITE = new Style.Builder()
			.setBackgroundColorValue(Style.holoBlueLight).build();
	private static final Configuration CONFIGURATION_INFINITE = new Configuration.Builder()
			.setDuration(Configuration.DURATION_INFINITE).build();
	EditText et_uname, et_pass, et_cpass, et_email, et_fname, et_lname,
			et_city;
	Spinner spinCountry, spinDay, spinYear, spinMonth;

	ArrayList<String> yr = new ArrayList<String>();
	ArrayList<String> dy = new ArrayList<String>();
	ArrayList<String> mn = new ArrayList<String>();
	String[] countryy;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("yerrsign", year);
		outState.putString("monthsign", month);
		outState.putString("dayign", day);
		outState.putString("countrysign", country);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signups);
		if (savedInstanceState != null) {
			year = savedInstanceState.getString("yerrsign");
			month = savedInstanceState.getString("monthsign");
			day = savedInstanceState.getString("dayign");
			country = savedInstanceState.getString("countrysign");
		}
		con = this;
		manager = getSupportFragmentManager();
		signUps = (Button) findViewById(R.id.butSignups);
		signUps.setOnClickListener(this);
		et_uname = (EditText) findViewById(R.id.eTUsername);
		et_pass = (EditText) findViewById(R.id.eTPassword);
		et_cpass = (EditText) findViewById(R.id.eTCPassword);
		et_email = (EditText) findViewById(R.id.eTEmail);
		et_fname = (EditText) findViewById(R.id.eTFname);
		et_lname = (EditText) findViewById(R.id.eTLname);
		gallery = (Button) findViewById(R.id.btnGall);
		camera = (Button) findViewById(R.id.btnCamera);
		et_city = (EditText) findViewById(R.id.eTcity);
		spinCountry = (Spinner) findViewById(R.id.spnrCountry);
		spinYear = (Spinner) findViewById(R.id.spinYear);
		spinMonth = (Spinner) findViewById(R.id.spinMonth);
		spinDay = (Spinner) findViewById(R.id.spinDay);
		countryy = getResources().getStringArray(R.array.countries_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner_layout, countryy);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Date dt = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		yr.clear();
		int curYr = calendar.get(Calendar.YEAR);
		for (int i = 1910; i <= curYr; i++) {
			yr.add(i + "");
		}
		mn.clear();
		dy.clear();
		for (int i = 1; i <= 12; i++) {
			if (i > 9)
				mn.add(i + "");
			else
				mn.add("0" + i);
		}
		for (int i = 1; i <= 31; i++) {

			if (i > 9)
				dy.add(i + "");
			else
				dy.add("0" + i);
		}
		ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
				R.layout.spinner_layout_grey, yr);
		adapterYear
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this,
				R.layout.spinner_layout_grey, mn);
		adapterMonth
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(this,
				R.layout.spinner_layout_grey, dy);
		adapterDay
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinCountry.setAdapter(adapter);
		spinDay.setAdapter(adapterDay);
		spinMonth.setAdapter(adapterMonth);
		spinYear.setAdapter(adapterYear);

		spinCountry.setOnItemSelectedListener(this);
		spinYear.setOnItemSelectedListener(this);
		spinMonth.setOnItemSelectedListener(this);
		spinDay.setOnItemSelectedListener(this);
		gallery.setOnClickListener(this);
		camera.setOnClickListener(this);

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.butSignups) {

			// LogInfo infos = new LogInfo(12, "Sanjib", "Sanjib", "maharjan",
			// "password", "aaaaa@gmail.com", "2014-04-05", "2049-05-07",
			// "Nepal", "Nepal Patan", "2014-05-06");
			// FinalSignUp finSgn = new FinalSignUp();
			// finSgn.setValues(infos, bitmap2);
			// finSgn.show(manager, "confirm_signup");

			NotEmptyValidator va = new NotEmptyValidator(con);
			ASingleValidator vSingle = new ASingleValidator(con, et_uname
					.getText().toString().trim());
			RangeValidator range_validator = new RangeValidator(con, 3, 15);
			ConfirmPassValidator matchPass = new ConfirmPassValidator(con,
					et_pass.getText().toString().trim(), "");
			EmailValidator e_validator = new EmailValidator(con);

			Validate et_name = new Validate(et_uname);
			Validate vd_pass = new Validate(et_pass);
			Validate vd_cpass = new Validate(et_cpass);
			Validate vd_email = new Validate(et_email);
			Validate vd_fname = new Validate(et_fname);
			Validate vd_lname = new Validate(et_lname);

			et_name.addValidator(va);
			et_name.addValidator(vSingle);
			et_name.addValidator(range_validator);
			vd_pass.addValidator(va);
			vd_pass.addValidator(range_validator);
			vd_cpass.addValidator(matchPass);
			vd_email.addValidator(e_validator);
			vd_fname.addValidator(va);
			vd_fname.addValidator(range_validator);
			vd_lname.addValidator(va);
			vd_lname.addValidator(range_validator);

			Form mForm = new Form();
			mForm.addValidates(et_name);
			mForm.addValidates(vd_pass);
			mForm.addValidates(vd_cpass);
			mForm.addValidates(vd_email);
			mForm.addValidates(vd_fname);
			mForm.addValidates(vd_lname);

			// ...

			// Launch Validation
			if (mForm.validate()) {
				// Message.message(con, "not Epp");
				if (country.equals("Select Country")) {
					showCrouton("Select country", Style.ALERT,
							Configuration.DEFAULT);
				} else {
					InputMethodManager imm = (InputMethodManager) con
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(signUps.getWindowToken(), 0);

					Validate vd_city = new Validate(et_city);

					vd_city.addValidator(va);
					vd_city.addValidator(range_validator);
					Form mForm2 = new Form();
					mForm2.addValidates(vd_city);
					if (mForm2.validate()) {
						showCrouton("Valid Form", Style.CONFIRM,
								Configuration.DEFAULT);
						LogInfo info = new LogInfo(1 + "", et_uname.getText()
								.toString().trim(), et_fname.getText()
								.toString().trim(), et_lname.getText()
								.toString().trim(), et_pass.getText()
								.toString().trim(), et_email.getText()
								.toString().trim(), "", year + "-" + month
								+ "-" + day, country, et_city.getText()
								.toString().trim(), "");
						FinalSignUp finSgns = new FinalSignUp();
						finSgns.setValues(info, bitmap2);
						finSgns.show(manager, "confirm_signup");
					} else {
						showCrouton("Fill the city field", Style.ALERT,
								Configuration.DEFAULT);
					}
				}
			} else {
				// error statement like toast, crouton, ...

				showCrouton("Correct the form element", Style.ALERT,
						Configuration.DEFAULT);
			}

		} else if (v.getId() == R.id.btnGall) {
			openGallery();

		} else if (v.getId() == R.id.btnCamera) {
			takePicture();
		}
	}

	public void showCrouton(String croutonText, Style croutonStyle,
			Configuration configuration) {
		final boolean infinite = INFINITE == croutonStyle;

		if (infinite) {
			croutonText = getString(R.string.Infinite_crou);
		}

		final Crouton crouton;
		// if (displayOnTop.isChecked()) {
		crouton = Crouton.makeText(this, croutonText, croutonStyle);
		// } else {
		// crouton = Crouton.makeText(getActivity(), croutonText, croutonStyle,
		// R.id.alternate_view_group);
		// }
		if (infinite) {
			// infiniteCrouton = crouton;
		}
		crouton.setOnClickListener(this)
				.setConfiguration(
						infinite ? CONFIGURATION_INFINITE : configuration)
				.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (parent.getId() == R.id.spnrCountry) {
			country = countryy[position];
		} else if (parent.getId() == R.id.spinYear) {
			year = yr.get(position);
		} else if (parent.getId() == R.id.spinMonth) {
			month = mn.get(position);
		} else if (parent.getId() == R.id.spinDay) {
			day = dy.get(position);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			} else {
				/*
				 * The solution is taken from here:
				 * http://stackoverflow.com/questions
				 * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Message.message(this, "Activity not found");
		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 2);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK) {

			return;
		}

		Bitmap bitmap;

		switch (requestCode) {

		case REQUEST_CODE_GALLERY:

			try {

				InputStream inputStream = getContentResolver().openInputStream(
						data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(
						mFileTemp);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();

				startCropImage();

			} catch (Exception e) {
				Message.message(this, "eroor temp");

				// Log.e(TAG, "Error while creating temp file", e);
			}

			break;
		case REQUEST_CODE_TAKE_PICTURE:

			startCropImage();
			break;
		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {

				return;
			}

			bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
			// mImageView.setImageBitmap(bitmap);
			// bitmap2 = bitmap;

			// Resize the image
			// double width = bitmap.getWidth();
			// double height = bitmap.getHeight();
			// double ratio = 400 / width;
			// int newheight = (int) (ratio * height);

			bitmap2 = Bitmap.createScaledBitmap(bitmap, 90, 90, true);

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

}
