package org.scenario.cloudsimplus.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.SanStorage;
import org.scenario.Utils.Utils;
import org.scenario.autoadaptive.ReplicaManager;

public class MountedSan extends SanStorage{
	
	
	private List<Host> hostsAccessingThisSan;
	
	public MountedSan(String name , long capacity, double bandwidth, double networkLatency) throws IllegalArgumentException {
		super(capacity, bandwidth, networkLatency);
		this.hostsAccessingThisSan = new ArrayList<>();
	}

	public void addAccessingHost(Host host) {
		hostsAccessingThisSan.add(host);
	}
	
	public List<Host> getAccessingHosts() {
		return hostsAccessingThisSan;
	}
	
	@Override
	public double addFile(File file) {
		((FileMetadata)file.getAttribute()).setContainingDevice(this);
        ReplicaManager.onFileCreate(file.getAttribute());
		return super.addFile(file);
		
	}


}
