package biyaniparker.com.parker.view.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import biyaniparker.com.parker.beans.EmptyProductStockBean;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

/**
 * Created by bt on 09/09/2016.
 */
public class EmptyStockAdapter extends ArrayAdapter
{
    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    Context context;
    ArrayList<EmptyProductStockBean> bagList=new ArrayList<EmptyProductStockBean>();

    public EmptyStockAdapter(Context context, int resource, ArrayList<EmptyProductStockBean> objects)
    {
        super(context, resource, objects);
        this.context=context;
        this.bagList=objects;

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



    public class ViewHolder
    {
        ImageView img;
        TextView txtName,txtCPrice;
        CheckBox chk;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView=null;
        if(convertView==null)
        {
            convertView= mInflater.inflate(R.layout.item_emptystock,null);
            holder=new ViewHolder();
            holder.chk=(CheckBox)convertView.findViewById(R.id.chk);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.txtCPrice=(TextView)convertView.findViewById(R.id.txtCPrice);
            holder.txtName=(TextView)convertView.findViewById(R.id.txtName);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtCPrice.setText("Consumer Price :- "+bagList.get(position).ConsumerPrice+"\nDealer Price :-"+bagList.get(position).DealerPrice);
        holder.txtName.setText(bagList.get(position).ProductName);
        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(bagList.get(position).IconThumb,holder.img, doption, animateFirstListener);
         if(bagList.get(position).isChecked)
         {
                 holder.chk.setChecked(true);
         }
        holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                bagList.get(position).isChecked=isChecked;
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ViewProductImage.class);
                intent.putExtra("path",  bagList.get(position).IconThumb);
                context.startActivity(intent);
            }
        });
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
