package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/30/2016.
 */
public class StockMasterBean
{
    public int stockId;
    public int productId;
    public int sizeId;
    public int inwardQty;
    public int outwardQty;
    public int stockQty;
    public int inBagQty;
    public int clientId;
    public  String transactionType, remark, deleteStatus ;
    public long userId, enterDate, enterBy, changedDate, changedBY, orderId;

    public int getOutwardQty() {
        return outwardQty;
    }

    public void setOutwardQty(int outwardQty) {
        this.outwardQty = outwardQty;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public int getInwardQty() {
        return inwardQty;
    }

    public void setInwardQty(int inwardQty) {
        this.inwardQty = inwardQty;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public int getInBagQty() {
        return inBagQty;
    }

    public void setInBagQty(int inBagQty) {
        this.inBagQty = inBagQty;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(long enterDate) {
        this.enterDate = enterDate;
    }

    public long getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(long enterBy) {
        this.enterBy = enterBy;
    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public long getChangedBY() {
        return changedBY;
    }

    public void setChangedBY(long changedBY) {
        this.changedBY = changedBY;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }


}
