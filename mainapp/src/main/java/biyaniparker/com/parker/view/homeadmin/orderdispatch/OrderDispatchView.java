package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.bal.ModuleOrder;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleSizeMaster;
import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterBean;
import biyaniparker.com.parker.beans.DispatchSummaryBean;
import biyaniparker.com.parker.beans.GsonDispatchSummary;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.view.adapter.OrderDispatchDetailAdapter;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

public class OrderDispatchView extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    String productsIds="";
    PopupWindow popUp;
    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    ModuleCategory moduleCategory;
    ModuleProduct moduleProduct;
    ModulePrice modulePrice;
    ModuleSizeMaster moduleSizeMaster;

    OrderDispatchDetailAdapter adapter;

    TextView txtShopName, txtAddress, txtGAmount, txtOrderNo, txtOrderDate;
    Button btnSummary;
    //LinearLayout linear;
    ListView listView;
    ArrayList<View> viewList;
    ModuleOrder moduleOrder;
    ModuleDispatch moduleDispatch;

    OrderMasterBean bean;
    ArrayList<OrderDetailBean> orderDetails=new ArrayList<>();
    ArrayList<OrderDetailBean> orderDetailsNew=new ArrayList<>();
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_order_dispatch_details);
        try
        {
      //  registerReceiver(mMessageReceiver, new IntentFilter("CloseMe"));
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

        moduleCategory=new ModuleCategory(this);
        moduleProduct=new ModuleProduct(this);
        modulePrice=new ModulePrice(this);
        moduleSizeMaster=new ModuleSizeMaster(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewList.clear();
        Intent intent=getIntent();
         orderId=intent.getIntExtra("OrderId", 0);
        bean=moduleOrder.getOrderBeanById(orderId);


        txtAddress.setText(bean.getAddress());
        txtShopName.setText(bean.getShopName());
        txtOrderNo.setText("Or.No : " + String.valueOf(bean.getOrderId()));
        txtOrderDate.setText(CommonUtilities.longToDate(bean.getOrderDate()));
        txtGAmount.setText("Total Amt : " + (int) Double.parseDouble(bean.getTotolAmount()) + " Rs");


        getSupportActionBar().setTitle(bean.getShopName());
        getSupportActionBar().setSubtitle("Or.No : " + String.valueOf(bean.getOrderId() + "         Or.Date : " + CommonUtilities.longToDate(bean.getOrderDate())));


        //CommonUtilities.alert(this,orderDetails.size()+"");

        CommonUtilities.hideatInItInputBoard(this);
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculateNewView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addViewHere();
                    }
                });
            }
        }).start();;*/

        calculateNewView();



            adapter = new OrderDispatchDetailAdapter(this, 1, orderDetailsNew, orderDetails);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }
        catch (Exception e)
        {
            CommonUtilities.alert(this,"WR:"+e.toString());
        }




    }
    void calculateNewView()
    {
        orderDetails.addAll(moduleOrder.getOrderDetailsById(orderId));

        for(int i=0;i<orderDetails.size();i++)
        {
            int flag=0;
            orderDetails.get(i).setEnteredQnty(orderDetails.get(i).getQuantity());
            for(int j=0;j<orderDetailsNew.size();j++)         // sorted ordered details in orderDetails list by non-repeating(productId)
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


    private String getCategoryName(int productId) {
        return moduleCategory.getCategoryName((moduleProduct.getProductBeanByProductId(productId)).getCategoryId());
    }

    private void initUi()
    {
        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txtShopName=(TextView)findViewById(R.id.txtShopName);
        txtGAmount=(TextView)findViewById(R.id.txtGAmount);
        txtOrderNo=(TextView)findViewById(R.id.txtOrderNo);
        txtOrderDate=(TextView)findViewById(R.id.txtOrderDate);
        btnSummary=(Button)findViewById(R.id.btnDispachSummary);
        btnSummary.setOnClickListener(this);
        viewList=new ArrayList<View>();
        listView=(ListView)findViewById(R.id.listView);
        moduleDispatch=new ModuleDispatch(this);
        moduleOrder=new ModuleOrder(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {
            CommonUtilities.hideSoftKeyBord(this);

            DispatchMasterBean dispatchMasterBean = new DispatchMasterBean();
            dispatchMasterBean.setOrderId(bean.getOrderId());
            dispatchMasterBean.setUserId(UserUtilities.getUserId(this));
            dispatchMasterBean.setTotolAmount(Double.parseDouble(bean.getTotolAmount()));
            dispatchMasterBean.setCustomerId(bean.getUserId());
            dispatchMasterBean.dispatchDetails = new ArrayList<>();


            for (int i = 0; i < orderDetails.size(); i++)
            {
                DispatchDetailBean detailBean = new DispatchDetailBean();
                OrderDetailBean orderDetailBean = orderDetails.get(i);

                    detailBean.setActualQnty(orderDetailBean.getEnteredQnty());
                    detailBean.setQuantity(orderDetailBean.getQuantity());

                detailBean.setOrderDetailId(orderDetailBean.getOrderDetailId());
                detailBean.setProductId(orderDetailBean.getProductId());
                detailBean.setSizeId(orderDetailBean.getSizeId());
                dispatchMasterBean.dispatchDetails.add(detailBean);
            }

            try
            {

                ArrayList<DispatchSummaryBean>  summaryBeanList=new ArrayList<>();
               // dispatchMasterBean.dispatchDetails.get(0).getProductId();
                String productsIds="";
                for(int i=0;i<dispatchMasterBean.dispatchDetails.size();i++)
                {
                    if(productsIds.equalsIgnoreCase(""))
                        productsIds=  dispatchMasterBean.dispatchDetails.get(i).getProductId()+"";
                    else
                        productsIds=productsIds+","+ dispatchMasterBean.dispatchDetails.get(i).getProductId()+"";
                }


                moduleProduct.setProductBuffer(productsIds);
                moduleProduct.setPriceBuffer();
                moduleProduct.setCategoryBuffer();
                moduleProduct.setSizeBuffer();

                for(int i=0;i<dispatchMasterBean.dispatchDetails.size();i++)
                {
                    summaryBeanList.add(convertToSummaryBean(dispatchMasterBean.dispatchDetails.get(i)));
                }


                GsonDispatchSummary gsonDispatchSummary=new GsonDispatchSummary();
                gsonDispatchSummary.dispatchSummaryList=new ArrayList<>();
                gsonDispatchSummary.dispatchSummaryList.addAll(summaryBeanList);

                gsonDispatchSummary.masterBean=dispatchMasterBean;

                Gson gson=new Gson();
                String strGson=gson.toJson(gsonDispatchSummary);
                Intent intent=new Intent(this,DispatchSummaryView.class);
                intent.putExtra("str", strGson);
                startActivity(intent);




            }
            catch (ArithmeticException e)
            {
                Toast.makeText(this,""+e.toString(),Toast.LENGTH_SHORT).show();
            }

    }


    // converts Dispach details model to Dispatch Summary model
    private DispatchSummaryBean convertToSummaryBean(DispatchDetailBean bean)
    {

        ProductBean pBean= moduleProduct.productMapping.get(bean.getProductId());
                //moduleProduct.getProductBeanByProductId(bean.getProductId());
        DispatchSummaryBean ds=new DispatchSummaryBean();

        /*
        ds.setCatName(moduleCategory.getCategoryName(pBean.getCategoryId()));
        ds.setQuantity(bean.getActualQnty());
        ds.setPrice(modulePrice.getPriceByPriceId(pBean.getPriceId()));
        ds.setSizeName(moduleSizeMaster.getSizeMasterBean(bean.getSizeId()).getSizeName());*/

        ds.setCatName( moduleProduct.categoryMapping.get(pBean.categoryId).categoryName);

        ds.setQuantity(bean.getActualQnty());

        ds.setPrice(moduleProduct.priceMapping.get(pBean.getPriceId()).consumerPrice   )   ;
        ds.setSizeName(moduleProduct.sizeMapping.get(bean.getSizeId()).sizeName);

        return ds;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id)
    {

       if(popUp!=null && popUp.isShowing())
           return ;

        if(popUp!=null )
            popUp.dismiss();


        final OrderDetailBean bean=orderDetailsNew.get(position);

         final List<View> vList=new ArrayList<>();
        LayoutInflater inflater= (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View mLayout=inflater.inflate(R.layout.item_popup_dis,null );
        final Button mCancel= (Button) mLayout.findViewById(R.id.btnCancel);
        Button mSave= (Button) mLayout.findViewById(R.id.btnSave);
        TextView pName = (TextView) mLayout.findViewById(R.id.txtName);
        TextView cPrice = (TextView) mLayout.findViewById(R.id.txtCPrice);
        TextView tPrice = (TextView) mLayout.findViewById(R.id.txtTPrice);
        TextView tStrip = (TextView) mLayout.findViewById(R.id.txtStripCode);
        ImageView image = (ImageView) mLayout.findViewById(R.id.img);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDispatchView.this,ViewProductImage.class);
                intent.putExtra("path", bean.getIconThumb());
                startActivity(intent);

            }
        });

        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(
                bean.getIconThumb()
                ,
                image, doption, animateFirstListener);


        pName.setText(bean.getProductName());
        cPrice.setText("" + (int) Double.parseDouble(bean.getConsumerPrice()) + "  Rs");
        tPrice.setText(getCategoryName(bean.getProductId()));
        tStrip.setText("Strip code :" + moduleProduct.getProductBeanByProductId(bean.getProductId()).getStripCode());

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation(vList)) {
                    updateBean(bean, vList);
                    adapter.notifyDataSetChanged();
                    popUp.dismiss();
                } else {
                    Toast.makeText(OrderDispatchView.this, "Please enter valid qnty", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearLayout layout=(LinearLayout)mLayout.findViewById(R.id.linear);

        mCancel.setTag(popUp);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ///CommonUtilities.alert(OrderDispatchView.this,"Hiiii");

                popUp.dismiss();
                try {
                 PopupWindow obj= (PopupWindow) mCancel.getTag();
                    obj.dismiss();
                }
                catch (Exception e){}
            }
        });


        for(int i=0;i<orderDetails.size();i++)
        {
            OrderDetailBean main=orderDetails.get(i);
            if(bean.getProductId()==main.getProductId())
            {
                View popUpView = inflater.inflate(R.layout.item_popup_first, null);

                TextView size = (TextView) popUpView.findViewById(R.id.txtsize);
                TextView qnty = (TextView) popUpView.findViewById(R.id.txtsqnty);
                EditText edDispatchQnty = (EditText) popUpView.findViewById(R.id.edDQnty);


                edDispatchQnty.setText(""+main.getEnteredQnty());


                size.setText(""+main.getSizeName());
                qnty.setText(""+main.getQuantity());
                layout.addView(popUpView);
                vList.add(popUpView);
            }
        }

        popUp=new PopupWindow(mLayout, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        popUp.setFocusable(true);
        popUp.showAtLocation(mLayout, Gravity.CENTER, 10, 10);

    }

    private void updateBean(OrderDetailBean bean, List<View> vList)
    {
        for(int i=0,j=0;i<orderDetails.size();i++)
        {
            OrderDetailBean main=orderDetails.get(i);

            if(bean.getProductId()==main.getProductId())
            {
                View popUpView=vList.get(j);
                j++;

                TextView size = (TextView) popUpView.findViewById(R.id.txtsize);
                TextView qnty = (TextView) popUpView.findViewById(R.id.txtsqnty);
                EditText edDispatchQnty = (EditText) popUpView.findViewById(R.id.edDQnty);
                if(edDispatchQnty.getText().toString().equals(""))
                {
                    main.setEnteredQnty(0);
                }
                else
                {
                    main.setEnteredQnty(Integer.parseInt(edDispatchQnty.getText().toString()));
                }

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



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            try {
               // String str = intent.getStringExtra("URL");

             //   Toast.makeText(OrderDispatchView.this,"Order Dispatch View  Recieved",Toast.LENGTH_LONG).show();
               finish();
            }
            catch (Exception e)
            {}
        }
    };
    @Override
    public void finish() {
        super.finish();
        //unregisterReceiver(mMessageReceiver);
    }



    private boolean checkValidation(List<View> vList)
    {
        int noneSelect=0;                       // flag to chech wether at least one product is selected
        for(int i=0;i<vList.size();i++) {
            View view = vList.get(i);
            EditText edDispatchQnty = (EditText) view.findViewById(R.id.edDQnty);
            TextView txtQNty = (TextView) view.findViewById(R.id.txtsqnty);

            try {

                if (edDispatchQnty.getText().toString().equals("") || Integer.parseInt(edDispatchQnty.getText().toString()) == 0)
                {
                    int y = 0;        //to skip
                }
                else if (Integer.parseInt(edDispatchQnty.getText().toString()) > Integer.parseInt(txtQNty.getText().toString()))
                {
                    return false;
                }

            }

            catch (NumberFormatException e)
            {
                Toast.makeText(this," Please enter valid number",Toast.LENGTH_SHORT ).show();
                return false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }

        return true;
    }


}
