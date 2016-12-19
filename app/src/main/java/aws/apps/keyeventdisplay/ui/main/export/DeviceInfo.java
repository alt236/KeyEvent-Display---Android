package aws.apps.keyeventdisplay.ui.main.export;

/*package*/ final class DeviceInfo {

    public static void collectDeviceInfo(final StringBuilder sb) {
        sb.append("OS Release: " + android.os.Build.VERSION.RELEASE + "\n");
        sb.append("OS API Level: " + android.os.Build.VERSION.SDK_INT + "\n");
        sb.append("Board: " + android.os.Build.BOARD + "\n");
        sb.append("Brand: " + android.os.Build.BRAND + "\n");
        sb.append("CPU_ABI: " + android.os.Build.CPU_ABI + "\n");
        sb.append("Device: " + android.os.Build.DEVICE + "\n");
        sb.append("Display: " + android.os.Build.DISPLAY + "\n");
        sb.append("Fingerprint: " + android.os.Build.FINGERPRINT + "\n");
        sb.append("Host: " + android.os.Build.HOST + "\n");
        sb.append("ID: " + android.os.Build.ID + "\n");
        sb.append("Manufacturer: " + android.os.Build.MANUFACTURER + "\n");
        sb.append("Model: " + android.os.Build.MODEL + "\n");
        sb.append("Product: " + android.os.Build.PRODUCT + "\n");
        sb.append("Tags: " + android.os.Build.TAGS + "\n");
        sb.append("Type: " + android.os.Build.TYPE + "\n");
        sb.append("User: " + android.os.Build.USER + "\n");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            sb.append("Bootloader: " + android.os.Build.BOOTLOADER + "\n");
            sb.append("CPU_ABI2: " + android.os.Build.CPU_ABI2 + "\n");
            sb.append("Hardware: " + android.os.Build.HARDWARE + "\n");
            sb.append("Radio: " + android.os.Build.RADIO + "\n");
        }
    }

}
