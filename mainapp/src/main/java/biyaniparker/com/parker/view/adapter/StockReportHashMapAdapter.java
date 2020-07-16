package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.ReportProductStockBean;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

/**
 * Created by bt on 12/22/2016.
 */
public class StockReportHashMapAdapter extends ArrayAdapter
{
    Context context;
    LinkedHashMap<Integer, ArrayList<ReportProductStockBean>> objectsMap;
   List<ArrayList<ReportProductStockBean>> objects;


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    public StockReportHashMapAdapter(Context context, int resource, LinkedHashMap<Integer, ArrayList<ReportProductStockBean>> objects)
    {

        super(context, resource);

        this.context=context;
        this.objects=new ArrayList<>(objects.values());


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

        animateFirstListener=new AnimateFirstDisplayListener();

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    private class ViewHolder
    {
        TextView txtCatName, txtPName, txtMrp, txtSize, txtQnty;
        LinearLayout linear;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder=null;

        convertView=null;
        if(convertView == null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.item_report_haspmap, null);

            viewHolder.txtPName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtMrp = (TextView) convertView.findViewById(R.id.mrp);
            //viewHolder.txtQnty = (TextView) convertView.findViewById(R.id.qnty);
            //viewHolder.txtSize = (TextView) convertView.findViewById(R.id.size);
            viewHolder.linear=(LinearLayout)convertView.findViewById(R.id.linSizeQnty);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.design);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        try
        {

             ArrayList arrayList=objects.get(position);
            final ReportProductStockBean firstBean= (ReportProductStockBean) arrayList.get(0);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewProductImage.class);
                    intent.putExtra("path", firstBean.getIconThumb());
                    ((Activity) context).startActivity(intent);

                }
            });

            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int i=0;i<arrayList.size();i++)
            {
                ReportProductStockBean bean= (ReportProductStockBean) arrayList.get(i);

                View view=layoutInflater.inflate(R.layout.o_item_recent_size_details,null);
                TextView txtSize= (TextView) view.findViewById(R.id.txtsize);
                TextView txtSQnty= (TextView) view.findViewById(R.id.txtsqnty);
                txtSize.setTextColor(Color.BLACK);
                txtSQnty.setTextColor(Color.BLACK);
                txtSize.setText(""+bean.getSizeName());
                txtSQnty.setText(""+bean.getQnty());
                viewHolder.linear.addView(view);
            }

            // txtCatName.setText(""+reportBean.getCategoryName());
            viewHolder.txtMrp.setText("" + (int) firstBean.getConsumerPrice());
            viewHolder.txtPName.setText("" + firstBean.getProductName());
            //  viewHolder.txtQnty.setText("" +firstBean.getQnty());
           // viewHolder.txtSize.setText("" +firstBean.getSizeName());

            imageLoader = ImageLoader.getInstance();
            //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
            imageLoader.displayImage(
                    firstBean.getIconThumb()
                    ,
                    viewHolder.img, doption, animateFirstListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("AdpaterHashmap",e.toString());
        }

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
