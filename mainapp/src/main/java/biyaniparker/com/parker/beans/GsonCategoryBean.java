package biyaniparker.com.parker.beans;

import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 08/11/2016.
 */
public class GsonCategoryBean
{
  public int CategoryId,ParentCategoryId;
  public String CategoryCode,CategoryName,Icon,Remark,IsLast,DeleteStatus;
  public long ClientId,CreatedBy,ChangedBy;
  public String CreatedDate,ChangedDate;



  public GsonCategoryBean()
  {

  }


  public CategoryBean toCategoryBean()
  {
      CategoryBean bean=new CategoryBean();
      bean.categoryId=CategoryId;
      bean.parentCategoryId=ParentCategoryId;

    bean.categoryCode=CategoryCode;
    bean.categoryName=CategoryName;
    bean.icon=Icon;
    bean.isLast=IsLast;
    bean.remark=Remark;
    bean.deleteStatus=DeleteStatus;

    bean.clientId=ClientId;
    bean.createdBy=CreatedBy;
    bean.changedBy=ChangedBy;

    try
    {
     bean.createdDate= CommonUtilities.parseDate(CreatedDate);
    }
    catch (Exception e)
    {}
    try
    {
      bean.changedDate= CommonUtilities.parseDate(ChangedDate);
    }
    catch (Exception e)
    {}




    return  bean;
  }

}
