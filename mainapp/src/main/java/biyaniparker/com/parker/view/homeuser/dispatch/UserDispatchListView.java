package biyaniparker.com.parker.view.homeuser.dispatch;

import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.view.adapter.RecentDispatchAdapter;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.RecentDispatchDetailView;

public class UserDispatchListView extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadUtility {

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
        moduleDispatch.getRecentDispatchListOfUser(UserUtilities.getUserId(this));
        adapter=new RecentDispatchAdapter(this,1,moduleDispatch.recentDispatchedUserList);
        listView.setAdapter(adapter);
        moduleDispatch.getRecentDispatchOfUser();

        listView.setOnItemClickListener(this);

        getSupportActionBar().setTitle("Recent Dispatch List ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DispatchMasterAndDetails bean= (DispatchMasterAndDetails) adapter.getItem(position);
        Gson gson=new Gson();
        String str=gson.toJson(bean);
        Intent intent=new Intent(this,RecentDispatchDetailView.class);
        intent.putExtra("str", str);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(requestCode==3 && responseCode==200)
        {
           adapter.notifyDataSetChanged();
        }


    }
}
