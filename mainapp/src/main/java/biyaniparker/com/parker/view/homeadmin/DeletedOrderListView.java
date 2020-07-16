package biyaniparker.com.parker.view.homeadmin;

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
import biyaniparker.com.parker.view.adapter.OrderAdapter;
import biyaniparker.com.parker.view.adapter.UserDeletedOrderAdapter;
import biyaniparker.com.parker.view.adapter.UserOrderAdapter;
import biyaniparker.com.parker.view.homeuser.userorders.UserOrderDetailView;
import biyaniparker.com.parker.view.product.ProductFilterView;

public class DeletedOrderListView extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ModuleOrder moduleOrder;
    UserDeletedOrderAdapter deleteOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin_home_screen);
        listView=(ListView)findViewById(R.id.list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Deleted Order List");

        moduleOrder=new ModuleOrder(this);
        moduleOrder.getDeletedOrderList();
        deleteOrderAdapter=new UserDeletedOrderAdapter(this,1,moduleOrder.deletedOrderList);
        listView.setAdapter(deleteOrderAdapter);
        listView.setOnItemClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menufilter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
         if(item.getItemId()==R.id.actionFilter)
        {
            startActivityForResult(new Intent(this, OrderFilterView.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        OrderMasterBean bean=moduleOrder.deletedOrderList.get(position);
        Intent intent=new Intent(this,DeletedOrderDetailView.class);
        intent.putExtra("OrderId", bean.getOrderId());
        startActivity(intent);
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
            moduleOrder.getDeleteCustomList(custName,fromDate,toDate);
            deleteOrderAdapter=new UserDeletedOrderAdapter(this,1,moduleOrder.deletedOrderList) ;
            listView.setAdapter(deleteOrderAdapter);
        }
    }

}
