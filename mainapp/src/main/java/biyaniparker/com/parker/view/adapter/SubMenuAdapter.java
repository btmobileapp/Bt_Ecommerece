package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.MenuBean;


public class SubMenuAdapter extends ArrayAdapter
{

	Context context;
	ArrayList<MenuBean> list;
	
	public SubMenuAdapter(Context context, int resource, ArrayList<MenuBean> list)
	{
		super(context, resource,list);
		// TODO Auto-generated constructor stub
		this.list=list;
		this.context=context;
	}
	
	
	class ViewHolder
	{
		TextView tv;
		ImageView im;
	
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		convertView=null;
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView=inflater.inflate(R.layout.itemsubmenu, null);
		ViewHolder holder=new ViewHolder();
		holder.tv=(TextView) convertView.findViewById(R.id.textView1);
		holder.im=(ImageView) convertView.findViewById(R.id.imageView1);
		
		convertView.setTag(holder);
		//Typeface tf=Typeface.createFromAsset(context.getAssets(), "NotoSans-Regular.ttf");
	//	holder.tv.setTypeface(tf);
		holder.tv.setText(list.get(position).nm);
		holder.im.setImageResource(list.get(position).logo);
		return convertView;
	}

}
