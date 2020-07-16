package biyaniparker.com.parker.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by bt18 on 11/19/2016.
 */
public class ImageRotateUtility
{
    Context act;
    static   int rotetInt;
    public ImageRotateUtility(Context act)
    {
        this.act=act;
        SharedPreferences rotetPreference=act.getSharedPreferences("rotet", act.MODE_PRIVATE);
        rotetInt= rotetPreference.getInt("rotet",0);

    }

    private static Bitmap rotateImage(Bitmap img, int degree)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
       // img.recycle();
        return rotatedImg;
    }


    public static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException
    {
        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

       /* switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            case ExifInterface.ORIENTATION_TRANSVERSE:
                return rotateImage(img, -90);
            case ExifInterface.ORIENTATION_TRANSPOSE:
                return rotateImage(img, 90);

            default:
                return img;
*/

       return rotateImage(img, rotetInt);

    }
}
