package biyaniparker.com.parker.view.category;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.utilities.BitmapUtilities;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.Constants;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class CategoryCreateView extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,DownloadUtility {

    Button btnSave;
    EditText edCatName;
    ImageView imageView;
    Spinner spCategory;
    ModuleCategory moduleCategory;
    int selectedId;
    ImageView img, camera, gallary;
    CheckBox checkBox,checkBox1;


    //
    String path="";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_create_category);
        intit();
        moduleCategory=new ModuleCategory(this);
        moduleCategory.getParentCategoryList();


        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,moduleCategory.notChildList);
        spCategory.setAdapter(arrayAdapter);
        btnSave.setOnClickListener(this);
        getSupportActionBar().setTitle("Create Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        CommonUtilities.hideatInItInputBoard(this);
        if(Build.VERSION.SDK_INT>=23)
              checkSDCardsWrite();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void intit() {
        btnSave=(Button)findViewById(R.id.save);
        edCatName=(EditText)findViewById(R.id.edtopic);
        spCategory=(Spinner)findViewById(R.id.spCategory);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        checkBox1=findViewById(R.id.checkBox1);
        img=(ImageView)findViewById(R.id.img);
        camera=(ImageView)findViewById(R.id.camera);
        gallary=(ImageView)findViewById(R.id.gallary);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
callCapturePhoto();
            }
        });
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    spCategory.setVisibility(View.GONE);
                }
                else
                {
                    spCategory.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        CommonUtilities.hideSoftKeyBord(this);

        if (!new ConnectionDetector(this).isConnectingToInternet()) {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        else {
            if (validation()) {
                boolean isLast = checkBox.isChecked();
                String strIsLast = isLast ? "true" : "false";
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setCategoryName(edCatName.getText().toString());
                categoryBean.setClientId(1);
                //  categoryBean.setParentCategoryId(selectedId);
                try {
                    categoryBean.setParentCategoryId(moduleCategory.notChildList.get(spCategory.getSelectedItemPosition()).getCategoryId());
                } catch (Exception e) {
                }

                categoryBean.setCategoryCode("Test");

                categoryBean.setCreatedBy(UserUtilities.getUserId(this));

                categoryBean.setCreatedDate(CommonUtilities.getCurrentTime());
                categoryBean.setDeleteStatus("false");
                categoryBean.setIcon(path);
                categoryBean.setRemark("Development phase record");
                categoryBean.setIsLast(strIsLast);

                Intent in=new Intent();
                in.setAction("LoadPhoto");
                in.putExtra("URL", path);
                sendBroadcast(in);


                if(checkBox1.isChecked())
                    categoryBean.parentCategoryId=0;
                moduleCategory = new ModuleCategory(this);
                try {
                    moduleCategory.insertCategory(categoryBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please Enter all fields .... ", Toast.LENGTH_LONG).show();
                edCatName.setCursorVisible(true);
            }


        }
    }
    private boolean validation() {
        if(edCatName.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position!=-1)
        {
            CategoryBean b=moduleCategory.notChildList.get(position);
                    //spCategory.getSelectedIndex());
            selectedId=b.categoryId;
        }


        Toast.makeText(getApplicationContext()," selected ItemId is"+selectedId,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

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
        else  if(requestCode==10)
        {
            path="";
//            Toast.makeText(getApplicationContext(),""+responseCode,Toast.LENGTH_LONG).show();
            if(responseCode==200)
            {
                path=str;
                Toast.makeText(this, "Photo Uploaded", Toast.LENGTH_LONG).show();
            }
            else
            {
                img.setImageResource(android.R.drawable.ic_menu_camera);
            }
        }
        else
        {
                Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }


    }


    // Call Capture Intent
    void callCapturePhoto()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				/*create instance of File with name img.jpg*/
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
				/*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				/*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, 1);
    }
// pick photo from device gallary
    void pickPhoto()
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    2);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, 192, 256);
            img.setImageBitmap(thePic);
            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
           // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);

            AlertDialog.Builder alerBuilder=new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            alerBuilder.setTitle(getString(R.string.app_name));
            alerBuilder.setMessage("Do you want to uplaod File? ");
            alerBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                     moduleCategory.loadFile(bitmapFile);
                }
            });
            alerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   // moduleCategory.loadFile(bitmapFile);
                    img.setImageResource(R.drawable.bgpaker);
                }
            });
            alerBuilder.show();


        }


        else if(requestCode==2 && resultCode==RESULT_OK) {

            Uri uri = data.getData();
            Log.d("TAG", "File Uri: " + uri.toString());
            // Get the path
            String path ="";
            try {
                path = CommonUtilities.getPath(this, uri);
            }catch (Exception e){}

            File file = new File(path);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, 192, 256);
            img.setImageBitmap(thePic);
            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
            // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);

            AlertDialog.Builder alerBuilder=new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            alerBuilder.setTitle(getString(R.string.app_name));
            alerBuilder.setMessage("Do you want to uplaod File ? ");
            alerBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    moduleCategory.loadFile(bitmapFile);
                }
            });
            alerBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // moduleCategory.loadFile(bitmapFile);
                    img.setImageResource(R.drawable.bgpaker);
                }
            });
            alerBuilder.show();


        }


    }




    void checkSDCardsWrite()
    {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
/*
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else
            {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.MY_PERMISSIONS_REQUEST_Write_SD_Card);

                // MY_PERMISSIONS_REQUEST_Write_SD_Card is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.MY_PERMISSIONS_REQUEST_Write_SD_Card);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_Write_SD_Card: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

}
