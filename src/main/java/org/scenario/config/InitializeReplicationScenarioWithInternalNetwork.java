package org.scenario.config;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.resources.SanStorage;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.network.NetworkVm;
import org.scenario.cloudsimplus.AdaptedCloudlet;
import org.scenario.cloudsimplus.AdaptedDatacenter;
import org.scenario.cloudsimplus.AdaptedDatacenterBroker;
import org.scenario.cloudsimplus.AdaptedDatacenterStorage;
import org.scenario.cloudsimplus.AdaptedFile;
import org.scenario.cloudsimplus.AdaptedHost;

public abstract class InitializeReplicationScenarioWithInternalNetwork extends InitializeReplicationScenario {

	/**
     * Creates internal Datacenter network.
     *
     * @param datacenter Datacenter where the network will be created
     */
    protected abstract void createNetwork(AdaptedDatacenter datacenter);
	
	@Override
	protected DatacenterBroker createBroker(CloudSim simulation) {
		DatacenterBroker broker = new AdaptedDatacenterBroker(simulation);
		return broker;
	}

	@Override
	protected Vm createVm(int id, int ram, long mips, int pes) {
		final long   storage = 10000; // vm image size (MEGABYTE)
        final long   bw = 1000; // vm bandwidth (Megabits/s)

        return new NetworkVm(id, mips, pes)
                .setRam(ram)
                .setBw(bw)
                .setSize(storage)
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
	}

	@Override
	protected Cloudlet createCloudlet(int id, Vm vm) {
		 final long length = 10000; //in Million Structions (MI)
	        final long fileSize = 300; //Size (in bytes) before execution
	        final long outputSize = 300; //Size (in bytes) after execution
	        final int  numberOfCpuCores = 2; // cores used by cloudlet

	        AdaptedCloudlet cloudlet
	                = (AdaptedCloudlet) new AdaptedCloudlet(
			        id, length, numberOfCpuCores)
			        .setFileSize(fileSize)
			        .setOutputSize(outputSize)
			        .setUtilizationModelRam(new UtilizationModelFull())
			        .setUtilizationModelCpu(new UtilizationModelFull())
			        .setUtilizationModelBw(new UtilizationModelFull());
//	        cloudlet.addRequiredFile("file1.dat");
//	        cloudlet.addRequiredFile("file2.dat");
//	        cloudlet.addRequiredFile("file3.dat");
	        cloudlet.addTask(new CloudletExecutionTask(cloudlet.getTasks().size(), SimulationConstParameters.CLOUDLET_EXECUTION_TASK_LENGTH));
	        cloudlet.setVm(vm);
	        return cloudlet;
	}

	@Override
	protected Host createHost(int ram, long mips, int pes) {
		 final long storage = 1000000; // host storage (MEGABYTE)
	        final long bw = 10000; //in Megabits/s

	        List<Pe> pesList = new ArrayList<>(); //List of CPU cores

	        for (int i = 0; i < pes; i++) {
	            pesList.add(new PeSimple(mips, new PeProvisionerSimple()));
	        }

	        return (AdaptedHost) new AdaptedHost(ram, bw, storage, pesList)
	                .setRamProvisioner(new ResourceProvisionerSimple())
	                .setBwProvisioner(new ResourceProvisionerSimple())
	                .setVmScheduler(new VmSchedulerSpaceShared());
	}

	@Override
	protected Datacenter createDatacenter(CloudSim simulation,
			List<Host> hostList, VmAllocationPolicy vmAllocationPolicy) {
		AdaptedDatacenter dc = new AdaptedDatacenter(simulation, hostList, new VmAllocationPolicySimple());
		createNetwork(dc);
		dc.setSchedulingInterval(5);
    	return dc;
	}

	@Override
	protected FileStorage createStorage(int i, double d, int j) {	
		return new SanStorage(1000000000, 10.0, 5);
	}

	@Override
	protected Datacenter createSuperDatacenter(CloudSim simulation) {
		List<Host> hostList = new ArrayList<>(SimulationConstParameters.HOST_SUPER);
		List<FileStorage> storageList = new ArrayList<FileStorage>();
        for(int j = 0; j < SimulationConstParameters.HOST_SUPER; j++) {
        	int currentRack = 0;
        	Host host = createHost(32768,4000,16);
            hostList.add(host);
            	storageList.add((createStorage(1000000000, 10.0, 5)));              
        }
	    storageList.get(0).addFile(new AdaptedFile("file1.dat", 1));
	    storageList.get(0).addFile(new AdaptedFile("file2.dat", 10));
	    storageList.get(0).addFile(new AdaptedFile("file3.dat", 150));
	    storageList.get(0).addFile(new AdaptedFile("file4.dat", 1000));
	    DatacenterStorage datacenterStorage = new  AdaptedDatacenterStorage();
        Datacenter dc = createDatacenter(simulation, hostList, new VmAllocationPolicySimple());
        dc.setDatacenterStorage(datacenterStorage);
        datacenterStorage.setStorageList(storageList);
        return dc;
	}

	@Override
	protected Datacenter createMainDatacenter(CloudSim simulation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Datacenter createOrdinaryDatacenter(CloudSim simulation) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
