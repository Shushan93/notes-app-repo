package com.dev.notes.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;


public class Utils {
    public static byte[]  serializeCalendar(Calendar calendar) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(calendar);
            out.flush();
            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    public static Calendar deserializeCalendar(byte[] serializedCalendar) {
        if (serializedCalendar == null) return null;
        ByteArrayInputStream bis = new ByteArrayInputStream(serializedCalendar);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return  (Calendar) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateInFormat(Calendar date) {
        return String.format("%1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", date);
    }

    public static String getOnlyDateInFormat(Calendar date) {
        return String.format("%1$tb %1$td %1$tY", date);
    }
    public static String getOnlyTimeInFormat(Calendar date) {
        return String.format("%1$tI:%1$tM %1$Tp", date);
    }

    public static int getNextId(Number currentIdNum) {
        if (currentIdNum == null) {
            return  1;
        } else {
            return currentIdNum.intValue() + 1;
        }
    }
}
