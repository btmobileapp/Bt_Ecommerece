package biyaniparker.com.parker.beans;

/**
 * Created by bt on 08/19/2016.
 */
public class GsonSizeDetailBean
{
    public int SizeDetailId, SizeId, CategoryId;
    public String DeleteStatus;

  public SizeDetailBean toSizeDetailBean()
  {
      SizeDetailBean bean=new SizeDetailBean();
      bean.sizeDetailId=SizeDetailId;

      bean.SizeId=SizeId;

      bean.CategoryId=CategoryId;

      bean.DeleteStatus=DeleteStatus;


      return   bean;
  }

}
