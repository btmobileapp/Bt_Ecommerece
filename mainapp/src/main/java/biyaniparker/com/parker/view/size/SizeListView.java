package biyaniparker.com.parker.view.size;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleSizeMaster;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.utilities.DownloadUtility;

public class SizeListView extends AppCompatActivity implements DownloadUtility, AdapterView.OnItemClickListener
{

    ModuleSizeMaster moduleSizeMaster;
    ListView listView;
    ArrayAdapter<SizeMaster> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_size_list);
        listView=(ListView)findViewById(R.id.listView);

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SizeCreateView.class));
            }
        });

        getSupportActionBar().setTitle("Product Size ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        moduleSizeMaster=new ModuleSizeMaster(this);
       // moduleSizeMaster.syncSize();
        moduleSizeMaster.getSizeList();
        if(moduleSizeMaster.sizeList.size()==0)
        {
            moduleSizeMaster.syncSize();
        }
        adapter=new ArrayAdapter<SizeMaster>(this,android.R.layout.simple_list_item_1,moduleSizeMaster.sizeList);
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
            moduleSizeMaster.syncSize();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduleSizeMaster.getSizeList();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(responseCode==200&& requestCode==1)
        {
            moduleSizeMaster.getSizeList();
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getApplicationContext(), " Server Communication failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SizeMaster sizeMaster=new SizeMaster();
        sizeMaster=(SizeMaster)adapter.getItem(position);
        Intent intent=new Intent(this,SizeEditView.class);
        intent.putExtra("SizeId",sizeMaster.getSizeId());
        startActivity(intent);
    }
}
