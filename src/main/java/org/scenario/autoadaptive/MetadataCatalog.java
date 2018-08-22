package org.scenario.autoadaptive;

import java.util.List;

import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.resources.FileAttribute;

/**
 * A replica catalog is holding a list of all replicas
 * and their locations.
 */

public interface MetadataCatalog {


	public void addNewEntry(FileAttribute attr) ;
	
	public boolean hasEntry(int registrationId) ;
	
	public void addReplica(FileAttribute attr);

	public List<Host> getNodesThatHasFile(int requestedFileId);

}
