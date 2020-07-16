package biyaniparker.com.parker.view.homeadmin;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.LaunchActivity;
import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleOrder;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.beans.RowItem;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.view.adapter.CustomAdapter;
import biyaniparker.com.parker.view.adapter.OrderAdapter;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.PartialDispatchListView;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.RecentDispatchListView;
import biyaniparker.com.parker.view.reports.DynamicCategoryReport;
import biyaniparker.com.parker.view.reports.ReportMenu;
import biyaniparker.com.parker.view.user.PasswordUpdateView;
import biyaniparker.com.parker.view.user.UserListView;

public class AdminHomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener,DownloadUtility
{

    ArrayList<RowItem> items=new ArrayList<>();
    CustomAdapter customAdapter;

    ListView listView;
    ModuleOrder moduleOrder;
    OrderAdapter orderAdapter;
    ModuleProduct moduleProduct;
    ListView slider_list;
    ImageView imgHide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.list);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


       // RowItem item=new RowItem("Home",R.drawable.ic_menu_camera);
       // items.add(item);
        RowItem item1= new RowItem("Orders", R.drawable.iconorder);
        RowItem item2= new RowItem("Products", R.drawable.ic_product);
        RowItem item3= new RowItem("Recent Dispatch", R.drawable.ic_dispatch);
        RowItem item4= new RowItem("Users", R.drawable.ic_user);
        RowItem item5= new RowItem("Change password", R.drawable.ic_changepass);
        RowItem item6= new RowItem("Logout", R.drawable.ic_logout);
        item1.isLocal=true;
        item2.isLocal=true;
        item3.isLocal=true;
        item4.isLocal=true;
        item4.isStartSection=true;
        item4.sectionName="Users And Account";
        item5.isLocal=true;
        item6.isLocal=true;
        RowItem item7= new RowItem("Stock Reports", R.drawable.reportsymbol);
        items.add(item1);
        items.add(item2);

        items.add(item3);
        item7.isLocal=true;
        items.add(item7);

        items.add(item4);
        items.add(item5);
        items.add(item6);


        slider_list=(ListView)findViewById(R.id.slider_list);
        customAdapter=new CustomAdapter(this,items);
        slider_list.setAdapter(customAdapter);
        slider_list.setOnItemClickListener(this);


        //    calling recent orders

        moduleOrder=new ModuleOrder(this);
        moduleProduct=new ModuleProduct(this);
        moduleOrder.getOrderList();


        orderAdapter=new OrderAdapter(this,1,moduleOrder.orderList);
        listView.setAdapter(orderAdapter);
//if(moduleOrder.orderList.size()==0) {
        moduleOrder.getRecentOrders(moduleOrder.getMaxChanedDate());  //sync method
