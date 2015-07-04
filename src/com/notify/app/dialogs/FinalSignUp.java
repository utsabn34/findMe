package com.notify.app.dialogs;
import com.notify.app.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.notify.app.adapters.Message;
import com.notify.app.adapters.MyCustomAdapter;
import com.notify.app.adapters.Classes.LogInfo;
import com.notify.app.signups.InternalStorageContentProvider;
import com.notify.app.signups.SignUps;
import eu.janmuller.android.simplecropimage.CropImage;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class FinalSignUp extends DialogFragment implements OnClickListener {
	int id;
	Button ok, cancel;
	LogInfo info;
	Dialog dialog;
	Bitmap bitmap = null;
	FragmentManager manager;
	Context context;
	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
	private File mFileTemp;
	ListView lv;
	ImageView imagePhoto;
	ArrayList<String> information = new ArrayList<String>();

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		arg0.putParcelable("par_information", info);
		// arg0.putString("title", title);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			info = savedInstanceState.getParcelable("par_information");
		}
		dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.final_signup);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		context = getActivity();
		manager = getActivity().getSupportFragmentManager();
		// Message.message(context, "as");
		cancel = (Button) dialog.findViewById(R.id.btnCancelSUP);
//		gallery = (Button) dialog.findViewById(R.id.btnGall);
		imagePhoto = (ImageView) dialog.findViewById(R.id.imageVww);
//		camera = (Button) dialog.findViewById(R.id.btnCamera);
		lv = (ListView) dialog.findViewById(R.id.lVDetails);
		// String[] df = { "dfsfsdf", "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf",
		// "kjhgkjh", "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf", "kjhgkjh",
		// "sdfsdfsdf", "kjhgkjh", "sdfsdfsdf", "kjhgkjh", "my" };
		// ArrayAdapter<String> adap = new ArrayAdapter<String>(context,
		// R.layout.listitem_signup, R.id.tVLeft, df);
		setup();
		MyCustomAdapter adapterCustom = new MyCustomAdapter(context,
				information);
		lv.setAdapter(adapterCustom);
		ok = (Button) dialog.findViewById(R.id.btnOkSUP);

		// if (Build.VERSION.SDK_INT >= 16)
		// ok.setBackground(getResources().getDrawable(
		// R.drawable._r2_c2));
		// else
		// ok.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable._r2_c2));

		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
//		gallery.setOnClickListener(this);
//		camera.setOnClickListener(this);

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(context.getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}

		if (SignUps.bitmap2 != null) {
			imagePhoto.setImageBitmap(SignUps.bitmap2);
		} else {
			imagePhoto.setImageDrawable(context.getResources().getDrawable(
					R.drawable.no_image));
		}

		// read();
		// Message.message(getActivity(), alContacts.get(0));
		dialog.show();

		return dialog;
	}

	public void setValues(LogInfo info, Bitmap bitmap) {
		this.info = info;
		this.bitmap = bitmap;

	}

	public void setup() {
		information.clear();
		information.add(info.uname);
		information.add(info.upass);
		information.add(info.email);
		information.add(info.fname);
		information.add(info.lname);
		information.add(info.dob);
		information.add(info.country);
		information.add(info.city);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.btnOkSUP) {
			// String save = saveData.getString("theme", "default");
			// Message.message(context, save);

			// System.exit(0);
			Signing signing = new Signing();
			signing.setValues(info);
			signing.show(manager, "signningg");
			dialog.dismiss();
		} else if (arg0.getId() == R.id.btnCancelSUP) {

			dialog.dismiss();
		} 
//		else if (arg0.getId() == R.id.btnGall) {
//			openGallery();
//
//		} else if (arg0.getId() == R.id.btnCamera) {
//			takePicture();
//		}
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

			Message.message(context, "Activity not found");
		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void startCropImage() {

		Intent intent = new Intent(context, CropImage.class);
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

				InputStream inputStream = context.getContentResolver()
						.openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(
						mFileTemp);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();

				startCropImage();

			} catch (Exception e) {
				Message.message(context, "eroor temp");

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