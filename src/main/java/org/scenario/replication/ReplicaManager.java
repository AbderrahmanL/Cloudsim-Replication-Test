package org.scenario.replication;

import org.cloudbus.cloudsim.core.CloudSimEntity;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.resources.FileAttribute;


public class ReplicaManager extends CloudSimEntity{


	public ReplicaManager(Simulation simulation) {
		super(simulation);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public void processEvent(SimEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void startEntity() {
		// TODO Auto-generated method stub
		
	}
	
	

}
