package biyaniparker.com.parker.view.deduct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleProductDetails;
import biyaniparker.com.parker.bal.ModuleSizeMaster;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductStockBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

public class DeductProductView extends AppCompatActivity implements View.OnClickListener , DownloadUtility{


    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;

    TextView txtProduct,txtPrice;
    Button btnSave;
    LinearLayout linear;
    ImageView img;
    ModuleProduct moduleProduct;
    ModuleCategory moduleCategory;
    ModulePrice modulePrice;
    ModuleProductDetails moduleProductDetails;
    ProductBean bean;
    ArrayList<StockMasterBean> stockList=new ArrayList<StockMasterBean>();
    ArrayList<View> viewList=new ArrayList<View>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_stock);                 //used same resource file again

        linear=(LinearLayout)findViewById(R.id.linear);
        btnSave=(Button)findViewById(R.id.buttonsave);
        btnSave.setOnClickListener(this);
        txtProduct=(TextView)findViewById(R.id.productName);
        txtPrice=(TextView)findViewById(R.id.productPrice);
        img=(ImageView)findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeductProductView.this,ViewProductImage.class);
                intent.putExtra("path",bean.getIconThumb());
                startActivity(intent);
            }
        });
        moduleProduct=new ModuleProduct(this);
        modulePrice=new ModulePrice(this);
        moduleCategory=new ModuleCategory(this);
        moduleProductDetails=new ModuleProductDetails(this);


        bean=new ProductBean();
        Intent intent=getIntent();
        int productId= intent.getIntExtra("ProductId",0);
        bean=moduleProduct.getProductBeanByProductId(productId);
        txtProduct.setText(bean.getProductName());
        txtPrice.setText(""+(int)(double)modulePrice.getPriceByPriceId(bean.getPriceId())+" Rs");

        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
                bean.iconThumb, img, doption, animateFirstListener);


        moduleProductDetails.setProductId(bean.getProductId());
        moduleProductDetails.syncAvailableProduct();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Product Stock Deduction ");

        viewList.clear();
        CategoryBean categoryBean=new CategoryBean();
        categoryBean=moduleCategory.getCategoryBeanById(bean.getCategoryId());


        moduleProduct.sizeMasters.clear();
        moduleProduct.getSizeDetailList(categoryBean);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v)
    {


        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {


                if( validation()) {
                    for (int i = 0; i < viewList.size(); i++) {

                        View v1 = viewList.get(i);
                        int SizeId = Integer.parseInt(v1.getTag().toString());
                        EditText ed = (EditText) v1.findViewById(R.id.edSizeQty);
                        StockMasterBean s = new StockMasterBean();
                        if (ed.getText().toString().equals("")) {

                        } else {
                            s.setSizeId(SizeId);
                            s.setOutwardQty(Integer.parseInt(ed.getText().toString()));
                            stockList.add(s);
                        }
                    }


                    bean.setClientId(UserUtilities.getClientId(this));
                    bean.setSequenceNo(0);
                    bean.setCreatedBy(UserUtilities.getUserId(this));
                    bean.setCreatedDate(CommonUtilities.getCurrentTime());
                    bean.setChangedBy(UserUtilities.getUserId(this));
                    bean.setChagedDate(CommonUtilities.getCurrentTime());
                    bean.setIsActive("false");
                    bean.setDeleteStatus("false");
                    if (stockList.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter quantity ... ", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            moduleProduct.productDeduction(bean, stockList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
               else
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid quantity ... ", Toast.LENGTH_SHORT).show();
                }
        }

    }

    private boolean validation()
    {

        for (int i = 0; i < moduleProductDetails.availabaleStockList.size(); i++)
        {

                ProductStockBean temp=moduleProductDetails.availabaleStockList.get(i);

            for(int j=0; j < viewList.size(); j++)
            {
                View v1 = viewList.get(j);
                int sizeId = Integer.parseInt(v1.getTag().toString());
                EditText ed = (EditText) v1.findViewById(R.id.edSizeQty);

                if (temp.getSizeId()==sizeId )
                {
                    if(ed.getText().toString().equals(""))
                    {

                    }
                    else
                    {
                        if (temp.getQnty() < Integer.parseInt(ed.getText().toString()))
                        {
                            return false;
                        }
                    }
                }
                else
                {

                }
            }


        }
        return true;
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

        if(requestCode==5 && responseCode==200)
        {
            //Toast.makeText(getApplicationContext()," Stock duducted Successfully",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Stock Removed Succefully");
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
        else if(requestCode==3 && responseCode==200)
        {
            renderView();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }


    }

    private void renderView()
    {
        linear.removeAllViews();
        ModuleSizeMaster moduleSizeMaster=new ModuleSizeMaster(this);
        for(int i=0;i<moduleProduct.sizeMasters.size();i++)
        {
           SizeMaster s= moduleProduct.sizeMasters.get(i);

            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_activity_sizes_for_product, null);
            linear.addView(v);

            TextView txtName=(TextView)v.findViewById(R.id.txtSizeName);
            TextView txtAvaibleQnty=(TextView)v.findViewById(R.id.txtAvaibleQnt);
            EditText edQty=(EditText)v.findViewById(R.id.edSizeQty);
            v.setTag((moduleProduct.sizeMasters.get(i).getSizeId()));
            //txtName.setTag(i);

            //SizeMaster sizeMaster=moduleSizeMaster.getSizeMasterBean(moduleProductDetails.availabaleStockList.get(i).getSizeId());
            int position=-1, flag=0;
            txtName.setText(moduleProduct.sizeMasters.get(i).getSizeName());
            for(int j=0;j<moduleProductDetails.availabaleStockList.size();j++)
            {
                if(s.getSizeId()==moduleProductDetails.availabaleStockList.get(j).getSizeId())
                {
                    position=j;
                    flag=1;
                }
            }

            txtAvaibleQnty.setText(flag==1?moduleProductDetails.availabaleStockList.get(position).getQnty()+"":"0");
            viewList.add(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  // Toast.makeText(getApplicationContext(), " : " + v.getTag().toString() + " , " + v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


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
                if (firstDisplay)
                {
                    FadeInBitmapDisplayer.animate(imageView, 0);
                    displayedImages.add(imageUri);
                }
                img.setVisibility(View.VISIBLE);

            }
        }
    }
}
