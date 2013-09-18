package com.example.ubs_android;

import com.example.ubs_android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class SplashScreen extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 2500; // time in ms
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // thread for displaying the SplashScreen
        Thread splashThread = new Thread() {
			@Override
        	public void run() {
        		try {
        			int waited = 0;
	        		while (_active && (waited < _splashTime)) {
	        			sleep(100);
	        			if(_active) {
	        				waited += 100;
	        			}
	        		}
        		} catch(InterruptedException e) {
        			// do nothing
        		} finally {
        			finish();
        			Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        			startActivity(intent);
        		}
        	}
        };
        splashThread.start();
    }
    
    public void onRestart(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // thread for displaying the SplashScreen
        Thread splashThread = new Thread() {
			@Override
        	public void run() {
        		try {
        			int waited = 0;
	        		while (_active && (waited < _splashTime)) {
	        			sleep(100);
	        			if(_active) {
	        				waited += 100;
	        			}
	        		}
        		} catch(InterruptedException e) {
        			// do nothing
        		} finally {
        			finish();
        			Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        			startActivity(intent);
        		}
        	}
        };
        splashThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
}
