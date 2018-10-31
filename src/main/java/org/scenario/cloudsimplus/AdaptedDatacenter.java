package org.scenario.cloudsimplus;


import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletExecution;
import org.cloudbus.cloudsim.cloudlets.network.CloudletExecutionTask;
import org.cloudbus.cloudsim.cloudlets.network.NetworkCloudlet;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.network.NetworkHost;
import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.network.VmPacket;
import org.cloudbus.cloudsim.network.switches.Switch;
import org.cloudbus.cloudsim.resources.FileAttribute;
import org.cloudbus.cloudsim.util.Conversion;
import org.cloudbus.cloudsim.vms.Vm;
import org.scenario.autoadaptive.CloudDataTags;
import org.scenario.autoadaptive.LoadBalancer;
import org.scenario.autoadaptive.MetadataManager;
import org.scenario.cloudsimplus.network.NetworkLoadGraph;
import org.scenario.cloudsimplus.network.switches.AdaptedAbstractSwitch;
import org.scenario.cloudsimplus.resources.AdaptedFile;
import org.scenario.cloudsimplus.resources.FileMetadata;
import org.scenario.config.SimulationParameters;
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
	private NetworkLoadGraph loadGraph;

	// Used for many purposes
	private static int debugCount = 0;
	
	public AdaptedDatacenter(Simulation simulation,
							List<? extends Host> hostList, VmAllocationPolicy vmAllocationPolicy, LoadBalancer balancer) {
		super(simulation, hostList, vmAllocationPolicy);
		this.balancer = balancer;
		this.balancer.setDatacenter(this);
	}
	
	@Override
    public void processEvent(final SimEvent ev) {
		//TODO refactor load graph update and new file placement into new methodes
		if(this.getSwitchMap().size() > 0) {
			if (loadGraph == null) {
				this.loadGraph = new NetworkLoadGraph(this.getSwitchMap());
			}
			for(Switch sw : this.getSwitchMap()) {			
				((AdaptedAbstractSwitch)sw).setSkipCount(((AdaptedAbstractSwitch)sw).getSkipCount()+1);
				if( ((AdaptedAbstractSwitch)sw).getSkipCount() > SimulationParameters.LOAD_HISTORY_UPDATE_INTERVAL) {
					((AdaptedAbstractSwitch)sw).getHistoryList().add(((AdaptedAbstractSwitch)sw).getCumulatedCharge());
					((AdaptedAbstractSwitch)sw).setCumulatedCharge(0);
					((AdaptedAbstractSwitch)sw).setSkipCount(0);
				}
			}
			if(((AdaptedAbstractSwitch)this.getSwitchMap().get(0)).getSkipCount() == 0) {
				this.loadGraph.updateGraph(this.getSwitchMap());
			}
			
			if(this.getSimulation().clock() > SimulationParameters.DEPLOY_NEW_FILE && !MetadataManager.getCatalogInstance().hasEntry("newlyPlaced")) {
//			if(debugCount == 0) {
				this.loadGraph.bestRoute();
				Entry<String, Integer> minimum = null;
				try {					
					for (Entry<String, Integer> entry : this.loadGraph.getRoutesWeight().entrySet()) {
						if (minimum == null || minimum.getValue() > entry.getValue()) {
							minimum = entry;
						}
					}
					if(minimum == null)
						throw new NullPointerException();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				String switchName = minimum.getKey();
				this.getDatacenterStorage().getStorageList().get(((AdaptedAbstractSwitch) this.getSwitchMap().stream().filter(
						s -> s.getName().equals(switchName))
						.findFirst().get()).getIdAmongSameLevel())
						.addFile(new AdaptedFile("newlyPlaced",110));
				debugCount++;
			}
		}
        super.processEvent(ev);
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
        List<Host> nodesThatHasTheFile = ((AdaptedDatacenterStorage) getDatacenterStorage()).getMetadataManager().getNodesThatHasFile(((AdaptedCloudlet)cl).getRequestedFileId());
        if(((AdaptedCloudlet)cl).getRequestedFileId() == -1 |  nodesThatHasTheFile.isEmpty())
        	return;
        List<Vm> vmsThatHasAccessToFile = new ArrayList<>();
        for(Host host : nodesThatHasTheFile) {
        	vmsThatHasAccessToFile.addAll(host.getVmList());
        }
        Vm electedVm = Vm.NULL;
        if(SimulationParameters.SINGLE_WORKER == 1)
        electedVm = vmsThatHasAccessToFile.get(0);
        else
        electedVm = balancer.electVm(vmsThatHasAccessToFile);
    	((AdaptedVm) electedVm).getOrUpdateRequestCount(1);
        cl.setVm(electedVm); // its done after initializing also for test	
        	HostPacket pkt = new HostPacket(null, new VmPacket(null, electedVm, CloudDataTags.PKT_SIZE , null, cl));
        	for (Switch sw : this.getSwitchMap()){
        		if(sw.getLevel() == 0){
        			sendNow(sw, CloudSimTags.NETWORK_EVENT_UP, pkt);
        		}       		
        	}
	}
	
	
	public void submitCloudletToVm(final Cloudlet cl, final boolean ack) {
        // time to transfer cloudlet's files

		List<String> fileNames = new ArrayList<>(); 
		fileNames.add(((FileMetadata)((AdaptedDatacenterStorage) getDatacenterStorage()).getMetadataManager().getFileMetadataWithId(((AdaptedCloudlet) cl).getRequestedFileId(),null,false)).getName());
		
        final double fileTransferTime = getDatacenterStorage().predictFileTransferTime(fileNames);
        ((AdaptedCloudlet)cl).setFileRetrievalTime(fileTransferTime);
        ((CloudletExecutionTask)((NetworkCloudlet)cl).getTasks().get(0)).setLength((long) (fileTransferTime * cl.getVm().getMips()));
        double estimatedFinishTime = cl.getVm().getCloudletScheduler().cloudletSubmit(cl,0);
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
        int listSize = nonReturnedCloudlets.size();
        for(Cloudlet cl : nonReturnedCloudlets) {
        	returnFinishedCloudletToBroker(cl, listSize);
        }
    }
	
	private void returnFinishedCloudletToBroker(final Cloudlet cloudlet , int size) {	
		FileAttribute fileAttr = ((AdaptedDatacenterStorage) getDatacenterStorage()).getMetadataManager().getFileMetadataWithId(((AdaptedCloudlet) cloudlet).getRequestedFileId(),((AdaptedHost)cloudlet.getVm().getHost()).getStorage(),true);
		long fileSize = fileAttr.getFileSize();
		double bwAvailableForThisPacket =(((NetworkHost) cloudlet.getVm().getHost()).getEdgeSwitch().getDownlinkBandwidth()) / size ;
		HostPacket pkt = null;
		Switch sw = ((AdaptedHost)cloudlet.getVm().getHost()).getEdgeSwitch();
		((AdaptedCloudlet) cloudlet).setLeftVmToBrokerTime(this.getSimulation().clock());
		pkt = new HostPacket((AdaptedHost)cloudlet.getVm().getHost(), new VmPacket(cloudlet.getVm(), null, (long) (fileSize * Conversion.MEGABYTE), null, cloudlet));	
//		pkt = new HostPacket((AdaptedHost)cloudlet.getVm().getHost(), new VmPacket(cloudlet.getVm(), null, DataCloudTags.PKT_SIZE, null, cloudlet));
		double delay = Conversion.bytesToMegaBits( pkt.getSize()) / bwAvailableForThisPacket;
		((AdaptedVm) cloudlet.getVm()).getOrUpdateRequestCount(-1);
		// TODO share bw across concurrent cloudlets 
		getSimulation().send(
                this, sw, delay ,CloudSimTags.NETWORK_EVENT_UP, pkt);
		cloudlet.getVm().getCloudletScheduler().addCloudletToReturnedList(cloudlet);
    }
	
	public LoadBalancer getBalancer() {
		return balancer;
	}
	 
	
//	private void startSending(Cloudlet cloudlet) {
//
//		FileAttribute fileAttr = ((AdaptedDatacenterStorage) getDatacenterStorage()).getMetadataManager().getFileMetadataWithId(((AdaptedCloudlet) cloudlet).getRequestedFileId(),((AdaptedHost)cloudlet.getVm().getHost()).getStorage(),true);
//		long fileSize = fileAttr.getFileSize();
//		double bwAvailableForThisPacket =(((NetworkHost) cloudlet.getVm().getHost()).getEdgeSwitch().getDownlinkBandwidth()) / cloudlet.getVm().getCloudletScheduler().getCloudletExecList()
//				.stream()
//				.filter(c -> c.getCloudlet().getStatus() == Status.INEXEC)
//				.collect(Collectors.toList())
//				.size();
//		Switch sw = ((AdaptedHost)cloudlet.getVm().getHost()).getEdgeSwitch();
//		double delay = Conversion.bytesToMegaBits( fileSize * Conversion.MEGABYTE / CloudDataTags.PKT_SIZE ) / bwAvailableForThisPacket;
//		int noOfPackets =  (int) (fileSize * Conversion.MEGABYTE / CloudDataTags.PKT_SIZE);
//
//		for(int i = 0 ; i <   noOfPackets ; i++) {
//
//			HostPacket pkt = new HostPacket((AdaptedHost)cloudlet.getVm().getHost(), new VmPacket(cloudlet.getVm(), null, CloudDataTags.PKT_SIZE , null, null));
//			((CloudSimEntity)cloudlet.getLastDatacenter()).schedule( sw, delay + ((AdaptedCloudlet) cloudlet).getFileRetrievalTime() * i / noOfPackets  ,CloudSimTags.NETWORK_EVENT_UP, pkt);
//		}
//	}
}
