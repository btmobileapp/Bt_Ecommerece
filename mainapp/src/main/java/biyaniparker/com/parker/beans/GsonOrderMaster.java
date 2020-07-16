package biyaniparker.com.parker.beans;

import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;

/**
 * Created by bt on 09/13/2016.
 */
public class GsonOrderMaster {
    public int OrderId, UserId;
    public String OrderDate, OrderStatus, ChangeBy, ChangedDate, DeleteStatus, Address, Name, ShopName;
    public long TotolAmount;


    public OrderMasterBean toOrderMaster()
    {
        OrderMasterBean orderM=new OrderMasterBean();
        orderM.address=Address;
        orderM.changeBy= Long.parseLong(ChangeBy);
        orderM.changedDate= CommonUtilities.parseDate(ChangedDate);
        orderM.deleteStatus=DeleteStatus;
        orderM.name=Name;
        orderM.orderId=OrderId;
        orderM.orderDate=  CommonUtilities.parseDate(OrderDate);
        orderM.orderStatus=OrderStatus;
        orderM.shopName=ShopName;
        orderM.userId= UserId;
        orderM.totolAmount= String.valueOf(TotolAmount);

        return orderM;
    }
}
