package biyaniparker.com.parker.view.category;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;

public class CategoryListView extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener {


    ArrayList<CategoryBean> categoryBeanList;

    ImageView categoryIcon;
    ListView listView;

    CategoryAdapter adapter;
    ModuleCategory moduleCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_category_list);
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CategoryCreateView.class));
            }
        });

       getSupportActionBar().setTitle("Product Category");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);


        init();
        //categoryList=new CategoryList(this);
        moduleCategory=new ModuleCategory(this);
      //  moduleCategory.syncCategory();
        moduleCategory.getCategoryList();
        if(moduleCategory.list.size() == 0)
        {
            moduleCategory.syncCategory();
        }
        adapter=new CategoryAdapter(this,1,moduleCategory.list);
        listView.setAdapter(adapter);
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
            moduleCategory.syncCategory();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduleCategory.getCategoryList();
        adapter.notifyDataSetChanged();
    }

    private void init() {

        listView=(ListView)findViewById(R.id.listViewCategory);
       // categoryBeanList=new ArrayList<CategoryBean>();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200&& requestCode==1)
        {
            moduleCategory.getCategoryList();
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Server Communication failed",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CategoryBean bean=new CategoryBean();
        bean=(CategoryBean)adapter.getItem(position);
       //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this,CategoryEditView.class);
        intent.putExtra("CategoryId",bean.getCategoryId());
        startActivity(intent);
    }
}
