package org.replicationTest.cloudsimplus;

import org.cloudbus.cloudsim.resources.File;

public class ObjectFile extends File{

	
	public ObjectFile(File file) throws IllegalArgumentException {
		super(file);
	}
	
	public ObjectFile(String fileName, int fileSize) {
		super(fileName, fileSize);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void createAttribute(final int fileSize) {
        this.setAttribute(new ObjectAttribute(this, fileSize));
    }
	

}
