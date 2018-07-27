package org.scenario.cloudsimplus;

import java.util.List;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.schedulers.cloudlet.network.CloudletTaskScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.vms.Vm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptedHost extends NetworkHost{
	private static final Logger logger = LoggerFactory.getLogger(AdaptedHost.class.getSimpleName());

	public AdaptedHost(long ram, long bw, long storage, List<Pe> peList) {
		super(ram, bw, storage, peList);
		// TODO Auto-generated constructor stub
	}
	
	public AdaptedHost(long ram, long bw, long storage, List<Pe> peList,
			VmScheduler vmScheduler) {
		super(ram, bw, storage, peList, vmScheduler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void receivePackets() {
		for (final HostPacket hostPkt : getHostPktsReceived()) {
            hostPkt.getVmPacket().setReceiveTime(getSimulation().clock());

            final Vm destinationVm = hostPkt.getVmPacket().getDestination();
            //Checks if the destinationVm is inside this host
            if(!getVmList().contains(destinationVm)){
                logger.error(
                    "{}: {}: Destination {} was not found inside {}",
                    getSimulation().clock(), getClass(),
                    hostPkt.getVmPacket().getDestination(), this);
                return;
            }

            if(hostPkt.getSource() != null){            	
            	final CloudletTaskScheduler taskScheduler = getVmPacketScheduler(destinationVm);
            	taskScheduler.addPacketToListOfPacketsSentFromVm(hostPkt.getVmPacket());
            	logger.trace(
            			"{}: {}: {} received pkt with {} bytes from {} in {} and forwarded it to {} in {}",
            			getSimulation().clock(), getClass().getSimpleName(),
            			this, hostPkt.getVmPacket().getSize(),
            			hostPkt.getVmPacket().getSenderCloudlet(),
            			hostPkt.getVmPacket().getSource(),
            			hostPkt.getVmPacket().getReceiverCloudlet(),
            			hostPkt.getVmPacket().getDestination());
            }
            else{
            	Cloudlet cl = hostPkt.getVmPacket().getReceiverCloudlet();
            	((AdaptedCloudlet)cl).setVmReceiveTime(this.getSimulation().clock());
            	((AdaptedDatacenter)this.getDatacenter()).submitCloudletToVm(cl, true);
            	
            }
            	
        }

		getHostPktsReceived().clear();
	}

}