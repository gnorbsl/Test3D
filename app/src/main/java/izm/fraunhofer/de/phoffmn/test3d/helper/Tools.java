package izm.fraunhofer.de.phoffmn.test3d.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phoffmn on 08.10.2015.
 */
public class Tools {


    private static final String SHARED_KEY = "FRAUNHOFER_IZM";
    public static String currentPhotoPath;

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Tools.SHARED_KEY, Context.MODE_PRIVATE);
    }


    /**
     * creates a file for the camera app and stores the path in currentPhotoPath
     *
     * @return empty file which will be filled by camera application
     * @throws IOException when file cant created
     */



    public static File createImageFile() throws IOException {
        // Create an image file name

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BusinessCards");

        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());
        File mediaFile = new File(storageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = mediaFile.getAbsolutePath();

        return mediaFile;
    }

}
