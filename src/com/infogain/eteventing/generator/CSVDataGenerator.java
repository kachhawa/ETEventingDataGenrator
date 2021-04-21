package com.infogain.eteventing.generator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.infogain.eteventing.dto.CSVDataDTO;
import com.infogain.eteventing.util.Constant;
import com.infogain.eteventing.util.DataGeneratorUtility;

public class CSVDataGenerator {

	public static void main(String[] args) throws IOException {
		System.out.println("============== ET EVENTING DATA GENERATOR ==============");
		Scanner inputData = null;
		try {
			/** 1. Get Input from user */
		    inputData = new Scanner(System.in);
		    System.out.println("Enter total number of records to be generated : ");
		    while (!inputData.hasNextInt()) {
		        System.out.println("Please enter numeric value : ");
		        inputData.next();
		    }
		    Integer inputRecords = inputData.nextInt();  

		    // Get version limit as per given input records
			Integer versionLimit = DataGeneratorUtility.getVersionLimit(inputRecords);
			
			String fileName = getFileName(inputRecords);
			
			// Create CSV File set header data
			FileWriter csvWriter = new FileWriter(fileName);
			csvWriter.append(Constant.CSV_HEADER);
			csvWriter.append(Constant.NEWLINE);

			// Locator map which stores all locators with default value which will used to create locator version sequencing   
			Map<String,Integer> locatorVersionMap = new HashMap<>();

			// Parent Data Keeper : Will keep all the version 1 and 2 locator data and will be appended first 
			Set<CSVDataDTO> parentDataKeeper = new HashSet<>();
			
			// Rest of version data will be kept here and this data will be added later 
			Set<CSVDataDTO> dataKeeper = new HashSet<>();

			setLocatorAndVersion(inputRecords, versionLimit, locatorVersionMap, parentDataKeeper, dataKeeper);

			// Write data in CSV
			for(CSVDataDTO data : parentDataKeeper) {
				writeInCSV(data,csvWriter,locatorVersionMap);
			}
			for(CSVDataDTO data : dataKeeper) {
				writeInCSV(data,csvWriter,locatorVersionMap);
			}
	
			csvWriter.flush();
			csvWriter.close();
			File csvFile = new File(fileName);
			System.out.println("Processed file : " + csvFile.getAbsolutePath());
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
		    if(inputData!=null)
		    	inputData.close();
		}
	}


	/**
	 * <ul>
	 * <li>Iterate over input records and increment the data keeper to track the total number of created records. 
	 * <li>With versionLimit, random version number will generated in the given limit.
	 * <li>According to the fetched randomVersion, version of the locator will be created.
	 * <li>If version is either 1 or 2, it will be added in parentDataKeeper else it will be stored in dataKeeper.
	 * <li>Parent locator also set in dataKeeper. Parent Locator will be set only if
	 * 		<li>- Version number is equivalent to randomVersion
	 * 		<li>- Locator's last digit is even and 
	 *      <li>- It should not be first locator, as it is referring to it's previous locator
	 * </ul>
	 * @param inputRecords
	 * @param versionLimit
	 * @param locatorVersionMap
	 * @param parentDataKeeper
	 * @param dataKeeper
	 */
	private static void setLocatorAndVersion(Integer inputRecords, Integer versionLimit,
			Map<String, Integer> locatorVersionMap, Set<CSVDataDTO> parentDataKeeper, Set<CSVDataDTO> dataKeeper) {
		
		// Get random prefix alphabet in between A to Z
		String prefix = DataGeneratorUtility.getPNRPrefix(); 
		
		for(Integer increment=Constant.ONE, dataKeeperSize = Constant.ONE; increment <= inputRecords && dataKeeperSize <= inputRecords; increment++) {
			String padded = String.format("%05d" , increment);  // Generate String of 5 char with increment value and pad remaining with zero
			String locator = prefix + padded;  // locator is combination of prefix and padded value
			
			locatorVersionMap.put(locator, Constant.ONE);
			Integer randomVersion = DataGeneratorUtility.getRandomVersion(versionLimit); // Get random version which depends on limit

			/** Iterate over version for each locator */
			for(Integer version=Constant.ONE; version <= randomVersion  && dataKeeperSize <= inputRecords; version++) {
				CSVDataDTO data = new CSVDataDTO();
				String parentLocator = "";
				data.setLocator(locator);
				data.setVersion(version.toString());
				data.setParent_locator(parentLocator);
				data.setData(Constant.DTO_DATA);

				if(version == Constant.ONE || version == Constant.TWO) {
					parentDataKeeper.add(data);
					dataKeeperSize++;
				} else {
					if(version == randomVersion && increment > Constant.ONE && increment % Constant.TWO == 0) {
						padded = String.format("%05d" , increment-Constant.ONE);
						parentLocator = prefix + padded;
						data.setParent_locator(parentLocator);
					}
					dataKeeper.add(data);
					dataKeeperSize++;
				}
			}
		}
	}

	/** Write in CSV file
	 * @param data
	 * @param csvWriter
	 * @param locatorVersionMap
	 */
	private static void writeInCSV(CSVDataDTO data, FileWriter csvWriter, Map<String, Integer> locatorVersionMap) {
		try {
			Integer versionVal = locatorVersionMap.get(data.getLocator());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			csvWriter.append(data.getLocator()+ Constant.COMMA + versionVal + Constant.COMMA + data.getParent_locator() + Constant.COMMA + timestamp.toInstant() + Constant.COMMA + data.getData());
			locatorVersionMap.put(data.getLocator(),++versionVal);
			csvWriter.append(Constant.NEWLINE);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Create File Name
	 * @param inputRecords
	 * @return
	 */
	private static String getFileName(Integer inputRecords) {
		Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		String fileName =  inputRecords + Constant.UNDERSCORE + currentTimeStamp.getTime()+ Constant.CSV_EXT;
		return fileName;
	}

}
