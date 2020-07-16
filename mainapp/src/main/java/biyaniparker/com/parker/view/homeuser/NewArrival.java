package biyaniparker.com.parker.view.homeuser;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleNewArrival;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.adapter.ProductRandomAdapter;
import biyaniparker.com.parker.view.homeuser.productdshopping.ProductDetailView;

public class NewArrival extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener, NotifyCallback {


    ProductRandomAdapter adapter;
    GridView gridView;
    ModuleNewArrival moduleNewArrival;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrival);
        getSupportActionBar().setTitle("New Arrival Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        gridView=(GridView)findViewById(R.id.gridView);
        moduleNewArrival=new ModuleNewArrival(this) ;
        adapter=new ProductRandomAdapter(this,1,moduleNewArrival.newProductList);
        gridView.setAdapter(adapter);

        if(new ConnectionDetector(this).isConnectingToInternet())
        {
            moduleNewArrival.loadNewArrivalProductWithNotify();
           //moduleNewArrival.loadNewArrivalProduct();
        }
        else
        {
            moduleNewArrival.getOffline();
            adapter=new ProductRandomAdapter(this,1,moduleNewArrival.newProductList);
            gridView.setAdapter(adapter);
        }
        gridView.setOnItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1)
        {
            if(responseCode==200)
            {
                //adapter=new ProductRandomAdapter(this,1,moduleNewArrival.newProductList);
              //  gridView.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(NewArrival.this, ProductDetailView.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(moduleNewArrival.newProductList.get(position));
        // finish();

        intent.putExtra("myjson",myJson);
        startActivity(intent);
    }

    @Override
    public void notifyToActivity()
    {
        Log.e("Notice", "Notified");

        runOnUiThread(
                new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
            }
        });
        int size=  moduleNewArrival.newProductList.size();


    }

    @Override
    public void finish() {
        super.finish();
        moduleNewArrival.stopAsyncWork();
    }
}
