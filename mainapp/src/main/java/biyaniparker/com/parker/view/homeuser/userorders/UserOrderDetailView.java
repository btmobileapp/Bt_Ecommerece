package biyaniparker.com.parker.view.homeuser.userorders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import biyaniparker.com.parker.bal.ModuleOrder;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.view.homeuser.productdshopping.ProductDetailView;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;
import biyaniparker.com.parker.view.reports.PrintOrderSummary;

public class UserOrderDetailView extends AppCompatActivity {

    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    TextView txtShopName, txtAddress, txtGAmount, txtOrderNo, txtOrderDate;
    Button btnDispatch, btnDelete;
    LinearLayout linear;

    ModuleOrder moduleOrder;

    OrderMasterBean bean;
    ArrayList<OrderDetailBean> orderDetails=new ArrayList<>();
    ArrayList<OrderDetailBean> orderDetailsNew=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_user_order_details);

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


        initUi();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" Order details");

        Intent intent=getIntent();
        int orderId=intent.getIntExtra("OrderId", 0);
        bean=moduleOrder.getOrderBeanById(orderId);


        txtAddress.setText(bean.getAddress());
        txtShopName.setText(bean.getShopName());
        txtOrderNo.setText("Or.No : " + String.valueOf(bean.getOrderId()));
        txtOrderDate.setText(CommonUtilities.longToDate(bean.getOrderDate()));
        try
        {
            txtGAmount.setText("Total Amt : " + (int) Double.parseDouble(bean.getTotolAmount())+ " Rs");
        }
        catch (Exception e)
        {}


        orderDetails.addAll(moduleOrder.getOrderDetailsById(orderId));
        //CommonUtilities.alert(this,orderDetails.size()+"");
        LayoutInflater inflater= (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        for(int i=0;i<orderDetails.size();i++)
        {
            int flag=0;
            for(int j=0;j<orderDetailsNew.size();j++)
            {
                if(orderDetailsNew.get(j).productId==orderDetails.get(i).productId)
                {
                    flag=1;
                    break;
                }

            }
            if(flag==0)
            {
                orderDetailsNew.add(orderDetails.get(i));
            }
        }
        int pid=0;

        for(int i=0;i<orderDetailsNew.size();i++)
        {
            final OrderDetailBean orderD=orderDetailsNew.get(i);
            pid=(i==0)?orderD.productId:0;
            View v=inflater.inflate(R.layout.o_item_order_details,null);

            ImageView image= (ImageView) v.findViewById(R.id.img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(UserOrderDetailView.this,ViewProductImage.class);
                    intent.putExtra("path", orderD.getIconThumb());
                    startActivity(intent);
                }
            });
            TextView pName= (TextView) v.findViewById(R.id.txtName);
            TextView cPrice= (TextView) v.findViewById(R.id.txtCPrice);
            TextView tPrice= (TextView) v.findViewById(R.id.txtTPrice);
            LinearLayout l = (LinearLayout) v.findViewById(R.id.linear);
            double total=0;


            pName.setText(orderD.getProductName());
            cPrice.setText("" + (int) Double.parseDouble(orderD.getConsumerPrice()) + ". Rs");


            imageLoader = ImageLoader.getInstance();
            //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
            imageLoader.displayImage(
                    orderD.getIconThumb()
                    ,
                    image, doption, animateFirstListener);

            for(int c=i;c<orderDetails.size();c++)
            {
                if(orderD.getProductId()==orderDetails.get(c).getProductId())
                {

                    View sub = inflater.inflate(R.layout.o_activity_two_textviews, null);

                    TextView size = (TextView) sub.findViewById(R.id.txtsize);
                    TextView qnty = (TextView) sub.findViewById(R.id.txtsqnty);

                    total = total + (orderDetails.get(c).getQuantity() * (Double.parseDouble(orderDetails.get(c).getDealerPrice())));
                    size.setText(orderDetails.get(c).getSizeName());
                    qnty.setText(String.valueOf(orderDetails.get(c).getQuantity()));
                    l.addView(sub);
                }
            }


            tPrice.setText("Total :" + (int)total + " Rs");


            linear.addView(v);

        }


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintOrderSummary p=new PrintOrderSummary(UserOrderDetailView.this,imageLoader);
                p.orderDetails=orderDetails;
                p.master=bean;
                p.call();
            }
        });

    }


    private void initUi()
    {
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtShopName=(TextView)findViewById(R.id.txtShopName);
        txtGAmount=(TextView)findViewById(R.id.txtGAmount);
        txtOrderNo=(TextView)findViewById(R.id.txtOrderNo);
        txtOrderDate=(TextView)findViewById(R.id.txtOrderDate);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnDispatch=(Button)findViewById(R.id.btnDispatch);
        linear=(LinearLayout)findViewById(R.id.linearScroll);

        moduleOrder=new ModuleOrder(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

}
