package com.ubookshare.ubs_android;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
 
public class FragmentTab2 extends SherlockFragment {
	String endpointUrl = "http://9.26.187.187:3000";
	String postAction = "/create";
	String title, response;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        View view = inflater.inflate(R.layout.fragmenttab2, container, false);
        
        final Spinner spinner = (Spinner) view.findViewById(R.id.coverTypeSpinner);
	    // Create an ArrayAdapter using the string array and a custom spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
	            R.array.cover_types_array, R.layout.spinner_item_text);
	    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    
	    final Button button = (Button) view.findViewById(R.id.postButton);
	    final AutoCompleteTextView bookTitleACTextView = (AutoCompleteTextView) view.findViewById(R.id.bookTitle);
	    final AutoCompleteTextView bookPriceACTextView = (AutoCompleteTextView) view.findViewById(R.id.bookPrice);
	    final AutoCompleteTextView bookIsbnACTextView = (AutoCompleteTextView) view.findViewById(R.id.bookIsbn);
	    
	    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	String bookTitle = bookTitleACTextView.getText().toString();
            	String bookPrice = bookPriceACTextView.getText().toString();
            	String bookIsbn = bookIsbnACTextView.getText().toString();
            	String spinnerVal = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
            	String coverType = "0";
            	if (spinnerVal.equals("Hard Cover")) {
            		coverType = "2";
            	} else if (spinnerVal.equals("Soft Cover")) {
            		coverType = "1";
            	}
            	if (bookTitle.isEmpty() || bookPrice.isEmpty() || bookIsbn.isEmpty()){
            		title = "Oops!";
					response = "Please make sure you have specified all the required information before posting your book for sale.";
					DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
				    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "searcherrordialog");
            	} else {
            		// Ensure price has no more than two decimal places
            		if (!isPriceValid(bookPrice)){
            			title = "Oops!";
    					response = "Please enter a valid price format.";
    					DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
    				    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "searcherrordialog");
            		}
            		// Ensure ISBN number has valid size
            		if (!(bookIsbn.length() == 10 || bookIsbn.length() == 13)){
            			title = "Oops!";
    					response = "Please enter a valid 10- or 13- digit ISBN number.";
    					DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
    				    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "searcherrordialog");
            		} else {
            			postBookForSale(v, bookTitle, bookPrice, bookIsbn, coverType);
            		}
            	}
            }

			private boolean isPriceValid(String bookPrice) {
				if (bookPrice.contains(".")){
					String[] tmp = bookPrice.split(".");
					if (tmp[1].length() == 2) {
						return true;
					}
				} else {
					return true;
				}
				return false;
			}

			private void postBookForSale(View v, String bookTitle, String bookPrice, String bookIsbn, String coverType) {
				try {
					HashMap<String, Map> data = new HashMap<String, Map>();
					HashMap<String, String> posts = new HashMap<String, String>();

					posts.put("title", bookTitle);
					posts.put("edition", "1");
					posts.put("price", bookPrice);
					posts.put("isbn", bookIsbn);
					posts.put("cover_type", coverType);
					posts.put("device", "Android");
					
					data.put("post", posts);
					
					new AsyncHttpPost(data).execute(endpointUrl + postAction).get(2000, TimeUnit.MILLISECONDS);
					
				} catch (Exception e) {
					e.printStackTrace();
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
    
    public class CoverTypeSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }
 
    private class AsyncHttpPost extends AsyncTask<String, String, String> {
        private HashMap<String, Map> mData = null;// post data

        /**
         * constructor
         */
        public AsyncHttpPost(HashMap<String, Map> data) {
            mData = data;
        }

        /**
         * background
         * @return 
         */
        @SuppressWarnings({ "unchecked", "rawtypes", "finally" })
		protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(params[0]); // in this case, params[0] is URL
		    // Prepare a variable to hold the result
		    String result = new String();
            try {
                // Convert parameters into JSON object
                JSONObject holder = getJsonObjectFromMap(mData);

                // Passes the results to a string builder/entity
                StringEntity se = new StringEntity(holder.toString());

                // Sets the post request as the resulting string
                httpost.setEntity(se);
                
                // Sets a request header so the page receving the request
                // will know what to do with it
                httpost.setHeader("Accept", "application/json");
                httpost.setHeader("Content-type", "application/json");

                //Handles what is returned from the page 
                ResponseHandler responseHandler = new BasicResponseHandler();
                result = httpclient.execute(httpost, responseHandler);
                
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
            	result = "Connect Exception";
            	e.printStackTrace();
            } finally {
            	return result;
            }
            
        }

        /**
         * on getting result
         */
        @Override
        protected void onPostExecute(String result) {
        	if (result.equals("Connect Exception")){
        		title = "Oops!";
        		response = "There was a problem connecting to the host. Please try again.";
    			DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
    		    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "postconnectfailuredialog");
        	} else if (!result.isEmpty()) {
        		title = "Congratulations!";
        		response = "Your book has successfully been posted for sale. Thank you for using UBookShare!";
    			DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
    		    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "postsuccessdialog");
        	} else {
        		title = "Oops!";
        		response = "There was a problem processing your post. Please try again.";
    			DialogFragment newFragment = AlertDialogFragment.newInstance(title, response);
    		    newFragment.show(FragmentTab2.this.getSherlockActivity().getSupportFragmentManager(), "postfailuredialog");
        	}
        	
        }
        
        private JSONObject getJsonObjectFromMap(Map params) throws JSONException {

            //all the passed parameters from the post request
            //iterator used to loop through all the parameters
            //passed in the post request
            Iterator iter = params.entrySet().iterator();

            //Stores JSON
            JSONObject holder = new JSONObject();

            //using the earlier example your first entry would get email
            //and the inner while would get the value which would be 'foo@bar.com' 
            //{ fan: { email : 'foo@bar.com' } }

            //While there is another entry
            while (iter.hasNext()) 
            {
                //gets an entry in the params
                Map.Entry pairs = (Map.Entry)iter.next();

                //creates a key for Map
                String key = (String)pairs.getKey();

                //Create a new map
                Map m = (Map)pairs.getValue();   

                //object for storing Json
                JSONObject data = new JSONObject();

                //gets the value
                Iterator iter2 = m.entrySet().iterator();
                while (iter2.hasNext()) 
                {
                    Map.Entry pairs2 = (Map.Entry)iter2.next();
                    data.put((String)pairs2.getKey(), (String)pairs2.getValue());
                }

                //puts email and 'foo@bar.com'  together in map
                holder.put(key, data);
            }
            return holder;
        }
    }
}