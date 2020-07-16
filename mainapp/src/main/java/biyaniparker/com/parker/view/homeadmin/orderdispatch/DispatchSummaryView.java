package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.DispatchSummaryBean;
import biyaniparker.com.parker.beans.GsonDispatchSummary;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.homeadmin.AdminHomeScreen;
import biyaniparker.com.parker.view.reports.PrintDeliverChallanReport;

public class DispatchSummaryView extends AppCompatActivity implements View.OnClickListener, DownloadUtility {

    ArrayList<DispatchSummaryBean> dispatchList;
    ArrayList<DispatchSummaryBean> dispatchSortedList=new ArrayList<>();

    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    LinearLayout linear;
    Button btnConfirm;
    ModuleDispatch moduleDispatch;
    GsonDispatchSummary gsonDispatchSummary;
    TextView txtTQnty;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_item_dispatch_summary);
        linear=(LinearLayout)findViewById(R.id.lSum);
        btnConfirm=(Button)findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        txtTQnty=(TextView)findViewById(R.id.txtTQnty);




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


        getSupportActionBar().setTitle("Dispatch Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        moduleDispatch=new ModuleDispatch(this);
        dispatchList=new ArrayList<>();
        gsonDispatchSummary=new GsonDispatchSummary();
        Intent intent=getIntent();
        String str=intent.getExtras().getString("str", "");
        Gson gson=new Gson();
        gsonDispatchSummary=gson.fromJson(str, GsonDispatchSummary.class);
        dispatchList.addAll(gsonDispatchSummary.dispatchSummaryList);






        for(int k=0;k<dispatchList.size();k++)
        {
            int position=0;
            int flag=0;
            DispatchSummaryBean oldSummary = dispatchList.get(k);

            for (int i = 0; i < dispatchSortedList.size(); i++)
            {
                DispatchSummaryBean newSummary = dispatchSortedList.get(i);


                if (oldSummary.catName.equals(newSummary.catName))
                {


                    if (Float.parseFloat(oldSummary.getPrice()+"") == Float.parseFloat( newSummary.getPrice()+""))
                    {
                        if (oldSummary.getSizeName().equals(newSummary.getSizeName()))
                        {
                            flag = 1;
                            position = i;
                        }
                    }
                }
            }

            if (flag == 0)
            {
                dispatchSortedList.add(oldSummary);
            }
            else
            {
                DispatchSummaryBean temp=dispatchSortedList.get(position);
                temp.setQuantity(temp.getQuantity()+dispatchList.get(k).getQuantity());
            }

        }





        LayoutInflater inflater= (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int qntyCount=0;
        for(int i=0;i<dispatchSortedList.size();i++)
        {
            DispatchSummaryBean summaryBean=dispatchSortedList.get(i);

            View v=inflater.inflate(R.layout.o_item_dispatch_summary_details,null);

            TextView txtCatName, txtPrice, txtSize, txtQnty;

            txtCatName=(TextView)v.findViewById(R.id.catName);
            txtPrice=(TextView)v.findViewById(R.id.Price);
            txtSize=(TextView)v.findViewById(R.id.Size);
            txtQnty=(TextView)v.findViewById(R.id.Qnatity);

            txtCatName.setText(summaryBean.getCatName());
            txtPrice.setText(summaryBean.getPrice()+"");
            txtSize.setText(summaryBean.getSizeName());
            txtQnty.setText(""+summaryBean.getQuantity());
            qntyCount+=summaryBean.getQuantity();
            linear.addView(v);
            txtTQnty.setText("    "+qntyCount);
        }





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
        if(btnConfirm.getId()==v.getId())
        {
            try
            {
               moduleDispatch.dispatchOrder(gsonDispatchSummary.masterBean);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1 && responseCode==200)
        {
            if(!str.equals("failed")) {

                Intent in = new Intent();
                in.setAction("RefreshMe");
                sendBroadcast(in);

                Intent in1 = new Intent();
                in1.setAction("CloseMe");
                sendBroadcast(in1);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage(" Order Successfully Dispatched.. Do you want Pdf report ?  ");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                  /*  Intent intent = new Intent(DispatchSummaryView.this, AdminHomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                   /* PrintDeliverChallanReport printDeliverChallanReport=new PrintDeliverChallanReport(DispatchSummaryView.this,imageLoader);
                    printDeliverChallanReport.setMaster(moduleDispatch.gsonDispatch.master.toDispatchMasterBean());
                    printDeliverChallanReport.setDeliverryChallanDataSet();*/
                        finish();

                        DispatchMasterAndDetails bean = new DispatchMasterAndDetails();

                        bean.master = moduleDispatch.gsonDispatch.master.toDispatchMasterBean();
                        bean.details = new ArrayList<DispatchDetailBean>();

                        for (int i = 0; i < moduleDispatch.gsonDispatch.list.size(); i++) {
                            bean.details.add(moduleDispatch.gsonDispatch.list.get(i).toDispatchDetailBean());
                        }

                        Gson gson = new Gson();
                        String str = gson.toJson(bean);
                        Intent intent = new Intent(DispatchSummaryView.this, RecentDispatchDetailView.class);
                        intent.putExtra("str", str);
                        startActivity(intent);


                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        Intent i = new Intent(DispatchSummaryView.this, AdminHomeScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);


                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                        Intent i = new Intent(DispatchSummaryView.this, AdminHomeScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                alertDialog.show();
            }
            else
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage("Failed... Same Order already dispatched. ");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DispatchSummaryView.this, AdminHomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
       }

       else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Dispatch failed .. Try Again.. " + responseCode);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DispatchSummaryView.this, AdminHomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }

    }

    private void innerFunction()
    {/*
        for(int i=0;i<moduleDispatch.gsonDispatch)

            Logic m=new Logic();
        m.list=details;
        m.getSummary(RecentDispatchDetailView.this);


        m.rowsToColumn(RecentDispatchDetailView.this);
        m.mapDeliveryChallanTOParentCategories(RecentDispatchDetailView.this);



        PrintDeliverChallanReport report=new PrintDeliverChallanReport(RecentDispatchDetailView.this,imageLoader);
        report.setDeliverryChallanDataSet(m.map);
        report.setMaster(bean.master);
        report.call();    */
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

