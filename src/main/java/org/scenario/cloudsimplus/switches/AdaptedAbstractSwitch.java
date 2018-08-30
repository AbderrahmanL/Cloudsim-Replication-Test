package org.scenario.cloudsimplus.switches;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.events.PredicateType;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.util.Conversion;

/**
 * An base class for implementing Network Switch.
 *
 * @author Saurabh Kumar Garg
 * @author Manoel Campos da Silva Filho
 * @author Abderrahman Lahiaouni
 */

public abstract class AdaptedAbstractSwitch extends AbstractSwitch{
	

	public AdaptedAbstractSwitch(CloudSim simulation, NetworkDatacenter dc) {
		super(simulation, dc);
		// TODO Auto-generated constructor stub
	}
	
		/**
	     * Sends a packet to Datacenter connected through a downlink port.
	     * A switch in reality has a buffer so when overwhelmed by sender
	     * packets get queued, also in reality when buffer is full packets
	     * get lost so it has to be send from source again, this is a bit 
	     * complex to implement, so adding a "queuing delay" proportional 
	     * to number of packets being processed by the switch for the time
	     * being will do fine.
	     * 
	     * @param ev Event/packet to process
	     */
		 protected void processPacketDown(final SimEvent ev) {
			
	        getSimulation().cancelAll(this, new PredicateType(CloudSimTags.NETWORK_EVENT_SEND));
	        schedule(this, getSwitchingDelay() + getQueueingDelay()  , CloudSimTags.NETWORK_EVENT_SEND);
	    }
	 
	 	/**
	     * Sends a packet to Datacenter connected through a uplink port.
	     *
	     * @param ev Event/packet to process
	     */
	    protected void processPacketUp(final SimEvent ev) {
	    	double queueingDelay = getQueueingDelay();
	    	if(queueingDelay > 0)
	    		System.out.println();
	        getSimulation().cancelAll(this, new PredicateType(CloudSimTags.NETWORK_EVENT_SEND));
	        schedule(this, getSwitchingDelay() + queueingDelay, CloudSimTags.NETWORK_EVENT_SEND);
	    }
	    
	    public double getQueueingDelay() {
	    	int temp = getNumberOfPacketsBeingProcessed();
	    	return  temp  * 1E-6 ;
	    }
	    
	    protected int getNumberOfPacketsBeingProcessed() {
	    	int packetsBeingProcessed = 0;
			for(Switch sw : getDownlinkSwitches()) {
				packetsBeingProcessed += getDownlinkSwitchPacketList(sw).size();
				if(sw.getLevel() == 2) {
					for(NetworkHost host : ((AdaptedEdgeSwitch)sw).getHostList())
					packetsBeingProcessed += getHostPacketList(host).size();
				}
			}
			for(Switch sw : getUplinkSwitches()) {
				packetsBeingProcessed += getUplinkSwitchPacketList(sw).size();
			}
			return packetsBeingProcessed;
	    }
}
