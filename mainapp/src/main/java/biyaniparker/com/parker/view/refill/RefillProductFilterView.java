package biyaniparker.com.parker.view.refill;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appyvet.rangebar.RangeBar;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleUserProduct;
import biyaniparker.com.parker.beans.CategoryBean;

public class RefillProductFilterView extends AppCompatActivity implements View.OnClickListener, RangeBar.OnRangeBarChangeListener {

    EditText edMax, edMin;
    RangeBar rangeBar;
    Button btnSave;
    LinearLayout l1;
    ModuleCategory moduleCategory;
    ModuleProduct moduleProduct;
    ModuleUserProduct moduleUserProduct;
    long maxPrice, lowPrice;
    private int catId;
  //  ArrayList<View> viewList=new ArrayList<View>() ;
   // ArrayList<Integer> selectedParentId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.o_activity_refill_product_filter);
        edMax=(EditText)findViewById(R.id.edMaxPrice);
        edMin=(EditText)findViewById(R.id.edMinPrice);
        rangeBar=(RangeBar)findViewById(R.id.rangebar);
        btnSave=(Button)findViewById(R.id.btnSave);
        l1=(LinearLayout)findViewById(R.id.linear);
  //      selectedParentId=new ArrayList<Integer>();
        moduleProduct=new ModuleProduct(this);
        moduleCategory=new ModuleCategory(this);
        moduleUserProduct=new ModuleUserProduct(this);
        Intent intent=getIntent();
        catId= intent.getIntExtra("CategoryId",0);

//  fetching category bean by category Id

        CategoryBean bean=new CategoryBean();
       bean= moduleCategory.getCategoryBeanById(catId);

        btnSave.setOnClickListener(this);
        edMin.setText(100 + "");
        edMax.setText(1500 + "");
        maxPrice=1500;lowPrice=100;
        rangeBar.setOnRangeBarChangeListener(this);


        // fetching size for category


      //  viewList.clear();

        moduleProduct.sizeMasters.clear();
        moduleProduct.getSizeDetailList(bean);


           }

    @Override
    public void onClick(View v)
    {
            /*String sizeIds="";
            for(int k=0;k<selectedParentId.size();k++)
            {
               if(sizeIds.equals(""))
                 sizeIds=sizeIds+selectedParentId.get(k)+"";
                else
                   sizeIds=","+sizeIds+selectedParentId.get(k)+"";

            }*/
        //    moduleUserProduct.setFilter(catId,lowPrice,maxPrice,sizeIds,null);


        lowPrice= Long.parseLong(edMin.getText().toString());
        maxPrice= Long.parseLong(edMax.getText().toString());


        //---------------------------------------------------
        Intent i=new Intent();
        i.putExtra("low",lowPrice);
        i.putExtra("max",maxPrice);
       // i.putExtra("sizeIds",sizeIds);
        i.putExtra("catId",catId);
       // i.putExtra("stripCode", (String) null);
        setResult(RESULT_OK, i);
        finish();

    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue)
    {
            maxPrice=Long.parseLong(rightPinValue);
            lowPrice= Long.parseLong(leftPinValue);
            edMin.setText(lowPrice + "");
            edMax.setText(maxPrice + "");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
