package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/23/2016.
 */
public class OrderDetailBean
{
    public OrderDetailBean()
    {

    }

    public int  orderDetailId;
    public int orderId;
    public int productId;
    public int sizeId;
    public int quantity;
    public int priceId;

    public int getEnteredQnty() {
        return enteredQnty;
    }

    public void setEnteredQnty(int enteredQnty) {
        this.enteredQnty = enteredQnty;
    }

    public int enteredQnty;
    public String deleteStatus;

    public String getIconThumb() {
        return iconThumb;
    }

    public void setIconThumb(String iconThumb) {
        this.iconThumb = iconThumb;
    }

    public String iconThumb;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getConsumerPrice() {
        return consumerPrice;
    }

    public void setConsumerPrice(String consumerPrice) {
        this.consumerPrice = consumerPrice;
    }

    public String productName;
    public String sizeName;
    public String consumerPrice;

    public String getDealerPrice() {
        return dealerPrice;
    }

    public void setDealerPrice(String dealerPrice) {
        this.dealerPrice = dealerPrice;
    }

    public String dealerPrice;

    public int getOrderDetailId()
    {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId)
    {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getSizeId()
    {
        return sizeId;
    }

    public void setSizeId(int sizeId)
    {
        this.sizeId = sizeId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }
}
