package org.scenario;

import java.util.Collections;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.scenario.cloudsimplus.AdaptedCloudlet;
import org.scenario.cloudsimplus.DetailedCloudletsTableBuilder;
import org.scenario.config.InitializeReplicationScenarioBasicTreeTopology;
import org.scenario.config.Utils;





public class RunReplicationScenario {
	
    
    public void run(){
        
        List<DatacenterBroker> brokers = new InitializeReplicationScenarioBasicTreeTopology().init(); 
        Utils.writeInAGivenFile("Log",  "" , false);
        brokers.get(0).getSimulation().start();
////        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // make out go back to default
////        try {
////			System.setOut(new PrintStream(new FileOutputStream("1000req_1MB_Fair")));
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
        List<Cloudlet> finished = brokers.get(0).getCloudletFinishedList();
//        Collections.sort(finished,(c1,c2) -> {
//        	if (c1.getId() > c2.getId()) 
//        		return	 1 ;
//        	else
//        		return -1;
//        	});
        
//        
        DetailedCloudletsTableBuilder results = new DetailedCloudletsTableBuilder(finished);
        		results.build();
        
        double overallAvg = 0;
        double variance = 0;
        for(Cloudlet cl : finished)
        	overallAvg += ((AdaptedCloudlet) cl).getOverallTime();
        overallAvg /= finished.size();
        System.out.println(overallAvg);
        
        for(Cloudlet cl : finished)
        	variance += Math.pow((((AdaptedCloudlet) cl).getOverallTime() - overallAvg), 2);
        variance /= finished.size();
        System.out.println(variance);
    }
    
}
