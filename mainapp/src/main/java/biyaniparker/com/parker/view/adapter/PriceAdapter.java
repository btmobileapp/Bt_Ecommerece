package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.beans.UserBean;

/**
 * Created by bt on 08/29/2016.
 */
public class PriceAdapter extends ArrayAdapter
{
    Context context;
    ArrayList<PriceBean> arrayList;


    public PriceAdapter(Context context, int resource, ArrayList<PriceBean> objects) {
        super(context, resource, objects);
        this.context=context;
        arrayList=objects;
    }

    class ViewHolder
    {
        TextView name, shopName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder=new ViewHolder();
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.o_item_user_adapter, null);
            convertView.setTag(viewHolder );
        }
        else
        {
            convertView.setTag(viewHolder);
        }

        viewHolder.name=(TextView)convertView.findViewById(R.id.textName);
        viewHolder.shopName=(TextView)convertView.findViewById(R.id.textShopName);

        viewHolder.shopName.setText((int) (double) arrayList.get(position).getConsumerPrice() + " Rs");
        viewHolder.name.setText((int)(double) arrayList.get(position).getDealerPrice()+" Rs");


        return convertView;
    }
}
