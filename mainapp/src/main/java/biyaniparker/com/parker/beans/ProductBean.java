package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/13/2016.
 */
public class ProductBean
{
   public int productId,categoryId,clientId, sequenceNo;
   public String productCode, productName, stripCode, details,   iconThumb, iconFull, iconFull1,deleteStatus,isActive;
    public int priceId;
   public long createdBy;
    public long createdDate;
    public long changedBy;
    public long changedDate;
    public float price;
    public String IconFull2;
    public String IconFull3;
    public String IconFull4;
    public String IconFull5;
    public String UnitName;


    public ProductBean()
    {

    }


    public ProductBeanWithQnty toProductBeanWithQnty()
    {
        ProductBeanWithQnty pWithQnty =new ProductBeanWithQnty();
        pWithQnty.setProductId(productId);
        pWithQnty.setProductCode(productCode);
        pWithQnty.setProductName(productName);
        pWithQnty.setStripCode(stripCode);
        pWithQnty.setDetails(details);
        pWithQnty.setPriceId(priceId);
        pWithQnty.setCategoryId(categoryId);
        pWithQnty.setIconThumb(iconThumb);
        pWithQnty.setIconFull(iconFull);
        pWithQnty.setIconFull1(iconFull1);
        pWithQnty.setClientId(clientId);
        pWithQnty.setSequenceNo(sequenceNo);
        pWithQnty.setCreatedBy(createdBy);
        pWithQnty.setCreatedDate(createdDate);
        pWithQnty.setChangedBy(changedBy);
        pWithQnty.setChagedDate(changedDate);
        pWithQnty.setDeleteStatus(deleteStatus);
        pWithQnty.setIsActive(isActive);

        return pWithQnty;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStripCode() {
        return stripCode;
    }

    public void setStripCode(String stripCode) {
        this.stripCode = stripCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public String getIconThumb() {
        return iconThumb;
    }

    public void setIconThumb(String iconThumb) {
        this.iconThumb = iconThumb;
    }

    public String getIconFull() {
        return iconFull;
    }

    public void setIconFull(String iconFull) {
        this.iconFull = iconFull;
    }

    public String getIconFull1() {
        return iconFull1;
    }

    public void setIconFull1(String iconFull1) {
        this.iconFull1 = iconFull1;
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

    public long getChagedDate() {
        return changedDate;
    }

    public void setChagedDate(long chagedDate) {
        this.changedDate = chagedDate;
    }

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }


//  orriding toString for display product names on listview or spinner\

    public String toString()
    {
        return this.getProductName();
    }

}


