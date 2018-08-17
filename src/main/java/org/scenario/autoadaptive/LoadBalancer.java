package org.scenario.autoadaptive;

import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.vms.Vm;

public interface LoadBalancer {

	Vm electVm();
	
	public int updateAvgCloudletLenght();

	void setDatacenter(Datacenter datacenter);

}
