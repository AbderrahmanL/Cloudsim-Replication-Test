package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.switches.AbstractSwitch;
import org.cloudbus.cloudsim.network.switches.AggregateSwitch;
import org.cloudbus.cloudsim.network.switches.RootSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology;
import org.cloudbus.cloudsim.util.Conversion;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptedRootSwitch extends AbstractSwitch {
	
	private static final Logger logger = LoggerFactory.getLogger(RootSwitch.class.getSimpleName());

    /**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 0;

    /**
     * Default number of root switch ports that defines the number of
     * {@link AggregateSwitch} that can be connected to it.
     */
    public static final int PORTS = 1;

    /**
     * Default switching delay in milliseconds.
     */
    public static final double SWITCHING_DELAY = 0.00285;

    /**
     * The downlink bandwidth of RootSwitch in Megabits/s.
     * It also represents the uplink bandwidth of connected aggregation Datacenter.
     */
    public static final long DOWNLINK_BW =  (long)Conversion.GIGABYTE * 10 * 8 ;// 40000 Megabits (40 Gigabits)


	public AdaptedRootSwitch(CloudSim simulation, NetworkDatacenter dc) {
		 super(simulation, dc);
	        setDownlinkBandwidth(DOWNLINK_BW);
	        setSwitchingDelay(SWITCHING_DELAY);
	        setPorts(PORTS);
	}
	
	@Override
    protected void processPacketUp(SimEvent ev) {
        super.processPacketUp(ev);
        
        final HostPacket netPkt = (HostPacket) ev.getData();

        final int srcID = this.getDatacenter().getId();
        final Vm receiverVm = netPkt.getVmPacket().getDestination();
        int destID = 0;
        if(receiverVm == null){
        	destID = netPkt.getVmPacket().getSource().getBroker().getId();        	
        }
        else{        	
        	destID = receiverVm.getHost().getDatacenter().getId();    
        }
        	
        double transferDelay = this.getSimulation().getNetworkTopology().getDelay(srcID, destID);
        
        if(receiverVm == null){
        	//dc to broker
        	AdaptedCloudlet cl = (AdaptedCloudlet) netPkt.getVmPacket().getReceiverCloudlet();
        	cl.setLeftDcToBrokerTime(this.getSimulation().clock());
        	cl.setGotToBrokerTime(cl.getLeftDcToBrokerTime() + transferDelay);
        	send(netPkt.getVmPacket().getSource().getBroker() ,transferDelay, CloudSimTags.CLOUDLET_RETURN, netPkt.getVmPacket().getReceiverCloudlet());
    		netPkt.getVmPacket().getSource().getCloudletScheduler().addCloudletToReturnedList(netPkt.getVmPacket().getReceiverCloudlet());
        }
        else {
        	// broker submit cloudlet to dc or cloudlet to cloudlet same dc
        	final Switch edgeSwitch = getVmEdgeSwitch(receiverVm);
        	final Switch aggSwitch = findAggregateSwitchConnectedToGivenEdgeSwitch(edgeSwitch);
        	if (destID == srcID ){	
	        	if (aggSwitch == Switch.NULL) {
	                logger.error("No destination switch for this packet");
	                return;
	            }
        	addPacketToBeSentToDownlinkSwitch(aggSwitch, netPkt);
        	}
	        else {
	        	// cloudlet to cloudlet diff dc
	        	send(((AdaptedDatacenter)netPkt.getDestination().getDatacenter()).getSwitchMap().get(0) ,transferDelay, CloudSimTags.NETWORK_EVENT_UP, netPkt);        	
	        }
        }
    }
	
	private Switch findAggregateSwitchConnectedToGivenEdgeSwitch(Switch edgeSwitch) {
        for (final Switch aggregateSw : getDownlinkSwitches()) {
            for (final Switch edgeSw : aggregateSw.getDownlinkSwitches()) {
                if (edgeSw.getId() == edgeSwitch.getId()) {
                    return aggregateSw;
                }
            }
        }

        return Switch.NULL;
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }

}
