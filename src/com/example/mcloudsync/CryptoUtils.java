package com.example.mcloudsync;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
  public static String computeSha1HashToString(final String x) throws NoSuchAlgorithmException
  {
    MessageDigest d = null;
    d = MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(x.getBytes());
    return  byteArrayToHexString(d.digest());
  }
  
  public static byte [] computeSha1HashToArray(final String x) throws NoSuchAlgorithmException
  {
    MessageDigest d = null;
    d = MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(x.getBytes());
    return d.digest();
  }
  
  public static byte [] computeHash( byte []  x) throws NoSuchAlgorithmException
  {
    MessageDigest d = null;
    d = MessageDigest.getInstance("SHA-1");
    d.reset();
    d.update(x);
    return  d.digest();
  }

  private static String byteArrayToHexString(final byte[] b){
    final StringBuffer sb = new StringBuffer(b.length * 2);
    for (int i = 0; i < b.length; i++){
      final int v = b[i] & 0xff;
      if (v < 16) {
        sb.append('0');
      }
      sb.append(Integer.toHexString(v));
    }
    return sb.toString().toUpperCase();
  }

}
