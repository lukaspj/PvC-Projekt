package com.pvc.projectslug;
public class CloudCallback {
	// Enum for possible returned results
	enum IsUserResult {
		Registered,
		NotRegistered,
		MultipleRegistered
	}
	// Callback for checking if an username is available
	public void IsUserRecieved(IsUserResult res) { };
}
