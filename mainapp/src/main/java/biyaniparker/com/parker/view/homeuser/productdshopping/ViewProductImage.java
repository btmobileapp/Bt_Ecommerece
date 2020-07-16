package biyaniparker.com.parker.view.homeuser.productdshopping;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.TouchImageView;
import biyaniparker.com.parker.utilities.BitmapUtilities;

public class ViewProductImage extends AppCompatActivity
{
    TouchImageView touchImag;
    //-------------------------Image Code---------------------------
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    BitmapUtilities utilities;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_image);
        touchImag=(TouchImageView)findViewById(R.id.touchImag);
        utilities=new BitmapUtilities();
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


        String path=getIntent().getExtras().getString("path");

        animateFirstListener=new AnimateFirstDisplayListener();
        imageLoader = ImageLoader.getInstance();
        //  ImageLoaderConfiguration.//408, 306, CompressFormat.JPEG, 75, null);
        imageLoader.displayImage(
              path,touchImag, doption, animateFirstListener);


        findViewById(R.id.imgclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }





    private  class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {
        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage)
        {
            if (loadedImage != null)
            {
                try {
                    utilities.saveToExtenalForParker(loadedImage, ViewProductImage.this, imageUri);
                }
                catch (Exception ex)
                {}
                try {
                    utilities.saveToExtenalForParker1(loadedImage, ViewProductImage.this, imageUri);
                }
                catch (Exception ex)
                {}
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay)
                {
                    FadeInBitmapDisplayer.animate(imageView, 0);
                    displayedImages.add(imageUri);
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                           // finish();
                        }
                    });
                }
            }).start();
        }
    }
}
