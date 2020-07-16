package biyaniparker.com.parker.view.emptystock;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleEmptyStock;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;
import biyaniparker.com.parker.view.adapter.EmptyStockAdapter;
import biyaniparker.com.parker.view.adapter.ProductGridAdapter;
import biyaniparker.com.parker.view.refill.RefillProductFilterView;

public class DynamicProducts extends AppCompatActivity implements AdapterView.OnItemClickListener ,DownloadUtility
{

    EditText fD,fM,fY, tD,tM,tY;
    ModuleCategory moduleCategory;
    int catId;
    CategoryAdapter categoryAdapter;
    ListView listView;
    ArrayList<CategoryBean> arrayList;
    GridView gridView;
    ProductGridAdapter productGridAdapter;
    ModuleProduct moduleProduct;
    boolean Pressed;
    Button btnfromDate,btntoDate,btnShow;
    ModuleEmptyStock moduleEmptyStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleEmptyStock = new ModuleEmptyStock(DynamicProducts.this);
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
            setContentView(R.layout.emptystocklayout);
            fD=(EditText) findViewById(R.id.fD);//,fM,fY, tD,tM,tY;
            fM=(EditText) findViewById(R.id.fM);
            fY=(EditText) findViewById(R.id.fY);
            tD=(EditText) findViewById(R.id.tD);
            tM=(EditText) findViewById(R.id.tM);
            tY=(EditText) findViewById(R.id.tY);

            String todaysDate= DateAndOther.getStringDayfromMillisecond(System.currentTimeMillis());
            String fourMonthDate= DateAndOther.getStringDayfromMillisecond(  DateAndOther.getForwordDay(System.currentTimeMillis(),-120)   );

            fD.setText(fourMonthDate.split("-")[0]);
            fM.setText(fourMonthDate.split("-")[1]);
            fY.setText(fourMonthDate.split("-")[2]);

            tD.setText(todaysDate.split("-")[0]);
            tM.setText(todaysDate.split("-")[1]);
            tY.setText(todaysDate.split("-")[2]);


            getSupportActionBar().setTitle("Empty Stock");
            getSupportActionBar().setSubtitle(moduleCategory.getCategoryBeanById(catId).getCategoryName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            Pressed=true;
            listView=(ListView)findViewById(R.id.listview);
            btnfromDate=(Button)findViewById(R.id.btnfromDate);
            btntoDate=(Button)findViewById(R.id.btntoDate);
            btnShow=(Button)findViewById(R.id.btnShow1);
        //    CommonUtilities.alert(this,catId+"");

            btnfromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment(v.getId(),"Select Date");
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            });

            btntoDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment(v.getId(),"Select Date");
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            });

            CheckBox ch= (CheckBox) findViewById(R.id.chkbox);
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      for(int i=0;i<moduleEmptyStock.list.size();i++)
                          moduleEmptyStock.list.get(i).isChecked=isChecked;
                      if(adapter!=null)
                        adapter.notifyDataSetChanged();

                }
            });

            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                      String fromDate="";
                      String toDate="";
                      long fromdate=0,todate=0;
                      int d,m,y;

                      try
                      {
                          d=Integer.parseInt(fD.getText().toString());
                          m=Integer.parseInt(fM.getText().toString());
                          y=Integer.parseInt(fY.getText().toString());
                          fromDate=d+"-"+m+"-"+y;
                          Date date=  stringToDate(fromDate);
                          fromdate= date.getTime();
                      }
                      catch (Exception ex)
                      {
                          Toast.makeText(DynamicProducts.this,"Invalid Date",Toast.LENGTH_LONG).show();
                          return;
                      }

                    try
                    {
                        d=Integer.parseInt(tD.getText().toString());
                        m=Integer.parseInt(tM.getText().toString());
                        y=Integer.parseInt(tY.getText().toString());
                        fromDate=d+"-"+m+"-"+y;
                        Date date=  stringToDate(fromDate);
                        todate= date.getTime();
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(DynamicProducts.this,"Invalid Date",Toast.LENGTH_LONG).show();
                        return;
                    }


                      /*

                      if(!btnfromDate.getText().toString().equalsIgnoreCase("Select Date"))
                      {
                           Date date=  stringToDate(btnfromDate.getText().toString());
                           fromdate= date.getTime();
                      }
                      if(!btntoDate.getText().toString().equalsIgnoreCase("Select Date"))
                      {
                            Date date=  stringToDate(btntoDate.getText().toString());
                            todate= date.getTime();
                      }
                      else
                      {
                          todate = 1924885800000l;
                      }
                      */
                    moduleEmptyStock.loadEmptyProduct(catId, fromdate, todate);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(Pressed)
        getMenuInflater().inflate(R.menu.menudelete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();


        if(item.getItemId()==R.id.actionDelete)
        {
           /*
            Intent intent =new Intent(this,RefillProductFilterView.class);
            intent.putExtra("CategoryId",catId);
            startActivityForResult(intent, 1);
            */
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Remove Product");
            alert.setMessage("Do you want to remove this product ?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   int cntRemove=   moduleEmptyStock.removeProduct();
                   if(cntRemove==0)
                       Toast.makeText(getBaseContext(),"No Product Seleted",Toast.LENGTH_LONG).show();
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();
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

    EmptyStockAdapter adapter;

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
            if(requestCode==1)
            {
                  adapter=new EmptyStockAdapter(this,1,moduleEmptyStock.list);
                  listView.setAdapter(adapter);
            }
            if(requestCode==2)
            {
               if(responseCode==200)
               {
                   AlertDialog.Builder alert=new AlertDialog.Builder(this);
                   alert.setMessage("Product Removed");
                   alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                               onBackPressed();
                       }
                   });
                   alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                       @Override
                       public void onCancel(DialogInterface dialog) {
                           onBackPressed();
                       }
                   });
                   alert.setCancelable(false);
                   alert.show();
               }
            }

    }


    @SuppressLint("ValidFragment")
    public  class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        int  res;
        String defaultText;
        DatePickerFragment(int res,String defaultText)
        {
            this.res=res;
            this.defaultText=defaultText;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog=   new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Button btn = (Button) getActivity().findViewById(res);
                    btn.setText(defaultText);
                }
            });

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Button btn = (Button) getActivity().findViewById(res);
            btn.setText(day + "-" + (month + 1) + "-" + year);

        }

    }



        public static Date stringToDate ( String strDate)
        {
            Date date=null;
            SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
            try
            {
                date=format.parse(strDate);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            return date;
        }
}
