package com.caowei.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import com.caowei.common.SHA256SignUtil;
import com.jcraft.jsch.Signature;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {

//	@Test
//	public void stringToBasic64Test() {
//		String user_pwd="cw:123456";
//		byte[] encode= Base64.encodeBase64(user_pwd.getBytes());
//		String encodeString=new String(encode);
//		System.out.println(user_pwd+"，转码后："+encodeString);
//		
//		byte[] decode= Base64.decodeBase64("dXNlci1zZXJ2aWNlOjEyMzQ1Ng==".getBytes());
//		System.out.println(new String(decode));
//	}
	
	private static final String ENCODING = "UTF-8";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final int KEY_SIZE = 2048;
	
	@Test
	public void signatureVertiry() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		String pwdsString="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTM4MjU2OTcsInVzZXJfbmFtZSI6ImN3IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiIzN2U1MGNlNS1lMWMwLTQzMDUtYTE1OS0wNWEzMWM3MzUwZDkiLCJjbGllbnRfaWQiOiJ1c2VyX3NlcnZpY2UiLCJzY29wZSI6WyJzZXJ2aWNlIl19";
		
		String path="public.txt";
		Resource resource=new ClassPathResource(path);
		String public_Key=new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));

//      // 公私钥对
//      Map<String, byte[]> keyMap = SHA256SignUtil.generateKeyBytes();
//      PublicKey publicKey = restorePublicKey(keyMap.get(RSA.PUBLIC_KEY));
//      PrivateKey privateKey = restorePrivateKey(keyMap.get(RSA.PRIVATE_KEY));
//      System.out.println("siyao="+Base64.encodeBase64String(privateKey.getEncoded()));
//      //待签名字符
//      String src = "acq_charge=50&bank_type=ICBC&charset=UTF-8&fee_type=CNY&mch_id=105456456456132&merchant_acc=621545678945642&nonce_str=12312456457897945465412313&other_charge=50&pay_result=0&result_code=0&settlement_amount=900&sign_type=RSA_1_256&sp_charge=50&status=0&time_end=20091227091010&total_fee=1000&transaction_id=2018071720551313212313132&version=2.0&wallet_id=7535845125";
//      //签名
//      byte[] sign256 = SHA256SignUtil.sign256(src, privateKey);
//      String sign = SHA256SignUtil.encodeBase64(sign256);
//      System.out.println("sign=" + sign);
//      //验签
//      boolean result = SHA256SignUtil.verify256(src, sign256, publicKey);
//      System.out.println(result);

	}
	
}
