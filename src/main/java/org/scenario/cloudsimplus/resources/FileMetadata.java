package org.scenario.cloudsimplus.resources;

import java.util.Date;
import java.util.UUID;

import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileAttribute;
import org.cloudbus.cloudsim.resources.FileStorage;

public class FileMetadata extends FileAttribute{
	
	private static int latestInode = 0; // id of the file
	
	private static int noOfAccesses = 0; 
	
	private FileStorage containingDevice;
	
	private int dcId;
	
	private String name;
	
	public FileMetadata(File file, int fileSize) {
		super(file, fileSize);
		this.name = file.getName();
		setCreationTime(new Date().getTime());
		setRegistrationId(latestInode);
		latestInode++;
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

	public FileStorage getContainingDevice() {
		return containingDevice;
	}

	public void setContainingDevice(FileStorage containingDevice) {
		this.containingDevice = containingDevice;
	}

	public int getDcId() {
		return dcId;
	}

	public void setDcId(int dcId) {
		this.dcId = dcId;
	}

	public String getName() {
		return name;
	}

}
