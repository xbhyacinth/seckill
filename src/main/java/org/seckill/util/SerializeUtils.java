package org.seckill.util;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: tang
 * Date: 16-7-20
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */
public class SerializeUtils {

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }

        }
    }

    /**
     * 反序列化对象
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object unserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
