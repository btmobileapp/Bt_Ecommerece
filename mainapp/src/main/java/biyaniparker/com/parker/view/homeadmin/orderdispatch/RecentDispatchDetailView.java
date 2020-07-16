package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;
import biyaniparker.com.parker.view.reports.PrintDeliverChallanFullReport;
import biyaniparker.com.parker.view.reports.PrintDeliverChallanReport;

public class RecentDispatchDetailView extends AppCompatActivity {

    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    ArrayList<DispatchDetailBean> details;

    TextView txtShopname, txtOrderNo, txtAddress, txtOrDate, txtUnDisQnty, txtTQnty;
    LinearLayout linear;
    ModuleDispatch moduleDispatch;
    ModuleProduct moduleProduct;
    LinearLayout mainlinearDetails;
    DispatchMasterAndDetails bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_recent_dispatch);

        ArrayList<DispatchDetailBean> sortedDetailList;


        getSupportActionBar().setTitle("Dispatch Detail ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        mainlinearDetails=(LinearLayout)findViewById(R.id.linearDetails);


        moduleDispatch=new ModuleDispatch(this);
        moduleProduct=new ModuleProduct(this);
        details = new ArrayList<>();
        sortedDetailList = new ArrayList<>();
        txtShopname = (TextView) findViewById(R.id.txtShopName);
        txtOrderNo = (TextView) findViewById(R.id.txtOrNo);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtOrDate = (TextView) findViewById(R.id.txtdate);
        txtUnDisQnty = (TextView) findViewById(R.id.txtUndisp);
        txtTQnty=(TextView)findViewById(R.id.txtTotal);
        linear = (LinearLayout) findViewById(R.id.linearDetails);



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




        Gson gson = new Gson();                                       // gets the partial dispatch  details
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        bean= gson.fromJson(str, DispatchMasterAndDetails.class);
        // Toast.makeText(this, " Bean" + bean.master.dispatchId, Toast.LENGTH_LONG).show();
        getSupportActionBar().setSubtitle("Dispatch Id "+bean.master.getDispatchId());
        details.clear();
        details.addAll(bean.details);


        txtShopname.setText(bean.master.shopName);
        txtOrderNo.setText("Order No : " + bean.master.orderId);
        txtAddress.setText(bean.master.address);
        txtOrDate.setText("" + CommonUtilities.longToDate(bean.master.dispatchDate));

        int total = 0;
        int totaldipatched=0;
        for (int i = 0; i < bean.details.size(); i++)                                 // counts the number of total updispatched quantity
        {
            DispatchDetailBean detailBean = bean.details.get(i);
            total += detailBean.orderQnty - detailBean.quantity;
            totaldipatched=totaldipatched+detailBean.quantity;
        }

        txtUnDisQnty.setText("Undispatch Qnty : " + total);

        txtTQnty.setText("Dispatch Qnty : "+totaldipatched);

        //   code to inflate layout

        //sortng

        for (int i = 0; i < details.size(); i++)
        {
            DispatchDetailBean detailBean = details.get(i);
            int flag = 0;
            for (int j = 0; j < sortedDetailList.size(); j++)
            {
                if (detailBean.getProductId() == sortedDetailList.get(j).getProductId())
                {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0)
            {
                sortedDetailList.add(detailBean);
            }
        }


        // inflate


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        for (int i = 0; i < sortedDetailList.size(); i++)
        {
            DispatchDetailBean detailBean = sortedDetailList.get(i);
            final ProductBean prodBean=moduleProduct.getProductBeanByProductId(detailBean.getProductId());
            View convertView = inflater.inflate(R.layout.o_item_recent_dispatch_details, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(RecentDispatchDetailView.this,ViewProductImage.class);
                    intent.putExtra("path", prodBean.getIconThumb());
                    startActivity(intent);
                }
            });
            TextView txtPName = (TextView) convertView.findViewById(R.id.txtName);
            TextView txtPrice = (TextView) convertView.findViewById(R.id.txtCPrice);
            TextView txtTPrice = (TextView) convertView.findViewById(R.id.txtTPrice);
            LinearLayout linear = (LinearLayout) convertView.findViewById(R.id.linear);


            txtPName.setText(detailBean.getProductName());
            txtPrice.setText(detailBean.getConsumerPrice() + " Rs");
            //holder.txtTPrice.setText(detailBean.get);
            int totalQnty = 0;
            for (int j = 0; j < details.size(); j++)
            {
                if (detailBean.getProductId() == details.get(j).getProductId()) {
                    View sizeView = inflater.inflate(R.layout.o_item_recent_size_details, null);
                    TextView txtSizeName = (TextView) sizeView.findViewById(R.id.txtsize);
                    TextView txtQntye = (TextView) sizeView.findViewById(R.id.txtsqnty);

                    totalQnty++;
                    txtSizeName.setText(details.get(j).getSizeName());
                    txtQntye.setText(details.get(j).getQuantity() + "");
                    linear.addView(sizeView);

                }
                //linear.addView(linear);
            }
            txtTPrice.setText("Total : " + (totalQnty * Integer.parseInt(detailBean.getConsumerPrice())));
            mainlinearDetails.addView(convertView);



            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(
                    prodBean.getIconThumb()
                    ,
                    img, doption, animateFirstListener);


        }



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate();
            }
        });

    }

    ProgressDialog pd;
    void generate()
    {
        pd=new ProgressDialog(this);
        pd.setMessage("Pls Wait...");
        pd.show();


      new Thread(new Runnable() {
          @Override
          public void run() {

              Logic m=new Logic();

              ArrayList<DispatchDetailBean> cloneList=new ArrayList<DispatchDetailBean>(details.size());
               for(DispatchDetailBean dBean :details)
               {
                   try {
                       cloneList.add((DispatchDetailBean)dBean.clone());
                   }
                   catch (Exception e){}

               }

              m.list=cloneList;

             // m.rowsToColumn(RecentDispatchDetailView.this);
              m.getSummary(RecentDispatchDetailView.this);



              m.rowsToColumn(RecentDispatchDetailView.this);


              m.mapDeliveryChallanTOParentCategories(RecentDispatchDetailView.this);



              PrintDeliverChallanReport report=new PrintDeliverChallanReport(RecentDispatchDetailView.this,imageLoader);
              report.setDeliverryChallanDataSet(m.map);
              report.setMaster(bean.master);
              report.call();

              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      pd.dismiss();
                  }
              });

          }
      }).start();




        /*
        PrintDeliverChallanReport report=new PrintDeliverChallanReport(this,imageLoader);
        report.setDeliverryChallanDataSet(m.map);
        report.setMaster(bean.master);
        report.call();
        */

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
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
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

     String summarystr;
    String detailstr;
    void printSummary()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtilities.alert(RecentDispatchDetailView.this, summarystr);
            }
        });

    }
}
