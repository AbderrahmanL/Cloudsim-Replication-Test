package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.events.PredicateType;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.vms.Vm;

public class AdaptedEdgeSwitch extends EdgeSwitch {

	public AdaptedEdgeSwitch(CloudSim simulation, NetworkDatacenter dc) {
		super(simulation, dc);
		// TODO Auto-generated constructor stub
	}

	 @Override
	    protected void processPacketUp(SimEvent ev) {
		 getSimulation().cancelAll(this, new PredicateType(CloudSimTags.NETWORK_EVENT_SEND));
	        schedule(this, this.getSwitchingDelay(), CloudSimTags.NETWORK_EVENT_SEND);

	        final HostPacket hostPkt = (HostPacket) ev.getData();
	        
	        final Vm receiverVm = hostPkt.getVmPacket().getDestination();

	        // packet is received from host
	        // packet is to be sent to aggregate level or to another host in the same level
	        if (receiverVm != null){
	        	
	        	final NetworkHost host = getVmHost(receiverVm);
	        	hostPkt.setDestination(host);
	        	
	        	// packet needs to go to a host which is connected directly to switch
	        	if (host != null && host != Host.NULL && host.getDatacenter().getId() == this.getDatacenter().getId()) {
	        		addPacketToBeSentToHost(host, hostPkt);
	        		return;
	        	}
	        }

	        // otherwise, packet is to be sent to upper switch
	        /*
	         * ASSUMPTION: Each Edge is connected to one Aggregate Switch.
	         * If there are more than one Aggregate Switch, the following code has to be modified.
	        */
	        final Switch aggregateSwitch = getUplinkSwitches().get(0);
	        addPacketToBeSentToUplinkSwitch(aggregateSwitch, hostPkt);
	    }
}
