package biyaniparker.com.parker.view.adapter;

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
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.CategoryBean;

/**
 * Created by bt on 08/11/2016.
 */
public class CategoryAdapter  extends ArrayAdapter {
    Context context;
    ArrayList<CategoryBean> categoryList;



    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
     //*************************************************


    public CategoryAdapter(Context context, int resource, ArrayList<CategoryBean> categoryBeanArrayList) {
        super(context, resource,categoryBeanArrayList);
        this.context=context;
        categoryList=categoryBeanArrayList;

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
        ;
        // RoundedBitmapDisplayer


        animateFirstListener=new AnimateFirstDisplayListener();
    }

    class ViewHolder
    {
        TextView tv;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // TODO Auto-generated method stub
        final ViewHolder holder = new ViewHolder();
        // v=holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_o_category, null);


            convertView.setTag(holder );

        }
        else
        {
            convertView.setTag(holder);
        }

        holder.tv = (TextView) convertView.findViewById(R.id.textView);
        // holder.im=(ImageView) convertView.findViewById(R.id.imageView1);
        holder.tv.setText( categoryList.get(position).categoryName);
        holder.imageView=(ImageView)convertView.findViewById(R.id.imageView);



        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
             categoryList.get(position).icon
                ,
                holder.imageView, doption, animateFirstListener);


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
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }








}
