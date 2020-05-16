package aws.apps.keyeventdisplay.deviceinfo;

import android.content.Context;
import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DeviceInfoCollator {
    private final DeviceCapabilities deviceCapabilities;

    public DeviceInfoCollator(Context context) {
        this.deviceCapabilities = new DeviceCapabilities(context);
    }

    @NonNull
    public Map<String, String> collectDeviceInfo() {
        final Map<String, String> retVal = new LinkedHashMap<>();

        retVal.put("OS Release", pretty(Build.VERSION.RELEASE));
        retVal.put("OS API Level", pretty(Build.VERSION.SDK_INT));

        retVal.put("Manufacturer", pretty(Build.MANUFACTURER));
        retVal.put("Brand", pretty(Build.BRAND));
        retVal.put("Model", pretty(Build.MODEL));

        retVal.put("Board", pretty(Build.BOARD));
        retVal.put("CPU_ABI", pretty(Build.CPU_ABI));
        retVal.put("CPU_ABI2", pretty(Build.CPU_ABI2));
        retVal.put("Device", pretty(Build.DEVICE));
        retVal.put("Display", pretty(Build.DISPLAY));
        retVal.put("Host", pretty(Build.HOST));
        retVal.put("ID", pretty(Build.ID));

        retVal.put("Product", pretty(Build.PRODUCT));
        retVal.put("Tags", pretty(Build.TAGS));
        retVal.put("Type", pretty(Build.TYPE));
        retVal.put("User", pretty(Build.USER));
        retVal.put("Bootloader", pretty(Build.BOOTLOADER));
        retVal.put("Hardware", pretty(Build.HARDWARE));
        retVal.put("Radio", pretty(Build.RADIO));

        retVal.put("Android TV", pretty(deviceCapabilities.isAndroidTv()));
        retVal.put("Android Wear", pretty(deviceCapabilities.isAndroidWear()));
        retVal.put("Probably Emulator", pretty(deviceCapabilities.isProbablyEmulator()));

        retVal.put("Fingerprint", pretty(Build.FINGERPRINT));
        return retVal;
    }

    private String pretty(final String value) {
        if (value == null || "".equals(value)) {
            return "[value missing]";
        } else {
            return value;
        }
    }

    private String pretty(final int value) {
        return String.valueOf(value);
    }

    private String pretty(final boolean value) {
        return String.valueOf(value);
    }
}
