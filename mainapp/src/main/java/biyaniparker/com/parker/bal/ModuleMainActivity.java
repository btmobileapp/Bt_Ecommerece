package biyaniparker.com.parker.bal;

import android.content.Context;

import java.util.ArrayList;

import biyaniparker.com.parker.database.ItemDAOAlbum;

/**
 * Created by bt18 on 08/08/2016.
 */
public class ModuleMainActivity
{
    Context context;
   public ArrayList<String> list=new ArrayList<>();
    public ModuleMainActivity(Context context)
    {
      this.context=context;
      ItemDAOAlbum itemDAOAlbum=new ItemDAOAlbum();
       list.addAll( itemDAOAlbum.getAllAlbum());

    }
}
