package org.scenario.replication;

import java.util.Map;

import org.cloudbus.cloudsim.resources.FileAttribute;


public class ReplicaManager{


	public static boolean addEntryToCatalog(FileAttribute attr) {
		((Map)ReplicaCatalog.getReplicaInstance()).put(attr.getRegistrationID(), attr);
		return false;
	}

}
