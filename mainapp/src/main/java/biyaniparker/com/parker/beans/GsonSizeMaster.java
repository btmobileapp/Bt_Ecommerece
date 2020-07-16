package biyaniparker.com.parker.beans;

import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 08/19/2016.
 */
public class GsonSizeMaster
{
    public int SizeId, ClientId, SequenceNo;
    public String CreatedDate;
    public long ChangedBy;
    public String ChangedDate;
    public long CreatedBy;
    public String SizeName, DeleteStatus;

   public SizeMaster toSizeMaster()
   {
       SizeMaster bean=new SizeMaster();
       bean.sizeId=SizeId;
       bean.clientId=ClientId;
       bean.sequenceNo=SequenceNo;
       bean.changedBy=ChangedBy;
       bean.createdBy=CreatedBy;
       bean.sizeName=SizeName;
        bean.deleteStatus=DeleteStatus;

       bean.createdDate= CommonUtilities.parseDate(CreatedDate);
       bean.changedDate= CommonUtilities.parseDate(ChangedDate);

        return bean;
   }
}
