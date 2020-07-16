package biyaniparker.com.parker.view.homeuser.userbag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import biyaniparker.com.parker.TouchImageView;
import biyaniparker.com.parker.bal.ModuleBag;
import biyaniparker.com.parker.beans.BagDetailsBean;
import biyaniparker.com.parker.beans.BagMasterBean;
import biyaniparker.com.parker.beans.GsonSelectedItem;
import biyaniparker.com.parker.beans.GsonSizeDetailBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.BagAdapter;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;
import biyaniparker.com.parker.view.user.UserListView;

public class UserBagView extends AppCompatActivity implements DownloadUtility, CompoundButton.OnCheckedChangeListener, View.OnClickListener {



    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    ArrayList<BagMasterBean> selectedProducts=new ArrayList<>();
    ImageView img;

    CheckBox checkAll;
    Button btnRemove, btnPreview;
    ModuleBag moduleBag;
    ArrayList<View>  viewList=new ArrayList<View>();
    LinearLayout lmain;
    CountDownTimer timer;

    TextView idtimer;


    long bagStartTimeMillis=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bag_view1);
        idtimer=(TextView)findViewById(R.id.idtimer);

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



        checkAll=(CheckBox)findViewById(R.id.chkAll);
        lmain=(LinearLayout)findViewById(R.id.lmain);
        btnRemove=(Button)findViewById(R.id.btnRemove);
        btnPreview=(Button)findViewById(R.id.btnViewSummary);
        checkAll.setOnCheckedChangeListener(this);
        btnPreview.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        moduleBag=new ModuleBag(this);
        moduleBag.lastOrder="";
        moduleBag.showBag();
        int length=moduleBag.bagMasterList.size();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Bag Items");

     //   Log.println(0, "", " Size : " + length);



        registerReceiver(mMessageReceiver, new IntentFilter("CloseMe"));


    }

    public void TimerTick()
    {
        try
        {

            timer = new CountDownTimer((bagStartTimeMillis+259200000)-System.currentTimeMillis(), 1000)
            {

                public void onTick(long millisUntilFinished)
                {
                  //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                    String tm=    DateAndOther.getHMSFrommillisecond((bagStartTimeMillis+259200000)-System.currentTimeMillis());
                    idtimer.setText("Bag will get empty after : "+tm+" hrs");
                }

                public void onFinish()
                {
                    // mTextField.setText("done!");
                    finish();
                    startActivity(new Intent(UserBagView.this,UserBagView.class));
                }
            };

        }
        catch (Exception e)
        {}


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(mMessageReceiver);
        try {
            timer.cancel();
        }
        catch (Exception e)
        {}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

        if(requestCode==1 && responseCode==200)
        {

           if(moduleBag.bagMasterList.size()==0)
           {
               AlertDialog.Builder al=new AlertDialog.Builder(this);
               al.setTitle("Bag is empty");
               al.setMessage( moduleBag.lastOrder);
               al.setPositiveButton("Ok ",null);
               al.show();
           }


            for(int i=0;i< moduleBag.bagMasterList.size();i++)
            {
              BagMasterBean master= moduleBag.bagMasterList.get(i);

                if(i==0)
                {
                    try
                    {
                        bagStartTimeMillis = master.EnterDate;
                       // CommonUtilities.alert(this, DateAndOther.getStringDayfromMillisecond(master.EnterDate));
                        TimerTick();

                        timer.start();
                    }
                    catch (Exception e){}

                }

                LayoutInflater mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View v= mInflater.inflate(R.layout.o_activity_bag_adapter,null);
                LinearLayout lnDetail=(LinearLayout)  v.findViewById(R.id.linear);
                CheckBox chk=(CheckBox)v.findViewById(R.id.chk);
                final int clicked=i;
                img=(ImageView)v.findViewById(R.id.img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str=moduleBag.bagMasterList.get(clicked).bagDetails.get(0).iconThmub;
                        //   CommonUtilities.alert(UserBagView.this,"String : "+str);
                        Intent intent=new Intent(UserBagView.this, ViewProductImage.class);
                        intent.putExtra("path",str);
                        startActivity(intent);
                    }
                });


                TextView txtCPrice=(TextView)v.findViewById(R.id.txtCPrice);
                TextView txtTPrice=(TextView)v.findViewById(R.id.txtTPrice);
                TextView txtName=(TextView)v.findViewById(R.id.txtName);
                double total=0;
                for(int c=0;c<master.bagDetails.size();c++)
                {
                    BagDetailsBean detailsBean=master.bagDetails.get(c);
                    total=total+(detailsBean.inBagQnty*detailsBean.dPrice);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    View lView=inflater.inflate(R.layout.o_activity_two_textviews,null);
                    TextView txtSizeName=(TextView) lView.findViewById(R.id.txtsize);
                    TextView txtSQnty=(TextView) lView.findViewById(R.id.txtsqnty);

                    txtSizeName.setText(detailsBean.sizeName);
                    txtSQnty.setText(detailsBean.inBagQnty+"");
                    lnDetail.addView(lView);
                }


                for(int j=0;j<master.bagDetails.size();j++)
                {
                    txtCPrice.setText("Price:  "+(int)(double) master.bagDetails.get(j).cPrice+" Rs");
                    txtName.setText("Name:  "+master.bagDetails.get(j).productName);
                    txtTPrice.setText("Total:  " + (int)total+" Rs");
                        v.setTag(master.bagDetails.get(j).stockId);

                    imageLoader = ImageLoader.getInstance();
                    //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
                    imageLoader.displayImage(
                            master.bagDetails.get(j).iconThmub
                            ,
                            img, doption, animateFirstListener);
                }

                lmain.addView(v);
                viewList.add(v);



            }
        }



        else if(requestCode==2 && responseCode==200)
        {
            if(str.equals("Success"))
            {




                finish();
                startActivity(new Intent(this, UserBagView.class));
            }
            else
            {
                Toast.makeText(this,"Remove failed  ",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(checkAll.isChecked())
        {
            for(int i=0;i<viewList.size();i++)
            {
                View v=viewList.get(i);
                CheckBox c= (CheckBox) v.findViewById(R.id.chk);
                c.setChecked(true);
            }
        }
        else
        {
            for(int i=0;i<viewList.size();i++)
            {
                View v=viewList.get(i);
                CheckBox c= (CheckBox) v.findViewById(R.id.chk);
                c.setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
       if(selectedProducts!=null)
        selectedProducts.clear();
        final ArrayList<Integer> stokIds=new ArrayList<>();
        if(v.getId()==btnRemove.getId())
        {
            for(int i=0;i<viewList.size();i++)
            {
                View view=viewList.get(i);
                CheckBox c= (CheckBox) view.findViewById(R.id.chk);
                if(c.isChecked())
                {

                    BagMasterBean bean=moduleBag.bagMasterList.get(i);
                    selectedProducts.add(bean);
                    for(int k=0;k<bean.bagDetails.size();k++)
                    {

                        stokIds.add(bean.bagDetails.get(k).stockId);
                    }

                }
            }
             if(selectedProducts.size()!=0) {
                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                 alertDialog.setTitle(getString(R.string.app_name));
                 alertDialog.setMessage("Do you want to remove these products from bags ?");
                 alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         try {

                             moduleBag.removeItems(stokIds);
                             selectedProducts.clear();
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 });
                 alertDialog.setNegativeButton("No", null);
                 alertDialog.show();
             }
             else
             {
                 Toast.makeText(this, " Please select the product to remove ", Toast.LENGTH_SHORT).show();
             }

        }

        else if(btnPreview.getId()==v.getId())
        {
            for(int i=0;i<viewList.size();i++)
            {
                View view=viewList.get(i);
                CheckBox c= (CheckBox) view.findViewById(R.id.chk);
                if(c.isChecked())
                {
                    BagMasterBean bean=moduleBag.bagMasterList.get(i);
                    selectedProducts.add(bean);
                }
            }


            try
            {
                if(selectedProducts.size()!=0)
                {
                    Gson gson = new Gson();
                    GsonSelectedItem gsonSelectedItem = new GsonSelectedItem();
                    gsonSelectedItem.masterBeans = selectedProducts;

                    String selectedItems = gson.toJson(gsonSelectedItem);

                    SharedPreferences sh= getSharedPreferences("BagSharedPreference",this.MODE_PRIVATE);
                    sh.edit().putString("SelectedItmes",selectedItems).commit();

                    Intent intent = new Intent(this, OrderSummaryView.class);
                    //intent.putExtra("SelectedItmes", selectedItems);

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this,"Please select the product to view summary",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {

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
                    FadeInBitmapDisplayer.animate(imageView, 0);
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

}
