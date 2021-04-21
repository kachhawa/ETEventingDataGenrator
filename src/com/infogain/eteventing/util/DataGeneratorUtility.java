package com.infogain.eteventing.util;

import java.util.Random;

public class DataGeneratorUtility {

	/**
	 * Get Locator prefix from the range A to Z
	 * @return
	 */
	public static String getPNRPrefix() {
	    int leftLimit = 65; // letter 'A'
	    int rightLimit = 90; // letter 'Z'
	    int targetStringLength = 1;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	
	}

	/**
	 * Based on Total Records, it will fetch version
	 * @param totalRecords
	 * @return
	 */
	public static Integer getVersionLimit(Integer totalRecords) {
		if(totalRecords < 10000) {
			return 10;
		} else if(totalRecords > 10000 && totalRecords < 100000){
			return 20;
		} else {
			return 30;
		}
	}

	/**
	 * Get Random Version as per given Max Limit
	 * @param maxLimit
	 * @return
	 */
	public static Integer getRandomVersion(Integer maxLimit) {
	    Random random = new Random();
	    return random.ints(1,maxLimit).findFirst().getAsInt();
	}



}

