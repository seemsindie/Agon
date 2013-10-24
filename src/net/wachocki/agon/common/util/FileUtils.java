package net.wachocki.agon.common.util;

import java.io.*;

/**
 * User: Marty
 * Date: 10/24/13
 * Time: 12:55 PM
 */
public class FileUtils {

    public static byte[] getFileByteArray(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null) ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

}
