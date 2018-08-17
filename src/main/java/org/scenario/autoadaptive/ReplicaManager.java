package org.scenario.autoadaptive;

import org.cloudbus.cloudsim.resources.FileAttribute;


public class ReplicaManager {


	/**
	 * A newly created file will be added as a new entry 
	 * in the catalog, and if it's a replica it will be 
	 * in the corresponding existing entry's linked list
	 * 
	 * @param attr file attributes
	 */
	public static void onFileCreate(FileAttribute attr) {
	
		MetadataCatalog catalog = ReplicaCatalog.getCatalogInstance();
		if(! catalog.hasEntry(attr.getRegistrationID())){
			catalog.addNewEntry(attr);		
		}
		else {
			catalog.addReplica(attr);
		}
	}


	

}
