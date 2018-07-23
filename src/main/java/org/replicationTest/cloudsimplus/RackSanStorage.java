package org.replicationTest.cloudsimplus;

import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.resources.SanStorage;

public class RackSanStorage extends SanStorage{

	public RackSanStorage(long capacity, double bandwidth, double networkLatency)
			throws IllegalArgumentException {
		super(capacity, bandwidth, networkLatency);
		// TODO Auto-generated constructor stub
	}
	
	private int rackId;
	
	public RackSanStorage(String name, long capacity, double bandwidth,
			double networkLatency) {
		super(name, capacity, bandwidth, networkLatency);
		// TODO Auto-generated constructor stub
	}

	public int getRackId() {
		return rackId;
	}

	public FileStorage setRackId(int rackId) {
		this.rackId = rackId;
		return this;
	}
	
	

}
