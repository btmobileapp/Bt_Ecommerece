package biyaniparker.com.parker.view.reports.physical_reports;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleReport;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ReportProductStockBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.view.adapter.CategoryAdapter;
import biyaniparker.com.parker.view.adapter.ProductGridAdapter;
import biyaniparker.com.parker.view.adapter.StockReportAdapter;
import biyaniparker.com.parker.view.adapter.StockReportHashMapAdapter;
import biyaniparker.com.parker.view.reports.PrintStock;

public class DynamicCategoryPhysicalReport extends AppCompatActivity implements AdapterView.OnItemClickListener, DownloadUtility, View.OnClickListener, NotifyCallback {

    ModuleCategory moduleCategory;
    StockReportAdapter stockReportAdapter;
    int catId;
    CategoryAdapter categoryAdapter;
    ListView listView;
    ArrayList<CategoryBean> arrayList;
    ModuleReport moduleReport;
    ListView productListView;
    Button btnPdfreport;
    ProductGridAdapter productGridAdapter;
    LinearLayout linear;
    StockReportHashMapAdapter stockReportHashMapAdapter;
    boolean Pressed;
    LinkedHashMap<Integer,ArrayList<ReportProductStockBean>> mapList;


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduleCategory = new ModuleCategory(this);
        arrayList=new ArrayList<CategoryBean>();
        moduleReport=new ModuleReport(this);
        Intent intent = getIntent();
        catId = intent.getIntExtra("CategoryId", 0);


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

        imageLoader = ImageLoader.getInstance();

        //   getting child category list by parent Id
       arrayList.addAll( moduleCategory.getListByParentId(catId));
        if (arrayList.size() != 0 || catId == 0)
        {

            // its parent category
            setContentView(R.layout.o_activity_refill_list);
            getSupportActionBar().setTitle("Categories");
            getSupportActionBar().setSubtitle(catId==0?" Parent Categories":moduleCategory.getCategoryName(catId));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            listView=(ListView)findViewById(R.id.listView);
            categoryAdapter=new CategoryAdapter(this,1,arrayList);
            listView.setAdapter(categoryAdapter);
            Pressed=false;   //if false then it is parent category    else it is last cat
            listView.setOnItemClickListener(this);
        }
        else if(arrayList.isEmpty())
        {
            setContentView(R.layout.o_activity_product_stock_report);
            btnPdfreport=(Button)findViewById(R.id.btnGetPDF);
            btnPdfreport.setOnClickListener(this);
            productListView =(ListView)findViewById(R.id.listView);
            getSupportActionBar().setTitle("Product Physical Stock Report ");
            getSupportActionBar().setSubtitle(moduleCategory.getCategoryBeanById(catId).getCategoryName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            //stockReportAdapter =new StockReportAdapter(this,1,moduleReport.reportDataList);
            //productListView.setAdapter(stockReportAdapter)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ;
            moduleReport.getProductPhysicalStockReportWithNotify(UserUtilities.getClientId(this), catId);
            linear=(LinearLayout)findViewById(R.id.lSum);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(!Pressed)
        {
            CategoryBean bean = new CategoryBean();
            bean = (CategoryBean) categoryAdapter.getItem(position);
            //Toast.makeText(this," "+bean.getCategoryName(),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, DynamicCategoryPhysicalReport.class);
            intent.putExtra("CategoryId", bean.getCategoryId());
            startActivity(intent);
        }
        else if(Pressed)
        {
            ProductBean bean=new ProductBean();
            bean=(ProductBean) productGridAdapter.getItem(position);
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==2 && responseCode==200)
        {
           //stockReportAdapter.notifyDataSetChanged();
          //  notifyToActivity();
            collectHashMapList();
          stockReportHashMapAdapter =new StockReportHashMapAdapter(this,1,mapList);
            productListView.setAdapter(stockReportHashMapAdapter);
        }
        else
        {
            Toast.makeText(this, "Server connection failed .. Try again ",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v)
    {
        if(v.getId()==btnPdfreport.getId())
        {
            collectHashMapList();
            PrintStock printStock=new PrintStock(this,imageLoader,moduleReport.reportDataList,catId,"Physical_Stock");
            printStock.setFileNameAndTitle("PHYSICAL STOCK REPORT","Physical_Stock");
            printStock.call();

        }
    }

    private void collectHashMapList()
    {
         mapList= new LinkedHashMap<>();

        for (ReportProductStockBean reportBean: moduleReport.reportDataList)
        {
            if(mapList.containsKey(reportBean.getProductId()))
            {
                ArrayList<ReportProductStockBean> listTmp= mapList.get(reportBean.getProductId());
                listTmp.add(reportBean);
            }
            else
            {
                ArrayList<ReportProductStockBean> newKeyList =new ArrayList<>();
                newKeyList.add(reportBean);
                mapList.put(reportBean.getProductId(),newKeyList);
            }
        }
        int j=0;
        j++;

    }

    @Override
    public void notifyToActivity()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                stockReportAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        try {
            moduleReport.stopAsyncStreaming();
        }
        catch (Exception e){}


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
