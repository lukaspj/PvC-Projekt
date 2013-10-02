package com.pik_ant.projectslug;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import android.location.Location;

public class User {
	public String Username;
	public double lat;
	public double lng;
	public String BluetoothID;
	private String password;
	private byte[] enc_password;
	private byte[] salt = {(byte)0xB4, (byte)0x9B, (byte)0x48, (byte)0x39,
		    (byte)0xC6, (byte)0xA5, (byte)0xF3, (byte)0xE3};
	
	public User(String userName, Location l, String bid){
		Username = userName;
		this.lat = l.getLatitude();
		this.lng = l.getLongitude();
		BluetoothID = bid;
	}
	public User(){
		
	}


	public byte[] getEncPassword() { return enc_password; };
	public void setPassword(String pass){
		password = pass;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 128);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
	
			//byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertext = cipher.doFinal("Jolly jumped the jumping jolly".getBytes("UTF-8"));
			enc_password = ciphertext;
		}
		catch(InvalidKeyException ex) {
			ex.printStackTrace();
		}
		catch(InvalidKeySpecException ex) {
			ex.printStackTrace();
		}
		catch(NoSuchPaddingException ex){
			ex.printStackTrace();
		}
		/*catch (InvalidParameterSpecException ex) {
			ex.printStackTrace();
		}*/
		catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		catch (BadPaddingException ex) {
			ex.printStackTrace();
		}
		catch (IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
	}
}
