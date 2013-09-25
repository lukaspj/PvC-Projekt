package com.pik_ant.projectslug;

public class CloudInterface {
	
	// Check if a username is available
	public static void is_username_available(String usn, CloudCallback cb){
		final String _usn = usn;
		final CloudCallback _cb = cb;
		// Run the download of the data in a seperate thread so the main thread
		//  wont get stalled
		Runnable worker = new Runnable() {
			@Override
			public void run() {
				CloudWorker.IsUserWork(_usn, _cb);
			}
		};
		// Start the thread
		Thread t = new Thread(worker);
		t.start();
	}
	
	public static void getUsers(CloudCallback cb) {
		final CloudCallback _cb = cb;
		
		Runnable worker = new Runnable() {
			
			@Override
			public void run() {
				CloudWorker.GetUserWork(_cb);
			}
		};
		
		Thread t = new Thread(worker);
		t.start();
	}
	
	public static void updatePosition(CloudCallback cb, User usr) {
		final CloudCallback _cb = cb;
		final User _usr = usr;
		
		Runnable worker = new Runnable() {
			
			@Override
			public void run() {
				CloudWorker.UpdatePositionWork(_cb, _usr);
			}
		};
		
		Thread t = new Thread(worker);
		t.start();
	}
	
	public static void registerUser(CloudCallback cb, User usr) {
		final CloudCallback _cb = cb;
		final User _usr = usr;
		
		Runnable worker = new Runnable() {
			
			@Override
			public void run() {
				CloudWorker.RegisterUserWork(_cb, _usr);
			}
		};
		
		Thread t = new Thread(worker);
		t.start();
	}
	
	public static void verifyUser(CloudCallback cb, User usr) {
		final CloudCallback _cb = cb;
		final User _usr = usr;
		
		Runnable worker = new Runnable() {
			
			@Override
			public void run() {
				CloudWorker.VerifyUserWork(_cb, _usr);
			}
		};
		
		Thread t = new Thread(worker);
		t.start();
	}
}
