package org.scenario.cloudsimplus.network.switches;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
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
		
		public int numberOfPacketsBeingProcessed = 0;
		public int cumulatedCharge = 0;
		/**
		 * The count between traffic load logs 
		 * (a log is adding a value to history list)
		 * i.e. how many clock ticks passed since last log
		 */
		public int skipCount =0;
		
		public List<Integer> historyList = new ArrayList<>();
		
		  /**
	     * Id among switches of same level
	     */
	    private int idAmongSameLevel;
	    
		public AdaptedAbstractSwitch(CloudSim simulation, NetworkDatacenter dc) {
			super(simulation, dc);
			historyList.add(0);
		}
		
		 /**
		 * Computes the network delay to send a packet through the network.
		 *
		 * @param netPkt     the packet to be sent
		 * @param bwCapacity the total bandwidth capacity (in Megabits/s)
		 * @param netPktList the list of packets waiting to be sent
		 * @param upOrDownOrHosts 0 1 or 2 if packets are being forwarded down up or to hosts
		 * @return the expected time to transfer the packet through the network (in seconds)
		 */
		    protected double networkDelayForPacketTransmission(final HostPacket netPkt, final double bwCapacity, final List<HostPacket> netPktList, int upOrDownOrHosts) {
		    	int temp = numberOfPacketsBeingProcessed;
		    	// TODO Test this number Packets of more
		    	//System.out.println(numberOfPacketsBeingProcessed);
		    	numberOfPacketsBeingProcessed--;
		        return  temp * 0.00001  + Conversion.bytesToMegaBits(netPkt.getVmPacket().getSize()) / getAvailableBwForEachPacket(bwCapacity, netPktList ,upOrDownOrHosts);
		    }
		    
		    private double getAvailableBwForEachPacket(final double bwCapacity, final List<HostPacket> netPktList , int upOrDownOrHosts) {
		        
		    	int packetsBeingSentFromTheOppositSide = 0;
		    	if(upOrDownOrHosts == 0) {
					for(Switch sw : getDownlinkSwitches()) {
						packetsBeingSentFromTheOppositSide += sw.getUplinkSwitchPacketList(this).size();
						
					}
		    	}
		    	else if (upOrDownOrHosts == 1) {
					for(Switch sw : getUplinkSwitches()) {
						packetsBeingSentFromTheOppositSide += sw.getUplinkSwitchPacketList(this).size();
					}
		    	}
		    	else {
		    		packetsBeingSentFromTheOppositSide += this.getSimulation().getNumberOfFutureEvents(ev -> ev.getTag() ==CloudSimTags.NETWORK_EVENT_UP && ev.getDestination() == this);
							
		    	}
				return (netPktList.isEmpty() ? bwCapacity : bwCapacity / (netPktList.size() + packetsBeingSentFromTheOppositSide));
		    }
		
		@Override
		public void addPacketToBeSentToDownlinkSwitch(final Switch downlinkSwitch, final HostPacket packet) {
			numberOfPacketsBeingProcessed++;
			cumulatedCharge++;
		    getDownlinkSwitchPacketList(downlinkSwitch).add(packet);
		}
		
		@Override
		public void addPacketToBeSentToUplinkSwitch(final Switch uplinkSwitch, final HostPacket packet) {
			numberOfPacketsBeingProcessed++;
			cumulatedCharge++;
		    getUplinkSwitchPacketList(uplinkSwitch).add(packet);
		}
		
		@Override
		public void addPacketToBeSentToHost(final NetworkHost host, final HostPacket packet) {
			numberOfPacketsBeingProcessed++;
			cumulatedCharge++;
		    getHostPacketList(host).add(packet);
		}

		public int getIdAmongSameLevel() {
			return idAmongSameLevel;
		}

		public void setIdAmongSameLevel(int idAmongSameLevel) {
			this.idAmongSameLevel = idAmongSameLevel;
		}
		
		
}
