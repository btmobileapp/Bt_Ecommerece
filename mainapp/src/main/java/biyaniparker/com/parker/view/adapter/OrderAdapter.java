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
 * Created by bt on 09/13/2016.
 */
public class OrderAdapter extends ArrayAdapter
{
    ArrayList<OrderMasterBean> orderList=new ArrayList<>();
    Context context;


    public OrderAdapter(Context context, int resource, ArrayList<OrderMasterBean> objects)
    {
        super(context, resource,objects);
        this.context=context;
        orderList=objects;
    }


    class ViewHolder
    {
        TextView txtShop, txtAddress, txtOrderId, txtOrderDate;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.o_item_orderlist,null);
            convertView.setTag(viewHolder);
        }
        else
        {
            convertView.setTag(viewHolder);
        }


        viewHolder.txtAddress=(TextView)convertView.findViewById(R.id.txtAddress);
        viewHolder.txtOrderDate=(TextView)convertView.findViewById(R.id.txtOrderDate);
        viewHolder.txtOrderId=(TextView)convertView.findViewById(R.id.txtOrder);
        viewHolder.txtShop=(TextView)convertView.findViewById(R.id.txtShop);


        viewHolder.txtShop.setText(orderList.get(position).getShopName());
        viewHolder.txtAddress.setText(orderList.get(position).getAddress());
        viewHolder.txtOrderId.setText("Or No : " + orderList.get(position).getOrderId() + "");
        viewHolder.txtOrderDate.setText(CommonUtilities.longToDate(orderList.get(position).getOrderDate()));


        return convertView;
    }
}
