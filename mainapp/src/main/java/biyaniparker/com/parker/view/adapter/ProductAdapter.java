package biyaniparker.com.parker.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.BitmapUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;

/**
 * Created by bt on 08/13/2016.
 */
public class ProductAdapter extends ArrayAdapter {

    ArrayList<ProductBean> productList;
    Context context;


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************



    public ProductAdapter(Context context, int resource, ArrayList<ProductBean> productList)
    {
        super(context, resource,productList);
        this.productList=productList;
        this.context=context;
        utilities=new BitmapUtilities();
        animateFirstListener=new AnimateFirstDisplayListener();
        doption=new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.bgpaker).
                showImageOnFail(R.drawable.bgpaker).
                showStubImage(R.drawable.bgpaker).
                cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5)).cacheOnDisc(true).build();
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
        holder.tv.setText( productList.get(position).productName);
        holder.imageView=(ImageView)convertView.findViewById(R.id.imageView);
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.bt.setText(studentslist.get(position).name);
//            }
//        });
        try
        {
           // String str = DateAndOther.convertToDMY(productList.get(position).createdDate);
            Date d=new Date(productList.get(position).createdDate);
            holder.tv.setText(productList.get(position).productName+"\n"+d.toString()+"\n"+productList.get(position).createdDate);
        }
        catch (Exception ex){}

        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
                productList.get(position).iconThumb
                ,
                holder.imageView, doption, animateFirstListener);

        return convertView;
    }






    //new class AnimateFirstDisplayListener
   BitmapUtilities utilities;
    private  class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {
        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            Log.d("VK","IMG");
            if (loadedImage != null)
            {
              //  utilities.saveToExtenalForParker(loadedImage,context,imageUri);
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
