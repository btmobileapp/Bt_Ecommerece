package biyaniparker.com.parker.view.adapter;

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

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.DispatchDetailBean;

/**
 * Created by bt on 09/26/2016.
 */
public class PartailDispachDetailAdapter extends ArrayAdapter
{
        Context context;
    ArrayList<DispatchDetailBean> details;
    ArrayList<DispatchDetailBean> sortedDetailList;

    public PartailDispachDetailAdapter(Context context, int resource, ArrayList<DispatchDetailBean> objects)
    {
        super(context, resource, objects);
        this.context=context;
        this.details=objects;
        sortList();
    }

    private void sortList()
    {
        for(int i=0;i<details.size();i++)
        {
            DispatchDetailBean detailBean=details.get(i);
            int flag=0;
            for(int j=0;j<sortedDetailList.size();j++)
            {
                if(detailBean.getProductId()==sortedDetailList.get(j).getProductId())
                {
                    flag=1;
                    break;
                }
            }
            if(flag==0)
            {
                sortedDetailList.add(detailBean);
            }
        }
    }

    class ViewHolder
    {
        ImageView img;
        TextView txtPName, txtPrice, txtTPrice;
        LinearLayout linear;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=new ViewHolder();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.o_item_partial_dispatch_details, null);
            convertView.setTag(holder);
        }
        else
        {
            convertView.setTag(holder);
        }
        holder.img= (ImageView) convertView.findViewById(R.id.img);
        holder.txtPName=(TextView)convertView.findViewById(R.id.txtName);
        holder.txtPrice=(TextView)convertView.findViewById(R.id.txtCPrice);
        holder.txtTPrice=(TextView)convertView.findViewById(R.id.txtTPrice);
        holder.linear=(LinearLayout)convertView.findViewById(R.id.linear);

            for(int i=0;i<sortedDetailList.size();i++)
            {
                DispatchDetailBean detailBean=sortedDetailList.get(i);

                holder.txtPName.setText(detailBean.getProductName());
                holder.txtPrice.setText(detailBean.getConsumerPrice()+" Rs");
                //holder.txtTPrice.setText(detailBean.get);
                int totalQnty=0;
                for(int j=0;j<details.size();j++)
                {
                    if(detailBean.getProductId()==details.get(j).getProductId())
                    {
                        View sizeView=inflater.inflate(R.layout.o_item_partial_size_details, null);
                        TextView txtSizeName=(TextView)sizeView.findViewById(R.id.txtsize);
                        TextView txtQntye=(TextView)sizeView.findViewById(R.id.txtsqnty);
                        CheckBox checkBox=(CheckBox)sizeView.findViewById(R.id.chkSelect);
                        totalQnty++;
                        txtSizeName.setText(details.get(j).getSizeName());
                        txtQntye.setText(details.get(j).getQuantity());
                        holder.linear.addView(sizeView);
                    }
                }
                holder.txtTPrice.setText("Total : "+(totalQnty*Integer.parseInt(detailBean.getConsumerPrice())));
            }

        return convertView;
    }
}
