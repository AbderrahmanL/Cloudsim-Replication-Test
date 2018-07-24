package org.scenario.cloudsimplus;

import java.util.Date;

import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileAttribute;

public class FileMetadata extends FileAttribute{
	
	private String id;
	
	public FileMetadata(File file, int fileSize) {
		super(file, fileSize);
		setCreationTime();
		setRegistrationId();
	}
	
	public boolean setCreationTime() {

        setCreationTime(new Date().getTime());
        return true;
    }
	
	private boolean setRegistrationId(){
		
		this.id = String.valueOf(this.hashCode())+this.getCreationTime();  
		return false;
		
	}


}
