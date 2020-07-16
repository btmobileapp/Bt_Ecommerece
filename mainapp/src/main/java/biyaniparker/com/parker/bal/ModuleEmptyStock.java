package biyaniparker.com.parker.bal;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import biyaniparker.com.parker.beans.EmptyProductStockBean;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

public class ModuleEmptyStock implements DownloadUtility,ParsingUtilities
{


    public String removeProductids="";
     public   ArrayList<EmptyProductStockBean>  list=new ArrayList<>();
    Context context;
    public  ModuleEmptyStock(Context context)
    {
         this.context=context;
    }
    public  void loadEmptyProduct(int cateid,long fromdate,long todate)
    {
        removeProductids="";
        String url=CommonUtilities.URL+"ProductService.svc/getemptystock?ClientId="+1+"&todate="+todate+"&fromdate="+fromdate+"&categoryid="+cateid;
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, url,"",1,this);
        serverAsync.execute();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        Gson gson=new Gson();
        DownloadUtility downloadUtility = (DownloadUtility) context;
       // CommonUtilities.alert(context,str);
        if(requestCode==1) {
            list.clear();
            if (responseCode == 200) {
                try {
                    JSONArray arr = new JSONArray(str);
                    for (int i = 0; i < arr.length(); i++)
                    {
                        JSONObject j = arr.getJSONObject(i);
                        EmptyProductStockBean bean=gson.fromJson(j.toString(),EmptyProductStockBean.class);
                        list.add(bean);
                    }
                } catch (Exception exz) {
                }

            }
            downloadUtility.onComplete(str, requestCode, responseCode);
        }
        if(requestCode==2)
        {
            /*
            if(responseCode==200)
            {

            }*/
            try {

                ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
                itemDAOProduct.deleteInBulk(removeProductids);
            }
            catch (Exception ex)
            {}
            downloadUtility.onComplete(str, requestCode, responseCode);

        }
    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        return false;
    }

    public int removeProduct()
    {
        removeProductids="";
        int cnt=0;
        String pids="";
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).isChecked)
            {
                if (pids.equalsIgnoreCase(""))
                     pids=list.get(i).ProductId+"";
                else
                    pids=pids+","+list.get(i).ProductId+"";
                cnt++;
            }
        }
       // CommonUtilities.alert(context,pids);
        if(cnt>0)
        {
               CommonUtilities.alert(context,pids);
               try
               {
                   removeProductids="";
                       JSONObject j=new JSONObject();
                       j.put("ProductIds",pids);
                       j.put("unm", UserUtilities.getUserName(context));
                       j.put("pwd",UserUtilities.getUserPassword(context));
                       j.put("UserId",UserUtilities.getUserId(context));

                       AsyncUtilities utilities=new AsyncUtilities(context,true,CommonUtilities.URL+"ProductService.svc/DeleteEmptyStock",j.toString(),2,this);
                       utilities.execute();
                   removeProductids=pids;
               }
               catch (Exception ex)
               {}
        }
        return cnt;
    }
}
