package com.ubookshare.ubs_android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
 
public class MainActivity extends SherlockFragmentActivity implements TestDialogFragment.TestDialogListener {
    // Declare Variables
    ActionBar mActionBar;
    ViewPager mPager;
    Tab tab;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);
 
        // Activate Navigation Mode Tabs
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Locate ViewPager in activity_main.xml
        mPager = (ViewPager) findViewById(R.id.pager);
 
        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();
 
        // Capture ViewPager page swipes
        ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
                mActionBar.setSelectedNavigationItem(position);
            }
        };
 
        mPager.setOnPageChangeListener(ViewPagerListener);
        // Locate the adapter class called ViewPagerAdapter.java
        ViewPagerAdapter viewpageradapter = new ViewPagerAdapter(fm);
        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(viewpageradapter);
 
        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                // Pass the position on tab click to ViewPager
                mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };
 
        // Create first Tab
        tab = mActionBar.newTab().setText("Search").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        // Create second Tab
        tab = mActionBar.newTab().setText("Sell").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
        // Create third Tab
        tab = mActionBar.newTab().setText("Help").setTabListener(tabListener);
        mActionBar.addTab(tab);
 
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    public void showTestDialog() {
    	// Create an instance of the dialog fragment and show it
        DialogFragment dialog = new TestDialogFragment();
        dialog.show(getSupportFragmentManager(), "TestDialogFragment");
    }


	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
 
}
