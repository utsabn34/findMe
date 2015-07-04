package com.notify.app;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
public class ImageUploadTry extends SherlockFragmentActivity implements
		OnClickListener {
	Button butts, butt;
	TextView textView;
	Uri currImageURI;
	String imagename;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.image_upload_try);
		butts = (Button) findViewById(R.id.butTry);
		butt = (Button) findViewById(R.id.butUpload);
		textView = (TextView) findViewById(R.id.tVTry);

		butts.setOnClickListener(this);
		butt.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.butTry) {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), 1);

		} else if (v.getId() == R.id.butUpload) {
			HttpUploaders uploader = new HttpUploaders();
			try {
				imagename = uploader.execute(getPath(this, currImageURI))
						.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		textView.setText(imagename);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				// currImageURI is the global variable I’m using to hold the
				// content:
				currImageURI = data.getData();
				System.out.println("\nCurrent image Path is ----->"
						+ getPath(this, currImageURI));

				textView.setText(getPath(this, currImageURI));
			}
		}
	}

	

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	private String getFilePathFromContentUrimmmmm(Uri selectedVideoUri) {
		String filePath;
		String[] filePathColumn = { MediaColumns.DATA };

		Cursor cursor = getContentResolver().query(selectedVideoUri,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}

	public String getRealPathFromURIsmmmm(Uri contentUri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		String selectedImagePath = "";
		if (isKitKat) {

			Uri originalUri = contentUri;

			// final int takeFlags = data.getFlags()
			// & (Intent.FLAG_GRANT_READ_URI_PERMISSION
			// | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			// Check for the freshest data.
			// getContentResolver().takePersistableUriPermission(originalUri,
			// takeFlags);

			/*
			 * now extract ID from Uri path using getLastPathSegment() and then
			 * split with ":" then call get Uri to for Internal storage or
			 * External storage for media I have used getUri()
			 */

			String id = originalUri.getLastPathSegment().split(":")[1];
			final String[] imageColumns = { MediaColumns.DATA };
			final String imageOrderBy = null;

			Uri uri = getUri();
			selectedImagePath = "path";

			Cursor imageCursor = managedQuery(uri, imageColumns,
					BaseColumns._ID + "=" + id, null, imageOrderBy);

			if (imageCursor.moveToFirst()) {
				selectedImagePath = imageCursor.getString(imageCursor
						.getColumnIndex(MediaColumns.DATA));
			}
		} else {
			// for older version use existing code here
		}
		return selectedImagePath;
	}

	public String getRealPathFromURIdddd(Uri contentUri) {
		String res = null;
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		String[] proj;
		Cursor cursor;

		if (isKitKat) {

			String[] fileSizeColumn = { OpenableColumns.DISPLAY_NAME };

			cursor = getContentResolver().query(contentUri, fileSizeColumn,
					null, null, null);
		} else {
			String[] fileSizeColumn = { MediaColumns.DATA };
			cursor = getContentResolver().query(contentUri, fileSizeColumn,
					null, null, null);

		}

		// String[] proj = { MediaStore.Images.Media.DATA };
		// Cursor cursor = getContentResolver().query(contentUri, proj, null,
		// null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaColumns.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	private Uri getUri() {
		String state = Environment.getExternalStorageState();
		if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
			return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

		return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	class HttpUploaders extends AsyncTask<String, Void, String> {
		ProgressDialog pdialog;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(ImageUploadTry.this);
			pdialog.setMessage("Connecting. Please wait...");
			pdialog.setIndeterminate(false);
			pdialog.setCancelable(true);
			pdialog.show();
		}

		@Override
		protected String doInBackground(String... path) {

			String outPut = null;

			for (String sdPath : path) {

				Bitmap bitmapOrg = BitmapFactory.decodeFile(sdPath);
				ByteArrayOutputStream bao = new ByteArrayOutputStream();

				// Resize the image
				double width = bitmapOrg.getWidth();
				double height = bitmapOrg.getHeight();
				double ratio = 400 / width;
				int newheight = (int) (ratio * height);

				System.out.println("———-width" + width);
				System.out.println("———-height" + height);

				bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400,
						newheight, true);

				// Here you can define .PNG as well
				bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
				byte[] ba = bao.toByteArray();
				// import com.guerrilla.ptf.Base64;
				// String ba1 = Base64.encodeBytes(ba);

				String ba1 = Base64.encodeToString(ba, 0);

				System.out.println("\nuploading image now ——" + ba1);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image", ba1));

				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://www.sanjibmaharjan.com.np/notifies/imageUpload.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					// print responce
					outPut = EntityUtils.toString(entity);
					Log.i("GET RESPONSE—-", outPut);

					// is = entity.getContent();
					Log.e("log_tag ******", "good connection");

					bitmapOrg.recycle();

				} catch (Exception e) {
					Log.e("log_tag ******",
							"Error in http connection " + e.toString());
				}
			}
			return outPut;
		}

	}

}
