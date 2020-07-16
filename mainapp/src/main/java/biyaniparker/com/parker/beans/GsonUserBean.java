                                                                                                                                                                                                                                                   package biyaniparker.com.parker.beans;

                                                                                                                                                                                                                                                   import biyaniparker.com.parker.utilities.CommonUtilities;

                                                                                                                                                                                                                                                   /**
 * Created by bt on 08/22/2016.
 */
public class GsonUserBean
{
   public int UserId,  AccessLavel, ClientId;
    public String ContactNo;
    public String MobileNo;
    public String RoleName;
    public String UserType;
    public String UserName;
    public String Password;
    public String OldPassword;

    public String Name;

    public String OrgnisationIds;
    public String DeleteStatus;
    public String DepartmentIds;
    public String CanCreateUser;
    public String CanAssignTask;
    public String GCMID;
    public String DeviceID;


    public String IsActive;


    public String EmailId;
    public String  ExpiryDate;
    public long EnterBy;
    public long ChangedBy;


    public long ShopId;
    public String EnterDate;
    public long CreatedBy;
    public String ChangedDate;






    public UserBean toUserBean()
    {
        UserBean bean=new UserBean();
        bean.userId=UserId;
        try{
        bean.accessLavel=AccessLavel;}catch (Exception e){}
        try{
        bean.clientId=ClientId;}catch (Exception e){}
        bean.contactNo= ContactNo;
        bean.mobileNo= MobileNo;
        bean.roleName= RoleName;
        bean.userType= UserType;
        bean.userName= UserName;
        bean.password= Password;
        bean.oldPassword= OldPassword;
        bean.name= Name;
        bean.orgnisationIds= OrgnisationIds;
        bean.deleteStatus=DeleteStatus;
        bean.departmentIds=DepartmentIds;
        bean.canCreateUser= CanCreateUser;
        bean.canAssignTask= CanAssignTask;
        bean.gCMID= GCMID;
        bean.deviceID= DeviceID;
        bean.isActive=IsActive;
        bean.emailId= EmailId;



       try{  bean.enterBy=EnterBy;}catch (Exception e){}
       try{  bean.changedBy=ChangedBy;}catch (Exception e){}
       try{  bean.shopId= ShopId;}catch (Exception e){}
       try{  bean.createdBy=CreatedBy;}catch (Exception e){}

        bean.changedDate= CommonUtilities.parseDate( ChangedDate);
        bean.expiryDate= CommonUtilities.parseDate(  ExpiryDate);
        bean.enterDate =  CommonUtilities.parseDate(EnterDate);








        return bean;
    }











}
