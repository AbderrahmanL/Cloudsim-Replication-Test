package org.scenario.config;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.scenario.autoadaptive.LoadBalancerWeightedLeastConnections;
import org.scenario.cloudsimplus.AdaptedCloudlet;
import org.scenario.cloudsimplus.AdaptedDatacenter;
import org.scenario.cloudsimplus.AdaptedDatacenterBroker;
import org.scenario.cloudsimplus.AdaptedDatacenterStorage;
import org.scenario.cloudsimplus.AdaptedFile;
import org.scenario.cloudsimplus.AdaptedHost;
import org.scenario.cloudsimplus.AdaptedVm;
import org.scenario.cloudsimplus.MountedSan;

public abstract class InitializeReplicationScenarioWithInternalNetwork extends InitializeReplicationScenario {

	/**
     * Creates internal Datacenter network.
     *
     * @param datacenter Datacenter where the network will be created
     */
    protected abstract void createNetwork(AdaptedDatacenter datacenter);
	
    public static int debugCount = 0;
    
    @Override
    public List<DatacenterBroker> init() {
	    	List<DatacenterBroker> brokers = super.init();
	    	populateBriteFile(brokers);
	    	NetworkTopology networkTopology = BriteNetworkTopology.getInstance("topology.brite");
		  	brokers.get(0).getSimulation().setNetworkTopology(networkTopology);
		  	for (int i=1 ; i<brokers.get(0).getSimulation().getEntityList().size() ; i++)
		  	networkTopology.mapNode(i, i);
		  	return brokers;
    }
    
	private void populateBriteFile(List<DatacenterBroker> brokers) {
		try {
			int edgeCounter = 0;
			FileWriter wr = new FileWriter("topology.brite") ;
			wr.write("Nodes: \n");
			for(SimEntity ent : brokers.get(0).getSimulation().getEntityList()) {
			wr.write(ent.getId() + "	1	1	0	0	-1	RT_NODE " + ent.getName() + "\n");
			}
			wr.write("\n");
			wr.write("Edges: \n");
			Iterator<Datacenter> itr = brokers.get(0).getSimulation().getDatacenterList().iterator();
			while(itr.hasNext() ) {
				AdaptedDatacenter dc = (AdaptedDatacenter) itr.next();
				List<Switch> switches = dc.getSwitchMap();
				for(Switch sw : switches) {
					if(sw.getLevel() == 0 ) {
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (sw.getId()+ 1) +  "	1.0		0.00005		0.0	-1	-1	E_RT	U\n");
						edgeCounter++;
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (sw.getId()+ 4) +  "	1.0		0.00005		0.0	-1	-1	E_RT	U\n");
						edgeCounter++;
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (sw.getId()+ 7) +  "	1.0		0.00005		0.0	-1	-1	E_RT	U\n");
						edgeCounter++;
						for(DatacenterBroker br : brokers ) {
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (br.getId()) +  "	1.0		0.2		11.0	-1	-1	E_RT	U\n");
						wr.write( edgeCounter +"	"+ (dc.getId()) +"	"+ (br.getId()) +  "	1.0		0.2		11.0	-1	-1	E_RT	U\n");
						edgeCounter++;	
						}
						
					}
					if(sw.getLevel() == 1 ) {
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (sw.getId()+ 1) +  "	1.0		0.00005		0.0	-1	-1	E_RT	U\n");
						edgeCounter++;
						wr.write( edgeCounter +"	"+ (sw.getId()) +"	"+ (sw.getId()+ 2) +  "	1.0		0.00005		0.0	-1	-1	E_RT	U\n");
						edgeCounter++;
					}
					
				}
					
			}
			wr.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected List<DatacenterBroker> createBrokers(CloudSim simulation) {
		DatacenterBroker broker = new AdaptedDatacenterBroker(simulation);
		List<DatacenterBroker> brokers = new ArrayList<>();
		brokers.add(broker);
		return brokers;
	}

	@Override
	protected Vm createVm(int id, int ram, long mips, int pes) {
		final long   storage = 10000; // vm image size (MEGABYTE)
        final long   bw = 1000; // vm bandwidth (Megabits/s)

        return new AdaptedVm(id, mips, pes)
                .setRam(ram)
                .setBw(bw)
                .setSize(storage)
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
	}

	@Override
	protected Cloudlet createCloudlet(int id, Vm vm) {
		 final long length = SimulationConstParameters.CLOUDLET_EXECUTION_TASK_LENGTH; //in Million Structions (MI)
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
	        if(debugCount < 500) {
	        	cloudlet.addRequiredFile("file1.dat");
	        }
	        if(debugCount < 650 && debugCount >= 500) {
	        	cloudlet.addRequiredFile("file2.dat");
	        }
	        if(debugCount < 800 && debugCount >= 650) {
	        	cloudlet.addRequiredFile("file3.dat");
	        }
	        if(debugCount >= 800) {
	        	cloudlet.addRequiredFile("file4.dat");
	        	debugCount++;
	        }
	        debugCount++;
	        cloudlet.addTask(new CloudletExecutionTask(numberOfCpuCores, SimulationConstParameters.CLOUDLET_EXECUTION_TASK_LENGTH));
//	        cloudlet.setVm(vm);
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
		AdaptedDatacenter dc = new AdaptedDatacenter(simulation, hostList, new VmAllocationPolicySimple(), new LoadBalancerWeightedLeastConnections());
		createNetwork(dc);
    	return dc;
	}

	@Override
	protected FileStorage createStorage(String name, int capacity, double Bandwidth, double networkLatency) {	
		return new MountedSan(name,capacity, Bandwidth, networkLatency);
	}

	@Override
	protected Datacenter createSuperDatacenter(CloudSim simulation) {
		List<Host> hostList = new ArrayList<>(SimulationConstParameters.HOST_SUPER);
		List<FileStorage> storageList = new ArrayList<FileStorage>();
		FileStorage san = new MountedSan("temporary", 1, 1, 1);
        for(int j = 0; j < SimulationConstParameters.HOST_SUPER; j++) {
        	Host host = createHost(32768,4000,16);
            hostList.add(host);
            	if(j % SimulationConstParameters.HOSTS_PER_SWITCH == 0) {
            		san = createStorage("San" + j,1000000000, 1000.0, 0.3);
            		storageList.add(san);  
            	}
            	((MountedSan)san).addAccessingHost(host);
            	((AdaptedHost)host).setStorage(san);   	
        }
        DatacenterStorage datacenterStorage = new  AdaptedDatacenterStorage(storageList);
        Datacenter dc = createDatacenter(simulation, hostList, new VmAllocationPolicySimple());
        File file = new AdaptedFile("file1.dat", 100);
        datacenterStorage.getStorageList().get(1).addFile(new AdaptedFile("file1.dat", 200));
        datacenterStorage.getStorageList().get(3).addFile(new AdaptedFile("file2.dat", 200));
        datacenterStorage.getStorageList().get(5).addFile(new AdaptedFile("file3.dat", 200));
        datacenterStorage.getStorageList().get(4).addFile(new AdaptedFile("file4.dat", 200));
        dc.setDatacenterStorage(datacenterStorage);
//        MetadataCatalog catalog = ReplicaCatalog.getCatalogInstance();
//        System.out.println(((HashMap<Integer, LinkedList<FileAttribute>>)catalog).get(0).get(0).getFileSize());
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
