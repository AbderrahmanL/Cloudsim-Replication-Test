package org.scenario.config;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimEntity;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.network.topologies.NetworkTopology;
import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.vms.Vm;

public abstract class InitializeReplicationScenario {
	
			protected List<Datacenter> dcs;
			protected DatacenterBroker broker;
			protected List<Vm> vmList;
			protected List<Cloudlet> cloudletList;
			
			protected abstract DatacenterBroker createBroker(CloudSim simulation) ;

			protected abstract Vm createVm(int id, int ram,long mips,int pes);
			
			protected abstract Cloudlet createCloudlet(int id, Vm vm);
			
			protected abstract  Host createHost(int ram,long mips,int pes);

			protected abstract Datacenter createDatacenter(CloudSim simulation, List<Host> hostList, VmAllocationPolicy vmAllocationPolicy);
			
			protected abstract FileStorage createStorage(int i, double d, double j) ;
			
			protected abstract Datacenter createSuperDatacenter(CloudSim simulation) ;
			
			protected abstract Datacenter createMainDatacenter(CloudSim simulation) ;
			
			protected abstract Datacenter createOrdinaryDatacenter(CloudSim simulation) ;
			
			public DatacenterBroker init(){
				CloudSim simulation = new CloudSim();
			    dcs = createDatacenters(simulation);
			    
			    broker = createBroker(simulation);
		
			    vmList = new ArrayList<>();
			    cloudletList = new ArrayList<>();
			    
			    createVms();
			    
			    NetworkTopology networkTopology = BriteNetworkTopology.getInstance("topology.brite");
			  	simulation.setNetworkTopology(networkTopology);
			  	for (int i=1 ; i<22 ; i++)
			  	networkTopology.mapNode(i, i);
				return broker;
				}
			
			/**
		     * Creates VMs, one VM for each host and one Cloudlet for each VM.
		     */
			protected void createVms(){
				
			    for (int i = 0; i < SimulationConstParameters.HOST_SUPER*SimulationConstParameters.DC_SUPER; i++) {
			        Vm vm = createVm(vmList.size(), 32768,1000,16);
			        vmList.add(vm);
			        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
			            Cloudlet cloudlet = createCloudlet(cloudletList.size(), vm);
			            cloudletList.add(cloudlet);
			        }
			    }
			    for (int i = 0; i < SimulationConstParameters.HOST_MID*SimulationConstParameters.DC_MID; i++) {
			        Vm vm = createVm(vmList.size(), 16348,2500,6);
			        vmList.add(vm);
			        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
//			            Cloudlet cloudlet = createCloudlet(cloudletList.size(), broker, vm);
//			            cloudletList.add(cloudlet);
			        }
			    }
			    for (int i = 0; i < SimulationConstParameters.HOST_STANDARD*SimulationConstParameters.DC_STANDARD; i++) {
			        Vm vm = createVm(vmList.size(), 8192,1000,4);
			        vmList.add(vm);
			        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
//			            Cloudlet cloudlet = createCloudlet(cloudletList.size(), broker, vm);
//			            cloudletList.add(cloudlet);
			        }
			    }
			    broker.submitVmList(vmList);
			    
			}
			
			protected List<Datacenter> createDatacenters(CloudSim simulation){
					
					List<Datacenter> datacenters = new ArrayList<Datacenter>(1);
					    
				        for(int i=0 ; i<SimulationConstParameters.DC_SUPER; i++){
				        	Datacenter dc = createSuperDatacenter(simulation);
					        datacenters.add(dc);
				        } 
				        
				        for(int i=0 ; i<SimulationConstParameters.DC_MID; i++){
				        	Datacenter dc = createMainDatacenter(simulation);
						    datacenters.add(dc);
				        }
				        
				        for(int i=0 ; i<SimulationConstParameters.DC_STANDARD; i++){
				        	Datacenter dc = createOrdinaryDatacenter(simulation);
						    datacenters.add(dc);
				        }
				        
					return datacenters;
				}
			
}
