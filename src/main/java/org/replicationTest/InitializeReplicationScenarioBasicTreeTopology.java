package org.replicationTest;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.network.CloudletReceiveTask;
import org.cloudbus.cloudsim.cloudlets.network.CloudletSendTask;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.RootSwitch;
import org.cloudbus.cloudsim.vms.Vm;
import org.replicationTest.cloudsim.RackHost;

public class InitializeReplicationScenarioBasicTreeTopology extends InitializeReplicationScenarioWithInternalNetwork{

	private static final long TASK_RAM = 100;
	private static final int NUMBER_OF_PACKETS_TO_SEND = 1;
	private static final long PACKET_DATA_LENGTH_IN_BYTES = 400000;

	/**
     * Creates internal Datacenter network.
     *
     * @param datacenter Datacenter where the network will be created
     */
	@Override
	protected void createNetwork(NetworkDatacenter datacenter) {
        EdgeSwitch[] edgeSwitches = new EdgeSwitch[4];
        AggregateSwitch[] aggregateSwitches = new AggregateSwitch[4];
        RootSwitch rootSwitch = new RootSwitch((CloudSim) datacenter.getSimulation(), datacenter);
        datacenter.addSwitch(rootSwitch);
        for (int i = 0; i < ((RackHost) datacenter.getHostList().get(0)).getHostCount()/(SimulationConstParameters.Hosts_PER_RACK * SimulationConstParameters.RACKS_PER_SWITCH); i++) {
            edgeSwitches[i] = new EdgeSwitch((CloudSim) datacenter.getSimulation(), datacenter);
            aggregateSwitches[i] = new AggregateSwitch((CloudSim) datacenter.getSimulation(), datacenter);

            aggregateSwitches[i].getDownlinkSwitches().add(edgeSwitches[i]);
            aggregateSwitches[i].getUplinkSwitches().add(rootSwitch);
            
            datacenter.addSwitch(aggregateSwitches[i]);
            datacenter.addSwitch(edgeSwitches[i]);
        }

        for (NetworkHost host : datacenter.<NetworkHost>getHostList()) {
        	int indexCurrentSwitch = 0, hostPerSwitchcounter = 0;
        	
            int switchNum = host.getId() / edgeSwitches[indexCurrentSwitch].getPorts();
            /**
            @TODO these two calls below are redundant.
            When connecting a host to a switch, the
            switch should be automatically linked to the Host
            to create the bi-directional association.
            */
            edgeSwitches[switchNum].connectHost(host);
            host.setEdgeSwitch(edgeSwitches[switchNum]);
            hostPerSwitchcounter++;
            if(hostPerSwitchcounter == 4){
            	hostPerSwitchcounter = 0;
            	indexCurrentSwitch++;
            }
        }
    }
	
	@Override
	protected void createVms(){
		
	    for (int i = 0; i < SimulationConstParameters.HOST_SUPER*SimulationConstParameters.DC_SUPER; i++) {
	        Vm vm = createVm(vmList.size(), 32768,4000,8);
	        vmList.add(vm);
	        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
	            NetworkCloudlet cloudlet = (NetworkCloudlet)createCloudlet(cloudletList.size(), broker, vm);
	            cloudletList.add(cloudlet);
	        }
	    }
	    for (int i = 0; i < SimulationConstParameters.HOST_MID*SimulationConstParameters.DC_MID; i++) {
	        Vm vm = createVm(vmList.size(), 16348,2500,6);
	        vmList.add(vm);
	        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
	        	NetworkCloudlet cloudlet = (NetworkCloudlet)createCloudlet(cloudletList.size(), broker, vm);
	            cloudletList.add(cloudlet);
	        }
	    }
	    for (int i = 0; i < SimulationConstParameters.HOST_STANDARD*SimulationConstParameters.DC_STANDARD; i++) {
	        Vm vm = createVm(vmList.size(), 8192,1000,4);
	        vmList.add(vm);
	        for (int j = 0; j < SimulationConstParameters.CLOUDLETS_PER_VM; j++) {
	        	NetworkCloudlet cloudlet = (NetworkCloudlet)createCloudlet(cloudletList.size(), broker, vm);
	            cloudletList.add(cloudlet);
	        }
	    }
//
        addSendTask((NetworkCloudlet)cloudletList.get(0), (NetworkCloudlet)cloudletList.get(1));
//        addReceiveTask((NetworkCloudlet)cloudletList.get(1), (NetworkCloudlet)cloudletList.get(0));
	    broker.submitVmList(vmList);
	    broker.submitCloudletList(cloudletList);
	    
	}
	
	private void addSendTask(
            NetworkCloudlet sourceCloudlet,
            NetworkCloudlet destinationCloudlet) {
        CloudletSendTask task = new CloudletSendTask(sourceCloudlet.getTasks().size());
        task.setMemory(TASK_RAM);
        sourceCloudlet.addTask(task);
        for (int i = 0; i < NUMBER_OF_PACKETS_TO_SEND; i++) {
            task.addPacket(destinationCloudlet, PACKET_DATA_LENGTH_IN_BYTES);
        }
    }

    /**
     * Adds a receive task to list of tasks of the given
     * {@link NetworkCloudlet}.
     *
     * @param cloudlet the {@link NetworkCloudlet} that the task will belong to
     * @param sourceCloudlet the cloudlet where it is expected to receive
     * packets from
     */
    private void addReceiveTask(NetworkCloudlet cloudlet, NetworkCloudlet sourceCloudlet) {
        CloudletReceiveTask task = new CloudletReceiveTask(
                cloudlet.getTasks().size(), sourceCloudlet.getVm());
        task.setMemory(TASK_RAM);
        task.setNumberOfExpectedPacketsToReceive(NUMBER_OF_PACKETS_TO_SEND);
        cloudlet.addTask(task);
    }

}
