package biyaniparker.com.parker.view.product;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;
import biyaniparker.com.parker.view.adapter.ProductAdapter;
import biyaniparker.com.parker.view.category.CategoryCreateView;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

public class ProductListView extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener {

    ArrayList<ProductBean> productBeanList;
    ListView listView;
    ProductAdapter adapter;
    ModuleProduct moduleProduct;
    boolean isCustomList=false;           // used to differntiate between custom list or complete list

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_product_list);

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductCreateViewNew.class));
            }
        });

        getSupportActionBar().setTitle("Product ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView=(ListView)findViewById(R.id.listView);
        moduleProduct=new ModuleProduct(this);
       // 1567017000000l
      //  1566719391523l

        //moduleProduct.getProductListAboveDate( 1567017000000l);
        moduleProduct.getProductList();
        adapter = new ProductAdapter(this, 1, moduleProduct.list);
        listView.setAdapter(adapter);
       // moduleProduct.syncRecentProducts();

        listView.setOnItemClickListener(this);

      //  startProcessImage();

    }

    void startProcessImage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                  for( int i=0;i<  moduleProduct.list.size();i++)
                  {
                      restoreImage(moduleProduct.list.get(i));
                      Thread.sleep(1000);
                  }
                }
                catch (Exception ex){}
            }
        }).start();
    }
    void restoreImage(final ProductBean bean)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Intent intent=new Intent(ProductListView.this, ViewProductImage.class);
                intent.putExtra("path",bean.getIconThumb());
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menufilter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {onBackPressed();}

        if(item.getItemId()==R.id.actionFilter)
        {
            startActivityForResult(new Intent(this, ProductFilterView.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==1)
        {
            if (resultCode == RESULT_OK)
            {
                String stripCode=intent.getStringExtra("StripCode");
                int catId=intent.getIntExtra("CatId", 0);
                int priceId=intent.getIntExtra("Price", 0);
                isCustomList=intent.getBooleanExtra("CustomList",false);

                if(isCustomList)              //( catId==0 means it opens complete list else it will open custom list
                {
                    stripCode=stripCode==null?"":stripCode;
                    adapter=new ProductAdapter(this,1,moduleProduct.getCustomList(stripCode, catId, priceId));
                    listView.setAdapter(adapter);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduleProduct.getProductList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200&& (requestCode==1 ||requestCode==6))
        {
           // moduleProduct.getProductList();
           // adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getApplicationContext(), " Server Communication failed", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ProductBean bean=new ProductBean();
        bean=(ProductBean)adapter.getItem(position);
        //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();


        Intent intent=new Intent(this,ProductEditViewNew.class);
        intent.putExtra("ProductId",bean.getProductId());
        startActivity(intent);

       /* Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, moduleProduct.list.get(position).createdDate+"");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, moduleProduct.list.get(position).createdDate+"");
        //intent.putExtra(Intent.EXTRA_TEXT,moduleProduct.list.get(position).createdDate+"");
        startActivity(sharingIntent);
         */


    }
}
