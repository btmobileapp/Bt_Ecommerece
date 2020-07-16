package biyaniparker.com.parker.beans;

import android.content.Context;

import java.util.ArrayList;

import biyaniparker.com.parker.bal.ModuleProduct;

/**
 * Created by bt18 on 09/30/2016.
 */
public class DeliveryChallanBean
{
    public String CategoryName,Price,TotalAmt;
    public  int colCnt=0;
    public  int parentId;
    public ArrayList<SizeAndQnt> sizesandqnts=new ArrayList<>();
    public String toString()
    {
        String str="";
        str=CategoryName+"-"+Price;
        for (int i=0;i<sizesandqnts.size();i++)
        {
            str=str+"----"+sizesandqnts.get(i).toString();
        }
        return str;
    }
    public int getRootCategoryId(Context context)
    {
        ModuleProduct moduleProduct=new ModuleProduct(context);
        return   moduleProduct.getParentCategoryId(parentId);
        //return 1;
    }


}