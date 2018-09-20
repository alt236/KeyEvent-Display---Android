package aws.apps.keyeventdisplay.ui.main.export;

/*package*/ final class DeviceInfo {

    public static void collectDeviceInfo(final StringBuilder sb) {
        append(sb, "OS Release: ", android.os.Build.VERSION.RELEASE, "\n");
        append(sb, "OS API Level: ", android.os.Build.VERSION.SDK_INT, "\n");
        append(sb, "Board: ", android.os.Build.BOARD, "\n");
        append(sb, "Brand: ", android.os.Build.BRAND, "\n");
        append(sb, "CPU_ABI: ", android.os.Build.CPU_ABI, "\n");
        append(sb, "CPU_ABI2: ", android.os.Build.CPU_ABI2, "\n");
        append(sb, "Device: ", android.os.Build.DEVICE, "\n");
        append(sb, "Display: ", android.os.Build.DISPLAY, "\n");
        append(sb, "Fingerprint: ", android.os.Build.FINGERPRINT, "\n");
        append(sb, "Host: ", android.os.Build.HOST, "\n");
        append(sb, "ID: ", android.os.Build.ID, "\n");
        append(sb, "Manufacturer: ", android.os.Build.MANUFACTURER, "\n");
        append(sb, "Model: ", android.os.Build.MODEL, "\n");
        append(sb, "Product: ", android.os.Build.PRODUCT, "\n");
        append(sb, "Tags: ", android.os.Build.TAGS, "\n");
        append(sb, "Type: ", android.os.Build.TYPE, "\n");
        append(sb, "User: ", android.os.Build.USER, "\n");
        append(sb, "Bootloader: ", android.os.Build.BOOTLOADER, "\n");
        append(sb, "Hardware: ", android.os.Build.HARDWARE, "\n");
        append(sb, "Radio: ", android.os.Build.RADIO, "\n");
    }

    private static void append(final StringBuilder sb,
                               final String val1,
                               final String val2,
                               final String val3) {
        sb.append(val1);
        sb.append(val2);
        sb.append(val3);
    }

    private static void append(final StringBuilder sb,
                               final String val1,
                               final int val2,
                               final String val3) {
        sb.append(val1);
        sb.append(val2);
        sb.append(val3);
    }
}
