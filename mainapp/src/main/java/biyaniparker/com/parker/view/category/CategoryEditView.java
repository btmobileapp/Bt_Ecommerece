package biyaniparker.com.parker.view.category;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.utilities.BitmapUtilities;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.Constants;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class CategoryEditView extends AppCompatActivity implements View.OnClickListener, DownloadUtility, AdapterView.OnItemSelectedListener {


    Button btnSave,btnDelete;
    EditText edCatName;
    ImageView imageView,camera,gallary;
    Spinner spCategory;
    ModuleCategory moduleCategory;
    int selectedId;
    CheckBox checkBox,checkBox1;
    CategoryBean bean=new CategoryBean();
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_edit_category);
        getSupportActionBar().setTitle("Edit Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        moduleCategory=new ModuleCategory(this);
        moduleCategory.getParentCategoryList();
        moduleCategory.getCategoryList();

        intit();
        renderView();
        btnSave.setOnClickListener(this);

        CommonUtilities.hideatInItInputBoard(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        imageView=(ImageView)findViewById(R.id.img);
        camera=(ImageView)findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCapturePhoto();
            }
        });
        gallary=(ImageView)findViewById(R.id.gallary);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
        btnDelete=(Button)findViewById(R.id.delete);
        btnDelete.setOnClickListener(this);

        doption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.facilities)
                .showImageOnFail(R.drawable.facilities)
                .showStubImage(R.drawable.facilities).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)) // 100
                        // for
                        // Rounded
                        // Image
                .cacheOnDisc(true)
                        //.imageScaleType(10)
                .build();


        if(Build.VERSION.SDK_INT>=23)
            checkSDCardsWrite();

    }

    private void renderView()
    {
try {

    int pos = -1;
    Intent intent = getIntent();
    int catId = intent.getIntExtra("CategoryId", 0);
    for (int i = 0; i < moduleCategory.list.size(); i++) {
        if (moduleCategory.list.get(i).getCategoryId() == catId) {
            bean = moduleCategory.list.get(i);

            pos = i;

        }
    }
    edCatName.setText(bean.getCategoryName());
    checkBox.setChecked(Boolean.parseBoolean(bean.getIsLast()));
    //if(bean.parentCategoryId==0){moduleCategory.parentList.remove(pos);}
    ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, moduleCategory.notChildList);
    spCategory.setAdapter(arrayAdapter);

    if (pos != -1)
    {
        String parentnm = moduleCategory.getCategoryName(bean.getParentCategoryId());
        if (!parentnm.equalsIgnoreCase(""))
        {
            // spCategory.setText(parentnm);
            //temp comment
            //spCategory.setSelection(pos);
        }
        spCategory.setSelection(-1);
        selectedId = bean.getParentCategoryId();
        for(int i=0;i<   moduleCategory.notChildList.size();i++)
        {
            if(moduleCategory.notChildList.get(i).getCategoryId()==selectedId)
            {
                spCategory.setSelection(i);
            }
        }
    }
    spCategory.setOnItemSelectedListener(this);


    // To Display Image ON Image view
    imageLoader = ImageLoader.getInstance();
    //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
    imageLoader.displayImage(
            bean.icon
            ,
            imageView, doption, animateFirstListener);

    if(bean.getParentCategoryId()==0)
    {
        checkBox1.setChecked(true);
        spCategory.setVisibility(View.GONE);
    }
}
catch (Exception edx)
{CommonUtilities.alert(this,edx.toString());}
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
            if(btnSave.isPressed())
            {
                if (validation())
                {
                    boolean isLast = checkBox.isChecked();
                    String strIsLast = isLast ? "true" : "false";
                    // CategoryBean categoryBean=new CategoryBean();
                    bean.setCategoryName(edCatName.getText().toString());
                    bean.setClientId(UserUtilities.getClientId(this));
                    //  bean.setParentCategoryId(moduleCategory.list.get(spCategory.getSelectedIndex()).getCategoryId());
                    try {
                        bean.setParentCategoryId(moduleCategory.notChildList.get(spCategory.getSelectedItemPosition()).getCategoryId());
                    } catch (Exception e) {
                    }
                    bean.setChangedDate(CommonUtilities.getCurrentTime());
                    bean.setChangedBy(UserUtilities.getUserId(this));

                    bean.setIsLast(strIsLast);

                    Intent in=new Intent();
                    in.setAction("LoadPhoto");
                    in.putExtra("URL", bean.getIcon());
                    sendBroadcast(in);
                    if(checkBox1.isChecked())
                        bean.parentCategoryId=0;
                    moduleCategory = new ModuleCategory(this);
                    try {
                        moduleCategory.updateCategory(bean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter all fields .... ", Toast.LENGTH_LONG).show();
                    edCatName.setCursorVisible(true);
                }
            }
            else if(btnDelete.isPressed())
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
                            //moduleCategory.updateCategory(bean);
                            moduleCategory.deleteCategory(bean);
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

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==10)
        {
           if (responseCode == 200)
           {
               Toast.makeText(this,"Photo Uploaded",Toast.LENGTH_LONG).show();
               bean.setIcon(str);
           }
            else
           {
               imageView.setImageResource(R.drawable.ic_menu_camera);
           }

        }

        else if(requestCode==4 && responseCode==200)
        {
            if(str.equals("Dependancy"))
            {
                Toast.makeText(getApplicationContext(), "Sorry Category "+bean.getCategoryName() +" Cannot be deleted , it may be in current use  ", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "  Record Deleted Successfully .....  ", Toast.LENGTH_LONG).show();
                finish();
            }
        }

       else if(requestCode==3)
       {
           if (responseCode == 200&& flag==0) {
               //   Toast.makeText(getApplicationContext(), " Record updated Successfully ..", Toast.LENGTH_LONG).show();
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
               alertDialog.setTitle(getString(R.string.app_name));
               alertDialog.setMessage(" Record updated Successfully ..");
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
           else if (responseCode == 200&& flag==1)
           {
                    Toast.makeText(getApplicationContext()," Record Deleted Successfully ",Toast.LENGTH_SHORT).show();
                    finish();
           }
           else
           {
               Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
           }
       }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        try {

            CategoryBean b = moduleCategory.list.get(position);
            selectedId = b.categoryId;
        }
        catch (Exception ex){}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }


    private boolean validation() {
        if(edCatName.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }







    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;






    //For Photo

    private  class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {
        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null)
            {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 0);
                    displayedImages.add(imageUri);
                }
            }
        }
    }







    //****************Capture Photo  ****************************
    // pick photo from device gallary
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, 192, 256);
            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
          // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);

            AlertDialog.Builder alerBuilder=new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            alerBuilder.setTitle(getString(R.string.app_name));
            alerBuilder.setMessage("Do you want to uplaod File ");
            alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    moduleCategory.loadFile(bitmapFile);
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

            imageView.setImageBitmap(thePic);
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
            final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
            // File bitmapFile1 = BitmapUtilities.saveToExtenal(bitmap, this);

            AlertDialog.Builder alerBuilder=new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            alerBuilder.setTitle(getString(R.string.app_name));
            alerBuilder.setMessage("Do you want to uplaod File ");
            alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    moduleCategory.loadFile(bitmapFile);
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

            imageView.setImageBitmap(thePic);
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
