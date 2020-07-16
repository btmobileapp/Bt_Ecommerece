package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.database.ItemDAOPrice;


public class ProductRandomAdapter extends ArrayAdapter implements OnClickListener
{
	//http://www.sidtech.co/Terms&Conditions.html
    public ArrayList<String> ch=new ArrayList<String>();
	Context context;
	ArrayList<ProductBeanWithQnty> subList;


	//*********************Copy This  ******************
	DisplayImageOptions doption = null;
	private AnimateFirstDisplayListener animateFirstListener;
	private ImageLoader imageLoader;
	//*************************************************



	public ProductRandomAdapter(Context context, int resourceId, ArrayList<ProductBeanWithQnty> subbean)
	{
		super(context, resourceId, subbean);
		this.context=context;
		this.subList=subbean;


		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.bgpaker)
				.showImageOnFail(R.drawable.bgpaker)
				.showStubImage(R.drawable.bgpaker).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)) // 100
						// for
						// Rounded
						// Image
				.cacheOnDisc(true)
						//.imageScaleType(10)
				.build();
		
	}
	private class ViewHolder 
	{
		TextView t1,t2;
     	ImageView picture;
 	
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
	  
		//convertView=null;
		
		    ViewHolder holder = null;	 
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	      
	        if(convertView == null)//position==ShowChapter.loading)
	        {	
	            convertView = mInflater.inflate(R.layout.gridview_item, null);
		        holder = new ViewHolder();
		                 
		        holder.t1= (TextView) convertView.findViewById(R.id.textView1);
				//holder.t1= (TextView) convertView.findViewById(R.id.textView1);
				holder.t2= (TextView) convertView.findViewById(R.id.textView2);
				holder.picture= (ImageView) convertView.findViewById(R.id.picture);

		            convertView.setTag(holder);
	        }   
	        else
	        {
	        	    holder = (ViewHolder) convertView.getTag();
	        }
	        	  
	        	    ProductBeanWithQnty rowItem=subList.get(position) ;

	        	  if(rowItem!=null) {
					  holder.t1.setText(rowItem.getProductName());
					  ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
					  try
					  {
						  double price = itemDAOPrice.getPriceBeanByPriceId(rowItem.getPriceId()).consumerPrice;
						  holder.t2.setText("Rs. " + (int) price);
					  }
					  catch (Exception e){  holder.t2.setText("Rs. ");}
					  //holder.picture.setImageResource(rowItem.imgrs);

					  imageLoader = ImageLoader.getInstance();
					  //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
					  imageLoader.displayImage(
							  rowItem.iconThumb
							  ,
							  holder.picture, doption, animateFirstListener);
				  }


		return convertView;
	}
	@Override
	public void onClick(View v)
    {
		// TODO Auto-generated method stub
	
	}



	private  class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
									  Bitmap loadedImage) {
			if (loadedImage != null)
			{
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}	