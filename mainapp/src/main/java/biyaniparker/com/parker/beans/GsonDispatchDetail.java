package biyaniparker.com.parker.beans;

/**
 * Created by bt on 09/23/2016.
 */
public class GsonDispatchDetail
{
    public int Quantity, OrderDetailId, SizeId, ProductId,DispatchDetailId, DispatchId;
    public int ActualQnty,OrderQnty;
    public String DeleteStatus,DispatchStatus, ProductName,ConsumerPrice,DealerPrice,SizeName;


    public DispatchDetailBean toDispatchDetailBean()
    {
        DispatchDetailBean bean=new DispatchDetailBean();
        bean.setQuantity(Quantity);
        bean.setOrderDetailId(OrderDetailId);
        bean.setSizeId(SizeId);
        bean.setProductId(ProductId);
        bean.setDispatchDetailId(DispatchDetailId);
        bean.setDispatchId(DispatchId);
        bean.setActualQnty(ActualQnty);
        bean.setDeleteStatus(DeleteStatus);
        bean.setDispatchStatus(DispatchStatus);
        bean.setProductName(ProductName);
        bean.setConsumerPrice(ConsumerPrice);
        bean.setDealerPrice(DealerPrice);
        bean.setSizeName(SizeName);
        bean.setOrderQnty(OrderQnty);

        return bean;
    }
}
