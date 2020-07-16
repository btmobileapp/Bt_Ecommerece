package biyaniparker.com.parker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHELPER extends SQLiteOpenHelper
{


//

	String Album="create table Album(AlbumId integer primary key,AlbumName text)";
	String category="  Create table Category (CategoryId Integer primary key, CategoryCode Text, CategoryName Text, ParentCategoryId Integer, Icon Text, IsLast Text, Remark Text, ClientId Integer, CreatedBy Integer, CreatedDate Integer, ChangedBy Integer, ChangedDate Integer, DeleteStatus Text)";
	String product=" Create table Product (ProductId Integer primary key, ProductCode Text, ProductName Text, StripCode Text, Details Text, PriceId Integer, CategoryId Integer, IconThumb Text, IconFull Text, IconFull1 Text, ClientId Integer, SequenceNo Integer, CreatedBy Integer, CreatedDate Integer, ChangedBy Integer, ChangedDate Integer, DeleteStatus Text, IsActive text,IsSelfCreated text,IsFromGCM text,Etc1 text,Etc2 text)";
	String sizeMaster=" Create table SizeMaster(SizeId Integer primary key, SizeName Text, ClientId Integer, SequenceNo Integer, DeleteStatus Text, CreatedBy Integer, CreatedDate Integer, ChangedBy Integer, ChangedDate Integer)";
	String sizeDetails=" Create table SizeDetails(SizeDetailId Integer primary key, SizeId Integer, CategoryId Integer, DeleteStatus Text)";
	String user="Create table User(UserId Integer primary key, ShopId Integer, CreatedBy Integer, AccessLavel Integer, RoleName Text, UserType Text, UserName Text, Password Text," +
			" OldPassword Text,  Name Text,  ContactNo Text," +
			"  MobileNo Text, IsActive Text, ClientId Integer, OrgnisationIds Text, EmailId Text, " +
			"DeleteStatus Text, ExpiryDate Integer, DepartmentIds Text," +
			" CanCreateUser Text, CanAssignTask Text, GCMID Text, DeviceID Text," +
			" EnterBy Integer, EnterDate Integer, ChangedBy Integer, ChangedDate Integer)";
	String dispatchMaster=" Create table DispatchMaster(DispatchId Integer primary key, DispatchNo Integer, " +
			"ChallanNo Integer, DispatchDate Integer," +
			" ChallanDate Integer, DispatchBy Integer, OrderId Integer, " +
			"CustomerId Integer, Transport Text, Parcel Text, VatTinNo Text, CstTinNo Text," +
			" CheckedBy Integer, ReceivedBy Integer, EnterBy Integer, EnterDate Integer, " +
			"ChangeBy Integer, ChangedDate Integer, TotolAmount Text, DispatchStatus Text, Name Text, ShopName Text, Address Text, OrderDate Integer, IsSelfCreated text,IsFromGCM text,Etc1 text,Etc2 text)";
	String dispatchDetails=" Create table DispatchDetails (" +
			"DispatchDetailId Integer primary key," +
			" DispatchId Integer, " +
			"OrderDetailId Integer," +
			" ProductId Integer , " +
			"SizeId Integer," +
			" Quantity Integer," +
			" DeleteStatus Text," +
			" DispatchStatus Text," +
			" ProductName Text, " +
			"ConsumerPrice Text," +
			" DealerPrice Text, " +
			"SizeName Text, OrderQnty Integer )";


	String orderMaster="Create table OrderMaster (OrderId Integer primary key , OrderDate Integer, UserId Integer, OrderStatus Text, ChangeBy Integer," +
			" ChangedDate Integer, DeleteStatus Text, TotolAmount Text, Address Text, Name Text, ShopName Text,TotalQnty integer, IsSelfCreated text,IsFromGCM text,Etc1 text,Etc2 text)";

	String orderDetails=" Create table OrderDetails (OrderDetailId Integer primary key, OrderId Integer, ProductId Integer, SizeId Integer,PriceId Integer,Quantity Integer, DeleteStatus Text, ProductName Text," +
			"SizeName Text, ConsumerPrice Text, IconThumb Text,DealerPrice Text)";




	String priceMaster=" Create table PriceMaster (PriceId Integer, ConsumerPrice Text, DealerPrice Text, ClientId Integer, CreatedBy Integer," +
			" CreatedDate Integer, ChangedBy Integer, ChangedDate Integer, DeleteStatus Text)";


	String shopMaster="Create table ShopMaster(ShopId Integer primary key, ShopName Text, Address Text, CreditLimit Text, CreatedBy Integer, CreatedDate Integer, " +
			"ChangedBy Integer, ChangedDate Integer, DeleteStatus Text)";

	Context context;
	public DBHELPER(Context context)
	{
		//super(context,Environment.getExternalStorageDirectory().toString()+"/parker", null, 1);
		// TODO Auto-generated constructor stub
	     super(context,"parker", null, 2);
		this.context=context;

	}

	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		// TODO Auto-generated method stub

		arg0.execSQL(Album);
		arg0.execSQL(category);
		arg0.execSQL(product);
		arg0.execSQL(sizeMaster);
		arg0.execSQL(sizeDetails);
		arg0.execSQL(user);
		arg0.execSQL(dispatchMaster);
		arg0.execSQL(dispatchDetails);
		arg0.execSQL(orderMaster);
		arg0.execSQL(orderDetails);
		arg0.execSQL(priceMaster);
		arg0.execSQL(shopMaster);
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub	
		//arg0.execSQL("dr")
		//onCreate(arg0);
	//	new ItemDAOPrice (context).delete();
	//	new ItemDAOCategory (context).delete();
	//	new ItemDAODispatch (context).delete();
	//new ItemDAOOrder (context).delete();
		//new ItemDAOAlbum(context).delete();
			
	}
	
	
	

}
