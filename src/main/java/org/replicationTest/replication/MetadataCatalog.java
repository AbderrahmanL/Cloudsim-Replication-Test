package org.replicationTest.replication;

import java.util.List;

/**
 * A replica catalog is holding a list of all replicas
 * and their locations.
 */

public interface IReplicaCatalog {
	
	/**
	 * Gets the list of replicas of a given file/object
	 * 
	 * @param replica a given file/object
	 * @return list of replicas
	 */
	List<IReplica> getAllReplicas(IReplica replica);

}