//}



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try
                {
                    OrderMasterBean bean = moduleOrder.orderList.get(position);
                    getDetails(bean);
                }
                catch (Exception e)
                {
                    CommonUtilities.alert(AdminHomeScreen.this,"Err:  "+e.toString());
                }

            }
        });






        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle(CommonUtilities.Slogan);


        TextView txtShop=(TextView)navigationView.getHeaderView(0).findViewById(R.id.txtshop);
        TextView txtWelcome=(TextView)navigationView.getHeaderView(0). findViewById(R.id.txtwelcome);

        txtShop.setText(UserUtilities.getShopName(this));
        txtWelcome.setText("Welcome : " + UserUtilities.getName(this));
        imgHide=(ImageView)findViewById(R.id.imghide);




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




        registerReceiver(mMessageReceiver,new IntentFilter("LoadPhoto"));
        registerReceiver(mMessageRefreshReceiver,new IntentFilter("RefreshMe"));

       // load();
    }

    private void getDetails(final OrderMasterBean bean)
    {
        try
        {
            Toast.makeText(this," Plz wait ...",Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(AdminHomeScreen.this,OrderDetailView.class);
                                intent.putExtra("OrderId",bean.getOrderId());
                                startActivity(intent);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        catch (Exception e)
        {
            CommonUtilities.alert(this,e.toString());
                    
            e.printStackTrace();
        }

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.admin_home_screen, menu);
        getMenuInflater().inflate(R.menu.menuadmindashboard,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.actionFilter)
        {
            startActivityForResult(new Intent(this, OrderFilterView.class), 1);
        }
        else if(id==R.id.actionpartial)
        {
            startActivity(new Intent(this, PartialDispatchListView.class));
        }
        else if(id==R.id.actionSync)
        {
           // startActivity(new Intent(this, PartialDispatchListView.class));
            moduleOrder.getRecentOrders(moduleOrder.getMaxChanedDate());
        }
        else if(id==R.id.actionRotate)
        {
            startActivity(new Intent(this, ImageRotateSetting.class));
        }
        else if(id==R.id.actionDelOrder)
        {
            startActivity(new Intent(this, DeletedOrderListView.class));
        }
        else if(id==R.id.actionProductSync)
        {
            moduleProduct.syncRecentProducts();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            String custName=data.getStringExtra("CustomerName");
            long fromDate=data.getLongExtra("FromDate", 0);
            long toDate=data.getLongExtra("ToDate",0);
            //Toast.makeText(this," : "+custName+" : "+fromDate+" : "+toDate,Toast.LENGTH_SHORT).show();
            moduleOrder.getCustomList(custName,fromDate,toDate);
           orderAdapter=new OrderAdapter(this,1,moduleOrder.customOrderList) ;
            listView.setAdapter(orderAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
       /* */


            if (items.get(position).isLocal) {
                if (items.get(position).getTitle().equalsIgnoreCase("Logout")) {
                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                    alBuilder.setTitle(getString(R.string.app_name));
                    alBuilder.setMessage("Do you want to Logout this app??");
                    alBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            UserUtilities.clearUser(AdminHomeScreen.this);
                            finish();
                            startActivity(new Intent(AdminHomeScreen.this, LaunchActivity.class));
                        }
                    });
                    alBuilder.setNegativeButton("No", null);
                    alBuilder.show();
                }  //
                else if (items.get(position).getTitle().equalsIgnoreCase("Change Password")) {
                    startActivity(new Intent(this, PasswordUpdateView.class));
                } else if (position == 1) {
                    startActivity(new Intent(this, AdminProductMenu.class));
                }
                else if(position==2)
                {
                    startActivity(new Intent(this, RecentDispatchListView.class));
                }
                else if (position == 4) {
                    startActivity(new Intent(this, UserListView.class));
                }
                else if(position==3)
                {
                    startActivity(new Intent(this, ReportMenu.class));
                }

            } else {

            }
        //}


           // OrderMasterBean bean= moduleOrder.orderList.get(position);


    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200 && requestCode==1)
        {
           // moduleOrder.getOrderList();
            //customAdapter.notifyDataSetChanged();
            orderAdapter.notifyDataSetChanged();
            //CommonUtilities.alert(this,"Onkar : "+moduleOrder.orderList.size());
        }
        else if( responseCode == 20 && requestCode == 200)
        {

        }

    }


    @Override
    public void finish()
    {
        super.finish();
        unregisterReceiver(mMessageReceiver);
        unregisterReceiver(mMessageRefreshReceiver);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                //Toast.makeText(AdminHomeScreen.this,"Toast",Toast.LENGTH_LONG).show();

                String str = intent.getStringExtra("URL");

                imageLoader = ImageLoader.getInstance();
                //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
                imageLoader.displayImage(
                        str
                        ,
                        imgHide, doption, animateFirstListener);
            }
            catch (Exception e)
            {}
        }
    };


    void load()
    {

        try {
            String str = CommonUtilities.URL+"l1.jpg";

            imageLoader = ImageLoader.getInstance();
            //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
            imageLoader.displayImage(
                    str
                    ,
                    imgHide, doption, animateFirstListener);
        }
        catch (Exception e)
        {}
    }


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************
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



    private BroadcastReceiver mMessageRefreshReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            try
            {

            //    Toast.makeText(AdminHomeScreen.this,"Refresh",Toast.LENGTH_LONG).show();
                moduleOrder.getOrderList();
                orderAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {}
        }
    };

}
