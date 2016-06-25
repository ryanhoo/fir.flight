package io.github.ryanhoo.firFlight.util;

import java.io.*;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/25/16
 * Time: 11:42 PM
 * Desc: ObjectUtils
 */
public class ObjectUtils {

    public static Object byteToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        } finally {
            if (in != null)
                in.close();
        }
    }

    public static byte[] objectToByte(Object obj) throws IOException {
        ObjectOutputStream out = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            return bos.toByteArray();
        } finally {
            if (out != null)
                out.close();
        }
    }
}
