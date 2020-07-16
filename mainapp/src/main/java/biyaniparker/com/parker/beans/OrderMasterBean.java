package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/23/2016.
 */
public class OrderMasterBean
{
    private int totalQnty;

    public OrderMasterBean()
    {
    }

    public int orderId;
    public long orderDate, changeBy, changedDate, userId;
    public String orderStatus,  deleteStatus, totolAmount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String address, name, shopName;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public long getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(long changeBy) {
        this.changeBy = changeBy;
    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getTotolAmount() {
        return totolAmount;
    }

    public void setTotolAmount(String totolAmount) {
        this.totolAmount = totolAmount;
    }


    public void setTotalQnty(int totalQnty) {
        this.totalQnty = totalQnty;
    }
    public int getTotalQnty() {
       return totalQnty;
    }
}
