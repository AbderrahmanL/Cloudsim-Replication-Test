package org.scenario.replication;

import java.util.HashMap;
import java.util.UUID;

import org.cloudbus.cloudsim.resources.FileAttribute;
import org.scenario.cloudsimplus.FileMetadata;


public class ReplicaCatalog extends HashMap<UUID,FileMetadata> implements MetadataCatalog {

	/**
	 * A singleton implementation to insure there is only one instance of the catalog
	 */
	
	private static final long serialVersionUID = 1L;
	
	private static final ReplicaCatalog instance = new ReplicaCatalog();
	
	public static ReplicaCatalog getReplicaInstance(){
		return instance;
	}

	private ReplicaCatalog(){
		super();
		//TODO complete if needed
	}

	

}
