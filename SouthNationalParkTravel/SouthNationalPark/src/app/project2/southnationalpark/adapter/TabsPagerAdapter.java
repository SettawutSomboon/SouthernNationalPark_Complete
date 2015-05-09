package app.project2.southnationalpark.adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import app.project2.southnationalpark.ActivityRafting;
import app.project2.southnationalpark.ActivityWalkingForest;
import app.project2.southnationalpark.NationalPark;
import app.project2.southnationalpark.ActivityPhoto;
import app.project2.southnationalpark.ProvinceNationalPark;
import app.project2.southnationalpark.ActivityDive;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	String id1;
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ActivityDive();
		case 1:
			// Games fragment activity
			return new ActivityRafting();
		case 2:
			// Movies fragment activity
			return new ActivityWalkingForest();
		case 3:
			// Movies fragment activity
			return new ActivityPhoto();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
