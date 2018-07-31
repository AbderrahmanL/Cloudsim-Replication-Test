package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.events.PredicateType;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.vms.Vm;

public class AdaptedAggregateSwitch extends AggregateSwitch {

	public AdaptedAggregateSwitch(CloudSim simulation, NetworkDatacenter dc) {
		super(simulation, dc);
		// TODO Auto-generated constructor stub
	}

	 @Override
	    protected void processPacketUp(SimEvent ev) {
		 	getSimulation().cancelAll(this, new PredicateType(CloudSimTags.NETWORK_EVENT_SEND));
	        schedule(this, this.getSwitchingDelay(), CloudSimTags.NETWORK_EVENT_SEND);

	        final HostPacket netPkt = (HostPacket) ev.getData();
	        final Vm receiverVm = netPkt.getVmPacket().getDestination();

	        // packet is coming from edge level router so need to be sent to
	        // either root or another edge level swich
	        // find the id for edge level switch
	        if(  receiverVm != null){
	        	
	        	final Switch edgeSwitch = getVmEdgeSwitch(receiverVm);
	        	if (findConnectedEdgeSwitch(edgeSwitch)) {
	        		addPacketToBeSentToDownlinkSwitch(edgeSwitch, netPkt);
	        	}
	        	} else { // send to up
	        		final Switch sw = getUplinkSwitches().get(0);
	        		addPacketToBeSentToUplinkSwitch(sw, netPkt);
	        	}
	    }
	 
	 private boolean findConnectedEdgeSwitch(Switch edgeSwitch) {
	        return getDownlinkSwitches().stream().anyMatch(edgeSwitch::equals);
	    }
}
