package biyaniparker.com.parker.view.price;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.PriceAdapter;
import biyaniparker.com.parker.view.user.UserCreateView;
import biyaniparker.com.parker.view.user.UserEditView;

public class PriceListView extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadUtility {

    ListView listView;
    ModulePrice modulePrice;
    PriceAdapter priceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_price_list);
        listView=(ListView)findViewById(R.id.listView);

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PriceCreateView.class));
            }
        });

        getSupportActionBar().setTitle(" Price  ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        modulePrice=new ModulePrice(this);
        modulePrice.getPrices();
        if(modulePrice.list.size()==0)
        {
            modulePrice.syncPrice();
        }
        priceAdapter=new PriceAdapter(this,1,modulePrice.list);
       // modulePrice.syncPrice();
        listView.setAdapter(priceAdapter);
        //modulePrice.getPrices();
        listView.setOnItemClickListener(this);

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
            modulePrice.syncPrice();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

      modulePrice.syncPrice();
       priceAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PriceBean bean=new PriceBean();
        bean=(PriceBean)priceAdapter.getItem(position);
        Intent intent=new Intent(this,PriceEditView.class);
        intent.putExtra("PriceId",bean.getPriceId());
        startActivity(intent);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200&& requestCode==1)
        {
            //modulePrice.syncPrice();
            modulePrice.getPrices();
            priceAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getApplicationContext(), " Server Communication failed", Toast.LENGTH_LONG).show();
        }
    }
}
