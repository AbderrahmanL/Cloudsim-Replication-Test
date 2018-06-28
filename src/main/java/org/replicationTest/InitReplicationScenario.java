package org.replicationTest;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.resources.SanStorage;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.replicationTest.cloudsim.ObjectFile;


public class InitReplicationScenario {

	public DatacenterBroker init(){
	CloudSim simulation = new CloudSim();
    List<Datacenter> dcs = createDatacenters(simulation);
    
    DatacenterBroker broker0 = new DatacenterBrokerSimple(simulation);

    List<Vm> vmList = new ArrayList<>();
    List<Cloudlet> cloudletList = new ArrayList<>();

    /*
     * Creates VMs, one VM for each host and one Cloudlet for each VM.
     */
    for (int i = 0; i < SimulationConstParameters.HOST_SUPER*SimulationConstParameters.DC_SUPER; i++) {
        Vm vm = createVm(vmList.size(), 32768,4000,8);
        vmList.add(vm);
        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
            Cloudlet cloudlet = createCloudlet(cloudletList.size(), broker0, vm);
            cloudletList.add(cloudlet);
        }
    }
    for (int i = 0; i < SimulationConstParameters.HOST_MID*SimulationConstParameters.DC_MID; i++) {
        Vm vm = createVm(vmList.size(), 16348,2500,6);
        vmList.add(vm);
        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
            Cloudlet cloudlet = createCloudlet(cloudletList.size(), broker0, vm);
            cloudletList.add(cloudlet);
        }
    }
    for (int i = 0; i < SimulationConstParameters.HOST_STANDARD*SimulationConstParameters.DC_STANDARD; i++) {
        Vm vm = createVm(vmList.size(), 8192,1000,4);
        vmList.add(vm);
        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
            Cloudlet cloudlet = createCloudlet(cloudletList.size(), broker0, vm);
            cloudletList.add(cloudlet);
        }
    }
    broker0.submitVmList(vmList);
    broker0.submitCloudletList(cloudletList);
    
    NetworkTopology networkTopology = BriteNetworkTopology.getInstance("topology.brite");
  	simulation.setNetworkTopology(networkTopology);
  	for (int i=1 ; i<18 ; i++)
  	networkTopology.mapNode(i, i-1);
  	networkTopology.mapNode(broker0.getId(),17);
    for(Datacenter dc : dcs)
    	System.out.println(dc.getId());
	return broker0;
	}
	
	public Host createHost(int ram,long mips,int pes) {
        final long storage = 1000000; // host storage (MEGABYTE)
        final long bw = 10000; //in Megabits/s

        List<Pe> pesList = new ArrayList<>(); //List of CPU cores

        for (int i = 0; i < pes; i++) {
            pesList.add(new PeSimple(mips, new PeProvisionerSimple()));
        }

        return new HostSimple(ram, bw, storage, pesList)
                .setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerSpaceShared());
    }

    public Vm createVm(int id, int ram,long mips,int pes) {
        final long   storage = 10000; // vm image size (MEGABYTE)
        final long   bw = 1000; // vm bandwidth (Megabits/s)

        return new VmSimple(id, mips, pes)
                .setRam(ram)
                .setBw(bw)
                .setSize(storage)
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
    }

    public Cloudlet createCloudlet(int id, DatacenterBroker broker, Vm vm) {
        final long length = 10000; //in Million Structions (MI)
        final long fileSize = 300; //Size (in bytes) before execution
        final long outputSize = 300; //Size (in bytes) after execution
        final int  numberOfCpuCores = 2; // cores used by cloudlet

        Cloudlet cloudlet
                = new CloudletSimple(
                        id, length, numberOfCpuCores)
                        .setFileSize(fileSize)
                        .setOutputSize(outputSize)
                        .setUtilizationModelRam(new UtilizationModelDynamic(0.5))
                        .setUtilizationModelCpu(new UtilizationModelDynamic(1))
                        .setUtilizationModelBw(new UtilizationModelDynamic(0.5));
        cloudlet.addRequiredFile("file1.dat");
        cloudlet.addRequiredFile("file2.dat");
        cloudlet.addRequiredFile("file3.dat");
        return cloudlet;
    }
    
    public List<Datacenter> createDatacenters(CloudSim simulation){
		
		List<Datacenter> datacenters = new ArrayList<Datacenter>(1);
		    
	        for(int i=0 ; i<SimulationConstParameters.DC_SUPER; i++){
	        	List<Host> hostList3 = new ArrayList<>(SimulationConstParameters.HOST_SUPER);
		        for(int j = 0; j < SimulationConstParameters.HOST_SUPER; j++) {
		            Host host = createHost(32768,4000,8);
		            hostList3.add(host);
		        }
		    
	        datacenters.add(new DatacenterSimple(simulation, hostList3, new VmAllocationPolicySimple()));
	        }
	        for(Datacenter dc : datacenters){
	        	List<FileStorage> storageList = new ArrayList<FileStorage>();
			    storageList.add(new SanStorage(1000000000, 10.0, 5));
			    storageList.get(0).addFile(new ObjectFile("file1.dat", 20));
			    storageList.get(0).addFile(new ObjectFile("file2.dat", 100));
			    storageList.get(0).addFile(new ObjectFile("file3.dat", 700));
			    dc.getDatacenterStorage().setStorageList(storageList);
	        }
	        
	        for(int i=0 ; i<SimulationConstParameters.DC_MID; i++){
	        	List<Host> hostList2 = new ArrayList<>(SimulationConstParameters.HOST_MID);
		        for(int j = 0; j < SimulationConstParameters.HOST_MID; j++) {
		            Host host = createHost(16348,2500,6);
		            hostList2.add(host);
		        }
		        List<FileStorage> storageList = new ArrayList<FileStorage>();
		        DatacenterStorage datacenterStorage = new  DatacenterStorage();
			    storageList.add(new SanStorage(100000000, 10.0, 3));
			    datacenterStorage.setStorageList(storageList);
			    Datacenter dc = new DatacenterSimple(simulation, hostList2, new VmAllocationPolicySimple());
			    dc.setDatacenterStorage(datacenterStorage);
	        datacenters.add(dc);
	        }
	        for(int i=0 ; i<SimulationConstParameters.DC_STANDARD; i++){
	        	List<Host> hostList = new ArrayList<>(SimulationConstParameters.HOST_STANDARD);
		        for(int j = 0; j < SimulationConstParameters.HOST_STANDARD; j++) {
		            Host host = createHost(8192,1000,4);
		            hostList.add(host);
		        }
		        List<FileStorage> storageList = new ArrayList<FileStorage>();
		        DatacenterStorage datacenterStorage = new  DatacenterStorage();
			    storageList.add(new SanStorage(10000000, 10.0, 4));
			    datacenterStorage.setStorageList(storageList);
			    Datacenter dc = new DatacenterSimple(simulation, hostList, new VmAllocationPolicySimple());
			    dc.setDatacenterStorage(datacenterStorage);
	        datacenters.add(dc);
	        }
	        
		return datacenters;
	}
    
}
