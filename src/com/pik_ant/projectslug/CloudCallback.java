package com.pik_ant.projectslug;

import java.util.List;

public class CloudCallback {
	// Enum for possible returned results
	enum IsUserResult {
		Registered,
		NotRegistered,
		MultipleRegistered
	}
	// Callback for checking if an username is available
	public void IsUserRecieved(IsUserResult res) { };
	
	// Callback for getting all users
	public void GetUsersRecieved(List<User> lis) {};
}
