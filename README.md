# How to execute JAR :
### 1. Open Command Prompt
### 2. Open location of ETEventingDataGenrator.jar file
### 3. Run below command in Command Prompt:
###    **java -jar ETEventingDataGenrator.jar**
### 4. Enter total number of records to be generated. Values tested from 1 to 500000
### 5. After processing, program will show processed file name with location.


# Files Description
### 1. **CSVDataGenerator.java** : Below are the step by step explanation of logic of this file : 
* Get total number of generated records from command line
* Set various variables like : 
* - **versionLimit** : Get version limit as per given input records 
* - **prefix** : Get random prefix alphabet in between A to Z
* - **csvWriter** : Create CSV, set headers 
* - **Parent Data Keeper Set** : Will keep all the version 1 and 2 locator data and will be appended first 
* - **Normal Data Keeper Set** : Rest of version data will be kept here and this data will be added later 
* - **locatorVersionMap** : Stores all locators with default value which will used to create locator version sequencing.
* Inside loop, iterate over input records and increment the data keeper to track the total number of created records. 
*  - With versionLimit, random version number will generated in the given limit.
*  - According to the fetched randomVersion, version of the locator will be created.
*  - If version is either 1 or 2, it will be added in parentDataKeeper else it will be stored in dataKeeper.
*  - Parent locator also set in dataKeeper. Parent Locator will be set only if
*  - - Version number is equivalent to randomVersion
*  - - Locator's last digit is even and 
*  - - It should not be first locator, as it is referring to it's previous locator

### 2. **DataGeneratorUtility.java** : Utility file have three methods:
*  - **getPNRPrefix()** : Get Locator's prefix from the range A to Z
*  - **getVersionLimit()** : Based on Total Records, it will fetch version, below is criteria:
*  - - If total records are less than 10000, then it will user version 10
*  - - If total records are between 10000 and 100000, then it will user version 20
*  - - If total records are greater than 100000, then it will user version 30
*  - **getRandomVersion()** : Get Random Version as per given version number. Ex: If version number is 20, it can given any number between 1 to 20.

### 3. **Constant.java** : Constant declaration
### 4. **CSVDataDTO.java** : DTO declaration
