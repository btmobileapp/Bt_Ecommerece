package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;

/**
 * Created by bt on 08/24/2016.
 */
public class UserAdapter extends ArrayAdapter
{
    Context context;
    ArrayList<UserShopBean> arrayList;

    public UserAdapter(Context context, int resource, ArrayList<UserShopBean> objects) {
        super(context, resource, objects);
        this.context=context;
        arrayList=objects;
    }

    class ViewHolder
    {
        TextView name, shopName,txtCityy;
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
        viewHolder.txtCityy=(TextView)convertView.findViewById(R.id.txtCityy);


        viewHolder.shopName.setText(arrayList.get(position).shopdetails.getShopName());
        viewHolder.name.setText(arrayList.get(position).user.getName());
        viewHolder.txtCityy.setText(arrayList.get(position).shopdetails.getAddress());



        return convertView;
    }
}
