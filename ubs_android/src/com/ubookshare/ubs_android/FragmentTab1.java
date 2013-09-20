package com.ubookshare.ubs_android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.actionbarsherlock.app.SherlockFragment;
 
public class FragmentTab1 extends SherlockFragment {
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragmenttab1, container, false);
        
        Button button= (Button) view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoIt(v);
            }

			private void DoIt(View v) {
				DialogFragment newFragment = new TestDialogFragment();
			    newFragment.show(FragmentTab1.this.getSherlockActivity().getSupportFragmentManager(), "testdialog");
  			}
        });
        
        return view;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    
 
}