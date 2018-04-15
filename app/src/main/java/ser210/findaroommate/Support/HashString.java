package ser210.findaroommate.Support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Joe Passanante on 4/12/2018.
 */

public class HashString {
    public static String hashPassword(String rawPassword){
        String hashedPassword="";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(rawPassword.getBytes());
            byte[] messageDigestMD5 = digest.digest();
            StringBuffer s = new StringBuffer();
            for(byte bytes: messageDigestMD5){
                s.append(String.format("%02x", bytes & 0xff));
            }
            hashedPassword = s.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return hashedPassword;
    }
}
