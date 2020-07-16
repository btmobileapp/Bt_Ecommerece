package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
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
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.beans.ProductBean;

/**
 * Created by bt on 09/03/2016.
 */
public class ProductGridAdapter extends ArrayAdapter
{
    ArrayList<ProductBean> productList;
    Context context;
    ModulePrice modulePrice;


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    public ProductGridAdapter(Context context, int resource, ArrayList<ProductBean> arrayList)
    {
        super(context,resource,arrayList);
        this.context=context;
        this.productList=arrayList;

        doption = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.bgpaker)
                .showImageOnFail(R.drawable.bgpaker)
                .showStubImage(R.drawable.bgpaker).
                        cacheInMemory(true)
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        modulePrice=new ModulePrice(context);
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)//position==ShowChapter.loading)
        {
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            holder = new ViewHolder();

            holder.t1= (TextView) convertView.findViewById(R.id.textView1);
            holder.t1= (TextView) convertView.findViewById(R.id.textView1);
            holder.t2= (TextView) convertView.findViewById(R.id.textView2);
            holder.picture= (ImageView) convertView.findViewById(R.id.picture);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductBean rowItem=productList.get(position) ;
        holder.t1.setText(rowItem.getProductName()+"\n"+rowItem.productId);
        holder.t2.setText(""+((int)(double)modulePrice.getPriceByPriceId(rowItem.getPriceId())));
        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
                productList.get(position).iconThumb
                ,
                holder.picture, doption, animateFirstListener);

        return convertView;
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
                if (firstDisplay)
                {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
