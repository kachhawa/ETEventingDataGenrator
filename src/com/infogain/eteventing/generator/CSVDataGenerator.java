package com.infogain.eteventing.generator;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.infogain.eteventing.dto.CSVDataDTO;
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
		    System.out.println("Processing for total records : " + inputRecords);  

		    // Get version limit as per given input records
			Integer versionLimit = DataGeneratorUtility.getVersionLimit(inputRecords);
			
			// Get random prefix alphabet in between A to Z
			String prefix = DataGeneratorUtility.getPNRPrefix(); 
			
			Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
			
			/** 2. CSV File Creation and set header data */
			String FILE_NAME = inputRecords + "_" + currentTimeStamp.getTime()+ ".csv";
			FileWriter csvWriter = new FileWriter(FILE_NAME);
			csvWriter.append("locator,version,parent_locator,created,data");
			csvWriter.append("\n");

			System.out.println("Storing data in file : " + FILE_NAME);

			// Locator map which stores all locators with default value which will used to create locator version sequencing   
			Map<String,Integer> locatorVersionMap = new HashMap<>();


			// Parent Data Keeper : Will keep all the version 1 and 2 locator data and will be appended first 
			Set<CSVDataDTO> parentDataKeeper = new HashSet<>();
			
			// Rest of version data will be kept here and this data will be added later 
			Set<CSVDataDTO> dataKeeper = new HashSet<>();
			
			/** 3. Iterate over input records and increment the data keeper to track the total number of created records */
			for(Integer increment=1, dataKeeperSize = 1; increment <= inputRecords && dataKeeperSize <= inputRecords; increment++) {
				String padded = String.format("%05d" , increment);  // Generate String of 5 char with increment value and pad remaining with zero
				String locator = prefix + padded;  // locator is combination of prefix and padded value
				
				locatorVersionMap.put(locator, 1);
				Integer randomVersion = DataGeneratorUtility.getRandomVersion(versionLimit); // Get random version which depends on limit

				/** Iterate over version for each locator */
				for(Integer version=1; version <= randomVersion  && dataKeeperSize <= inputRecords; version++) {
					CSVDataDTO data = new CSVDataDTO();
					String parentLocator = "";
					data.setLocator(locator);
					data.setVersion(version.toString());
					data.setParent_locator(parentLocator);
					data.setData("\\\"{\\\"pnr\\\":\\\"{}\\\"\\\"cart\\\":\\\"{}\\\"\\\"session\\\":\\\"{}\\\"");

					if(version == 1 || version == 2) {
						parentDataKeeper.add(data);
						dataKeeperSize++;
					} else {
						if(version == randomVersion && increment > 1 && increment % 2 == 0) {
							padded = String.format("%05d" , increment-1);
							parentLocator = prefix + padded;
							data.setParent_locator(parentLocator);
						}
						dataKeeper.add(data);
						dataKeeperSize++;
					}
				}
			}

			for(CSVDataDTO data : parentDataKeeper) {
				writeInCSV(data,csvWriter,locatorVersionMap);

			}
			for(CSVDataDTO data : dataKeeper) {
				writeInCSV(data,csvWriter,locatorVersionMap);
			}
	
			csvWriter.flush();
			csvWriter.close();
			System.out.println("Completed");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
		    if(inputData!=null)
		    	inputData.close();
		}
	}

	/**
	 * @param data
	 * @param csvWriter
	 * @param locatorVersionMap
	 */
	private static void writeInCSV(CSVDataDTO data, FileWriter csvWriter, Map<String, Integer> locatorVersionMap) {
		try {
			Integer versionVal = locatorVersionMap.get(data.getLocator());
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			csvWriter.append(data.getLocator()+ "," + versionVal + "," + data.getParent_locator() + "," + timestamp.toInstant() + "," + data.getData());
			locatorVersionMap.put(data.getLocator(),++versionVal);
			csvWriter.append("\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
