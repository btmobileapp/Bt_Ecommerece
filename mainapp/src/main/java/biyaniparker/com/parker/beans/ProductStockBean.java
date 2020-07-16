package biyaniparker.com.parker.beans;

/**
 * Created by bt on 09/03/2016.
 */
public class ProductStockBean
{
    public int getSizeId() {
        return SizeId;
    }

    public void setSizeId(int sizeId) {
        SizeId = sizeId;
    }

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    public int SizeId,qnty;

}
