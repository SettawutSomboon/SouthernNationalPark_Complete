package app.project2.southnationalpark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Category extends Activity implements OnClickListener{
	Button btnCategory,btnProvince;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		btnCategory = (Button)findViewById(R.id.img_category);
		btnProvince = (Button)findViewById(R.id.img_province);
		btnCategory.setOnClickListener(this);
		btnProvince.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_category:
			Log.d("Category","Category");
			Intent CategoryIntent = new Intent(this, CategoryNationalPark.class);
			CategoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(CategoryIntent);
			finish();
			break;
			
		case R.id.img_province:
			Log.d("Province","Province");
			Intent ProvinceIntent = new Intent(this, NationalPark.class);
			ProvinceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(ProvinceIntent);
			finish();
			break;
		default:
			break;
		}
	}
}
