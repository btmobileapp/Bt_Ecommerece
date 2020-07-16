package biyaniparker.com.parker.beans;

import android.content.Context;

import biyaniparker.com.parker.bal.ModuleCategory;
import biyaniparker.com.parker.bal.ModuleProduct;
import biyaniparker.com.parker.database.ItemDAOProduct;

/**
 * Created by bt18 on 09/17/2016.
 */
public class DispatchDetailBean  implements Cloneable
{
    public int quantity, orderDetailId, sizeId, productId,dispatchDetailId, dispatchId;
    public int actualQnty;

    public int orderQnty;
    public String deleteStatus,dispatchStatus,productName,ConsumerPrice,dealerPrice,sizeName;


    //**********************************
    public String categoryName;
    public int CateId;




    //************************************************************
           //    newly     addedd    16-Nov-2016

        public int moveStockQnt,deleteStockQnt;


    //************************************************************
    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    public String getCategoryName(Context context)
    {
        ModuleProduct moduleProduct=new ModuleProduct(context);
        ModuleCategory moduleCategory =new ModuleCategory(context);

        CateId=moduleProduct.getProductBeanByProductId(productId).getCategoryId();

        categoryName= moduleCategory.getCategoryName(CateId);


        return categoryName;
    }
    //*********************************

    public int getOrderQnty() {
        return orderQnty;
    }

    public void setOrderQnty(int orderQnty) {
        this.orderQnty = orderQnty;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getDealerPrice() {
        return dealerPrice;
    }

    public void setDealerPrice(String dealerPrice) {
        this.dealerPrice = dealerPrice;
    }

    public String getConsumerPrice() {
        return ConsumerPrice;
    }

    public void setConsumerPrice(String consumerPrice) {
        ConsumerPrice = consumerPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public int getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(int dispatchId) {
        this.dispatchId = dispatchId;
    }

    public int getDispatchDetailId() {
        return dispatchDetailId;
    }

    public void setDispatchDetailId(int dispatchDetailId) {
        this.dispatchDetailId = dispatchDetailId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getActualQnty() {
        return actualQnty;
    }

    public void setActualQnty(int actualQnty) {
        this.actualQnty = actualQnty;
    }



    public String toString()
    {

        return categoryName+" -  "+getConsumerPrice()+" - "+sizeName+" - "+quantity+"\n";
    }
}
