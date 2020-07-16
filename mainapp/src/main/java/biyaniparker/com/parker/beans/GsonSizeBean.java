package biyaniparker.com.parker.beans;

import java.util.ArrayList;

/**
 * Created by bt on 09/03/2016.
 */
public class GsonSizeBean
{
    public GsonSizeMaster sizeMasters;
    public ArrayList<GsonSizeDetailBean> details;

    public ArrayList<SizeDetailBean> getDetailsList()
    {
        ArrayList<SizeDetailBean> list=new ArrayList<>();
        if(details!=null)
        {
            for(int i=0;i<details.size();i++)
            {
                list.add(details.get(i).toSizeDetailBean());
            }
        }
        return  list;

    }
}
