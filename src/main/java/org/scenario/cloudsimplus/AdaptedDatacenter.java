package org.scenario.cloudsimplus;


import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapted dc with possibility to inject strategies to choose execution node
 * in the superclass it was done in the broker, we believe its the dc's job
 * 
 * @author Abderrahman Lahiaouni
 *
 */
public class AdaptedDatacenter extends NetworkDatacenter{
	private static final Logger logger = LoggerFactory.getLogger(DatacenterSimple.class.getSimpleName());

	
	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList, VmAllocationPolicy vmAllocationPolicy) {
		super(simulation, hostList, vmAllocationPolicy);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void processCloudletSubmit(final SimEvent ev, final boolean ack) {
		final Cloudlet cl = (Cloudlet) ev.getData();
        if (cl.isFinished()) {
        	logger.warn(
                    "{}: {} owned by {} is already completed/finished. It won't be executed again.",
                    getName(), cl, cl.getBroker());

            sendAcknowledgement(ack, cl);
            
        	sendCloudletReturn(cl);
            return;
        }
        cl.assignToDatacenter(this);
        // TODO assign to vm, next line is a dummy assignement for test
        cl.setVm(this.getVmList().get(0)); // TODO replace this
        submitCloudletToVm(cl,ack);
        
	}
	
	private void submitCloudletToVm(final Cloudlet cl, final boolean ack) {
        // time to transfer cloudlet's files
        final double fileTransferTime = getDatacenterStorage().predictFileTransferTime(cl.getRequiredFiles());

        final CloudletScheduler scheduler = cl.getVm().getCloudletScheduler();
        final double estimatedFinishTime = scheduler.cloudletSubmit(cl, fileTransferTime);

        // if this cloudlet is in the exec queue
        if (estimatedFinishTime > 0.0 && !Double.isInfinite(estimatedFinishTime)) {
            send(this,
                getCloudletProcessingUpdateInterval(estimatedFinishTime),
                CloudSimTags.VM_UPDATE_CLOUDLET_PROCESSING_EVENT);
        }

        sendAcknowledgement(ack, cl);
    }

	private void sendCloudletReturn(Cloudlet cl) {
		sendNow(cl.getBroker(), CloudSimTags.CLOUDLET_RETURN, cl);
		
	}

	private void sendAcknowledgement(boolean ack, Cloudlet cl) {
		if(!ack){
            return;
        }
		sendNow(cl.getBroker(), CloudSimTags.CLOUDLET_SUBMIT_ACK, cl);	
	}

}
