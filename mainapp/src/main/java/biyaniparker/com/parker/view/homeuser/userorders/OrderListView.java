package biyaniparker.com.parker.view.homeuser.userorders;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleOrder;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.OrderAdapter;
import biyaniparker.com.parker.view.adapter.UserOrderAdapter;

public class OrderListView extends AppCompatActivity implements AdapterView.OnItemClickListener,DownloadUtility {

    ListView listView;
    ModuleOrder moduleOrder;
    UserOrderAdapter userOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin_home_screen);
        listView=(ListView)findViewById(R.id.list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(" Order List");

        moduleOrder=new ModuleOrder(this);
        moduleOrder.getUserOrderList();
        userOrderAdapter=new UserOrderAdapter(this,1,moduleOrder.userOrderList);
        listView.setAdapter(userOrderAdapter);

        /*if(moduleOrder.userOrderList.size()==0) {
            moduleOrder.getRecentOrdersOfUser();
        }*/
        moduleOrder.getRecentOrdersOfUser();
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.syncmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        OrderMasterBean bean=moduleOrder.userOrderList.get(position);
        Intent intent=new Intent(this,UserOrderDetailView.class);
        intent.putExtra("OrderId", bean.getOrderId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        if(item.getItemId()==R.id.actionSync)
            moduleOrder.getRecentOrdersOfUser();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200 && requestCode==1)
        {
           // CommonUtilities.alert(this,moduleOrder.userOrderList.size()+"");
              userOrderAdapter.notifyDataSetChanged();

        }
    }
}
