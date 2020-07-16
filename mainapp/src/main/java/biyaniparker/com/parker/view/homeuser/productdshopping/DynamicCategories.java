package biyaniparker.com.parker.view.homeuser.productdshopping;

import android.content.Intent;

import android.os.Bundle;
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
import biyaniparker.com.parker.view.homeuser.NewArrival;

public class DynamicCategories extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
    protected void onCreate(Bundle savedInstanceState)
    {
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

            Pressed=true;
            /*setContentView(R.layout.activity_product_grid_list_for_refill);
            gridView=(GridView)findViewById(R.id.gridView);
            getSupportActionBar().setTitle("Products");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            moduleProduct=new ModuleProduct(this);
            productGridAdapter=new ProductGridAdapter(this,1,moduleProduct.getProductListByCatId(catId));
            gridView.setAdapter(productGridAdapter);
            gridView.setOnItemClickListener(this);*/

            finish();
            Intent i=new Intent(this,SearchUserProductsList.class);
            i.putExtra("CategoryId",catId);
            startActivity(i);


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
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

            Intent intent = new Intent(this, DynamicCategories.class);
            intent.putExtra("CategoryId", bean.getCategoryId());
            startActivity(intent);
        }
        else if(Pressed)
        {
            ProductBean bean=new ProductBean();
            bean=(ProductBean) productGridAdapter.getItem(position);
            //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,NewArrival.class);
            intent.putExtra("ProductId",bean.getProductId());
            startActivity(intent);
        }

    }

}
