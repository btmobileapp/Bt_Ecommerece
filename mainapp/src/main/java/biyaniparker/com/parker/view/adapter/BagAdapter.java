package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.BagMasterBean;

/**
 * Created by bt on 09/09/2016.
 */
public class BagAdapter extends ArrayAdapter
{
    Context context;
    ArrayList<BagMasterBean> bagList=new ArrayList<BagMasterBean>();

    public BagAdapter(Context context, int resource, ArrayList<BagMasterBean> objects)
    {
        super(context, resource, objects);

        this.context=context;
        this.bagList=objects;
    }



    public class ViewHolder
    {
        ImageView img;
        TextView txtName,txtCPrice, txtTPrice;
        CheckBox chk;
        LinearLayout linear;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView==null)
        {
            convertView= mInflater.inflate(R.layout.o_activity_bag_adapter,null);
            holder=new ViewHolder();


            holder.chk=(CheckBox)convertView.findViewById(R.id.chk);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.linear=(LinearLayout)convertView.findViewById(R.id.linear);
            holder.txtCPrice=(TextView)convertView.findViewById(R.id.txtCPrice);
            holder.txtTPrice=(TextView)convertView.findViewById(R.id.txtTPrice);
            holder.txtName=(TextView)convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}
