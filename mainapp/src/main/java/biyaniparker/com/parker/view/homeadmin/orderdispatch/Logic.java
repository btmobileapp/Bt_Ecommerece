package biyaniparker.com.parker.view.homeadmin.orderdispatch;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.DeliveryChallanBean;
import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.SizeAndQnt;

/**
 * Created by bt18 on 09/29/2016.
 */
public class Logic
{
   // public DispatchMasterAndDetails  obj;
    public ArrayList<DispatchDetailBean>  list;
    public ArrayList<DispatchDetailBean>  summaryList=new ArrayList<>();
    public ArrayList<DeliveryChallanBean> deliverChallanList=new ArrayList<>();



    public void getSummary(Context context)
    {
        summaryList.clear();
        for(int i=0;i<list.size();i++)
        {
            DispatchDetailBean var1=list.get(i);
            list.get(i).getCategoryName(context);
            int flag =0;
            int jflagindex=0;
            for(int j=0;j<summaryList.size();j++)
            {
                DispatchDetailBean var2=summaryList.get(j);

                if(var2.categoryName.equals(var1.categoryName))
                {
                    if(var2.ConsumerPrice.equalsIgnoreCase(var1.ConsumerPrice))
                    {
                        if(var2.getSizeId()==var1.getSizeId())
                        {
                            flag=1;
                            jflagindex=j;
                            break;
                        }
                    }
                }
            }

            if(flag==0)
            {
                summaryList.add(list.get(i));
            }
            else
            {
                DispatchDetailBean temp=   summaryList.get(jflagindex);
                temp.quantity=  temp.getQuantity()+list.get(i).getQuantity();
            }
        }
    }
    public void rowsToColumn(Context context)
    {




       for(int i=0;i<summaryList.size();i++)
       {
           DispatchDetailBean var1=summaryList.get(i);
           int flag=0,jFlagIndex=0;
           for(int j=0;j<deliverChallanList.size();j++)
           {
                   if( var1.getCategoryName(context).equalsIgnoreCase(deliverChallanList.get(j).CategoryName))
                   {
                       if( var1.getConsumerPrice().equalsIgnoreCase(deliverChallanList.get(j).Price))
                       {
                           flag = 1;
                           jFlagIndex = j;
                       }
                   }
           }
           if(flag==0)
           {
               DeliveryChallanBean bean=new DeliveryChallanBean();
               bean.CategoryName=var1.categoryName;
               bean.parentId=var1.CateId;


               bean.Price=var1.ConsumerPrice;
               ArrayList<SizeAndQnt> sizeQnts=new ArrayList<>();
               SizeAndQnt o=new SizeAndQnt();
               o.SizeName=var1.sizeName;
               o.Qnt=var1.quantity;
               sizeQnts.add(o);
               bean.sizesandqnts.add(o);
               bean.colCnt++;
               deliverChallanList.add(bean);
           }
           else
           {
               DeliveryChallanBean bean=  deliverChallanList.get(jFlagIndex);
               ArrayList<SizeAndQnt> sizeQnts=new ArrayList<>();
               SizeAndQnt o=new SizeAndQnt();
               o.SizeName=var1.sizeName;
               o.Qnt=var1.quantity;
               sizeQnts.add(o);
               bean.colCnt++;
               bean.sizesandqnts.add(o);

           }
       }
    }




    HashMap<CategoryBean,ArrayList<DeliveryChallanBean>> map;
    public  void mapDeliveryChallanTOParentCategories(Context context)
    {
        //ArrayList<DeliveryChallanBean> list=new ArrayList<>();

        map=new HashMap<>();
        ModuleCategory moduleCategory=new ModuleCategory(context);
        moduleCategory.getParentCategoryList();
        ModuleProduct moduleProduct=new ModuleProduct(context);
        ArrayList<CategoryBean>  cateList= moduleCategory.parentList;
        for(int i=0;i<cateList.size();i++)
        {
            ArrayList<DeliveryChallanBean> innerList=new ArrayList<>();
            for(int j=0;j<deliverChallanList.size();j++)
            {
                int id1=moduleProduct.getParentCategoryId( deliverChallanList.get(j).parentId);
                if(id1 ==cateList.get(i).categoryId)
                {
                    innerList.add(deliverChallanList.get(j));
                }
            }
            map.put(cateList.get(i),innerList);


        }
    }


}






