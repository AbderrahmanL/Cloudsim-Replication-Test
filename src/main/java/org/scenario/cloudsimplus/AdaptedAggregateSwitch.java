package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.events.PredicateType;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.util.Conversion;
import org.cloudbus.cloudsim.vms.Vm;

public class AdaptedAggregateSwitch extends AbstractSwitch {
	
	/**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 1;

    /**
     * Default delay of {@link AggregateSwitch} in milliseconds.
     */
    public static final double SWITCHING_DELAY = 0.00245;
	
    /**
     * Default downlink bandwidth of {@link AggregateSwitch} in Megabits/s.
     * It also represents the uplink bandwidth of connected edge Datacenter.
     */
    public static final long DOWNLINK_BW =  (long)Conversion.GIGABYTE * 10 * 8 ;
    
    /**
     * Default number of aggregation switch ports that defines the number of
     * {@link EdgeSwitch} that can be connected to it.
     */
    public static final int PORTS = 1;

    public AdaptedAggregateSwitch(CloudSim simulation, NetworkDatacenter dc) {
        super(simulation, dc);
        setUplinkBandwidth(AdaptedRootSwitch.DOWNLINK_BW);
        setDownlinkBandwidth(DOWNLINK_BW);
        setSwitchingDelay(SWITCHING_DELAY);
        setPorts(PORTS);

    }
    
	    @Override
	    protected void processPacketDown(SimEvent ev) {
	        super.processPacketDown(ev);
	
	        final HostPacket netPkt = (HostPacket) ev.getData();
	        final Vm receiverVm = netPkt.getVmPacket().getDestination();
	
	        // packet is coming from root so need to be sent to edgelevel swich
	        // find the id for edgelevel switch
	        final Switch netSwitch = getVmEdgeSwitch(receiverVm);
	        addPacketToBeSentToDownlinkSwitch(netSwitch, netPkt);
	    }

	 @Override
	    protected void processPacketUp(SimEvent ev) {
		 	super.processPacketUp(ev);

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

	    @Override
	    public int getLevel() {
	        return LEVEL;
	    }
}
