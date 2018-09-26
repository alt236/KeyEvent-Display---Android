package aws.apps.keyeventdisplay.ui.main.export;

import android.content.Context;

import java.util.Map;

import aws.apps.keyeventdisplay.deviceinfo.DeviceInfoCollator;

/*package*/ final class DeviceInfo {

    private DeviceInfoCollator deviceInfoCollator;

    DeviceInfo(Context context) {
        this.deviceInfoCollator = new DeviceInfoCollator(context);
    }

    public void collectDeviceInfo(final StringBuilder sb) {
        final Map<String, String> info = deviceInfoCollator.collectDeviceInfo();

        for (final String key : info.keySet()) {
            append(sb, key + ": ", info.get(key), "\n");
        }
    }

    private void append(final StringBuilder sb,
                        final String val1,
                        final String val2,
                        final String val3) {
        sb.append(val1);
        sb.append(val2);
        sb.append(val3);
    }
}
