package biyaniparker.com.parker.view.product;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModulePrice;

public class ProductFilterView extends AppCompatActivity implements View.OnClickListener {

    EditText edStripCode;
    Spinner spnPrice, spnCat;
    Button btnSave;



    ModuleCategory moduleCategory;
    ModulePrice modulePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_view);

        btnSave=(Button)findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(this);
        edStripCode=(EditText)findViewById(R.id.edStripCode);
        spnCat=(Spinner)findViewById(R.id.spinnerCategory);
        spnPrice=(Spinner)findViewById(R.id.spinnerPrice);

        moduleCategory=new ModuleCategory(this);
        modulePrice=new ModulePrice(this);
        ArrayAdapter priceArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,modulePrice.list);
        ArrayAdapter catArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,moduleCategory.lastCategoryList);
        modulePrice.getPrices();
        moduleCategory.getLastCategoryList();
        spnPrice.setAdapter(priceArrayAdapter);
        spnCat.setAdapter(catArrayAdapter);



        getSupportActionBar().setTitle("Product Filter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {onBackPressed();}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {

            int price= spnPrice.getSelectedItemPosition()<0?0:modulePrice.list.get(spnPrice.getSelectedItemPosition()).getPriceId();
            int catId=spnCat.getSelectedItemPosition()<0?0:moduleCategory.lastCategoryList.get(spnCat.getSelectedItemPosition()).getCategoryId();
            String stripCode=edStripCode.getText().toString();

        Intent intent=new Intent();
        intent.putExtra("Price",price);
        intent.putExtra("CatId",catId);
        intent.putExtra("StripCode", stripCode);
        intent.putExtra("CustomList",true);
        //startActivity(intent);



           //setResult(RESULT_OK);
        //setIntent(intent);
        //setResult(RESULT_OK);
           setResult(RESULT_OK,intent);
        finish();

    }
}
