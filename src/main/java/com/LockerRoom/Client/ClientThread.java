package com.LockerRoom.Client;

public class ClientThread extends Thread{
	
	private LockerRoomClient lrc;

	public ClientThread(LockerRoomClient client) {
		this.setLrc(client);
	}
	
	public void run(){
		lrc.run();
	}

	public LockerRoomClient getLrc() {
		return lrc;
	}

	public void setLrc(LockerRoomClient lrc) {
		this.lrc = lrc;
	}

}
