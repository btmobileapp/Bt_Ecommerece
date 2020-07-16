package biyaniparker.com.parker.view.productscategarywise;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;
import biyaniparker.com.parker.view.adapter.ProductAdapter;
import biyaniparker.com.parker.view.adapter.ProductGridAdapter;
import biyaniparker.com.parker.view.deduct.DeductProductView;

import biyaniparker.com.parker.view.product.ProductCreateViewNew;
import biyaniparker.com.parker.view.product.ProductEditViewNew;
import biyaniparker.com.parker.view.refill.RefillProductFilterView;

public class DynamicProducts extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ModuleCategory moduleCategory;
    int catId;
    CategoryAdapter categoryAdapter;
    ListView listView;
    ArrayList<CategoryBean> arrayList;


    ListView listViewProducts;
    ProductAdapter productGridAdapter;
    ModuleProduct moduleProduct;

    boolean Pressed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleCategory = new ModuleCategory(this);
        arrayList=new ArrayList<CategoryBean>();

        Intent intent = getIntent();
        catId = intent.getIntExtra("CategoryId", 0);

       arrayList.addAll( moduleCategory.getListByParentId(catId));
        if (arrayList.size() != 0 || catId == 0)
        {
            setContentView(R.layout.o_activity_refill_list);
            getSupportActionBar().setTitle("Categories");
            getSupportActionBar().setSubtitle(catId == 0 ? " Parent Categories" : moduleCategory.getCategoryName(catId));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            listView=(ListView)findViewById(R.id.listView);
            categoryAdapter=new CategoryAdapter(this,1,arrayList);
            listView.setAdapter(categoryAdapter);
            Pressed=false;
            listView.setOnItemClickListener(this);
        }
        else if(arrayList.isEmpty())
        {
            setContentView(R.layout.activity_product_list_catewise);
            listViewProducts=(ListView)findViewById(R.id.listView);
            FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 startActivity(new Intent(DynamicProducts.this, ProductCreateViewNew.class));
                }
            });


            getSupportActionBar().setTitle("Products");
            getSupportActionBar().setSubtitle(moduleCategory.getCategoryBeanById(catId).getCategoryName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            Pressed=true;
            moduleProduct=new ModuleProduct(this);
            productGridAdapter=new ProductAdapter(this,1,moduleProduct.getProductListByCatId(catId));
            listViewProducts.setAdapter(productGridAdapter);
            listViewProducts.setOnItemClickListener(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(Pressed)
        getMenuInflater().inflate(R.menu.menufilter,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();


        if(item.getItemId()==R.id.actionFilter)
        {
            Intent intent =new Intent(this,ProductFilterView.class);
            intent.putExtra("CategoryId",catId);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(!Pressed)
        {
            CategoryBean bean = new CategoryBean();
            bean = (CategoryBean) categoryAdapter.getItem(position);
            //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, DynamicProducts.class);
            intent.putExtra("CategoryId", bean.getCategoryId());
            startActivity(intent);
        }
        else if(Pressed)
        {
            ProductBean bean=new ProductBean();
            bean=(ProductBean) productGridAdapter.getItem(position);
            //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,ProductEditViewNew.class);
            intent.putExtra("ProductId",bean.getProductId());
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode==1 && resultCode==RESULT_OK)
        {

            int catID=data.getIntExtra("catId", 0);
            long min=data.getLongExtra("low", 0);
            long max=data.getLongExtra("max",1500);
            String stripVal=data.getStringExtra("stripCode");


            moduleProduct.getCustomListByFilter(catID,min,max, stripVal );
            productGridAdapter=new ProductAdapter(this,1,moduleProduct.filterList);
            listViewProducts.setAdapter(productGridAdapter);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
