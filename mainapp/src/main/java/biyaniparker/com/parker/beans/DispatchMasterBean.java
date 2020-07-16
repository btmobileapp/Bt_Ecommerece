package biyaniparker.com.parker.beans;

import java.util.ArrayList;

/**
 * Created by bt on 08/23/2016.
 */
public class DispatchMasterBean
{
    public int dispatchId, dispatchNo, challanNo,orderId ;
    public long dispatchDate;
    public long challanDate;

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public long orderDate;
    public long customerId;
    public long dispatchBy;
    public long checkedBy;
    public long receivedBy;
    public long userId;
    public long changeBy;
    public long enterDate;
    public long changedDate;
    public String transport;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String name;
    public String shopName;
    public String address;
    public String parcel;
    public String vatTinNo;
    public String cstTinNo;

    public String getDispatchStatus()
    {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String dispatchStatus ;
    public double totolAmount;

    public ArrayList<DispatchDetailBean> dispatchDetails;

    public DispatchMasterBean() {
    }

    public int getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(int dispatchId) {
        this.dispatchId = dispatchId;
    }

    public int getDispatchNo() {
        return dispatchNo;
    }

    public void setDispatchNo(int dispatchNo) {
        this.dispatchNo = dispatchNo;
    }

    public int getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(int challanNo) {
        this.challanNo = challanNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(long dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public long getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(long challanDate) {
        this.challanDate = challanDate;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getDispatchBy() {
        return dispatchBy;
    }

    public void setDispatchBy(long dispatchBy) {
        this.dispatchBy = dispatchBy;
    }

    public long getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(long checkedBy) {
        this.checkedBy = checkedBy;
    }

    public long getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(long receivedBy) {
        this.receivedBy = receivedBy;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(long changeBy) {
        this.changeBy = changeBy;
    }

    public long getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(long enterDate) {
        this.enterDate = enterDate;
    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getParcel() {
        return parcel;
    }

    public void setParcel(String parcel) {
        this.parcel = parcel;
    }

    public String getVatTinNo() {
        return vatTinNo;
    }

    public void setVatTinNo(String vatTinNo) {
        this.vatTinNo = vatTinNo;
    }

    public String getCstTinNo() {
        return cstTinNo;
    }

    public void setCstTinNo(String cstTinNo) {
        this.cstTinNo = cstTinNo;
    }

    public double getTotolAmount() {
        return totolAmount;
    }

    public void setTotolAmount(double totolAmount) {
        this.totolAmount = totolAmount;
    }


}
