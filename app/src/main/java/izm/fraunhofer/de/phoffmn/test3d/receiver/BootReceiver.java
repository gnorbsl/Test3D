package izm.fraunhofer.de.phoffmn.test3d.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import izm.fraunhofer.de.phoffmn.test3d.activities.MainActivity;

/**
 * Created by phoffmn on 24.11.2015.
 */
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){


            Log.d("TEST", "BOOTSHIT");

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
        }

    }
}
