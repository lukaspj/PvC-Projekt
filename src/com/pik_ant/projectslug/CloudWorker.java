package com.pik_ant.projectslug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CloudWorker {
	
	public static void IsUserWork(String usn, CloudCallback cb)
	{
		URL url;
		try{
			// get URL content
			url = new URL("http://fuzzyvoidstudio.com/request/get/is_username_available/");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			
			// Send our POST data
			writer.write("username="+usn);
			writer.flush();
			
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
 
			// The Cloud API will always return a single line result
			String inputLine = br.readLine();
			// Interpret the result
			if(inputLine.contains("-1"))
				cb.IsUserRecieved(CloudCallback.IsUserResult.MultipleRegistered);
			else if(inputLine.contains("1"))
				cb.IsUserRecieved(CloudCallback.IsUserResult.NotRegistered);
			else
				cb.IsUserRecieved(CloudCallback.IsUserResult.Registered);
			return;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Else
		cb.IsUserRecieved(CloudCallback.IsUserResult.Registered);
	}
	
	public static void GetUserWork(CloudCallback cb) {
		URL url;
		ArrayList<User> retUsers = new ArrayList<User>();
		try{
			// get URL content
			url = new URL("http://fuzzyvoidstudio.com/request/get/all_app_users/");
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = br.readLine()) != null)
			{
				line = line.replace('|', '_');
				String[] fields = line.split("_");
				if(fields.length != 4)
					continue;
				User usr = new User();
				usr.Username = fields[0];
				usr.lng = Double.parseDouble(fields[1]);
				usr.lat = Double.parseDouble(fields[2]);
				usr.BluetoothID = Integer.parseInt(fields[3]);
				retUsers.add(usr);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Else
		cb.GetUsersRecieved(retUsers);
	}
}
