package org.scenario.cloudsimplus;

import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.util.DataCloudTags;
import org.scenario.replication.ReplicaManager;

public class AdaptedDatacenterStorage extends DatacenterStorage {
	
	@Override
	public int addFile(final File file) {
		int result = super.addFile(file);
		if (result == DataCloudTags.FILE_ADD_SUCCESSFUL) {
			ReplicaManager.onFileCreate(file.getAttribute());
		}
		return result;
	}
	
	public File getFile(String name){
		File file = new File("a", 1);
		for(FileStorage storage : this.getStorageList()){
			if(storage.getFileNameList().contains(name)){
				file = storage.getFile(name);
			}
		}
		return file;
	}

}
