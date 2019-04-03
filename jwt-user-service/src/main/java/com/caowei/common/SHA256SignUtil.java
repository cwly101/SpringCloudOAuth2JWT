package com.caowei.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class SHA256SignUtil {

    private static final String ENCODING = "UTF-8";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final int KEY_SIZE = 2048;

//    public static void main(String[] args) throws InvalidKeyException,
//            NoSuchAlgorithmException, InvalidKeySpecException,
//            SignatureException, UnsupportedEncodingException {
//
//        // 公私钥对
//        Map<String, byte[]> keyMap = generateKeyBytes();
//        PublicKey publicKey = restorePublicKey(keyMap.get(RSA.PUBLIC_KEY));
//        PrivateKey privateKey = restorePrivateKey(keyMap.get(RSA.PRIVATE_KEY));
//        System.out.println("siyao="+Base64.encodeBase64String(privateKey.getEncoded()));
//        //待签名字符
//        String src = "acq_charge=50&bank_type=ICBC&charset=UTF-8&fee_type=CNY&mch_id=105456456456132&merchant_acc=621545678945642&nonce_str=12312456457897945465412313&other_charge=50&pay_result=0&result_code=0&settlement_amount=900&sign_type=RSA_1_256&sp_charge=50&status=0&time_end=20091227091010&total_fee=1000&transaction_id=2018071720551313212313132&version=2.0&wallet_id=7535845125";
//        //签名
//        byte[] sign256 = sign256(src, privateKey);
//        String sign = encodeBase64(sign256);
//        System.out.println("sign=" + sign);
//        //验签
//        boolean result = verify256(src, sign256, publicKey);
//        System.out.println(result);
//    }

    /** SHA256WithRSA 签名 */
    public static byte[] sign256(String data, PrivateKey privateKey)throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException,UnsupportedEncodingException {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data.getBytes(ENCODING));
        return signature.sign();
    }
    
    /** SHA256WithRSA 验证签名 */
    public static boolean verify256(String data, byte[] sign,PublicKey publicKey) {
        if (data == null || sign == null || publicKey == null) {
            return false;
        }
        try {
            Signature signetcheck = Signature.getInstance(SIGNATURE_ALGORITHM);
            signetcheck.initVerify(publicKey);
            signetcheck.update(data.getBytes("UTF-8"));
            return signetcheck.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }

    /** 二进制数据编码为BASE64字符串  */
    public static String encodeBase64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /** BASE64 解码 */
    public static byte[] decodeBase64(byte[] bytes) {
        byte[] result = null;
        try {
            result = Base64.decodeBase64(bytes);
        } catch (Exception e) {
            return null;
        }
        return result;
    }
    
    /** 生成密钥对 */
    public static Map<String, byte[]> generateKeyBytes() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, byte[]> keyMap = new HashMap<String, byte[]>();
            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
            keyMap.put(PRIVATE_KEY, privateKey.getEncoded());
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /** 还原公钥  */
    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /** 还原私钥 */
    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory
                    .generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}