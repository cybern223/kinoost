package com.cyber.kinoost.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyber.kinoost.R;

public class OstFragment extends Fragment {
    
    public OstFragment() {
        // Empty constructor required for fragment subclasses
    }
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.osts, container, false);
   	
        return rootView;
    }
    

}
