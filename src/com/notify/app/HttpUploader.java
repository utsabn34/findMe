package com.notify.app;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
//Uploader class
public class HttpUploader extends AsyncTask<String, Void, String> {
	ProgressDialog pdialog;
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPreExecute();
//		 pdialog = new ProgressDialog(getClass().get);
//		 pdialog.setMessage("Connecting. Please wait...");
//		 pdialog.setIndeterminate(false);
//		 pdialog.setCancelable(true);
//		 pdialog.show();
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

			bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight,
					true);

			// Here you can define .PNG as well
			bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
			byte[] ba = bao.toByteArray();
//			import com.guerrilla.ptf.Base64;
//			String ba1 = Base64.encodeBytes(ba);
			
			String ba1 = Base64.encodeToString(ba, 0);

			System.out.println("\nuploading image now ——" + ba1);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", ba1));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://10.0.2.2/notified/imageUpload.php");
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