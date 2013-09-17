package com.pvc.projectslug;
public class CloudCallback {
	enum IsUserResult {
		Registered,
		NotRegistered,
		MultipleRegistered
	}
	public void IsUserRecieved(IsUserResult res) { };
}
