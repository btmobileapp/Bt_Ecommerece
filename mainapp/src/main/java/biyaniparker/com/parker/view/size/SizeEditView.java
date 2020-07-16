package biyaniparker.com.parker.view.size;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

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

import org.json.JSONException;

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

public class SizeEditView extends AppCompatActivity implements View.OnClickListener,DownloadUtility {

    LinearLayout l1;
    Button buttonUpdate, buttonDelete;
    EditText edName, edSequenceNo;
    ModuleCategory moduleCategory,cbean;
    SizeMaster sizeMaster;
    SizeDetailBean sizeDetailBean;
    ArrayList<Integer> selectedParentId;
    ModuleSizeMaster moduleSizeMaster;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_edit_size);
        init();
        getSupportActionBar().setTitle("Edit Size");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        l1=(LinearLayout)findViewById(R.id.ll);
        moduleSizeMaster=new ModuleSizeMaster(this);
        moduleCategory=new ModuleCategory(this);
       // moduleCategory.syncCategory();
        moduleCategory.getParentCategoryList();
        selectedParentId=new ArrayList<Integer>();
        renderView();

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
        buttonDelete=(Button)findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);
        buttonUpdate=(Button)findViewById(R.id.buttonupdate);
        buttonUpdate.setOnClickListener(this);
    }


    private void renderView()
    {
        Intent intent=getIntent();
        int sizeId=intent.getIntExtra("SizeId", 0);
        //SizeMaster sizeMaster=new SizeMaster();

        // getting size master
        sizeMaster=moduleSizeMaster.getSizeMasterBean(sizeId);
        edName.setText(sizeMaster.getSizeName());
        edSequenceNo.setText(sizeMaster.getSequenceNo() + "");


        // getting size details for size master

        ArrayList<SizeDetailBean> sizeDetails=new ArrayList<SizeDetailBean>();

        sizeDetails=moduleSizeMaster.getSizeDetails(sizeId);


        for(int i=0;i<moduleCategory.parentList.size();i++)
        {
            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_parent_category_for_size, null);

            l1.addView(v);
            v.setTag(i);
            final CheckBox ch=(CheckBox)v.findViewById(R.id.ch);
            ch.setTag(i);
            ch.setText(moduleCategory.parentList.get(i).getCategoryName());

            for(int j=0;j<sizeDetails.size();j++) {
                if (moduleCategory.parentList.get(i).getCategoryId() == sizeDetails.get(j).getCategoryId()) {
                    ch.setChecked(true);
                    selectedParentId.add(sizeDetails.get(j).getCategoryId());
                }
            }
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (ch.isChecked()) {
                        //Toast.makeText(getApplicationContext(), " : " + moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId(), Toast.LENGTH_LONG).show();
                        selectedParentId.add(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                    }
                    else {
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
    }

    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(buttonUpdate.isPressed()) {

                if (validation()) {
                    sizeMaster.setSizeName(edName.getText().toString());
                    sizeMaster.setSequenceNo(Integer.parseInt(edSequenceNo.getText().toString()));
                    sizeMaster.setClientId(UserUtilities.getClientId(this));
                    sizeMaster.setChangedBy(UserUtilities.getUserId(this));
                    sizeMaster.setChangedDate(CommonUtilities.getCurrentTime());
                    sizeMaster.setDeleteStatus("false");
                    try {
                        moduleSizeMaster.updateSize(sizeMaster, selectedParentId);
                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter all fileds ", Toast.LENGTH_LONG).show();
                }
            }
            else if(buttonDelete.isPressed())
            {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage("Are you sure to delete ? ");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        try {
                            flag = 1;
                            sizeMaster.setDeleteStatus("true");
                            moduleSizeMaster.deleteSize(sizeMaster, selectedParentId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.setCancelable(true);
                    }
                });

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //finish();
                    }
                });
                alertDialog.show();
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


        if(requestCode==3 && responseCode==200&& flag==0)
        {
            Toast.makeText(getApplicationContext()," Record updated Successfully ..",Toast.LENGTH_LONG).show();




            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Record Updated");
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
        else if(requestCode==4 && responseCode==200&& flag==1)
        {
            if(str.equals("dependancy"))
            {
                Toast.makeText(getApplicationContext(), " Sorry Size"+sizeMaster.getSizeName()+" cannot be deleted, it may in current use", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), " Record Deleted Successfully ", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }
}
