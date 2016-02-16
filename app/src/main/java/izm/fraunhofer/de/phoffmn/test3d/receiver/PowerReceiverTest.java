package izm.fraunhofer.de.phoffmn.test3d.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by phoffmn on 24.11.2015.
 */
public class PowerReceiverTest extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        Log.d("Battery", "status " + status + " isCharging "  + isCharging + " chargePlug " + chargePlug + " usbCharge " + usbCharge);

        try {
            Process proc = Runtime.getRuntime()
                    .exec(new String[]{ "su", "-c", "reboot -p" });
            proc.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}