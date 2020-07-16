package biyaniparker.com.parker.beans;

/**
 * Created by bt on 09/30/2016.
 */
public class ReportProductStockBean
{
    public int SizeId;


    public int ProductId;
    public int qnty;
    public String CategoryName, ProductName, SizeName, IconThumb, IconFull, IconFull1;
    public double ConsumerPrice,DealerPrice;

    public int getSizeId() {
        return SizeId;
    }

    public void setSizeId(int sizeId) {
        this.SizeId = sizeId;
    }

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int Qnty) {
        this.qnty = Qnty;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.CategoryName = categoryName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

    public String getSizeName() {
        return SizeName;
    }

    public void setSizeName(String sizeName) {
        this.SizeName = sizeName;
    }

    public String getIconThumb() {
        return IconThumb;
    }

    public void setIconThumb(String iconThumb) {
        this.IconThumb = iconThumb;
    }

    public String getIconFull() {
        return IconFull;
    }

    public void setIconFull(String iconFull) {
        this.IconFull = iconFull;
    }

    public String getIconFull1() {
        return IconFull1;
    }

    public void setIconFull1(String iconFull1) {
        this.IconFull1 = iconFull1;
    }

    public double getConsumerPrice() {
        return ConsumerPrice;
    }

    public void setConsumerPrice(double consumerPrice) {
        this.ConsumerPrice = consumerPrice;
    }

    public double getDealerPrice() {
        return DealerPrice;
    }

    public void setDealerPrice(double dealerPrice) {
        this.DealerPrice = dealerPrice;
    }


    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }


}
