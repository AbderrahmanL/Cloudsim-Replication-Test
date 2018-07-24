package org.scenario.cloudsimplus;

import java.util.List;

import org.cloudbus.cloudsim.resources.DatacenterStorage;

public class ReplicationStorage extends DatacenterStorage{
	
	@Override
	public double predictFileTransferTime(final List<String> requiredFiles) {
        double totalTime = super.predictFileTransferTime(requiredFiles);

        for (final String fileName: requiredFiles) {
        	// TODO redirect to replica catalog
        	// to look for entries corresponding to this file
        }

        return totalTime;
    }
	

}
