package biyaniparker.com.parker.view.homeadmin;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.MenuBean;
import biyaniparker.com.parker.view.refill.DynamicProducts;
import biyaniparker.com.parker.view.adapter.SubMenuAdapter;
import biyaniparker.com.parker.view.category.CategoryListView;
import biyaniparker.com.parker.view.price.PriceListView;
import biyaniparker.com.parker.view.product.ProductCreateViewNew;
import biyaniparker.com.parker.view.product.ProductListView;
import biyaniparker.com.parker.view.size.SizeListView;
import biyaniparker.com.parker.view.user.UserListView;

public class AdminProductMenu extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ArrayList<MenuBean> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_menu);
        list.add(new MenuBean(R.drawable.facilities, "Add new Product"));
        list.add(new MenuBean(R.drawable.facilities, "Category"));
        list.add(new MenuBean(R.drawable.facilities,"Products"));
        list.add(new MenuBean(R.drawable.facilities,"Categorywise Products"));
        list.add(new MenuBean(R.drawable.facilities,"Manage Stock"));
      //  list.add(new MenuBean(R.drawable.facilities,"Deduct Stock "));
        list.add(new MenuBean(R.drawable.facilities,"Size Master"));
      //  list.add(new MenuBean(R.drawable.facilities,"User Master"));
        list.add(new MenuBean(R.drawable.facilities,"Price Master"));
        list.add(new MenuBean(R.drawable.facilities,"Remove Empty Stock"));






        ListView listView2=(ListView)findViewById(R.id.listView2);

        SubMenuAdapter adapter=new SubMenuAdapter(this,1,list);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(this);
        getSupportActionBar().setTitle("Products ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

      //  getSupportActionBar().setTitle("a");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        MenuBean menuBean=list.get(position);
        String menuVal=menuBean.nm;

        if(menuVal.equals("Category"))
        {
            startActivity(new Intent(this, CategoryListView.class));
        }
        else if(menuVal.equals("Products"))
        {
            startActivity(new Intent(this, ProductListView.class));
        }
        else if(menuVal.equals("Size Master"))
        {
            startActivity(new Intent(this, SizeListView.class));
        }
       /* else if(position==3)
        {
            startActivity(new Intent(this, UserListView.class));
        }*/
        else if(menuVal.equals("Price Master"))
        {
            startActivity(new Intent(this, PriceListView.class));
        }
        else if(menuVal.equals("Add new Product"))
        {
            startActivity(new Intent(this, ProductCreateViewNew.class));
        }
        else if(menuVal.equals("Manage Stock"))
        {
            Intent intent=new Intent(this, DynamicProducts.class);
            intent.putExtra("CategoryId",0);
            startActivity(intent);
        }
        else if(menuVal.equals("Deduct Stock "))
        {
            Intent intent=new Intent(this, biyaniparker.com.parker.view.deduct.DynamicProducts.class);
            intent.putExtra("CategoryId",0);
            startActivity(intent);
        }
        else if(menuVal.equals("Categorywise Products"))
        {
            Intent intent=new Intent(this, biyaniparker.com.parker.view.productscategarywise.DynamicProducts.class);
            intent.putExtra("CategoryId",0);
            startActivity(intent);
        }
        else if(menuVal.equals("Remove Empty Stock"))
        {
            Intent intent=new Intent(this,   biyaniparker.com.parker.view.emptystock.DynamicProducts.class);
            intent.putExtra("CategoryId",0);
            startActivity(intent);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
