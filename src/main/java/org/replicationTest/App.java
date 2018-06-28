package org.replicationTest;

import org.cloudbus.cloudsim.util.Log;

public class App {

	public static void main(String[] args) {
		Log.printLine("Starting replication scenario ...");
        new ReplicationScenario().run();
        Log.printLine("Replication scenario finished!");
//        File f = new File("file", 50);
//        System.out.println(f.getRegistrationID());
        
	}
}
