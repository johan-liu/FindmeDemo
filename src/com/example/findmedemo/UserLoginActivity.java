package com.example.findmedemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.damy.HttpConn.AsyncHttpClient;
import com.damy.HttpConn.JsonHttpResponseHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginActivity extends Activity {
	private JsonHttpResponseHandler handlerRespInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlogin);
		
		initControls();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void initControls()
	{
		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    EditText txtName, txtPass;
			    txtName = (EditText)findViewById(R.id.editPhoneNum);
			    txtPass = (EditText)findViewById(R.id.editPass);
			    new FetchTask().execute(txtName.getText().toString(), txtPass.getText().toString());
			}
			
		});
	}

	public class FetchTask extends AsyncTask<String, Void, JSONObject> {
	    @Override
	    protected JSONObject doInBackground(String... params) {
	        try {
	        	String strData = "";
	        	String url="http://www.scannity.com/findme/loginservice.php";

	    	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    	    HttpPost httpPost = new HttpPost(url);

	    	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    	    nameValuePairs.add(new BasicNameValuePair("phone", params[0]));
	    	    nameValuePairs.add(new BasicNameValuePair("password", params[1]));

	    	    try {
	    			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	    		    HttpResponse httpResponse;
	    			try {
	    				httpResponse = httpClient.execute(httpPost);
	    			    InputStream httpEntity = httpResponse.getEntity().getContent();
	    			    byte[] b = new byte[1024];
	    			    httpEntity.read(b);
	    			    strData = new String(b);
	    			    Log.e("", strData);
	    			} catch (ClientProtocolException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		} catch (UnsupportedEncodingException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}

	            // parsing data
	            return new JSONObject(strData);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    @Override
	    protected void onPostExecute(JSONObject result) {
	        if (result != null) {
	        	String strResult = "";
	            try {
					strResult = result.getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            if (strResult.equalsIgnoreCase("200"))
		            startActivity(new Intent(UserLoginActivity.this, WelcomePageActivity.class));
	            else
		            Toast.makeText(UserLoginActivity.this, "Not registered user", Toast.LENGTH_SHORT).show();

	        } else {
	            Toast.makeText(UserLoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
	        }
	    }
	}
}
