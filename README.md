# Problem Statement :
* 1. Store data randomly
* 2. While storing the data randomly, take care that parent_locator should stored before the locator
* 3. Version sequencing
```
# Resolution :
* 1. Store data randomly : Used Sets to store data for random 
```	
* 2. While storing the data randomly, take care that parent_locator should stored before the locator:
*	Created two Sets : 
*	- parentDataKeeper(parent_locator) : Store Version 1 and 2 data for all PNR Number. So that can be stored before the locator
*	- dataKeeper(locator) : Store data from Version 3 to 10. On 10th version, storing parent_locator
```				
* 3. Version sequencing :
*	- Create CSVDataDTO class which have locator,version,parent_locator,data fields
*	- Create a Map which have key as locator and value as default increment value 1
*	- While iterating over the Set, increment the version value and use the version value stored in map