package com.infogain.eteventing.util;

import java.util.Random;

public class DataGeneratorUtility {

	/**
	 * Get Locator prefix from the range A to Z
	 * @return
	 */
	public static String getPNRPrefix() {
	    int leftLimit = Constant.ASCII_A; // letter 'A'
	    int rightLimit = Constant.ASCII_Z; // letter 'Z'
	    int targetStringLength = 1;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + Constant.ONE)
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
		if(totalRecords < Constant.TEN_THOUSAND) {
			return Constant.TEN;
		} else if(totalRecords > Constant.TEN_THOUSAND && totalRecords < Constant.ONE_LAKH){
			return Constant.TWENTY;
		} else {
			return Constant.THIRTY;
		}
	}

	/**
	 * Get Random Version as per given Max Limit
	 * @param maxLimit
	 * @return
	 */
	public static Integer getRandomVersion(Integer maxLimit) {
	    Random random = new Random();
	    return random.ints(Constant.ONE,maxLimit).findFirst().getAsInt();
	}
}

