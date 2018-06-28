package org.replicationTest;

import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;





public class ReplicationScenario {
	
    
    public void run(){
        
        DatacenterBroker broker = new InitReplicationScenario().init(); 
        broker.getSimulation().start();
        List<Cloudlet> finished = broker.getCloudletFinishedList();
        new CloudletsTableBuilder(finished).build();
        
        
    }
    
}
