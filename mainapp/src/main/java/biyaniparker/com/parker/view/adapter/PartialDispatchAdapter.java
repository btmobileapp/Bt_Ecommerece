package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;

/**
 * Created by bt on 09/24/2016.
 */
public class PartialDispatchAdapter extends ArrayAdapter
{
    Context context;
    ArrayList <DispatchMasterAndDetails> list;


    public PartialDispatchAdapter(Context context, int resource, ArrayList<DispatchMasterAndDetails> objects)
    {
        super(context, resource, objects);
        this.context=context;
        this.list=objects;
    }


    class ViewHolder
    {
        TextView txtShopName, txtAddress, txtDispatchNo, txtOrderNo, txtUnDisQnty,txtDisDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=new ViewHolder();
        convertView=null;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.o_item_recent_dispatch, null);
            convertView.setTag(holder );
        }
        else
        {
            convertView.setTag(holder );
        }


        holder.txtShopName=(TextView)convertView.findViewById(R.id.txtCustomer);
        holder.txtAddress=(TextView)convertView.findViewById(R.id.txtAddress);
        holder.txtUnDisQnty=(TextView)convertView.findViewById(R.id.txtAmount);
        holder.txtDispatchNo=(TextView)convertView.findViewById(R.id.txtDispatchNo);
        holder.txtOrderNo=(TextView)convertView.findViewById(R.id.txtOrderNo);
        holder.txtDisDate=(TextView)convertView.findViewById(R.id.txtDispatchDate);

        holder.txtAddress.setText(list.get(position).master.address);
        holder.txtDisDate.setText(CommonUtilities.longToDate(list.get(position).master.dispatchDate));
        holder.txtDispatchNo.setText("Dispatch Id :"+list.get(position).master.dispatchId);
        holder.txtOrderNo.setText("Or No :"+list.get(position).master.orderId);
        holder.txtShopName.setText(list.get(position).master.shopName);

        int totalUnDispatchedQnty=0;
        int totalOrder=0;
        int totalQnty=0;







        DispatchMasterAndDetails tmp=list.get(position);
        for (int i=0;i<tmp.details.size();i++)
        {

            totalOrder=totalOrder+tmp.details.get(i).orderQnty;
            totalQnty=totalQnty+(tmp.details.get(i).quantity);
            //int toStockQnty=tmp.details.get(i).getDispatchStatus().equals("tostock")?tmp.details.get(i).getQuantity():0;
            totalUnDispatchedQnty=totalUnDispatchedQnty+((list.get(position).details.get(i).orderQnty)/*-(list.get(position).details.get(i).quantity)-toStockQnty*/);
        }


        holder.txtUnDisQnty.setText("Undispatched Qnty : "+(int)(totalOrder - totalQnty));


        return convertView;
    }
}
