package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.Collections;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.OrderAdapter;
import biyaniparker.com.parker.view.adapter.RecentDispatchAdapter;
import biyaniparker.com.parker.view.homeadmin.OrderFilterView;


public class RecentDispatchListView extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadUtility {

    Button b;
    ListView listView;

    ModuleDispatch moduleDispatch;
    RecentDispatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_recent_dispatch_list_view);
        listView=(ListView)findViewById(R.id.lstRecentDispatch);

        moduleDispatch=new ModuleDispatch(this);

        moduleDispatch.getRecenteDispatchList();
        Collections.reverse(moduleDispatch.recentDispatchedList);
        adapter=new RecentDispatchAdapter(this,1, moduleDispatch.recentDispatchedList);
        listView.setAdapter(adapter);
        if(moduleDispatch.recentDispatchedList.size()==0)
        {
             moduleDispatch.getRecentDispatch();
        }

        listView.setOnItemClickListener(this);
        getSupportActionBar().setTitle("Recent Dispatch List ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.syncandfilter,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        DispatchMasterAndDetails bean= (DispatchMasterAndDetails) adapter.getItem(position);
        Gson gson=new Gson();
        String str=gson.toJson(bean);
        Intent intent=new Intent(this,RecentDispatchDetailView.class);
        intent.putExtra("str", str);
        startActivity(intent);

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==3 && responseCode==200)
        {
            Collections.reverse(moduleDispatch.recentDispatchedList);
           adapter.notifyDataSetChanged();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        if(R.id.actionSync==item.getItemId())
        {
            moduleDispatch.getRecentDispatch();
        }
        if(R.id.actionFilter==item.getItemId())
        {
            startActivityForResult(new Intent(this, OrderFilterView.class), 1);
        }
        return super.onOptionsItemSelected(item);
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
            moduleDispatch.getCustomList(custName,fromDate,toDate);
            adapter=new RecentDispatchAdapter(this,1,moduleDispatch.customDispatchList) ;
            listView.setAdapter(adapter);
        }
    }

}
