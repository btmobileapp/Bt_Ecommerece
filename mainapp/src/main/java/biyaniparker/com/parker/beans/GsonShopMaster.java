package biyaniparker.com.parker.beans;

import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 09/02/2016.
 */
public class GsonShopMaster
{
    public long ShopId,CreatedBy, ChangedBy ;
    public String ShopName, Address,DeleteStatus;
    public Double CreditLimit;
    public String CreatedDate,ChangedDate;

    public ShopMaster toShopMaster()
    {
        ShopMaster bean=new ShopMaster();
        bean.shopId=ShopId;
        bean.createdBy=CreatedBy;
        bean.changedBy=ChangedBy;
        bean.shopName=ShopName;
        bean.address=Address;
        bean.deleteStatus=DeleteStatus;
        bean.creditLimit=CreditLimit;
        bean.createdDate= CommonUtilities.parseDate(CreatedDate);
        bean.changedDate=CommonUtilities.parseDate(ChangedDate);

        return  bean;
    }
}
