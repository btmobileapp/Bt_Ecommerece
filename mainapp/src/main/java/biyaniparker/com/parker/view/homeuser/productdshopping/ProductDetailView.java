package biyaniparker.com.parker.view.homeuser.productdshopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleProductDetails;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.ProductStockBean;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.product.ProductListView;

public class ProductDetailView extends AppCompatActivity implements DownloadUtility, View.OnClickListener {

    TextView txtproductname,txtprice;
    ImageView image;
    Button btnAdd;
    HorizontalScrollView horizontal;
    ProductBeanWithQnty bean;
    LinearLayout linearSizeMain,lhorizontal;
    ArrayList<StockMasterBean> stockList=new ArrayList<>();

    ModuleProductDetails moduleProductDetails;
    ModuleCategory moduleCategory;

    ImageView btnplus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_view);
        moduleProductDetails=new ModuleProductDetails(this);
        try
        {
             Gson gson = new Gson();
             bean = gson.fromJson(getIntent().getStringExtra("myjson"), ProductBeanWithQnty.class);

        }
        catch (Exception e)
        {}
        inItUi();
        if (bean != null)
        {

            txtproductname.setText(bean.getProductName());
            try {
                double price= moduleProductDetails.getPriceFromPriceId(bean.getPriceId());
                txtprice.setText((int)price+" Rs");
            }
            catch (Exception e){ txtprice.setText(" Rs");}

            // To Display Image ON Image view
            imageLoader = ImageLoader.getInstance();
            //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
            imageLoader.displayImage(
                    bean.iconThumb, image, doption, animateFirstListener);
        }

        moduleProductDetails.setProductId(bean.getProductId());
        moduleCategory=new ModuleCategory(this);
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            moduleProductDetails.syncProduct();
        }
        else
        {
            moduleProductDetails.loadFromDb(bean.getStripCode());
            addStripCodeProducts();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setSubtitle(""+moduleCategory.getCategoryName(bean.getCategoryId()));

        CommonUtilities.hideatInItInputBoard(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    void inItUi()
    {
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        txtproductname=(TextView)findViewById(R.id.txtproductname);
        txtprice=(TextView)findViewById(R.id.txtprice);
        image=(ImageView)findViewById(R.id.image);
        linearSizeMain=(LinearLayout)findViewById(R.id.linearSizeMain);
        horizontal=(HorizontalScrollView)findViewById(R.id.horizontal);
        lhorizontal=(LinearLayout)findViewById(R.id.lhorizontal);

        doption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.bgpaker)
                .showImageOnFail(R.drawable.bgpaker)
                .showStubImage(R.drawable.bgpaker).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)) // 100
                        // for
                        // Rounded
                        // Image
                .cacheOnDisc(true)
                        //.imageScaleType(10)
                .build();
        btnplus=(ImageView)findViewById(R.id.btnplus);
       // btnplus.setVisibility(View.INVISIBLE);
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductDetailView.this,ViewProductImage.class);
                intent.putExtra("path",bean.getIconThumb());
                startActivity(intent);
            }
        });

    }



    //-------------------------Image Code---------------------------
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;

    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);
        stockList.clear();
        if(validation())
        {
            if(stockList.isEmpty())
            {
                Toast.makeText(getApplicationContext()," Please enter stock quantity ..  ",Toast.LENGTH_SHORT).show();
            }
            else
            {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("Parker");
                alertDialog.setMessage("Do you want to add these products in bags");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            moduleProductDetails.addToBag(stockList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
              alertDialog.setNegativeButton("No", null);
                /*alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                })*/
                alertDialog.show();


            }
        }
        else
        {
            Toast.makeText(this,"Please enter valid quantity ...  ",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validation()
    {
        int valid;

        for(int i=0;i<moduleProductDetails.stockList.size();i++)
        {
            ProductStockBean stock=moduleProductDetails.stockList.get(i);
            View view=sizeView.get(i);
            EditText ed=(EditText)view.findViewById(R.id.edOrderQnty);
            StockMasterBean s=new StockMasterBean();
            String val=ed.getText().toString();
            if(val.equals("") || val.equals("0"))
            {
                int c=0;
            }
            else if(stock.getQnty()<Integer.parseInt(val))
            {
                return false;
            }
            else
            {
                s.setProductId(bean.getProductId());
                s.setInBagQty(Integer.parseInt(val));
                s.setSizeId(stock.getSizeId());
                s.setTransactionType("inbag");
                s.setDeleteStatus("false");
                s.setUserId(UserUtilities.getUserId(this));
                s.setChangedBY(UserUtilities.getUserId(this));
                s.setEnterBy(UserUtilities.getUserId(this));
                s.setClientId(UserUtilities.getClientId(this));
                stockList.add(s);
            }

        }

        return true;
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
                btnplus.setVisibility(View.VISIBLE);

            }
        }
    }
    //---------------------------------------------------------------------------------




    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1 && responseCode==200)
        {
              // Toast.makeText(this,""+str,Toast.LENGTH_LONG).show();
                addSizeView();
                addStripCodeProducts();
        }

        else if(requestCode==2 && responseCode==200)
        {
            if(str.equals("Success"))
            {
                Toast.makeText(getApplicationContext(),"Products Succesfully added to bag ",Toast.LENGTH_LONG).show();
               // finish();
                callRefresh();
            }
            else if(str.equals("Failed"))
            {

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage("Required quantity not available in stock ..Try Again !!");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callRefresh();
                    }
                });

            }
        }

    }


    //------------------------------   Add Size and Available qnty and -------------------


    ArrayList<View>  sizeView=new ArrayList<>();
    void addSizeView()
    {


        sizeView.clear();
        for(int i=0;i<moduleProductDetails.stockList.size();i++)
        {
            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v= inflater.inflate(R.layout.itemaddsizetoproductdetail, null);
            v.setTag(i);
            TextView txtSizeName=(TextView)v.findViewById(R.id.txtSizeName);
            TextView txtAvaibleQnt=(TextView)v.findViewById(R.id.txtAvaibleQnt);
            EditText edOrderQnty=(EditText)v.findViewById(R.id.edOrderQnty);
            txtSizeName.setText(moduleProductDetails.getSizeNameBySizeId(moduleProductDetails.stockList.get(i).getSizeId()));
            txtAvaibleQnt.setText(moduleProductDetails.stockList.get(i).getQnty() + "");

            sizeView.add(v);
            if( moduleProductDetails.stockList.get(i).getQnty()!=0)
            {
                edOrderQnty.setText(""+1);
            }
            linearSizeMain.addView(v);
        }

    }


    void addStripCodeProducts()
    {

        if(moduleProductDetails.stripCodeProducts!=null)
        {
            for(int i=0;i<moduleProductDetails.stripCodeProducts.size();i++)
            {
                LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v= inflater.inflate(R.layout.itemimageview, null);
                ImageView im=(ImageView)v.findViewById(R.id.im1);
                imageLoader.displayImage(
                        moduleProductDetails.stripCodeProducts.get(i).getIconThumb(), im, doption, animateFirstListener);
                v.setTag(i);
                if(bean.getProductId()!=  moduleProductDetails.stripCodeProducts.get(i).getProductId())
                         lhorizontal.addView(v);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(ProductDetailView.this, ProductDetailView.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(moduleProductDetails.stripCodeProducts.get(
                                Integer.parseInt((v.getTag().toString()))));
                        finish();
                        intent.putExtra("myjson",myJson);
                        startActivity(intent);
                    }
                });

            }
        }

    }

    void callRefresh()
    {
        Intent intent=new Intent(ProductDetailView.this, ProductDetailView.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(bean);
        finish();
        intent.putExtra("myjson",myJson);
        startActivity(intent);
    }

}
