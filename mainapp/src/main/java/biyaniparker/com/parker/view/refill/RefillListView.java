package biyaniparker.com.parker.view.refill;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;

public class RefillListView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    CategoryAdapter categoryAdapter;
    ModuleCategory moduleCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_refill_list);

        getSupportActionBar().setTitle("Parent Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView=(ListView)findViewById(R.id.listView);
        moduleCategory=new ModuleCategory(this);
        categoryAdapter=new CategoryAdapter(this,1,moduleCategory.getListByParentId(0));
        listView.setAdapter(categoryAdapter);
        listView.setOnItemClickListener(this);
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
        CategoryBean bean=new CategoryBean();
        bean=(CategoryBean)categoryAdapter.getItem(position);
        //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this,RefillSubListView.class);
        intent.putExtra("CategoryId",bean.getCategoryId());
        startActivity(intent);
    }
}
