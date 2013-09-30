package com.pik_ant.projectslug;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class User {
	public String Username;
	public double lat;
	public double lng;
	public String BluetoothID;
	private String password;
	private byte[] enc_password;
	public byte[] getEncPassword() { return enc_password; };
	public void setPassword(String pass){
		password = pass;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray());
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
	
			byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertext = cipher.doFinal("Jolly jumped the jumping jolly".getBytes("UTF-8"));
			enc_password = ciphertext;
		}
		catch(InvalidKeyException ex) {
			
		}
		catch(InvalidKeySpecException ex) {

		}
		catch(NoSuchPaddingException ex){
			
		}
		catch (InvalidParameterSpecException ex) {

		}
		catch (UnsupportedEncodingException ex) {

		}
		catch (BadPaddingException ex) {

		}
		catch (IllegalBlockSizeException ex) {

		}
		catch(NoSuchAlgorithmException ex) {
			
		}
	}
}
