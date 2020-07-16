package biyaniparker.com.parker.beans;

/**
 * Created by bt on 09/13/2016.
 */
public class GsonOrderDetails
{
    public int OrderDetailId, OrderId,ProductId, SizeId, PriceId,Quantity;
        public String DeleteStatus, ProductName, SizeName, ConsumerPrice, IconThumb, DealerPrice;



    public OrderDetailBean toOrderDetail()
    {
        OrderDetailBean orderDetailBean=new OrderDetailBean();
        orderDetailBean.setConsumerPrice(ConsumerPrice);
        orderDetailBean.setSizeName(SizeName);
        orderDetailBean.setProductName(ProductName);
        orderDetailBean.setDeleteStatus(DeleteStatus);
        orderDetailBean.setQuantity(Quantity);
        orderDetailBean.setPriceId(PriceId);
        orderDetailBean.setSizeId(SizeId);
        orderDetailBean.setProductId(ProductId);
        orderDetailBean.setOrderId(OrderId);
        orderDetailBean.setOrderDetailId(OrderDetailId);
        orderDetailBean.setIconThumb(IconThumb);
        orderDetailBean.setDealerPrice(DealerPrice);
        return orderDetailBean;
    }
}
