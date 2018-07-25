package org.scenario;

import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.scenario.config.InitializeReplicationScenarioBasicTreeTopology;





public class RunReplicationScenario {
	
    
    public void run(){
        
        DatacenterBroker broker = new InitializeReplicationScenarioBasicTreeTopology().init(); 
        broker.getSimulation().start();
        List<Cloudlet> finished = broker.getCloudletFinishedList();
        new CloudletsTableBuilder(finished).build();
        
        
    }
    
}
