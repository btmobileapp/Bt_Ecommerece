package biyaniparker.com.parker.view.homeadmin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.view.adapter.UserAdapter;

public class OrderFilterView extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher, View.OnClickListener {

    Button btnFromDate, btnToDate, btnShow;
    ListView lstCustomer;
    EditText edCustName;

    ModuleUser moduleUser;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_order_filter_view);

        getSupportActionBar().setTitle(" Filter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        btnFromDate=(Button)findViewById(R.id.dtFrom);
        btnToDate=(Button)findViewById(R.id.dtTo);
        btnShow=(Button)findViewById(R.id.btnShow);
        lstCustomer=(ListView)findViewById(R.id.listCustomer);
        edCustName=(EditText)findViewById(R.id.edCustomer);
        edCustName.addTextChangedListener(this);

        moduleUser=new ModuleUser(this);
        moduleUser.getUserList();
        userAdapter=new UserAdapter(this,1,moduleUser.userList);
        lstCustomer.setAdapter(userAdapter);
        lstCustomer.setOnItemClickListener(this);


        btnShow.setOnClickListener(this);
        btnFromDate.setOnClickListener(this);
        btnToDate.setOnClickListener(this);
        CommonUtilities.hideatInItInputBoard(this);
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
        UserShopBean userShopBean=(UserShopBean)userAdapter.getItem(position);

        edCustName.setText("");
        edCustName.setText(userShopBean.user.getName());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
            String txt=edCustName.getText().toString();
            moduleUser.getUsersByCustomerName(txt);
            userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(btnFromDate.getId()==v.getId())
        {
            DialogFragment newFragment = new DatePickerFragment(btnFromDate.getId());
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if(btnToDate.getId()==v.getId())
        {
            DialogFragment newFragment = new DatePickerFragment(btnToDate.getId());
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if(btnShow.getId()==v.getId())
        {
            String fromDate=btnFromDate.getText().toString();
            String toDate=btnToDate.getText().toString();
            long fromDateLong= DateAndOther.convertToLongDate(fromDate);
            long toDateLong= DateAndOther.convertToLongDate(toDate);
            String custName=edCustName.getText().toString();

            Intent intent=new Intent();
            intent.putExtra("FromDate",fromDateLong);
            intent.putExtra("ToDate",toDateLong);
            intent.putExtra("CustomerName",custName);

            setResult(RESULT_OK, intent);
            finish();

        }
    }


    @SuppressLint("ValidFragment")
    public  class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        int  res;
        DatePickerFragment(int res)
        {
            this.res=res;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            // Do something with the date chosen by the user
            Button ed=(Button) getActivity().findViewById(res);
            ed.setText(day+"/"+(month+1)+"/"+year);

        }

    }


}
