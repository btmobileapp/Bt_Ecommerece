package biyaniparker.com.parker.utilities;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.view.product.ProductFilterView;

public class ImageQuality extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    RadioButton rdo20, rdo40,rdo60, rdo80, rdo100;
    RadioGroup radioGroup;
    TextView txtVal;
    int imageQ=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_quality);
        getSupportActionBar().setTitle("Image Quality Setting  ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rdo100=(RadioButton)findViewById(R.id.rdo100);
        rdo100.setOnCheckedChangeListener(this);
        rdo80=(RadioButton)findViewById(R.id.rdo80);
        rdo80.setOnCheckedChangeListener(this);
        rdo60=(RadioButton)findViewById(R.id.rdo60);
        rdo60.setOnCheckedChangeListener(this);
        rdo40=(RadioButton)findViewById(R.id.rdo40);
        rdo40.setOnCheckedChangeListener(this);
        rdo20=(RadioButton)findViewById(R.id.rdo20);
        rdo20.setOnCheckedChangeListener(this);
        txtVal=(TextView)findViewById(R.id.txtVal);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        SharedPreferences sh=this.getSharedPreferences("ImageQuality",MODE_PRIVATE);
        int val=sh.getInt("QualityValue",80);
        CommonUtilities.setHeightWidth(val);
        if(val==20)rdo20.setChecked(true);
        if(val==60)rdo60.setChecked(true);
        if(val==40)rdo40.setChecked(true);
        if(val==80)rdo80.setChecked(true);
        if(val==100)rdo100.setChecked(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {onBackPressed();}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(rdo100.getId()==buttonView.getId())
        {
            if(rdo100.isChecked()) {
                txtVal.setText("Excellent");
                imageQ=100;
            }
        }
        if(rdo80.getId()==buttonView.getId())
        {
            if(rdo80.isChecked()) {
                txtVal.setText("Good");
                imageQ=80;
            }

        }
        if(rdo60.getId()==buttonView.getId())
        {
            if(rdo60.isChecked()) {
                txtVal.setText("Average");
                imageQ=60;
            }
        }
        if(rdo20.getId()==buttonView.getId())
        {
            if(rdo20.isChecked()) {
                txtVal.setText("Poor");
                imageQ=20;
            }
        }
        if(rdo40.getId()==buttonView.getId())
        {
            if(rdo40.isChecked()) {
                txtVal.setText("Fair");
                imageQ=40;
            }
        }

        SharedPreferences sh=this.getSharedPreferences("ImageQuality",MODE_PRIVATE);
        SharedPreferences.Editor ed= sh.edit();
        ed.putInt("QualityValue",imageQ);
        ed.commit();

        CommonUtilities.setHeightWidth(imageQ);

    }
}
