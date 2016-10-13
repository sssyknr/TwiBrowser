package ss.ss.sss.twibrowser.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

/**
 * シリアライズユーティリティクラス
 */
public class SerializableUtils {
    private static final String path = "timeline.txt";

    /**
     * シリアライズ
     *
     * @param statuses ステータス
     * @param context  コンテキスト
     */
    public static void save(ArrayList<Status> statuses, Context context) {

        Log.d("さささ", "save");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            try {
                fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            try {
                if (fos != null) {
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(statuses);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (fos != null) {
                try {
                    if (oos != null) oos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * デシリアライズ
     *
     * @param context コンテキスト
     * @return ステータス
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Status> load(Context context) {

        Log.d("さささ", "load");

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        ArrayList<Status> obj = null;

        try {
            try {
                fis = context.openFileInput(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            try {
                ois = new ObjectInputStream(fis);
                if (ois != null) {
                    obj = (ArrayList<Status>) ois.readObject();
                }
            } catch (IOException | ClassNotFoundException | RuntimeException e) {
                e.printStackTrace();
            }

        } finally {
            if (fis != null) {
                try {
                    if (ois != null) ois.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
