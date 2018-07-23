package org.replicationTest.cloudsimplus;

import java.util.Date;

import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileAttribute;

public class ObjectAttribute extends FileAttribute{
	
	private String id;
	
	public ObjectAttribute(File file, int fileSize) {
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
