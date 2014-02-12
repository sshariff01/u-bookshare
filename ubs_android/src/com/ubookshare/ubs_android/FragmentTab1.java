package com.ubookshare.ubs_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
 
public class FragmentTab1 extends SherlockFragment {
	// Declare Variables
	String endpointUrl = "http://192.168.1.102:3000/";
	String findAction = "find/";
	String isbnStr = new String();
	String title, response = new String();
	EditText mEdit;
//	AutoCompleteTextView acTextView;	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragmenttab1, container, false);
        final Button button = (Button) view.findViewById(R.id.searchButton);
        final AutoCompleteTextView isbnACTextView = (AutoCompleteTextView) view.findViewById(R.id.searchIsbn);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				isbnStr = isbnACTextView.getText().toString();
//				FragmentManager fragmentManager = getFragmentManager();  
//				FragmentTransaction fragmentTransaction = fragmentManager  
//				        .beginTransaction();  
//				Fragment fragment3 = new Fragment();  
//				fragmentTransaction.replace(R.id.inner_frag, fragment3);  
//				fragmentTransaction.commit(); 
				
        		searchByIsbn(v);
            }

			private void searchByIsbn(View v) {
				if (!isbnStr.isEmpty()) {
					String urlStr = (endpointUrl + findAction + isbnStr);
					String result = new String();
					try {
						AsyncTaskApiRequest searchTask = new AsyncTaskApiRequest();
						result = searchTask.execute(urlStr).get();
						searchTask.cancel(true);
//							Intent searchIntent = new Intent(FragmentTab1.this.getActivity(), SearchActivity.class);
//							searchIntent.putExtra("queryResult", result); //Optional parameters
//							FragmentTab1.this.startActivity(searchIntent);
							
							
//							Fragment innerFragment = new Fragment();
//							FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//							transaction.add(R.id.inner_frag, innerFragment).commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (!result.isEmpty()) {
						Log.i("RESULT", "Result came back with something!");
						title = "Worked";
						response = "Launch new activity here instead of dialog.";
						DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
					    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "searchsuccessdialog");
					} else {
						Log.i("RESULT", "Result came back empty");
						title = "Kinda Worked";
						response = "Result came back empty.";
						DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
					    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "searcherrordialog");
					}
				} else {
					title = "Oops!";
					response = "Please specify the ISBN you would like to search.";
					DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
				    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "searcherrordialog");
				}
  			}
		});
        
        return view;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    private class AsyncTaskApiRequest extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    //Do some prepartations over here, before the task starts to execute
		    //Like freeze the button and/or show a progress bar
		
		}
		
		@Override
		protected String doInBackground(String... urls) {
		    // Task starts executing.
			String urlStr = endpointUrl + findAction + isbnStr;
			String result = makeApiRequest(urlStr);
		
		    // Execute HTTP requests here, with one url(urls[0]),
		    // or many urls using the urls table
		    // Save result in myresult
			return result;
		
		}
		
		protected void onPostExecute(String result) {
            //Do modifications you want after everything is finished
            //Like re-enable the button, and/or hide a progressbar
            //And of course do what you want with your result got from http-req
//			if (result.equals("Connect Exception")) {
//				title = "Oops!";
//        		response = "There was a problem connecting to the host. Please try again.";
//    			DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
//    		    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "searchconnectfailuredialog");
//			} else {
//				title = "Result";
//				response = result;
//				DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
//			    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "searchsuccessdialog");
//			}
			super.onPostExecute(result);
		}
		
		private String makeApiRequest(String url) {
			HttpResponse response = null;
			BasicHttpParams params = new BasicHttpParams();
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
			DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);

		    // Prepare a request object
		    HttpGet httpget = new HttpGet(url); 
		    // Prepare a variable to hold the result
		    String result = null;
		    // Execute the request
		    
		    try {
		        response = httpclient.execute(httpget);

		        // Get hold of the response entity
		        HttpEntity entity = response.getEntity();
		        
		        // If the response does not enclose an entity, there is no need
		        // to worry about connection release
		        if (entity != null) {

		            // A Simple JSON Response Read
		            InputStream instream = entity.getContent();
		            result = convertStreamToString(instream);
		            // Now you have the string representation of the HTML request
		            instream.close();
		        }
		    
		    } catch (Exception e) {
		    	result = "Connect Exception";
		    	e.printStackTrace();
		    }
		    
		    return result;
		}
		
		private String convertStreamToString(InputStream is) {
	        /*
	         * To convert the InputStream to String we use the BufferedReader.readLine()
	         * method. We iterate until the BufferedReader return null which means
	         * there's no more data to read. Each line will appended to a StringBuilder
	         * and returned as String.
	         */
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	}
    
}