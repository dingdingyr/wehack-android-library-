package cn.wehax.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileUtils {

    public static class FileIO {
        private final static String TAG = "FileIO";

        /**
         * 读取保存序列化对象的文件，返回序列化对象，如果读取失败返回null
         */
        public static <T extends Serializable> T readSerializableObjectFromFile(String filePath) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;

            try {
                // 如果文件不存在，FileInputStream语句会报出异常
                File file = new File(filePath);

                // 取出数据
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                T data = (T) objectInputStream.readObject();

                return data;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            } finally {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        /**
         * 将序列化对象保存到文件中
         * <p/>
         * <p>如果成功返回true，否则返回false</p>
         */
        public static <T extends Serializable> boolean writeSerializableObjectToFile(T data, String filePath) {
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;

            try {
                File file = new File(filePath);

                // 父目录不存在创建之
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                fileOutputStream = new FileOutputStream(file.toString());
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(data);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                return false;
            } finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
