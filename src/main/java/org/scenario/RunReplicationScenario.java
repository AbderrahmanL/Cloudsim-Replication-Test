package org.scenario;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.scenario.cloudsimplus.DetailedCloudletsTableBuilder;
import org.scenario.config.InitializeReplicationScenarioBasicTreeTopology;





public class RunReplicationScenario {
	
    
    public void run(){
        
        DatacenterBroker broker = new InitializeReplicationScenarioBasicTreeTopology().init(); 
        broker.getSimulation().start();
//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // make out go back to default
//        try {
//			System.setOut(new PrintStream(new FileOutputStream("500req_2MB_SpaceShared")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        List<Cloudlet> finished = broker.getCloudletFinishedList();
        
        new DetailedCloudletsTableBuilder(finished).build();
        
        
    }
    
}
