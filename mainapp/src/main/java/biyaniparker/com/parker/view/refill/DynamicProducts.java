package biyaniparker.com.parker.view.refill;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;
import biyaniparker.com.parker.view.adapter.ProductGridAdapter;
import biyaniparker.com.parker.view.managestock.StockManageProductView;

public class DynamicProducts extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ModuleCategory moduleCategory;
    int catId;
    CategoryAdapter categoryAdapter;
    ListView listView;
    ArrayList<CategoryBean> arrayList;


    GridView gridView;
    ProductGridAdapter productGridAdapter;
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
            setContentView(R.layout.activity_product_grid_list_for_refill);
            gridView=(GridView)findViewById(R.id.gridView);


            getSupportActionBar().setTitle("Products");
            getSupportActionBar().setSubtitle(moduleCategory.getCategoryBeanById(catId).getCategoryName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            Pressed=true;
            moduleProduct=new ModuleProduct(this);
            productGridAdapter=new ProductGridAdapter(this,1,moduleProduct.getProductListByCatId(catId));
            gridView.setAdapter(productGridAdapter);
            gridView.setOnItemClickListener(this);
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
            Intent intent =new Intent(this,RefillProductFilterView.class);
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

            Intent intent=new Intent(this,StockManageProductView.class);
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
            long min=data.getLongExtra("low",0);
            long max=data.getLongExtra("max",1500);


            moduleProduct.getCustomListByPriceLimit(catID,min,max );
            productGridAdapter=new ProductGridAdapter(this,1,moduleProduct.filterList);
            gridView.setAdapter(productGridAdapter);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
