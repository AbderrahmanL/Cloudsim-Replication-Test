package org.scenario.cloudsimplus;

import java.util.Date;
import java.util.UUID;

import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileAttribute;

public class FileMetadata extends FileAttribute{
	
	private static int latestInode = 0;
	
	private int noOfAccesses = 0; 
	
	private UUID containingDevice;
	
	public FileMetadata(File file, int fileSize) {
		super(file, fileSize);
		setCreationTime(new Date().getTime());
		setRegistrationId(latestInode++);
	}
	
	@Override
	public boolean setRegistrationId(int latestInode) {
		return super.setRegistrationId(latestInode);
	}

	public int getNoOfAccesses() {
		return noOfAccesses;
	}

	public void incrementNoOfAccesses() {
		this.noOfAccesses++;
	}

	public UUID getContainingDevice() {
		return containingDevice;
	}

	public void setContainingDevice(UUID containingDevice) {
		this.containingDevice = containingDevice;
	}
	
	

}
