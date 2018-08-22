package org.scenario.cloudsimplus;


import static java.util.stream.Collectors.toList;

import java.util.List;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletExecution;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.VmPacket;
import org.cloudbus.cloudsim.network.switches.EdgeSwitch;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.util.Conversion;
import org.cloudbus.cloudsim.util.DataCloudTags;
import org.cloudbus.cloudsim.vms.Vm;
import org.scenario.autoadaptive.CloudDataTags;
import org.scenario.autoadaptive.LoadBalancer;
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
	
	private LoadBalancer balancer;

	// Used for many purposes
	private static int debugCount = 0;
	
	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList, VmAllocationPolicy vmAllocationPolicy, LoadBalancer balancer) {
		super(simulation, hostList, vmAllocationPolicy);
		this.balancer = balancer;
		this.balancer.setDatacenter(this);
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
        ((AdaptedCloudlet)cl).setDcReceiveTime(this.getSimulation().clock());
        cl.assignToDatacenter(this);
        // TODO assign to vm, next line is a dummy assignement for test
        Vm vm = this.getVmList().get(0);
        if(debugCount % 2 == 0)
        	vm = this.getVmList().get(0);
        else
        	vm = this.getVmList().get(15);
        debugCount++;
        
    	((AdaptedVm) vm).getOrUpdateRequestCount(1);
        cl.setVm(vm); // its done after initializing also for test	
        	HostPacket pkt = new HostPacket(null, new VmPacket(null, vm, CloudDataTags.DEFAULT_MTU + cl.getFileSize(), null, cl));
        	for (Switch sw : this.getSwitchMap()){
        		if(sw.getLevel() == 0){
        			sendNow(sw, CloudSimTags.NETWORK_EVENT_UP, pkt);
        		}       		
        	}
	}
	
	public void submitCloudletToVm(final Cloudlet cl, final boolean ack) {
        // time to transfer cloudlet's files
        final double fileTransferTime = getDatacenterStorage().predictFileTransferTime(cl.getRequiredFiles());
        
        ((AdaptedCloudlet) cl).setFileRetrievalTime(this.getSimulation().clock() + fileTransferTime);        
        
        final CloudletScheduler scheduler = cl.getVm().getCloudletScheduler();
        final double estimatedFinishTime = scheduler.cloudletSubmit(cl, fileTransferTime);
        // if this cloudlet is in the exec queue
        if (estimatedFinishTime > 0.0 && !Double.isInfinite(estimatedFinishTime)) {
            send(this,
                getCloudletProcessingUpdateInterval(estimatedFinishTime) ,
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
	
	@Override
	protected void checkCloudletsCompletionForAllHosts() {
		final List<? extends Host> hosts = this.getVmAllocationPolicy().getHostList();
        hosts.forEach(this::checkCloudletsCompletionForGivenHost);
	}
	
	private void checkCloudletsCompletionForGivenHost(final Host host) {
        host.getVmList().forEach(this::checkCloudletsCompletionForGivenVm);
    }
	
	private void checkCloudletsCompletionForGivenVm(final Vm vm) {
        final List<Cloudlet> nonReturnedCloudlets =
            vm.getCloudletScheduler().getCloudletFinishedList().stream()
                .map(CloudletExecution::getCloudlet)
                .filter(c -> !vm.getCloudletScheduler().isCloudletReturned(c))
                .collect(toList());
        
        nonReturnedCloudlets.forEach(this::returnFinishedCloudletToBroker);
    }
	
	private void returnFinishedCloudletToBroker(final Cloudlet cloudlet) {
		long fileSize = 0;
		try{
			fileSize = ((AdaptedDatacenterStorage)this.getDatacenterStorage()).getFile(cloudlet.getRequiredFiles().get(0)).getSize();			
		}
		catch (IndexOutOfBoundsException e){
			System.out.print("");
		}
		((AdaptedCloudlet) cloudlet).setLeftVmToBrokerTime(this.getSimulation().clock());
		((AdaptedVm) cloudlet.getVm()).getOrUpdateRequestCount(-1);
		System.out.println( (long) (fileSize * Conversion.MEGABYTE));
		HostPacket pkt = new HostPacket((AdaptedHost)cloudlet.getVm().getHost(), new VmPacket(cloudlet.getVm(), null, DataCloudTags.DEFAULT_MTU + cloudlet.getFileSize() + (long) (fileSize * Conversion.MEGABYTE) , null, cloudlet));
		EdgeSwitch sw = ((AdaptedHost)cloudlet.getVm().getHost()).getEdgeSwitch();
		// TODO share bw across concurrent cloudlets 
		double delay = pkt.getSize()/(((NetworkHost) cloudlet.getVm().getHost()).getEdgeSwitch().getDownlinkBandwidth());
		getSimulation().send(
                this, sw, 0 ,CloudSimTags.NETWORK_EVENT_UP, pkt);
		cloudlet.getVm().getCloudletScheduler().addCloudletToReturnedList(cloudlet);
		// these tow lines are invoked in root switch
//        sendNow(cloudlet.getBroker(), CloudSimTags.CLOUDLET_RETURN, cloudlet);
    }
	
	public LoadBalancer getBalancer() {
		return balancer;
	}
}
