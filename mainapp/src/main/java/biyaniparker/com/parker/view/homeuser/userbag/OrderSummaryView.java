package biyaniparker.com.parker.view.homeuser.userbag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleBag;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.BagDetailsBean;
import biyaniparker.com.parker.beans.BagMasterBean;
import biyaniparker.com.parker.beans.GsonSelectedItem;
import biyaniparker.com.parker.beans.GsonShopMaster;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.homeuser.UserHomeScreen;
import biyaniparker.com.parker.view.homeuser.userorders.UserOrderDetailView;
import biyaniparker.com.parker.view.reports.PrintOrderSummary;

public class OrderSummaryView extends AppCompatActivity implements View.OnClickListener , DownloadUtility{


    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;

    Button btnOrder;
    TextView txtGTotal;
    LinearLayout linearLayout;
    ArrayList<View> viewList=new ArrayList<>();

    ModuleProduct moduleProduct;
    ModuleCategory moduleCategory;
    ModuleBag moduleBag;

    Double grandTotal=0d;
    int totalQuan=0;
    int greatTotalQty=0;
    GsonSelectedItem gsonSelectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_summary_view);


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




        btnOrder=(Button)findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);
        linearLayout=(LinearLayout)findViewById(R.id.linear);
        txtGTotal=(TextView)findViewById(R.id.txtGTotal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Bag Summary");
//
        moduleProduct=new ModuleProduct(this);
        moduleCategory=new ModuleCategory(this);
        moduleBag=new ModuleBag(this);

        ArrayList<BagMasterBean> bagMasterBeans=new ArrayList<>();
        Intent intent=getIntent();

        SharedPreferences sh= getSharedPreferences("BagSharedPreference",this.MODE_PRIVATE);
        // sh.edit().putString("SelectedItmes",selectedItems).commit();


        String selectItems=sh.getString("SelectedItmes","");
                //intent.getExtras().getString("SelectedItmes", "");
        Gson gson=new Gson();
        gsonSelectedItem=gson.fromJson(selectItems,GsonSelectedItem.class);

        for (int i=0;i<gsonSelectedItem.masterBeans.size();i++)
        {
            BagMasterBean masterBean=new BagMasterBean();
            masterBean=gsonSelectedItem.masterBeans.get(i);

            View v;
            LayoutInflater inflater=(LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.o_item_summary,null);


            TextView txtPName=(TextView)v.findViewById(R.id.txtName);
            TextView txtCName=(TextView)v.findViewById(R.id.txtCatName);
            ImageView imageView=(ImageView)v.findViewById(R.id.image);

            int cid=moduleProduct.getProductBeanByProductId(masterBean.productId).getCategoryId();
            String CName= moduleCategory.getCategoryName(cid);
            double total=0;
            for(int k=0;k<masterBean.bagDetails.size();k++)
            {
                BagDetailsBean details=masterBean.bagDetails.get(k);
                total=total+(details.inBagQnty*details.dPrice);
            }


            TextView txtPrice=(TextView)v.findViewById(R.id.txtPrice);
            LinearLayout linearSub= (LinearLayout) v.findViewById(R.id.lineainner);


            for(int c=0;c<masterBean.bagDetails.size();c++)
            {

                //LayoutInflater sub=(LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


                View subView=inflater.inflate(R.layout.o_activity_two_textviews,null);
                TextView txtSize=(TextView)subView.findViewById(R.id.txtsize);
                TextView txtQnty=(TextView)subView.findViewById(R.id.txtsqnty);
                BagDetailsBean details=masterBean.bagDetails.get(c);
                txtQnty.setText(String.valueOf(details.inBagQnty));
                txtSize.setText(details.sizeName);
                linearSub.addView(subView);

                totalQuan=totalQuan+details.inBagQnty;

                greatTotalQty=greatTotalQty+details.inBagQnty;

                // image bitmap
                if(c==0)
                {
                    grandTotal=grandTotal+total;
                    //txtPrice.setText((int)total+" Rs");
                    txtPrice.setText(details.cPrice+" Rs");
                    txtPName.setText(details.productName);
                    txtCName.setText(CName);
                    imageLoader = ImageLoader.getInstance();
                    //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
                    imageLoader.displayImage(
                            details.iconThmub
                            ,
                            imageView, doption, animateFirstListener);
                }

            }
            txtGTotal.setText(" Total : "+(int)(double)grandTotal +" Rs");
            TextView txtGTotalQnty=(TextView)findViewById(R.id.txtGTotalQnty);
            txtGTotalQnty.setText("Total Qty : "+greatTotalQty);

            linearLayout.addView(v);
            viewList.add(v);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.app_name));
        alertDialog.setMessage("Are you sure to order ? ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    moduleBag.placeOrder(grandTotal, gsonSelectedItem);
                   // finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.show();

        //startActivity(new Intent(this, UserHomeScreen.class));
    }

 @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==3 && responseCode==200)
        {
           /* Intent intent=new Intent(this,UserHomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            Toast.makeText(this, " Order placed Successfully .. ",Toast.LENGTH_LONG).show();*/

            if(moduleBag.gsonMasterObject!=null)
            {
                if(moduleBag.gsonMasterObject.warningMessage!=null)
                {
                     if(moduleBag.gsonMasterObject.warningMessage.contains("Minimum 10 Minute difference required in between placing order"))
                     {
                         final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                         alertDialog.setTitle(getString(R.string.app_name));
                         alertDialog.setMessage(moduleBag.gsonMasterObject.warningMessage +"\n Check your bag item.");
                         alertDialog.setPositiveButton("Check bag again", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which)
                             {
                                startActivity(new Intent(OrderSummaryView.this,UserBagView.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                );
                             }
                         });
                         alertDialog.setCancelable(false);
                         alertDialog.show();
                         return;
                     }
                }
            }

            Intent in=new Intent();
            in.setAction("CloseMe");
            sendBroadcast(in);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage(" Do you want Pdf report of Order ? ");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final ProgressDialog pd = new ProgressDialog(OrderSummaryView.this);
                    pd.setMessage(" Loading ...");
                   // pd.show();
                    finish();
                    Intent intent=new Intent(OrderSummaryView.this, UserOrderDetailView.class);
                    intent.putExtra("OrderId", moduleBag.orderCombine.master.toOrderMaster().getOrderId());
                    startActivity(intent);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            PrintOrderSummary printOrderSummary = new PrintOrderSummary(OrderSummaryView.this, imageLoader);

                            ArrayList<OrderDetailBean> detailBeans = new ArrayList<OrderDetailBean>();

                            for (int i = 0; i < moduleBag.orderCombine.list.size(); i++) {
                                detailBeans.add(moduleBag.orderCombine.list.get(i).toOrderDetail());
                            }

                            printOrderSummary.orderDetails = detailBeans;
                            printOrderSummary.master = moduleBag.orderCombine.master.toOrderMaster();
                            printOrderSummary.call();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                }
                            });

                        }
                    }).start();*/
                }
            });

            alertDialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //alertDialog.setCancelable(true);
                    finish();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });

            alertDialog.show();

          /*  Intent intent=new Intent(this,UserHomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
*/


        }
        else
        {
            CommonUtilities.alert(this," Failed. ");
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
                    FadeInBitmapDisplayer.animate(imageView, 0);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
