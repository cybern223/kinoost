package com.cyber.kinoost;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SavedActivity extends Activity {

	RelativeLayout menuContainer;
	RelativeLayout listContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.film);
		menuContainer = (RelativeLayout) findViewById(R.id.menu_container);
		listContainer = (RelativeLayout) findViewById(R.id.list_container);
		ToggleButton toogleButton = (ToggleButton) findViewById(R.id.main_button);
		toogleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (buttonView.isChecked()) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							listContainer.getWidth(), LayoutParams.MATCH_PARENT);
					lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
					listContainer.setLayoutParams(lp);
				} else {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					lp.setMargins(0, 0, 0, 0);
					listContainer.setLayoutParams(lp);
				}
			}
		});
	}
}
