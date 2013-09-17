package com.pvc.projectslug;

public class CloudInterface {
	
	public static void is_username_available(String usn, CloudCallback cb){
		final String _usn = usn;
		final CloudCallback _cb = cb;
		Runnable worker = new Runnable() {
			@Override
			public void run() {
				CloudWorker.IsUserWork(_usn, _cb);
			}
		};
		Thread t = new Thread(worker);
		t.start();
	}
}
