package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.RowItem;

public class CustomAdapter extends BaseAdapter {

	Context context;
	List<RowItem> rowItem;
	//*********************Copy This  ******************
	DisplayImageOptions doption = null;
	private AnimateFirstDisplayListener animateFirstListener;
	private ImageLoader imageLoader;
	//*************************************************




	public CustomAdapter(Context context, List<RowItem> rowItem) {
		this.context = context;
		this.rowItem = rowItem;
		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.facilities)
				.showImageOnFail(R.drawable.facilities)
				.showStubImage(R.drawable.facilities).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)) // 100
						// for
						// Rounded
						// Image
				.cacheOnDisc(true)
						//.imageScaleType(10)
				.build();

	}

	private class ViewHolder {
		RelativeLayout rl;
		ImageView icon;
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		convertView = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


			return getTView(convertView, position, holder, mInflater);

	}

	View getTView(View convertView, int position, ViewHolder holder,
			LayoutInflater mInflater)
	{
		RowItem row_pos = rowItem.get(position);
		if(row_pos.isStartSection)
		{
			convertView = mInflater.inflate(R.layout.d_item_drawermenusection, null);				((TextView)convertView.findViewById(R.id.txtsection)).setText(row_pos.sectionName);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.d_item_drawermenu, null);
		}
		holder = new ViewHolder();
		holder.icon = (ImageView) convertView.findViewById(R.id.icon);
		holder.title = (TextView) convertView.findViewById(R.id.title);
		/*holder.rl = (RelativeLayout) convertView
				.findViewById(R.id.RelativeLayout1);
	*/	// holder.rl.setBackground(new ColorDrawable(Color.rgb(22,169,171)));

		// setting the image resource and title
		try {
			holder.icon.setImageResource(row_pos.getIcon());
		}
		catch (Exception e){}
		holder.title.setText(row_pos.getTitle());
		convertView.setTag(holder);

		imageLoader = ImageLoader.getInstance();
		//  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
		if(!row_pos.isLocal)
		imageLoader.displayImage(
				row_pos.getUrl()+""
				,
				holder.icon, doption, animateFirstListener);


		return convertView;
	}

	@Override
	public int getCount() {
		return rowItem.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItem.indexOf(getItem(position));
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
