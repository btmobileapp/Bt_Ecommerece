package biyaniparker.com.parker.beans;

import java.security.interfaces.DSAPrivateKey;

import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 09/23/2016.
 */
public class GsonDispatchMaster
{
    public int DispatchId, DispatchNo, ChallanNo,OrderId ;
    public long   CustomerId, DispatchBy, CheckedBy, ReceivedBy, UserId, ChangeBy;
    public String Transport,Name, ShopName, Address;
    public String Parcel;
    public String VatTinNo;
    public String CstTinNo;
    public String DispatchStatus;

    public String DispatchDate, ChallanDate,EnterDate,  ChangedDate,OrderDate;
    public double TotolAmount;

    public DispatchMasterBean toDispatchMasterBean()
    {
        DispatchMasterBean bean=new DispatchMasterBean();

        bean.setDispatchId(DispatchId);
        bean.setDispatchNo(DispatchNo);
        bean.setChallanNo(ChallanNo);
        bean.setOrderId(OrderId);
        bean.setDispatchDate(CommonUtilities.parseDate(DispatchDate));
        bean.setChangedDate(CommonUtilities.parseDate(ChallanDate));
        bean.setCustomerId(CustomerId);
        bean.setDispatchBy(DispatchBy);
        bean.setCheckedBy(CheckedBy);
        bean.setReceivedBy(ReceivedBy);
        bean.setChangeBy(ChangeBy);
        bean.setEnterDate(CommonUtilities.parseDate(EnterDate));
        bean.setChangedDate(CommonUtilities.parseDate(ChangedDate));
        bean.setTransport(Transport);
        bean.setParcel(Parcel);
        bean.setVatTinNo(VatTinNo);
        bean.setCstTinNo(CstTinNo);
        bean.setName(Name);
        bean.setAddress(Address);
        bean.setShopName(ShopName);
        bean.setDispatchStatus(DispatchStatus);
        bean.setTotolAmount(TotolAmount);
        bean.setOrderDate(CommonUtilities.parseDate(OrderDate));
        return bean;
    }
}


