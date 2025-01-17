package cloud.eppo.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cloud.eppo.android.dto.ShardRange;

public class Utils {
    public static String getMD5Hex(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error computing md5 hash", e);
        }
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashText = no.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }

        return hashText;
    }

    public static int getShard(String input, int maxShardValue) {
        String hashText = getMD5Hex(input);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return (int) (Long.parseLong(hashText.substring(0, 8), 16) % maxShardValue);
    }

    public static boolean isShardInRange(int shard, ShardRange range) {
        return shard >= range.getStart() && shard < range.getEnd();
    }

    public static void validateNotEmptyOrNull(String input, String errorMessage) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static String getISODate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String safeCacheKey(String key) {
        // Take the first eight characters to avoid the key being sensitive information
        // Remove non-alphanumeric characters so it plays nice with filesystem
        return key.substring(0,8).replaceAll("\\W", "");
    }

    public static SharedPreferences getSharedPrefs(Context context, String keySuffix) {
        return context.getSharedPreferences("eppo-"+keySuffix, Context.MODE_PRIVATE);
    }

    public static String generateExperimentKey(String featureFlagKey, String allocationKey) {
        return featureFlagKey + '-' + allocationKey;
    }

    public static String logTag(Class loggingClass) {
        // Common prefix can make filtering logs easier
        String logTag = ("EppoSDK:"+loggingClass.getSimpleName());

        // Android prefers keeping log tags 23 characters or less
        if (logTag.length() > 23) {
            logTag = logTag.substring(0, 23);
        }

        return logTag;
    }
}
