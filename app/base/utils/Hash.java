package base.utils;

import static org.apache.commons.lang.StringUtils.*;

import java.security.*;

/**
 * ハッシュ値生成ユーティリティ
 */
public class Hash {
    
    /**
     * ハッシュ値生成（MD5）
     * @param orgString 元文字列
     */
    public static String generate(final String orgString) {
        return generate(orgString, HashAlgorithm.MD5);
    }
    
    /**
     * ハッシュ値生成
     * @param orgString 元文字列
     * @param algorithm ハッシュアルゴリズム名(Hash.ALG_xxxで指定）
     */
    public static String generate(final String orgString,
                                  final HashAlgorithm algorithm) {
        if (isEmpty(orgString) || algorithm == null) {
            throw new IllegalArgumentException();
        }
        //ハッシュ生成
        final byte[] hash = toHash(orgString, algorithm.toString());
        //ハッシュを16進数文字列に変換
        return toHexString(hash);
    }
    
    /** ハッシュ値取得 */
    private static byte[] toHash(final String orgString, final String algorithm) {
        final MessageDigest md = getInstance(algorithm);
        md.reset();
        md.update(orgString.getBytes());
        return md.digest();
    }
    
    /** MessageDigestインスタンス取得 */
    private static MessageDigest getInstance(final String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return md;
    }
    
    /** ハッシュ値を16進数文字列化 */
    private static String toHexString(final byte[] hash) {
        final StringBuffer sb = new StringBuffer();
        final int cnt = hash.length;
        for (int i = 0; i < cnt; i++) {
            sb.append(Integer.toHexString(hash[i] >> 4 & 0x0F));
            sb.append(Integer.toHexString(hash[i] & 0x0F));
        }
        return sb.toString();
    }
    
    /** ハッシュアルゴリズム */
    public static enum HashAlgorithm {
        
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA384("SHA-384"),
        SHA512("SHA-512");
        
        private final String strValue;
        
        private HashAlgorithm(final String strValue) {
            this.strValue = strValue;
        }
        
        @Override
        public String toString() {
            return strValue;
        }
    }
    
}
