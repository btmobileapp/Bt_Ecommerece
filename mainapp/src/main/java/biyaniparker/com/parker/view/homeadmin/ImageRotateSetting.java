package biyaniparker.com.parker.view.homeadmin;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;

public class ImageRotateSetting extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    RadioButton  rd90,rd180,rd270,rd_90,rd_180,rd_270,rd0;
    RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_rotate_setting);
        getSupportActionBar().setTitle("Rotet Image");
        getSupportActionBar().setSubtitle("Rotet Image After Caputre");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        rd90=(RadioButton)findViewById(R.id.rd90);
        rd180=(RadioButton)findViewById(R.id.rd180);
        rd270=(RadioButton)findViewById(R.id.rd270);
        rd_180=(RadioButton)findViewById(R.id.rd_180);
        rd_90=(RadioButton)findViewById(R.id.rd_90);
        rd_270=(RadioButton)findViewById(R.id.rd_270);
        rd0=(RadioButton)findViewById(R.id.rd0);

        rg=(RadioGroup)findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);

        SharedPreferences rotetPreference=getSharedPreferences("rotet",MODE_PRIVATE);
        int rto=rotetPreference.getInt("rotet",0);
        if(90== rto)
        {
           rd90.setChecked(true);
        }
        else if(180== rto)
        {
            rd180.setChecked(true);
        }
        else if(270== rto)
        {
            rd270.setChecked(true);
        }
        else if(-90== rto)
        {
            rd_90.setChecked(true);
        }
        else if(-180== rto )
        {
            rd_180.setChecked(true);
        }
        else if(-270== rto)
        {
            rd_270.setChecked(true);
        }
        else if(0== R.id.rd0)
        {
            rd0.setChecked(true);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        SharedPreferences rotetPreference=getSharedPreferences("rotet",MODE_PRIVATE);
        if(checkedId== R.id.rd90)
        {
            rotetPreference.edit().putInt("rotet",90).commit();
        }
        else if(checkedId== R.id.rd180)
        {
            rotetPreference.edit().putInt("rotet", 180).commit();
        }
        else if(checkedId== R.id.rd270)
        {
            rotetPreference.edit().putInt("rotet", 270).commit();
        }
        else if(checkedId== R.id.rd_90)
        {
            rotetPreference.edit().putInt("rotet", -90).commit();
        }
        else if(checkedId== R.id.rd_180)
        {
            rotetPreference.edit().putInt("rotet", -180).commit();
        }
        else if(checkedId== R.id.rd_270)
        {
            rotetPreference.edit().putInt("rotet", -270).commit();
        }
        else if(checkedId== R.id.rd0)
        {
            rotetPreference.edit().putInt("rotet",0).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
