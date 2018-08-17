package org.scenario.autoadaptive;

import java.util.HashMap;
import java.util.LinkedList;

import org.cloudbus.cloudsim.resources.FileAttribute;


public class ReplicaCatalog extends HashMap<Integer,LinkedList<FileAttribute>> implements MetadataCatalog {

	/**
	 * A singleton implementation to insure there is only one instance of the catalog
	 */
	
	private static final long serialVersionUID = 1L;
	
	private static final MetadataCatalog instance = new ReplicaCatalog();
	
	public static MetadataCatalog getCatalogInstance(){
		return instance;
	}

	private ReplicaCatalog(){
		super();
		//TODO complete if needed
	}
	
	public boolean hasEntry(int registrationId) {
		return this.containsKey(registrationId);
	}
	
	public void addNewEntry(FileAttribute attr) {
		LinkedList<FileAttribute> holder = new LinkedList<>();
		holder.add(attr);
		this.put(attr.getRegistrationID(), holder);
	}

	public void addReplica(FileAttribute attr) {
		this.get(attr.getRegistrationID()).add(attr);
	}

}
