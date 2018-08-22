package org.scenario.config;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.math3.random.RandomDataGenerator;

public class Utils {
	
	public static double generateRandomBounded(double leftLimit,double rightLimit) {
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	public static double givenUsingApache_whenGeneratingRandomDoubleBounded_thenCorrect(double leftLimit,double rightLimit) {
	    double generatedDouble = new RandomDataGenerator().nextUniform(leftLimit, rightLimit);
	    return generatedDouble;
	}
	public static int givenUsingApache_whenGeneratingRandomIntegerBounded_thenCorrect(int leftLimit,int rightLimit) {
	    int generatedInteger = new RandomDataGenerator().nextInt(leftLimit, rightLimit);
		return generatedInteger;
	}
	
	public static int generateRandomBounded(int leftLimit,int rightLimit) {
	    return leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
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
