package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/27/2016.
 */
public class PriceBean
{
    public int priceId;
    public  Double consumerPrice, dealerPrice;
    public long clientId, createdBy, createdDate, changedBy, changedDate;
    public String deleteStatus;

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public Double getConsumerPrice() {
        return consumerPrice;
    }

    public void setConsumerPrice(Double consumerPrice) {
        this.consumerPrice = consumerPrice;
    }

    public Double getDealerPrice() {
        return dealerPrice;
    }

    public void setDealerPrice(Double dealerPrice) {
        this.dealerPrice = dealerPrice;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(long changedBy) {
        this.changedBy = changedBy;
    }

    public long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(long changedDate) {
        this.changedDate = changedDate;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String toString()
    {
        String s= (int)(double) this.getConsumerPrice()+"";
        return s;
    }
}
