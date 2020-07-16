package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.PartialDispatchAdapter;

public class PartialDispatchListView extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadUtility {

    ListView listPartialDispatch;

    ModuleDispatch moduleDispatch;
    PartialDispatchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_partial_dispatch_list_view);
        listPartialDispatch=(ListView)findViewById(R.id.lstPartial);
        moduleDispatch=new ModuleDispatch(this);
        moduleDispatch.getPartialDispatchList();
        if(moduleDispatch.partialDispatchList.size() == 0)
        {
            moduleDispatch.getRecentDispatch();
        }
        Collections.reverse(moduleDispatch.partialDispatchList);
        adapter=new PartialDispatchAdapter(this,1,moduleDispatch.partialDispatchList);
        listPartialDispatch.setAdapter(adapter);
        listPartialDispatch.setOnItemClickListener(this);

        getSupportActionBar().setTitle("Partial Dispatch List ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.syncmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        DispatchMasterAndDetails bean=(DispatchMasterAndDetails)adapter.getItem(position);
        Gson gson=new Gson();
        String str=gson.toJson(bean);
        Intent intent=new Intent(this,PartialDispatchDetail.class);
        intent.putExtra("str", str);
        startActivity(intent);
       // finish();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode == 3 &&  responseCode == 200 )
        {
            moduleDispatch.getPartialDispatchList();
            Collections.reverse(moduleDispatch.partialDispatchList);
            adapter.notifyDataSetChanged();
            CommonUtilities.alert(this," Sync Completed");
        }
        else
        {
            CommonUtilities.alert(this," Sync Failed ");
        }
    }
}
