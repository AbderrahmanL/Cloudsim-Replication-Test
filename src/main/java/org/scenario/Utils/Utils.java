package org.scenario.Utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.scenario.config.SimulationParameters;

public class Utils {
	
	public static UniformRealDistribution getuniformRealDist( ) {
		return new UniformRealDistribution();
	}
	
	public static UniformRealDistribution getuniformRealDist(double left, double right ) {
		return new UniformRealDistribution(left, right);
	}
	
	public static UniformIntegerDistribution getuniformIntegerDist(int left, int right ) {
		return new UniformIntegerDistribution(left, right);
	}
	
	
	public static void writeInAGivenFile(String path , String text, boolean append) {
    try {
		System.setOut(new PrintStream(new FileOutputStream(path,append)));
		System.out.print(text);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	public static UUID getUUIDFromString(String string) {
		
		byte[] b = 	{(byte)1};
		try {
			b = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return UUID.nameUUIDFromBytes(b);
	}
}
