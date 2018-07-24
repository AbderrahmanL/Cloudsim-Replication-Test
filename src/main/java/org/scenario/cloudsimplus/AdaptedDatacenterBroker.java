package org.scenario.cloudsimplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptedDatacenterBroker extends DatacenterBrokerSimple{
	private static final Logger logger = LoggerFactory.getLogger(AdaptedDatacenterBroker.class.getSimpleName());
	
	private final Map<Cloudlet, Datacenter> cloudletCreationRequestsMap;


	public AdaptedDatacenterBroker(CloudSim simulation) {
		super(simulation);
        cloudletCreationRequestsMap = new HashMap<>();
        // TODO set DatacenterSupplier FallbackDatacenterSupplier will be set with method references from broker or preferably another entity 
        // TODO vmMapper will be set with a method reference from dc or any entity in dc
	}
	
	@Override
	protected void requestDatacentersToCreateWaitingCloudlets() {
		final List<Cloudlet> successfullySubmitted = new ArrayList<>();
		
		for (final Cloudlet cloudlet : getCloudletWaitingList()) {
			
			if (cloudletCreationRequestsMap.containsKey(cloudlet)) {
                continue;
            }
	          
			final String delayStr =
	                cloudlet.getSubmissionDelay() > 0 ?
	                    String.format(" with a requested delay of %.0f seconds", cloudlet.getSubmissionDelay()) :
	                    "";
            Datacenter electedDc = Datacenter.NULL;
            // TODO Here the dc was selected using the vm but vm wont be chosen until dc
            logger.info(
                    "{}: {}: Sending {} to {} in {}.",
                    getSimulation().clock(), getName(), cloudlet,
                     electedDc.getName(),delayStr);
            send(electedDc,
                    cloudlet.getSubmissionDelay() , CloudSimTags.CLOUDLET_SUBMIT, cloudlet);    
            cloudletCreationRequestsMap.put(cloudlet, electedDc);
            successfullySubmitted.add(cloudlet);
        }
		getCloudletWaitingList().removeAll(successfullySubmitted);
	}
	
	
}
