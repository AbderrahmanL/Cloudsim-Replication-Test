package org.scenario.autoadaptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.resources.FileAttribute;
import org.scenario.cloudsimplus.resources.FileMetadata;
import org.scenario.cloudsimplus.resources.MountedSan;


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
	
	public FileAttribute getFileMetadataWithId(int id) {
		return this.get(id).get(0);
	}
	
	@Override
	public List<Host> getNodesThatHasFile(int requestedFileId) {
		List<Host> listToReturn = new ArrayList<Host>();
		if(!this.containsKey(requestedFileId))
			return listToReturn;
		for (FileAttribute fileMetaData : this.get(requestedFileId)) {
			listToReturn.addAll(((MountedSan)((FileMetadata)fileMetaData).getContainingDevice()).getAccessingHosts());
		}
		return listToReturn;
	}

}
