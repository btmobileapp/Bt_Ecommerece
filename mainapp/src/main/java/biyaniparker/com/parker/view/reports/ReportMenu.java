package biyaniparker.com.parker.view.reports;

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
import biyaniparker.com.parker.view.adapter.SubMenuAdapter;
import biyaniparker.com.parker.view.product.ProductListView;
import biyaniparker.com.parker.view.reports.physical_reports.DynamicCategoryPhysicalReport;


public class ReportMenu extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<MenuBean> list;
    SubMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_menu);

        getSupportActionBar().setTitle("Parker ");
        getSupportActionBar().setSubtitle("Report Menu ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView=(ListView)findViewById(R.id.listView);
        list=new ArrayList<>();
        MenuBean bean1= new MenuBean(R.drawable.facilities, "Stock Report");
        MenuBean bean2= new MenuBean(R.drawable.facilities, "Physical Stock Report");

        list.add(bean1);
        list.add(bean2);

         adapter=new SubMenuAdapter(this,1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MenuBean menuBean=list.get(position);
        String menuVal=menuBean.nm;
        if(menuVal.equals("Stock Report"))
        {
            startActivity(new Intent(this, DynamicCategoryReport.class));
        }
        else if(menuVal.equals("Physical Stock Report"))
        {
            startActivity(new Intent(this, DynamicCategoryPhysicalReport.class));
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
