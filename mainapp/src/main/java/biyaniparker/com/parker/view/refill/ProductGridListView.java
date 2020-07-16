package biyaniparker.com.parker.view.refill;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.view.adapter.ProductGridAdapter;
import biyaniparker.com.parker.view.managestock.StockManageProductView;

public class ProductGridListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    GridView gridView;
    ProductGridAdapter productGridAdapter;
    ModuleProduct moduleProduct;
    int catid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid_list_for_refill);
        gridView=(GridView)findViewById(R.id.gridView);


        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent=getIntent();
        catid=intent.getIntExtra("CategoryId",0);
        moduleProduct=new ModuleProduct(this);
        productGridAdapter=new ProductGridAdapter(this,1,moduleProduct.getProductListByCatId(catid));
        gridView.setAdapter(productGridAdapter);
        gridView.setOnItemClickListener(this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ProductBean bean=new ProductBean();
        bean=(ProductBean) productGridAdapter.getItem(position);
        //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this, StockManageProductView.class );
                //RefillProductView.class);
        intent.putExtra("ProductId",bean.getProductId());
        startActivity(intent);
    }


}
