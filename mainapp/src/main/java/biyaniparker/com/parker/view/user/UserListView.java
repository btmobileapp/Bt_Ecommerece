package biyaniparker.com.parker.view.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.UserAdapter;



public class UserListView extends AppCompatActivity implements AdapterView.OnItemClickListener,DownloadUtility {

    ListView listView;
    ModuleUser moduleUser;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_user_list);
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserCreateView.class));
            }
        });

        getSupportActionBar().setTitle(" User  ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        listView=(ListView)findViewById(R.id.listView);
        moduleUser=new ModuleUser(this);
        moduleUser.getUserList();
        if(moduleUser.userList.size()==0)
        {
            moduleUser.syncUser();
        }
        userAdapter=new UserAdapter(this,1,moduleUser.userList);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(this);


        registerReceiver(mMessageReceiver, new IntentFilter("SyncUser"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.syncmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        else if(item.getItemId() == R.id.actionSync)
        {
            moduleUser.syncUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

       // moduleUser.syncUser();
        moduleUser.getUserList();
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        UserShopBean bean=new UserShopBean();
        bean=(UserShopBean)userAdapter.getItem(position);
        Intent intent=new Intent(this,UserEditView.class);
        intent.putExtra("UserId", bean.user.getUserId());
        startActivity(intent);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

        if(responseCode==200&& requestCode==1)
        {
            moduleUser.getUserList();
            userAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getApplicationContext(), " Server Communication failed", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent)
        {
            try {


                Toast.makeText(UserListView.this,"In BrodCast ..",Toast.LENGTH_SHORT).show();
                moduleUser.syncUser();
            }
            catch (Exception e)
            {}
        }
    };
}
