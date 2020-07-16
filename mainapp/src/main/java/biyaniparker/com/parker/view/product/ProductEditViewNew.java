package biyaniparker.com.parker.view.product;

import android.Manifest;
import android.app.AlertDialog;
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
import android.provider.MediaStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleProduct1;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.BitmapUtilities;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.Constants;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ImageQuality;
import biyaniparker.com.parker.utilities.MultifileUploadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.deduct.DeductProductView;
import biyaniparker.com.parker.view.homeadmin.ImageRotateSetting;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;
import biyaniparker.com.parker.view.managestock.StockManageProductView;

public class ProductEditViewNew extends AppCompatActivity implements  View.OnClickListener,DownloadUtility,MultifileUploadUtility, AdapterView.OnItemClickListener {

    int width=525,height=700;


    EditText edName, edStripCode, edPrice;
    CheckBox chkIsActive;
   Spinner spCategory, spPrice;
    Button btnSave, btnDelete;
    ImageView img, camera, gallary;
    ModuleProduct1 moduleProduct;
    ModuleCategory moduleCategory;
    ModulePrice modulePrice;
    Double price;
    ProductBean bean;
    String path1="",path="";
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_edit_product);


        init();
        modulePrice=new ModulePrice(this);
        modulePrice.getPrices();
        moduleProduct = new ModuleProduct1(this);
        moduleProduct.getProductList();
        moduleCategory = new ModuleCategory(this);
        moduleCategory.getLastCategoryList();


        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        bean = new ProductBean();
        renderView();
        CommonUtilities.hideatInItInputBoard(this);
        checkSDCardsWrite();



        findViewById(R.id.btnStock).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(ProductEditViewNew.this,StockManageProductView.class);
                intent.putExtra("ProductId",bean.getProductId());
                startActivity(intent);
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
        return super.onOptionsItemSelected(item);
    }


    private void init() {

        edName = (EditText) findViewById(R.id.name);
        edStripCode = (EditText) findViewById(R.id.stripCode);
        //edPrice = (EditText) findViewById(R.id.price);
        btnSave = (Button) findViewById(R.id.buttonSave);
        btnDelete=(Button)findViewById(R.id.buttonDelete);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        //spCategory.setEnabled(true);
        spPrice=(Spinner)findViewById(R.id.spinnerPrice);
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=bean.getIconThumb();
                //   CommonUtilities.alert(UserBagView.this,"String : "+str);
                Intent intent=new Intent(ProductEditViewNew.this, ViewProductImage.class);
                intent.putExtra("path",str);
                startActivity(intent);
            }
        });
        camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto();
            }
        });
        gallary = (ImageView) findViewById(R.id.gallary);
        chkIsActive=(CheckBox)findViewById(R.id.isActive);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
        getSupportActionBar().setTitle("Edit Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

  int previousCateposition=-1;
    private void renderView() {
        ArrayAdapter priceArray=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,modulePrice.list);
        spPrice.setAdapter(priceArray);
       // spPrice.setOnItemClickListener(this);


        Intent intent = getIntent();
        int productId = intent.getIntExtra("ProductId", 0);
        for (int i = 0; i < moduleProduct.list.size(); i++) {
            if (moduleProduct.list.get(i).getProductId() == productId) {
                bean = moduleProduct.list.get(i);
            }
        }


        edName.setText(bean.getProductName());
        //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, moduleCategory.list);
        //spCategory.setAdapter(arrayAdapter);
        String catName="";

        moduleProduct.setSameParentCategoryList(bean.getCategoryId());
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, moduleProduct.sameParentCategoryList);
        //moduleProduct.sameParentCategoryList);
        spCategory.setAdapter(arrayAdapter);

        for (int i = 0; i < moduleProduct.sameParentCategoryList.size(); i++)
        {
            if (moduleProduct.sameParentCategoryList.get(i).getCategoryId() == bean.getCategoryId()) {
                catName=moduleProduct.sameParentCategoryList.get(i).categoryName;
               // spCategory.setSelection(i);
                previousCateposition=i;
            }
        }


        for(int j=0;j<modulePrice.list.size();j++)
        {
                if(modulePrice.list.get(j).getPriceId()==bean.getPriceId())
                {
                    price=modulePrice.list.get(j).getConsumerPrice();
                    spPrice.setSelection(j);
                }
        }


        chkIsActive.setChecked((bean.getIsActive().equals("true")));
        edStripCode.setText(bean.getStripCode());



      //  spPrice.setText(((int) (double) price) + "");



        moduleProduct.sameParentCategoryList= moduleCategory.lastCategoryList;//  getLastCategoryList();;

       // spCategory.setText(catName);
       //
        //spCategory.setEnabled(true);



        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
                bean.iconThumb
                ,
                img, doption, animateFirstListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {

                           // Toast.makeText(ProductEditViewNew.this, previousCateposition+"", Toast.LENGTH_SHORT).show();
                            spCategory.setSelection(previousCateposition);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

   //     Toast.makeText(this, spCategory.getSelectedItemPosition()+"", Toast.LENGTH_SHORT).show();
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
            if(btnSave.getId()==v.getId()) {
                if (validation()) {
                    bean.setProductCode("Test");
                    bean.setProductName(edName.getText().toString());
                    bean.setStripCode(edStripCode.getText().toString());
                    bean.setDetails("Product Details");
                    bean.setPriceId(modulePrice.list.get(spPrice.getSelectedItemPosition()).getPriceId());
                    int index=spPrice.getSelectedItemPosition();
                    /*try {
                        bean.setPrice(modulePrice.list.get(spPrice.getSelectedIndex()).getConsumerPrice());
                    }
                    catch (Exception e){}*/



                    try {
                        int selectedIndex=spCategory.getSelectedItemPosition();
                        CategoryBean selectedBean=moduleProduct.sameParentCategoryList.get(selectedIndex);
                        bean.setCategoryId(selectedBean.getCategoryId());
                        int i=0;
                        i++;
                    } catch (Exception e) {
                        e.toString();
                    }
                    //   bean.setIconThumb("Icon Thumb");

                   // bean.setIconFull(path1);


                    Intent in=new Intent();
                    in.setAction("LoadPhoto");
                    in.putExtra("URL",bean.getIconThumb());
                    sendBroadcast(in);
                    bean.setIconFull1("");
                    bean.setClientId(UserUtilities.getClientId(this));
                    bean.setSequenceNo(0);
                    bean.setCreatedBy(UserUtilities.getUserId(this));
                    bean.setCreatedDate(CommonUtilities.getCurrentTime());
                    bean.setChangedBy(UserUtilities.getUserId(this));
                    bean.setChagedDate(CommonUtilities.getCurrentTime());
                    bean.setIsActive(chkIsActive.isChecked()?"true":"false");
                    bean.setDeleteStatus("false");
                    try {
                        moduleProduct.updateProduct(bean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter all fileds ", Toast.LENGTH_LONG).show();
                }
            }
            else if(btnDelete.getId()==v.getId())
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
                            bean.setDeleteStatus("true");
                            //moduleProduct.updateProduct(bean);
                            moduleProduct.deleteProduct(bean);
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
        if(edStripCode.getText().toString().equals("")||edName.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }


    void pickPhoto()
    {
        try {
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

    // Call Capture Intent
    void callCapturePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				/*create instance of File with name img.jpg*/
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
				/*put uri as extra in intent object*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				/*start activity for result pass intent as argument and request code */
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            try {


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
                // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);

                Bitmap thePic1 = ThumbnailUtils.extractThumbnail(bitmap, 81, 112);
                final File bitmapFile1 = BitmapUtilities.saveToExtenal(thePic1, this);

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
                        //img.setImageResource(R.drawable.bgpaker);
                    }
                });
                alerBuilder.show();

                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                img.setImageBitmap(bitmap);
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

            try {

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

                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, CommonUtilities.Width, CommonUtilities.Height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);


                Bitmap thePic1 = ThumbnailUtils.extractThumbnail(bitmap, 81, 112);
                final File bitmapFile1 = BitmapUtilities.saveToExtenal(thePic1, this);


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
                        // img.setImageResource(R.drawable.bgpaker);
                    }
                });
                alerBuilder.show();

                bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                img.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {

                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
                alerBuilder.setTitle(getString(R.string.app_name));
                alerBuilder.setMessage(""+e.toString()+"\n\n"+e.getMessage());
                alerBuilder.show();

            }
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if (requestCode == 3 && responseCode == 200 && flag==0)
        {
            Toast.makeText(getApplicationContext(), " Record updated Successfully ..", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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

        else if (requestCode == 5 && responseCode == 200 && flag==1)
        {
            if(str.equals("Dependancy"))
            {
                Toast.makeText(getApplicationContext(), "Sorry Product"+bean.getProductName() +" Cannot be deleted , it may be in current use  ", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "  Record Deleted Successfully .....  ", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else if (requestCode == 10)
        {

//            Toast.makeText(getApplicationContext(), "" + responseCode, Toast.LENGTH_LONG).show();
            if (responseCode == 200) {
                bean.iconThumb=str;
            } else {
                img.setImageResource(android.R.drawable.ic_menu_camera);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      //  bean.setPriceId(modulePrice.list.get(position).getPriceId());
    }

    @Override
    public void onUploadComplete(String str1, String str2, int requestCode, int responseCode)
    {

        //Toast.makeText(getApplicationContext(),""+responseCode,Toast.LENGTH_LONG).show();
        if(requestCode==10)
        {
            path="";
            path1="";
           // Toast.makeText(getApplicationContext(),""+responseCode,Toast.LENGTH_LONG).show();
            if(responseCode==200)
            {
                path=str1;
                path1=str2;
                bean.iconThumb=path;
                bean.iconFull=path1;
                Toast.makeText(this,"Photo Uploaded",Toast.LENGTH_LONG).show();

                imageLoader.displayImage(
                        bean.iconThumb
                        ,
                        img, doption, animateFirstListener);

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

    private class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener
    {
        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 0);
                    displayedImages.add(imageUri);
                }
            }
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

}
