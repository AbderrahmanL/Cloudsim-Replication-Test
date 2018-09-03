package org.scenario.autoadaptive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.resources.FileAttribute;
import org.scenario.cloudsimplus.resources.FileMetadata;
import org.scenario.cloudsimplus.resources.MountedSan;


public class MetadataManager {

	private static final MetadataInterface instance = MetadataCatalog.getCatalogInstance();	

	public static MetadataInterface getCatalogInstance(){
		return instance;
	}
	
	/**
	 * A newly created file will be added as a new entry 
	 * in the catalog, and if it's a replica it will be 
	 * in the corresponding existing entry's linked list
	 * 
	 * @param attr file attributes
	 */
	public static void onFileCreate(FileAttribute attr) {
	
		MetadataInterface catalog = instance;
		if(! catalog.hasEntry(attr)){
			catalog.registerNewFile(attr);		
		}
		else {
			catalog.registerReplica(attr);
		}
	}

	@SuppressWarnings("unchecked")
	public FileAttribute getFileMetadataWithId(int id) {
		return ((HashMap<Integer, LinkedList<FileAttribute>>) instance).get(id).get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Host> getNodesThatHasFile(int requestedFileId) {
		List<Host> listToReturn = new ArrayList<Host>();
		if(!((HashMap<Integer, LinkedList<FileAttribute>>) instance).containsKey(requestedFileId))
			return listToReturn;
		for (FileAttribute fileMetaData : ((HashMap<Integer, LinkedList<FileAttribute>>) instance).get(requestedFileId)) {
			listToReturn.addAll(((MountedSan)((FileMetadata)fileMetaData).getContainingDevice()).getAccessingHosts());
		}
		return listToReturn;
	}
	

}
