package org.scenario;			

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;



public class App {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if(0 == 0){ //just making my life easier 
			try {
				System.setOut(new PrintStream(new FileOutputStream("Log")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}		
		}
        new RunReplicationScenario().run();
//		String ch = "file1.txt" + "8c20eb0a-4bd2-353a-849e-ed6e3390b5d7" + "01"; // file name + storage device UUID + broker id 
//		byte[] b = 	{(byte)1};
//		try {
//			b = ch.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		UUID uid = UUID.nameUUIDFromBytes(b);
//        System.out.println(uid.toString());
//        System.out.println(uid.getLeastSignificantBits());
//        System.out.println(uid.getMostSignificantBits());
//        System.out.println(uid.timestamp());
	}
}
