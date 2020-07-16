package biyaniparker.com.parker.beans.gson;

import android.app.Instrumentation;

import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 12/09/2016.
 */
public class GsonProductWithQnty
{
    public int ProductId,CategoryId,ClientId, SequenceNo;
    public String ProductCode, ProductName, StripCode, Details,   IconThumb, IconFull, IconFull1, DeleteStatus, IsActive;
    public int PriceId;
    public long CreatedBy;
    public String CreatedDate;
    public long ChangedBy;
    public String ChangedDate;
    public int qnt;



   public  int  getObjeSize()
   {
        String strByte=ProductCode+ProductName+StripCode+Details+ IconThumb+IconFull+IconFull1+DeleteStatus+IsActive+CreatedDate+ChangedDate;
        return  40+(strByte.getBytes().length);
   }


    public GsonProductWithQnty ()
    {

    }


    public ProductBeanWithQnty toProductBeanWithQnty()
    {
        ProductBeanWithQnty pWithQnty=new ProductBeanWithQnty();
        pWithQnty.setProductId(ProductId);
        pWithQnty.setProductName(ProductName);
        pWithQnty.setStripCode(StripCode);
        pWithQnty.setDetails(Details);
        pWithQnty.setPriceId(PriceId);
        pWithQnty.setCategoryId(CategoryId);
        pWithQnty.setIconThumb(IconThumb);
        pWithQnty.setIconFull(IconFull);
        pWithQnty.setIconFull1(IconFull1);
        pWithQnty.setClientId(ClientId);
        pWithQnty.setSequenceNo(SequenceNo);
        pWithQnty.setCreatedBy(CreatedBy);
        pWithQnty.setCreatedDate(CommonUtilities.parseDate(CreatedDate));
        pWithQnty.setChangedBy(ChangedBy);
        pWithQnty.setChagedDate(CommonUtilities.parseDate(ChangedDate));
        pWithQnty.setDeleteStatus(DeleteStatus);
        pWithQnty.setIsActive(IsActive);
        pWithQnty.setQnt(qnt);

        return pWithQnty;
    }

}


