package biyaniparker.com.parker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncParsingUtilities;

/**
 * Created by bt18 on 09/16/2016.
 */
public class SyncServiceBg extends Service implements DownloadUtility
{

    private int InstituteId;
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try
        {

            AsyncParsingUtilities asyncParsingUtilities = new AsyncParsingUtilities(this, false, CommonUtilities.URL + "SyncService.svc/getSyncData?ClientId=" + UserUtilities.getClientId(this), "", 1, this);
            asyncParsingUtilities.setProgressDialoaugeVisibility(false);
            asyncParsingUtilities.execute();

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
             try
            {
                 stopSelf();
            }catch (Exception e)
            {}
    }
}
