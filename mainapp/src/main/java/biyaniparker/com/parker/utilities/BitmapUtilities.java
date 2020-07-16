package biyaniparker.com.parker.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by bt18 on 08/13/2016.
 */
public class BitmapUtilities
{
    public static File saveToExtenal(Bitmap bitmapImage,Context act)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        File myDir = new File(root + "/parker");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }

        return file;
    }

    public static File saveToExtenalForParker(Bitmap bitmapImage,Context act, String Pathname)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        Pathname=Pathname.split("/")[Pathname.split("/").length-1];

        File myDir = new File(root + "/parker");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = Pathname+".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }

        return file;
    }

    public static File saveToExtenalForParker1(Bitmap bitmapImage,Context act, String Pathname)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        Pathname=Pathname.split("/")[Pathname.split("/").length-1];

        File myDir = new File(root + "/parkerbackup");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = Pathname;
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }

        return file;
    }

    public static Bitmap rotetBitmap(Bitmap bitmap,Context act)
    {

        Bitmap rotetionBitmap=null;
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        String fname = "Image-Rotet"+".jpg";


        File myDir = new File(root + "/parker");
        myDir.mkdirs();
        File file = new File (myDir, fname);


        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ImageRotateUtility imageRotateUtility=new ImageRotateUtility(act);

        try
        {
            rotetionBitmap= imageRotateUtility.rotateImageIfRequired(bitmap, Uri.fromFile(file));

            if (file.exists ()) file.delete ();
            try
            {
                file= new File (myDir, fname);
                FileOutputStream out = new FileOutputStream(file);
                rotetionBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        catch (Exception e)
        {}

        return  rotetionBitmap;
    }


    public static File saveToExtenal1(Bitmap bitmapImage,Context act)
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);


        File myDir = new File(root + "/parker");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, out);

            //  Toast.makeText(act,  file.getAbsolutePath()+"", 1000).show();

            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (Build.VERSION.SDK_INT >= 18)
        {

        }
        else
        {
            act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));// Environment.getExternalStorageDirectory())));
        }
        return file;
    }









}
