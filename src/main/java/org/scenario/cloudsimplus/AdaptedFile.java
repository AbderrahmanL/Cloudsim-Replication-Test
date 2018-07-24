package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.resources.File;

public class AdaptedFile extends File{

	
	public AdaptedFile(File file) throws IllegalArgumentException {
		super(file);
	}
	
	public AdaptedFile(String fileName, int fileSize) {
		super(fileName, fileSize);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void createAttribute(final int fileSize) {
        this.setAttribute(new AdaptedAttributes(this, fileSize));
    }
	

}
