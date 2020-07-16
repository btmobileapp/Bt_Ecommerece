package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 09/27/2016.
 */
public class RecentDispatchAdapter extends ArrayAdapter
{
    Context context;
    ArrayList<DispatchMasterAndDetails> list;


    public RecentDispatchAdapter(Context context, int resource, ArrayList<DispatchMasterAndDetails> objects)
    {
        super(context, resource, objects);
        this.context=context;
        list=objects;
    }


    class ViewHolder
    {
        TextView txtShopName, txtAddress, txtDispatchNo, txtOrderNo, txtTAmount,txtDisDate;
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
        holder.txtTAmount=(TextView)convertView.findViewById(R.id.txtAmount);
        holder.txtDispatchNo=(TextView)convertView.findViewById(R.id.txtDispatchNo);
        holder.txtOrderNo=(TextView)convertView.findViewById(R.id.txtOrderNo);
        holder.txtDisDate=(TextView)convertView.findViewById(R.id.txtDispatchDate);

        holder.txtAddress.setText(list.get(position).master.address);
        holder.txtDisDate.setText(CommonUtilities.longToDate(list.get(position).master.dispatchDate));
        holder.txtDispatchNo.setText("Dispatch Id :"+list.get(position).master.dispatchId);
        holder.txtOrderNo.setText("Or No :"+list.get(position).master.orderId);
        holder.txtShopName.setText(list.get(position).master.shopName);

  /*      int totalUnDispatchedQnty=0;
        int totalOrder=0;
        int totalQnty=0;*/


       /* DispatchMasterAndDetails tmp=list.get(position);
        for (int i=0;i<tmp.details.size();i++)
        {

            totalOrder=totalOrder+tmp.details.get(i).orderQnty;
            totalQnty=totalQnty+(tmp.details.get(i).quantity);
            totalUnDispatchedQnty=totalUnDispatchedQnty+((list.get(position).details.get(i).orderQnty)-(list.get(position).details.get(i).quantity));
        }
*/

        holder.txtTAmount.setText(""+(int)list.get(position).master.getTotolAmount()+" Rs");


        return convertView;
    }
}
