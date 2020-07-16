package biyaniparker.com.parker.view.product;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleProduct1;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.utilities.AsyncFileUpload;
import biyaniparker.com.parker.utilities.BitmapUtilities;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.Constants;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ImageQuality;
import biyaniparker.com.parker.utilities.MultifileUploadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.utilities.serverutilities.FileUpload;
import biyaniparker.com.parker.view.homeadmin.ImageRotateSetting;

public class ProductCreateViewNew extends AppCompatActivity implements View.OnClickListener, DownloadUtility, MultifileUploadUtility, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    //int width=525,height=700;

    ImageView image1,image2,image3,image4;

    EditText edName, edStripCode, edPrice,edUnitName;
    Spinner spCategory   ;//, spPrices;
    StockMasterBean stockBean;
    LinearLayout l1;
    CheckBox chkIsActive;
    Button btnSave;
    ImageView img,camera,gallery;
    ModuleProduct1 moduleProduct;
    ModuleCategory moduleCategory;
    ModulePrice modulePrice;
    String path="";
    String path1="";
    ArrayList<View> viewList=new ArrayList<View>() ;
    ArrayList<StockMasterBean> stockList=new ArrayList<StockMasterBean>();
     int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_create_product_new);
        init();
        stockBean=new StockMasterBean();
        moduleProduct=new ModuleProduct1(this);
        moduleCategory=new ModuleCategory(this);
        modulePrice=new ModulePrice(this);
        moduleCategory.getLastCategoryList();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,moduleCategory.lastCategoryList);
        spCategory.setAdapter(arrayAdapter);

        //  setting consumer price list for spinner
        modulePrice.getPrices();
        ArrayAdapter priceArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,modulePrice.list);
       // spPrices.setAdapter(priceArrayAdapter);
        btnSave.setOnClickListener(this);


        CommonUtilities.hideatInItInputBoard(this);
        intitMultipleImages();

    }

    private void intitMultipleImages() {
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto(12);
               // pickPhoto();
               // pickPhoto(12);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto(22);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto(32);
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto(42);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.admin_home_screen, menu);
        getMenuInflater().inflate(R.menu.ic_rotate_menu,menu);
        return true;
    }


    private void init() {
        edPrice=findViewById(R.id.edPrice);
        edName=(EditText)findViewById(R.id.name);
        edUnitName=(EditText)findViewById(R.id.edUnitName);

        edStripCode=(EditText)findViewById(R.id.stripCode);
       // spPrices=(Spinner)findViewById(R.id.spinnerPrice);
        btnSave=(Button)findViewById(R.id.buttonSave);
        l1=(LinearLayout)findViewById(R.id.linearLayout);
        spCategory= (Spinner) findViewById(R.id.spinnerCategory);
        chkIsActive=(CheckBox)findViewById(R.id.isActive);
        chkIsActive.setChecked(true);
        img=(ImageView)findViewById(R.id.img);
        camera=(ImageView)findViewById(R.id.camera);

        //spPrices.setOnItemClickListener(this);
        spCategory.setOnItemSelectedListener(this);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto(1);
            }
        });
        gallery=(ImageView)findViewById(R.id.gallary);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto(2);
            }
        });
        getSupportActionBar().setTitle("Create Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        checkSDCardsWrite();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        else if(item.getItemId()==R.id.actionRotate)
        {
            startActivity(new Intent(this, ImageRotateSetting.class));
        }
        else if(item.getItemId()==R.id.actionImageQ)
        {
            startActivity(new Intent(this, ImageQuality.class));
        }
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        CommonUtilities.hideSoftKeyBord(this);
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (validation()) {


                for(int i=0;i<viewList.size();i++)
                {

                      View v1=viewList.get(i);
                      int SizeId= Integer.parseInt(v1.getTag().toString());
                     EditText ed=(EditText)  v1.findViewById(R.id.edSizeQty);
                    StockMasterBean s=new StockMasterBean();
                    if(ed.getText().toString().equals(""))
                    {

                    }
                    else
                    {
                            s.setSizeId(SizeId);
                            s.setInwardQty(Integer.parseInt(ed.getText().toString()));
                            stockList.add(s);
                    }
                }
                ProductBean bean = new ProductBean();
                bean.setProductCode("Test");
                bean.setProductName(edName.getText().toString());
                bean.setStripCode(edStripCode.getText().toString());
                bean.setDetails(" Product Details");
                try
                {
                   // bean.setPriceId(modulePrice.list.get(spPrices.getSelectedItemPosition()).getPriceId());
                    edPrice=findViewById(R.id.edPrice);
                    bean.price=Float.parseFloat( edPrice.getText().toString());
                }
                catch (Exception e)
                {

                }
                bean.setIconThumb(path);
                bean.setIconFull(path1);

                Intent in=new Intent();
                in.setAction("LoadPhoto");
                in.putExtra("URL", path);
                sendBroadcast(in);


                bean.setIconFull1("");
                try
                {
                    bean.setCategoryId(categoryId);
                } catch (Exception e){}
                bean.setClientId(UserUtilities.getClientId(this));
                bean.setSequenceNo(0);
                bean.setCreatedBy(UserUtilities.getUserId(this));
                bean.setCreatedDate(CommonUtilities.getCurrentTime());
                bean.setChangedBy(UserUtilities.getUserId (this));
                bean.setChagedDate(CommonUtilities.getCurrentTime());
                bean.setIsActive(chkIsActive.isChecked()?"true":"false");
                bean.setDeleteStatus("false");
                try {
                    bean.UnitName=edUnitName.getText().toString();
                    bean.IconFull2 = (String)image1.getTag();
                    bean.IconFull3 = (String)image2.getTag();
                    bean.IconFull4 = (String)image3.getTag();
                    bean.IconFull5 = (String)image4.getTag();

                }
                catch (Exception ex)
                {}
                try
                {
                    moduleProduct.createProduct(bean,stockList);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                //Intent in=new Intent(this,)
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please Enter all fileds ", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean validation() {
        if(edStripCode.getText().toString().equals("")||edName.getText().toString().equals(""))
        {return false;}
        else  if(edPrice.getText().toString().equals("")||edUnitName.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(requestCode==121)
        {
            if(responseCode==200)
            {
                try
                {
                      JSONObject j=new JSONObject(str);
                     String mPath=    j.getString("path");
                     image1.setTag(mPath);
                }
                catch (Exception ex)
                {}
            }
            else
            {
                image1.setImageResource(R.drawable.ic_menu_camera);
            }
        }
        else
        if(requestCode==221)
        {
            if(responseCode==200)
            {
                try
                {
                    JSONObject j=new JSONObject(str);
                    String mPath=    j.getString("path");
                    image2.setTag(mPath);
                }
                catch (Exception ex)
                {}
            }
            else
            {
                image2.setImageResource(R.drawable.ic_menu_camera);
            }
        }
        else   if(requestCode==321)
        {
            if(responseCode==200)
            {
                try
                {
                    JSONObject j=new JSONObject(str);
                    String mPath=    j.getString("path");
                    image3.setTag(mPath);
                }
                catch (Exception ex)
                {}
            }
            else
            {
                image3.setImageResource(R.drawable.ic_menu_camera);
            }
        }
        else   if(requestCode==421)
        {
            if(responseCode==200)
            {
                try
                {
                    JSONObject j=new JSONObject(str);
                    String mPath=    j.getString("path");
                    image4.setTag(mPath);
                }
                catch (Exception ex)
                {}
            }
            else
            {
                image4.setImageResource(R.drawable.ic_menu_camera);
            }
        }
        else  if(requestCode==2 && responseCode==200)
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
    void callCapturePhoto(int i)
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				/*create instance of File with name img.jpg*/
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
				/*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				/*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, i);
    }

    void pickPhoto(int i)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    i);
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

            try
            {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                bitmap=BitmapUtilities.rotetBitmap(bitmap,this);



                //***************************************************************************
                SharedPreferences rotetPreference=getSharedPreferences("rotet", MODE_PRIVATE);
                int rotetInt= rotetPreference.getInt("rotet", 0);

              /*

                if(rotetInt==90)
                {
                    int tmp=height;
                    height=width;
                    width=tmp;
                }
                */
                //*************************************************************************



                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);


                Bitmap thePic1 = ThumbnailUtils.extractThumbnail(bitmap, 81, 112);
                final File bitmapFile1 = BitmapUtilities.saveToExtenal(thePic1, this);
                // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                img.setImageBitmap(bitmap);
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                alerBuilder.setTitle(getString(R.string.app_name));
                alerBuilder.setMessage("Do you want to uplaod File ");
                alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moduleProduct.loadFile(bitmapFile, bitmapFile1);
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
            catch (Exception e)
            {
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
                alerBuilder.setTitle(getString(R.string.app_name));
                alerBuilder.setMessage(""+e.toString()+"\n\n"+e.getMessage());
                alerBuilder.show();

            }


        }
        else if(requestCode==2 && resultCode==RESULT_OK) {

            try
            {

                Uri uri = data.getData();
                Log.d("TAG", "File Uri: " + uri.toString());
                // Get the path
                String path = "";
                try {
                    path = CommonUtilities.getPath(this, uri);
                } catch (Exception e) {
                }

                File file = new File(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap,CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                Bitmap thePic1 = ThumbnailUtils.extractThumbnail(bitmap, 81, 112);
                final File bitmapFile1 = BitmapUtilities.saveToExtenal(thePic1, this);

                // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                img.setImageBitmap(bitmap);
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                alerBuilder.setTitle(getString(R.string.app_name));
                alerBuilder.setMessage("Do you want to uplaod File ");
                alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moduleProduct.loadFile(bitmapFile, bitmapFile1);
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

                // img.setImageBitmap(thePic);
            }
            catch (Exception e)
            {
                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
                alerBuilder.setTitle(getString(R.string.app_name));
                alerBuilder.setMessage(""+e.toString()+"\n\n"+e.getMessage());
                alerBuilder.show();

            }
        }
        else if(requestCode==12 && resultCode==RESULT_OK) {

            try
            {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                //   File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap,CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                image1.setImageBitmap(bitmap);

                AsyncFileUpload uploadUtility=new AsyncFileUpload(this,bitmapFile,CommonUtilities.URL+"ProductService.svc/UploadFile",121,this);
                uploadUtility.execute();
            }
            catch (Exception e)
            {

            }
        }
        else if(requestCode==22 && resultCode==RESULT_OK) {

            try
            {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap,CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                image2.setImageBitmap(bitmap);

                AsyncFileUpload uploadUtility=new AsyncFileUpload(this,bitmapFile,CommonUtilities.URL+"ProductService.svc/UploadFile",221,this);
                uploadUtility.execute();
            }
            catch (Exception e)
            {

            }
        }
        else if(requestCode==32 && resultCode==RESULT_OK) {

            try
            {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap,CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                image3.setImageBitmap(bitmap);

                AsyncFileUpload uploadUtility=new AsyncFileUpload(this,bitmapFile,CommonUtilities.URL+"ProductService.svc/UploadFile",321,this);
                uploadUtility.execute();
            }
            catch (Exception e)
            {

            }
        }
        else if(requestCode==42 && resultCode==RESULT_OK) {

            try
            {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap,CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                image4.setImageBitmap(bitmap);

                AsyncFileUpload uploadUtility=new AsyncFileUpload(this,bitmapFile,CommonUtilities.URL+"ProductService.svc/UploadFile",421,this);
                uploadUtility.execute();
            }
            catch (Exception e)
            {

            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        viewList.clear();
        CategoryBean bean=new CategoryBean();
        bean=(CategoryBean)moduleCategory.lastCategoryList.get(position);
        categoryId=bean.getCategoryId();
        //Toast.makeText(getApplicationContext()," : "+bean.getParentCategoryId(),Toast.LENGTH_SHORT).show();
        moduleProduct.sizeMasters.clear();
        moduleProduct.getSizeDetailList(bean);


        l1.removeAllViews();
        for(int i=0;i<moduleProduct.sizeMasters.size();i++)
        {
            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_activity_sizes_for_product, null);
            l1.addView(v);
           // v.setId(moduleProduct.sizeMasters.get(i).getSizeId());
           // v.setId(i);

            //final CheckBox ch=(CheckBox)v.findViewById(R.id.ch);
            TextView txtName=(TextView)v.findViewById(R.id.txtSizeName);
            TextView txtAvaibleQnt=(TextView)v.findViewById(R.id.txtAvaibleQnt);
            txtAvaibleQnt.setVisibility(View.INVISIBLE);
            EditText edQty=(EditText)v.findViewById(R.id.edSizeQty);
            v.setTag((moduleProduct.sizeMasters.get(i).getSizeId()));
            //txtName.setTag(i);
            txtName.setText(moduleProduct.sizeMasters.get(i).getSizeName());
            /*ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(ch.isChecked())
                    {
                        // Toast.makeText(getApplicationContext()," : "+ moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId(),Toast.LENGTH_LONG).show();
                        selectedParentId.add(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                    }
                }
            });*/
            viewList.add(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), " : " + v.getTag().toString()+" , "+v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onUploadComplete(String str1, String str2, int requestCode, int responseCode)
    {
    // Toast.makeText(getApplicationContext(),""+responseCode,Toast.LENGTH_LONG).show();
    if(requestCode==10)
    {
        path="";
        path1="";
        // Toast.makeText(getApplicationContext(),""+responseCode,Toast.LENGTH_LONG).show();
        if(responseCode==200)
        {
            path=str1;
            path1=str2;
            Toast.makeText(this,"Photo Uploaded",Toast.LENGTH_LONG).show();
        }
        else
        {
            img.setImageResource(android.R.drawable.ic_menu_camera);
        }
    }
    else
    {
        Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        img.setImageResource(android.R.drawable.ic_menu_camera);
    }


    }


    void checkSDCardsWrite()
    {

        if(Build.VERSION.SDK_INT>=23)
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        viewList.clear();
        CategoryBean bean=new CategoryBean();
        bean=(CategoryBean)moduleCategory.lastCategoryList.get(position);
        categoryId=bean.getCategoryId();
        //Toast.makeText(getApplicationContext()," : "+bean.getParentCategoryId(),Toast.LENGTH_SHORT).show();
        moduleProduct.sizeMasters.clear();
        moduleProduct.getSizeDetailList(bean);


        l1.removeAllViews();
        for(int i=0;i<moduleProduct.sizeMasters.size();i++)
        {
            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_activity_sizes_for_product, null);
            l1.addView(v);
            // v.setId(moduleProduct.sizeMasters.get(i).getSizeId());
            // v.setId(i);

            //final CheckBox ch=(CheckBox)v.findViewById(R.id.ch);
            TextView txtName=(TextView)v.findViewById(R.id.txtSizeName);
            TextView txtAvaibleQnt=(TextView)v.findViewById(R.id.txtAvaibleQnt);
            txtAvaibleQnt.setVisibility(View.INVISIBLE);
            EditText edQty=(EditText)v.findViewById(R.id.edSizeQty);
            v.setTag((moduleProduct.sizeMasters.get(i).getSizeId()));
            //txtName.setTag(i);
            txtName.setText(moduleProduct.sizeMasters.get(i).getSizeName());
            /*ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(ch.isChecked())
                    {
                        // Toast.makeText(getApplicationContext()," : "+ moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId(),Toast.LENGTH_LONG).show();
                        selectedParentId.add(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                    }
                }
            });*/
            viewList.add(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), " : " + v.getTag().toString()+" , "+v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
