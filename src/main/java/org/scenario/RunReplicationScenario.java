package org.scenario;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.scenario.cloudsimplus.AdaptedCloudlet;
import org.scenario.cloudsimplus.DetailedCloudletsTableBuilder;
import org.scenario.config.InitializeReplicationScenarioBasicTreeTopology;





public class RunReplicationScenario {
	
    
    public void run(){
        
        DatacenterBroker broker = new InitializeReplicationScenarioBasicTreeTopology().init(); 
        broker.getSimulation().start();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        List<Cloudlet> finished = broker.getCloudletFinishedList();
        AdaptedCloudlet cl = ((AdaptedCloudlet)finished.get(1));
        
        new DetailedCloudletsTableBuilder(finished).build();
        System.out.println( " send time " +cl.getSendTime() + " | dc receive time " + cl.getDcReceiveTime()+"/"+cl.getArrivalTime(cl.getLastDatacenter()) + " | vm receive time " + cl.getVmReceiveTime() 
        		+ " | Exec start time "+ cl.getExecStartTime() + " | File retrieval time " + cl.getFileRetrievalTime() 
        		+ " | Leaves vm to broker time " + cl.getLeftVmToBrokerTime() + " | Leaves dc to broker time " + cl.getLeftDcToBrokerTime() +
        		" | got to broker time " + cl.getGotToBrokerTime());
        
        
    }
    
}
