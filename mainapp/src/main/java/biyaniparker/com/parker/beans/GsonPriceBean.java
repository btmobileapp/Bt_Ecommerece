package biyaniparker.com.parker.beans;

import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 08/27/2016.
 */
public class GsonPriceBean
{
    public int PriceId;
    public  Double ConsumerPrice, DealerPrice;
    public long ClientId, CreatedBy, ChangedBy;
    public String CreatedDate,ChangedDate,DeleteStatus;

    public PriceBean toPriceBean()
    {
        PriceBean bean=new PriceBean();


        bean.priceId=PriceId;
        bean.consumerPrice=ConsumerPrice;
        bean.dealerPrice=DealerPrice;
        bean.clientId=ClientId;
        bean.createdBy=CreatedBy;
        bean.changedBy=ChangedBy;
        bean.deleteStatus=DeleteStatus;
        try
        {
            bean.createdDate= CommonUtilities.parseDate(CreatedDate);
        }
        catch (Exception e){}
        try
        {
            bean.changedDate= CommonUtilities.parseDate(ChangedDate);
        }
        catch (Exception e){}

        return bean;
    }


}
