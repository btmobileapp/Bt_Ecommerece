package biyaniparker.com.parker.view.size;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleSizeMaster;
import biyaniparker.com.parker.beans.SizeDetailBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class SizeCreateView extends AppCompatActivity implements View.OnClickListener,DownloadUtility {
    LinearLayout l1;
    Button buttonSave;
    EditText edName, edSequenceNo;
    ModuleCategory moduleCategory,cbean;
    SizeMaster sizeMaster;
    SizeDetailBean sizeDetailBean;
    ArrayList<Integer> selectedParentId;
    ModuleSizeMaster moduleSizeMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_create_size);
        getSupportActionBar().setTitle("Create Size Master");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        selectedParentId=new ArrayList<Integer>();
        l1=(LinearLayout)findViewById(R.id.ll);
        moduleCategory=new ModuleCategory(this);
        moduleSizeMaster=new ModuleSizeMaster(this);
        //moduleCategory.syncCategory();
        moduleCategory.getParentCategoryList();
        for(int i=0;i<moduleCategory.parentList.size();i++)
        {
            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_parent_category_for_size, null);


            l1.addView(v);
            v.setTag(i);
            final CheckBox ch=(CheckBox)v.findViewById(R.id.ch);
            ch.setTag(i);
            ch.setText(moduleCategory.parentList.get(i).getCategoryName());
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(ch.isChecked())
                    {
                       // Toast.makeText(getApplicationContext()," : "+ moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId(),Toast.LENGTH_LONG).show();
                        selectedParentId.add(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                    }
                    else
                    {
                        //selectedParentId.remove(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                        boolean b=selectedParentId.remove((Object)moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                    }
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), " : " + v.getTag().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        init();
        sizeMaster=new SizeMaster();
        sizeDetailBean =new SizeDetailBean();



        CommonUtilities.hideatInItInputBoard(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        edName=(EditText)findViewById(R.id.edSize);
        edSequenceNo=(EditText)findViewById(R.id.edSequence);
        buttonSave=(Button)findViewById(R.id.buttonsave);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);

        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else {
            if (validation()) {
                sizeMaster.setSizeName(edName.getText().toString());
                sizeMaster.setSequenceNo(Integer.parseInt(edSequenceNo.getText().toString()));
                sizeMaster.setClientId(UserUtilities.getClientId(this));
                sizeMaster.setCreatedBy(UserUtilities.getUserId(this));
                sizeMaster.setCreatedDate(CommonUtilities.getCurrentTime());
                sizeMaster.setDeleteStatus("false");
                try {
                    moduleSizeMaster.insertSize(sizeMaster, selectedParentId);
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Enter all fileds ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validation() {
        if(edName.getText().toString().equals("")||edSequenceNo.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }


    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==2 && responseCode==200)
        {
            Toast.makeText(getApplicationContext()," Record inserted Successfully ..",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Record Saved");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            alertDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }
}
