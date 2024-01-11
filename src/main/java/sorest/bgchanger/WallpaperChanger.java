package sorest.bgchanger;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;


public class WallpaperChanger {
    public interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class,W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo(int uiAction, int uiParam, String pvParam, int fWinIni);
    }

    public static void changeWallpaper(String imagePath) {
        boolean result = User32.INSTANCE.SystemParametersInfo(0x0014, 0, imagePath, 0x0002 | 0x0001);
        if (result) {
            System.out.println("Desktop wallpaper changed successfully.");
        } else {
            System.err.println("Failed to change desktop wallpaper.");
        }
    }

}
