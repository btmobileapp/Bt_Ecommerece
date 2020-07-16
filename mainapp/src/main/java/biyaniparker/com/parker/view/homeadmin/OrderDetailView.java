package biyaniparker.com.parker.view.homeadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.LinkAddress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleOrder;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.OrderDetailAdapter;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.OrderDispatchView;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

public class OrderDetailView extends AppCompatActivity implements View.OnClickListener, DownloadUtility {


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    TextView txtShopName, txtAddress, txtGAmount, txtOrderNo, txtOrderDate, txtTQnty;
    Button btnDispatch, btnDelete;
    OrderDetailAdapter adapter;
    //LinearLayout linear;
    ListView listView;

    ModuleOrder moduleOrder;
    ModuleProduct moduleProduct;
    ModuleCategory moduleCategory;

    OrderMasterBean bean;
    ArrayList<OrderDetailBean> orderDetails=new ArrayList<>();
    ArrayList<OrderDetailBean> orderDetailsNew=new ArrayList<>();
    int totalQnty=0;
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_order_details);

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

        Intent intent =getIntent();
        orderId=intent.getIntExtra("OrderId", 0);
        bean=moduleOrder.getOrderBeanById(orderId);


        txtAddress.setText(bean.getAddress());
        txtShopName.setText(bean.getShopName());
        txtOrderNo.setText("Or.No : " + String.valueOf(bean.getOrderId()));
        txtOrderDate.setText(CommonUtilities.longToDate(bean.getOrderDate()));
        txtGAmount.setText("Total Amt : " + (int) Double.parseDouble(bean.getTotolAmount()) + " Rs");


      /*  new Thread(new Runnable() {
            @Override
            public void run()
            {
                calculateNewView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addViewHere();
                    }
                });
            }
        }).start();
*/


        calculateNewView();

        adapter=new OrderDetailAdapter(this,1,orderDetailsNew,orderDetails);

        listView.setAdapter(adapter);


        txtTQnty.setText("Total Qnty : "+totalQnty);



        registerReceiver(mMessageReceiver, new IntentFilter("CloseMe"));

    }

    void calculateNewView()
    {
        orderDetails.addAll(moduleOrder.getOrderDetailsById(orderId));
        for(int i=0;i<orderDetails.size();i++)
        {
            totalQnty=totalQnty+orderDetails.get(i).quantity;
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
    }


    /*void addViewHere()
    {


        //CommonUtilities.alert(this,orderDetails.size()+"");
        LayoutInflater inflater= (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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
                    Intent intent=new Intent(OrderDetailView.this,ViewProductImage.class);
                    intent.putExtra("path", orderD.getIconThumb());
                    startActivity(intent);
                }
            });
            TextView pName= (TextView) v.findViewById(R.id.txtName);
            TextView cPrice= (TextView) v.findViewById(R.id.txtCPrice);
            TextView tPrice= (TextView) v.findViewById(R.id.txtTPrice);
            TextView tStrip= (TextView) v.findViewById(R.id.txtStripCode);
            LinearLayout l = (LinearLayout) v.findViewById(R.id.linear);
            double total=0;


            pName.setText(orderD.getProductName());
            cPrice.setText("" + (int) Double.parseDouble(orderD.getConsumerPrice()) + "  Rs");


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

                    totalQnty+=orderDetails.get(c).getQuantity();
                    total = total + (orderDetails.get(c).getQuantity() * (Double.parseDouble(orderDetails.get(c).getDealerPrice())));
                    size.setText(orderDetails.get(c).getSizeName());
                    qnty.setText(String.valueOf(orderDetails.get(c).getQuantity()));
                    l.addView(sub);
                }
            }


            //tPrice.setText("Total :" + (int)total + " Rs");
            tPrice.setText(getCategoryName(orderD.getProductId()));
            tStrip.setText("Strip code :"+moduleProduct.getProductBeanByProductId(orderD.getProductId()).getStripCode());
            linear.addView(v);
        }
        txtTQnty.setText("Total Qnty : "+totalQnty);
    }*/

    private String getCategoryName(int productId)
    {
        return moduleCategory.getCategoryName((moduleProduct.getProductBeanByProductId(productId)).getCategoryId());
    }

    private void initUi()
    {
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtShopName=(TextView)findViewById(R.id.txtShopName);
        txtGAmount=(TextView)findViewById(R.id.txtGAmount);
        txtOrderNo=(TextView)findViewById(R.id.txtOrderNo);
        txtOrderDate=(TextView)findViewById(R.id.txtOrderDate);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        txtTQnty=(TextView)findViewById(R.id.txtTQnty);
        btnDispatch=(Button)findViewById(R.id.btnDispatch);
        btnDispatch.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listView);


        moduleOrder=new ModuleOrder(this);
        moduleCategory=new ModuleCategory(this);
        moduleProduct =new ModuleProduct(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)   // button click
    {
        if(v.getId()==btnDispatch.getId())
        {
            Toast.makeText(this," Plz wait ...",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, OrderDispatchView.class);
            intent.putExtra("OrderId",bean.getOrderId());
            startActivity(intent);
        }
        else if(v.getId()==btnDelete.getId())
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage(" Are you sure to delete ?  ");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try
                    {
                        moduleOrder.deleteOrder(bean);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
            if(requestCode==2 && responseCode==200) {
                if (str.equals("Success")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle(getString(R.string.app_name));
                    alertDialog.setMessage(" Order Successfully Deleted ");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(OrderDetailView.this, AdminHomeScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
                else
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle(getString(R.string.app_name));
                    alertDialog.setMessage("Order delete failed .. Try Again.. " + responseCode);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(OrderDetailView.this, AdminHomeScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
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
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }


    //Intent in=new Intent();
    //in.setAction("RefreshMe");
    //sendBroadcast(in);
    //registerReceiver(mMessageReceiver, new IntentFilter("CloseMe"));
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            try {
                // String str = intent.getStringExtra("URL");
               // Toast.makeText(OrderDetailView.this,"Order Detail View  Recieved",Toast.LENGTH_LONG).show();
                finish();
            }
            catch (Exception e)
            {}
        }
    };
    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(mMessageReceiver);
    }
}
