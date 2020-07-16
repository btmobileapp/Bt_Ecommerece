package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleDispatch;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.view.homeuser.productdshopping.ViewProductImage;

public class PartialDispatchDetailNew extends AppCompatActivity implements View.OnClickListener, DownloadUtility
{


    //*********************Copy This  ******************
    DisplayImageOptions doption = null;
    private AnimateFirstDisplayListener animateFirstListener;
    private ImageLoader imageLoader;
    //*************************************************

    TextView txtShopname, txtOrderNo, txtAddress, txtOrDate, txtUnDisQnty;
    Button btnSave;
    ArrayList<DispatchDetailBean> details;
    ArrayList<DispatchDetailBean> sortedDetailList;
    LinearLayout linear;
    ArrayList<View> viewList=new ArrayList<>();

    LinearLayout linearLayout2;

    ModuleDispatch moduleDispatch;
    ModuleProduct moduleProduct;
    LinearLayout mainlinearDetails;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_partial__dispatch__detail);
        mainlinearDetails=(LinearLayout)findViewById(R.id.linearDetails);
        linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);

        getSupportActionBar().setTitle("Partial Dispatch Details ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


            moduleDispatch=new ModuleDispatch(this);
            moduleProduct=new ModuleProduct(this);
            details = new ArrayList<>();
            sortedDetailList = new ArrayList<>();
            txtShopname = (TextView) findViewById(R.id.txtShopName);
            txtOrderNo = (TextView) findViewById(R.id.txtOrNo);
            txtAddress = (TextView) findViewById(R.id.txtAddress);
            txtOrDate = (TextView) findViewById(R.id.txtdate);
            txtUnDisQnty = (TextView) findViewById(R.id.txtUndisp);
            linear = (LinearLayout) findViewById(R.id.linearDetails);
            btnSave = (Button) findViewById(R.id.txtSave);
            btnSave.setText("Submit");
            btnSave.setOnClickListener(this);


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




        Gson gson = new Gson();                                       // gets the partial dispatch  details
            Intent intent = getIntent();
            String str = intent.getStringExtra("str");
            DispatchMasterAndDetails bean = gson.fromJson(str, DispatchMasterAndDetails.class);
           // Toast.makeText(this, " Bean" + bean.master.dispatchId, Toast.LENGTH_LONG).show();

            details.clear();
            details.addAll(bean.details);


            txtShopname.setText(bean.master.shopName);
            txtOrderNo.setText("Order No : " + bean.master.orderId);
            txtAddress.setText(bean.master.address);
            txtOrDate.setText("" + CommonUtilities.longToDate(bean.master.dispatchDate));

            int total = 0;
            for (int i = 0; i < bean.details.size(); i++)                                 // counts the number of total updispatched quantity
            {
                DispatchDetailBean detailBean = bean.details.get(i);
                //int toStockQnty=bean.details.get(i).getDispatchStatus().equals("tostock")?bean.details.get(i).getQuantity():0;
                total += detailBean.orderQnty - detailBean.quantity;//-toStockQnty;
            }

            txtUnDisQnty.setText("Undispatch Qnty : " + total);


            //   code to inflate layout

            //sortng

            for (int i = 0; i < details.size(); i++)
            {
                DispatchDetailBean detailBean = details.get(i);
                int flag = 0;
                for (int j = 0; j < sortedDetailList.size(); j++)
                {
                    if (detailBean.getProductId() == sortedDetailList.get(j).getProductId()) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0)
                {
                    sortedDetailList.add(detailBean);
                }
            }


            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);




            for (int i = 0; i < sortedDetailList.size(); i++)
            {
                DispatchDetailBean detailBean = sortedDetailList.get(i);
                View convertView = inflater.inflate(R.layout.o_item_partial_dispatch_details_new, null);
                final ProductBean prodBean=moduleProduct.getProductBeanByProductId(detailBean.getProductId());
                ImageView img = (ImageView) convertView.findViewById(R.id.img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(PartialDispatchDetailNew.this,ViewProductImage.class);
                        intent.putExtra("path", prodBean.getIconThumb());
                        startActivity(intent);
                    }
                });
                TextView txtPName = (TextView) convertView.findViewById(R.id.txtName);
                TextView txtPrice = (TextView) convertView.findViewById(R.id.txtCPrice);
                TextView txtTPrice = (TextView) convertView.findViewById(R.id.txtTPrice);
                LinearLayout linear = (LinearLayout) convertView.findViewById(R.id.linear);





                txtPName.setText(detailBean.getProductName());
                txtPrice.setText(detailBean.getConsumerPrice() + " Rs");
                //holder.txtTPrice.setText(detailBean.get);
                int totalQnty = 0;
                for (int j = 0; j < details.size(); j++) {
                    if (detailBean.getProductId() == details.get(j).getProductId()&& details.get(j).getDispatchStatus().equals("partial") )  //&&(detailBean.dispatchStatus.equals("partial")))
                    {


                        View sizeView = inflater.inflate(R.layout.o_item_partial_size_details_new, null);
                        TextView txtSizeName = (TextView) sizeView.findViewById(R.id.txtsize);
                        TextView txtQntye = (TextView) sizeView.findViewById(R.id.txtsqnty);
                      //  CheckBox checkBox = (CheckBox) sizeView.findViewById(R.id.chkSelect);
                        totalQnty++;
                        txtSizeName.setText(details.get(j).getSizeName());
                        txtQntye.setText(details.get(j).getOrderQnty()- details.get(j).getQuantity()+ "");
                        sizeView.setTag(details.get(j));
                        linear.addView(sizeView);

                        viewList.add(sizeView);
                    }
                    //linear.addView(linear);
                }
                txtTPrice.setText("Total : " + (totalQnty * Integer.parseInt(detailBean.getConsumerPrice())));
                mainlinearDetails.addView(convertView);



                imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(
                        prodBean.getIconThumb()
                        ,
                        img, doption, animateFirstListener);


            }



        scrollView=(ScrollView)findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
              //  int scrollY = rootScrollView.getScrollY(); // For ScrollView
           //     int scrollX = rootScrollView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
             // CommonUtilities.hideatInItInputBoard(PartialDispatchDetailNew.this);
               // Toast.makeText(PartialDispatchDetailNew.this, "Scroll", Toast.LENGTH_SHORT).show();
                linearLayout2.requestFocus();
              //  txtShopname.setRe
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {

        int flag =0;
        CommonUtilities.hideSoftKeyBord(this);
        ArrayList<DispatchDetailBean> selected=new ArrayList<>();
            for(int i=0;i<viewList.size();i++)
            {
                View view =viewList.get(i);
                final EditText  edToStock=(EditText)view.findViewById(R.id.edToStock);
                final  EditText  edToDelete=(EditText)view.findViewById(R.id.edToDelete);
                TextView txtQntye = (TextView) view.findViewById(R.id.txtsqnty);
                int qnt=Integer.parseInt(txtQntye.getText().toString());
                int deleqnt=0,stockqnt=0;
                if(!edToStock.getText().toString().trim().equalsIgnoreCase(""))
                {
                    stockqnt=Integer.parseInt(edToStock.getText().toString());
                }
                if(!edToDelete.getText().toString().trim().equalsIgnoreCase(""))
                {
                    deleqnt=Integer.parseInt(edToDelete.getText().toString());
                }



                if(stockqnt+deleqnt<=qnt)
                {

                }
                else
                {

                    if(!edToStock.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        edToStock.setError("Invalid Qny");
                        flag=1;
                        edToStock.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            edToStock.setError(null);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }

                    if(!edToDelete.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        edToDelete.setError("Invalid Qny");
                        flag=1;
                        edToDelete.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                edToDelete.setError(null);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }

                }


                if(deleqnt>0 || stockqnt>0)
                {
                    DispatchDetailBean mBean= (DispatchDetailBean) viewList.get(i).getTag();
                    mBean.deleteStockQnt=deleqnt;
                    mBean.moveStockQnt=stockqnt;
                    selected.add(mBean);
                }



                /*
                CheckBox chk= (CheckBox) view.findViewById(R.id.chkSelect);
                if(chk.isChecked())
                {
                    selected.add((DispatchDetailBean) viewList.get(i).getTag());
                }
                */

            }

        if(flag==0)
        if(selected.size()>0)
        try
        {
            moduleDispatch.deletePartialNew(selected);
            throw new JSONException("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(responseCode==200 && requestCode==2)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Partially undispatched products added to stock successfully  ");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PartialDispatchDetailNew.this,PartialDispatchListView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            });

            alertDialog.show();

        }

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
