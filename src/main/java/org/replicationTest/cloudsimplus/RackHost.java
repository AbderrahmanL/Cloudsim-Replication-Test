package org.replicationTest.cloudsimplus;

import java.util.List;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;

public class RackHost extends NetworkHost{

	private int rackId;
	
	private static int hostCount = 0 ;
	
	public RackHost(long ram, long bw, long storage, List<Pe> peList) {
		super(ram, bw, storage, peList);
		hostCount++;
	}

	public RackHost(long ram, long bw, long storage, List<Pe> peList,
			VmScheduler vmScheduler) {
		super(ram, bw, storage, peList, vmScheduler);
		hostCount++;
	}

	public int getHostCount(){
		return hostCount;
	}

	public int getRackId() {
		return rackId;
	}

	public Host setRackId(int rackId) {
		this.rackId = rackId;
		return this;
	}

}
