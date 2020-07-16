package biyaniparker.com.parker.view.homeuser.productdshopping;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appyvet.rangebar.RangeBar;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.bal.ModuleUserProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.ProductRandomAdapter;

public class UserProductFilterView extends AppCompatActivity implements View.OnClickListener, RangeBar.OnRangeBarChangeListener {

    EditText edMax, edMin;
    RangeBar rangeBar;
    Button btnSave;
    LinearLayout l1;
    ModuleCategory moduleCategory;
    ModuleProduct moduleProduct;
    ModuleUserProduct moduleUserProduct;
    long maxPrice, lowPrice;
    private int catId;
    ArrayList<View> viewList=new ArrayList<View>() ;
    ArrayList<Integer> selectedParentId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.o_activity_product_filter);
        edMax=(EditText)findViewById(R.id.edMaxPrice);
        edMin=(EditText)findViewById(R.id.edMinPrice);
        rangeBar=(RangeBar)findViewById(R.id.rangebar);
        btnSave=(Button)findViewById(R.id.btnSave);
        l1=(LinearLayout)findViewById(R.id.linear);
        selectedParentId=new ArrayList<Integer>();
        moduleProduct=new ModuleProduct(this);
        moduleCategory=new ModuleCategory(this);
        moduleUserProduct=new ModuleUserProduct(this);
        Intent intent=getIntent();
        catId= intent.getIntExtra("CategoryId",0);

        CommonUtilities.hideatInItInputBoard(this);

//  fetching category bean by category Id

        CategoryBean bean=new CategoryBean();
       bean= moduleCategory.getCategoryBeanById(catId);

        btnSave.setOnClickListener(this);
        edMin.setText(100 + "");
        edMax.setText(1500 + "");
        maxPrice=1500;lowPrice=100;
        rangeBar.setOnRangeBarChangeListener(this);


        // fetching size for category


        viewList.clear();

        moduleProduct.sizeMasters.clear();
        moduleProduct.getSizeDetailList(bean);


        l1.removeAllViews();
        for(int i=0;i<moduleProduct.sizeMasters.size();i++) {

            LayoutInflater inflater=(LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v= inflater.inflate(R.layout.o_parent_category_for_size, null);


            l1.addView(v);
            v.setTag(i);
            final CheckBox ch=(CheckBox)v.findViewById(R.id.ch);
            ch.setTag(i);
            ch.setText(moduleProduct.sizeMasters.get(i).getSizeName());
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(ch.isChecked())
                    {
                        // Toast.makeText(getApplicationContext()," : "+ moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId(),Toast.LENGTH_LONG).show();
                        selectedParentId.add(moduleProduct.sizeMasters.get(Integer.parseInt(v.getTag().toString())).getSizeId());
                    }
                    else
                    {
                        //selectedParentId.remove(moduleCategory.parentList.get(Integer.parseInt(v.getTag().toString())).getCategoryId());
                        boolean b=selectedParentId.remove((Object)moduleProduct.sizeMasters.get(Integer.parseInt(v.getTag().toString())).getSizeId());
                    }
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), " : " + v.getTag().toString(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    @Override
    public void onClick(View v)
    {
            String sizeIds="";
            for(int k=0;k<selectedParentId.size();k++)
            {
               if(sizeIds.equals(""))
                 sizeIds=sizeIds+selectedParentId.get(k)+"";
                else
               {
                   sizeIds = sizeIds + "," + selectedParentId.get(k) + "";
               }

            }
        //    moduleUserProduct.setFilter(catId,lowPrice,maxPrice,sizeIds,null);


        lowPrice= Long.parseLong(edMin.getText().toString());
        maxPrice= Long.parseLong(edMax.getText().toString());


        //---------------------------------------------------
        Intent i=new Intent();
        i.putExtra("low",lowPrice);
        i.putExtra("max",maxPrice);
        i.putExtra("sizeIds",sizeIds);
        i.putExtra("catId",catId);
        i.putExtra("stripCode", (String) null);
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
