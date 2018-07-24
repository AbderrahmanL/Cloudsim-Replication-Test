package org.scenario.replication;

import java.util.List;

import org.scenario.cloudsimplus.AdaptedFile;

/**
 * A replica catalog is holding a list of all replicas
 * and their locations.
 */

public interface MetadataCatalog {
	
	/**
	 * Gets the list of replicas of a given file/object
	 * 
	 * @param replica a given file/object
	 * @return list of replicas
	 */
	List<AdaptedFile> getAllReplicas(AdaptedFile file);

}
