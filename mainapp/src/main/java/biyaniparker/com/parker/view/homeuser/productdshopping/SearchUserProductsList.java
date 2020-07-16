package biyaniparker.com.parker.view.homeuser.productdshopping;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleNewArrival;
import biyaniparker.com.parker.bal.ModuleUserProduct;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.adapter.ProductRandomAdapter;

public class SearchUserProductsList extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener, NotifyCallback {

    ProductRandomAdapter adapter;
    GridView gridView;
    ModuleUserProduct moduleUserProduct;
    ModuleCategory moduleCategory;
    int catId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrival);
        Intent intent=getIntent();
        moduleCategory=new ModuleCategory(this);
        catId=intent.getIntExtra("CategoryId",0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(moduleCategory.getCategoryBeanById(catId).getCategoryName());
        getSupportActionBar().setHomeButtonEnabled(true);
        gridView=(GridView)findViewById(R.id.gridView);
        moduleUserProduct=new ModuleUserProduct(this) ;
        adapter=new ProductRandomAdapter(this,1,moduleUserProduct.newProductList);
        gridView.setAdapter(adapter);
        if(new ConnectionDetector(this).isConnectingToInternet())
        {
            //moduleUserProduct.loadFromDb(catId);
           // moduleUserProduct.getUserProducts(catId);
            moduleUserProduct.getUserProductsWithNotify(catId);

        }
        else
        {
            moduleUserProduct.loadFromDb(catId);
        }

        gridView.setOnItemClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        if(item.getItemId()==R.id.actionFilter)
        {
            Intent intent=new Intent(this,UserProductFilterView.class);
            intent.putExtra("CategoryId",catId);
            startActivityForResult(intent, 100);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menufilter,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
            if(requestCode==1&&responseCode==200)
            {
                try
                {
                   // adapter.notifyDataSetChanged();

                }
                catch (Exception e)
                {

                }
            }

        if(requestCode==2&&responseCode==200)
        {
            try
            {

                adapter.notifyDataSetChanged();


            }
            catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(SearchUserProductsList.this, ProductDetailView.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(moduleUserProduct.newProductList.get(position));
       // finish();

        intent.putExtra("myjson",myJson);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100)
        {
            if(resultCode==RESULT_OK)
            {
                data.getStringExtra("");
                Bundle b=data.getExtras();
                long lPrice = b.getLong("low");
                long mPrice=b.getLong("max");
                int categoryId=b.getInt("catId");
                String sizeIds=b.getString("sizeIds");
                String stripCode=b.getString("stripCode");

                moduleUserProduct.newProductList.clear();
                adapter.notifyDataSetChanged();
                moduleUserProduct.setFilterWithNotify(categoryId,lPrice,mPrice,sizeIds,stripCode);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        moduleUserProduct.stopAsyncWork();
    }
    @Override
    public void notifyToActivity()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        int size=  moduleUserProduct.newProductList.size();
    }
}
