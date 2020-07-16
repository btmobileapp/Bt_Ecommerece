package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt18 on 09/16/2016.
 */
public class UserDeletedOrderAdapter extends ArrayAdapter
{
    ArrayList<OrderMasterBean> orderList=new ArrayList<>();
    Context context;


    public UserDeletedOrderAdapter(Context context, int resource, ArrayList<OrderMasterBean> objects)
    {
        super(context, resource,objects);
        this.context=context;
        orderList=objects;
    }


    class ViewHolder
    {
        TextView txtOrNo, txtTAmt, txtOrDate, txtTQnty, txtOStatus, txtShopName;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.o_item_user_delete_orderlist,null);
            convertView.setTag(viewHolder);
        }
        else
        {
            convertView.setTag(viewHolder);
        }


        viewHolder.txtOrNo=(TextView)convertView.findViewById(R.id.txtOrderNo);
        viewHolder.txtTAmt=(TextView)convertView.findViewById(R.id.txtTAmt);
        viewHolder.txtOrDate=(TextView)convertView.findViewById(R.id.txtOrderDate);
        viewHolder.txtTQnty=(TextView)convertView.findViewById(R.id.txtTQnty);
        viewHolder.txtOStatus=(TextView)convertView.findViewById(R.id.txtStatus);
        viewHolder.txtShopName=(TextView)convertView.findViewById(R.id.txtShopName);


        viewHolder.txtOrNo.setText("Or No : " +orderList.get(position).getOrderId());
        viewHolder.txtTAmt.setText("Total  Amt : " +Integer.parseInt(orderList.get(position).getTotolAmount())+" Rs");
        viewHolder.txtOrDate.setText(CommonUtilities.longToDate(orderList.get(position).getOrderDate()));
        viewHolder.txtTQnty.setText("Total Qnty :"+orderList.get(position).getTotalQnty());
        viewHolder.txtShopName.setText(""+orderList.get(position).getShopName());
        viewHolder.txtOStatus.setText("Status : "+orderList.get(position).getOrderStatus().replace("inrequest", "In-Request").replace("dispatch", "Dispatch"));



        return convertView;
    }
}
