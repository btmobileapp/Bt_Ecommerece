package biyaniparker.com.parker.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.ReportProductStockBean;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;
import biyaniparker.com.parker.view.reports.DynamicCategoryReport;

/**
 * Created by bt on 12/15/2016.
 */
public class StockReportAdapter extends ArrayAdapter
{
    Context context;
    List<ReportProductStockBean> objects;

    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************
    public StockReportAdapter(Context context, int resource, List<ReportProductStockBean> objects)
    {
        super(context, resource, objects);
        this.objects=objects;
        this.context=context;
    }


    private class ViewHolder
    {
        TextView txtCatName, txtPName, txtMrp, txtSize, txtQnty;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder=new ViewHolder();

        convertView=null;
        if(convertView == null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.o_item_product_stock_report, null);
            convertView.setTag(viewHolder);
        }
        else
        {
            convertView.setTag(viewHolder);
        }

        try {
            viewHolder.txtPName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtMrp = (TextView) convertView.findViewById(R.id.mrp);
            viewHolder.txtQnty = (TextView) convertView.findViewById(R.id.qnty);
            viewHolder.txtSize = (TextView) convertView.findViewById(R.id.size);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.design);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewProductImage.class);
                    intent.putExtra("path", objects.get(position).getIconThumb());
                    ((Activity) context).startActivity(intent);

                }
            });

            // txtCatName.setText(""+reportBean.getCategoryName());
            viewHolder.txtMrp.setText("" + (int) objects.get(position).getConsumerPrice());
            viewHolder.txtPName.setText("" + objects.get(position).getProductName());
            viewHolder.txtQnty.setText("" + objects.get(position).getQnty());
            viewHolder.txtSize.setText("" + objects.get(position).getSizeName());

            imageLoader = ImageLoader.getInstance();
            //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
            imageLoader.displayImage(
                    objects.get(position).getIconThumb()
                    ,
                    viewHolder.img, doption, animateFirstListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
