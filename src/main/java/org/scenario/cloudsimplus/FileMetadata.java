package org.scenario.cloudsimplus;

import java.util.Date;
import java.util.UUID;

import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileAttribute;

public class FileMetadata extends FileAttribute{
	
	private static int latestInode = 0; // id of the file
	
	private static int noOfAccesses = 0; 
	
	private UUID containingDevice;
	
	private int dcId;
	
	public FileMetadata(File file, int fileSize) {
		super(file, fileSize);
		setCreationTime(new Date().getTime());
		setRegistrationId(latestInode++);
	}
	
	@Override
	public boolean setRegistrationId(int latestInode) {
		return super.setRegistrationId(latestInode);
	}
	
	@Override
	public int getRegistrationID() {
		return super.getRegistrationID();
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

	public int getDcId() {
		return dcId;
	}

	public void setDcId(int dcId) {
		this.dcId = dcId;
	}
	
	

}
