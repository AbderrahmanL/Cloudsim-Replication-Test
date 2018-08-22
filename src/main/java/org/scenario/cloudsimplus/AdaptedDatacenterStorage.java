package org.scenario.cloudsimplus;

import java.util.List;
import java.util.Objects;

import org.cloudbus.cloudsim.resources.DatacenterStorage;
import org.cloudbus.cloudsim.resources.File;
import org.cloudbus.cloudsim.resources.FileStorage;
import org.cloudbus.cloudsim.util.DataCloudTags;
import org.scenario.autoadaptive.ReplicaManager;

public class AdaptedDatacenterStorage extends DatacenterStorage {
	
	public AdaptedDatacenterStorage(final List<FileStorage> storageList){
    super(storageList);
    }
	
	@Override
	public int addFile(final File file) {
		 Objects.requireNonNull(file);

	        if (contains(file.getName())) {
	        	return DataCloudTags.FILE_ADD_ERROR_EXIST_READ_ONLY;
	        }

	        // check storage space first
	        if (getStorageList().isEmpty()) {
	        	return DataCloudTags.FILE_ADD_ERROR_STORAGE_FULL;
	        }

	        for (final FileStorage storage : getStorageList()) {
	            if (storage.isAmountAvailable((long) file.getSize())) {
	                storage.addFile(file);
	                ((FileMetadata)file.getAttribute()).setContainingDevice(storage);
	                ReplicaManager.onFileCreate(file.getAttribute());
	                return DataCloudTags.FILE_ADD_SUCCESSFUL;
	            }
	        }
	        return DataCloudTags.FILE_ADD_ERROR_STORAGE_FULL;
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
