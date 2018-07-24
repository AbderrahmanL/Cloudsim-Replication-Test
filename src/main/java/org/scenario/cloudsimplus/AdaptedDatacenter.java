package org.scenario.cloudsimplus;

import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.FileStorage;

public class AdaptedDatacenter extends DatacenterSimple{

	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList,
							VmAllocationPolicy vmAllocationPolicy, List<FileStorage> storageList) {
		super(simulation, hostList, vmAllocationPolicy, storageList);
		// TODO Auto-generated constructor stub
	}
	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList,
							VmAllocationPolicy vmAllocationPolicy, DatacenterStorage storage) {
		super(simulation, hostList, vmAllocationPolicy, storage);
		// TODO Auto-generated constructor stub
	}
	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList, VmAllocationPolicy vmAllocationPolicy) {
		super(simulation, hostList, vmAllocationPolicy);
		// TODO Auto-generated constructor stub
	}
	
	
	

}
