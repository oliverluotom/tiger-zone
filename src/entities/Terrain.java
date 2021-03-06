package entities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Terrain is the parent class of all the subregions: cities, fields,
 * roads, and monasteries. These are a part of the tiles.
 */
public abstract class Terrain {
	// The attributes
	protected int theTerrainID;
	protected int theRegionID;
	protected int tileID;
	protected Animal theAnimal;
	protected TigerObject theTiger;
	protected CrocodileObject theCrocodile;
	protected char theType;
	protected ArrayList<Integer> theTileConnections;
	public static int terrainNum = 0;
//	protected int orientation;
	protected int zoneMin; 

	// Constructors
	public Terrain() {
	}

	/**
	 * Terrain is an object of tiles that describes cities, fields,
	 * roads, and monasteries.
	 * @param aTileConnections A set of connections the terrain makes with
	 *                         the tile
	 * @return Terrain
	 */
	public Terrain(ArrayList<Integer> aTileConnections) {
		theTerrainID       = terrainNum++;
		theTileConnections = aTileConnections;
		theRegionID		   = -1;
	}

	//ACCESSORSS
	public int getTileID() { 
		return tileID;
	}

	public int getRegionID() {
		return theRegionID;
	}

	public int getZoneMin() {
		return zoneMin;
	}
	
	public int adjust(int value) { 
		if(value == 0) return 1;
		else if (value == 1) return 2;
		else if (value == 2) return 3;
		else if (value == 3) return 6;
		else if (value == 4) return 9;
		else if (value == 5) return 8;
		else if (value == 6) return 7;
		else if (value == 7) return 4;
		else return value;			
	}
	
	public int zoneListMin(ArrayList<Integer> list) { 
		int min = Integer.MAX_VALUE;
		if (!list.isEmpty()) { 
			min = adjust(list.get(0));
			for (Integer value : list) {
				int adjustedValue = adjust(value);
				if (adjustedValue < min) { 
					min = adjustedValue;
				}
			}
		}
		return min;
	}


	public int getTerrainID() {
		return theTerrainID;
	}

	public ArrayList<Integer> getTileConnections() {
		return theTileConnections;
	}

	public char getType() {
		return theType;
	}
//	
//	public int getOrientation() {
//		return orientation;
//	}

	public TigerObject getTiger() {
		return theTiger;
	}

	public CrocodileObject getCrocodile() {
		return theCrocodile;
	}

	public Animal getAnimal() {
		return theAnimal;
	}

	public char getAnimalType() {
		char result = '-';
		if (theAnimal != null) {
			result = theAnimal.getType();
		}
		return result;
	}

	//MUTATORS
	public void setTileID(int tileID) { 
		this.tileID = tileID;
	}

	public void setRegionID(int theRegionID) {
		this.theRegionID = theRegionID;
	}

	public void setMin(int zoneMin) { 
		this.zoneMin = zoneMin;
	}

	public void setTerrainID(int terrainID) {
		this.theTerrainID = terrainID;
	}

	/**
	 * Adds a Tiger to the terrain. Can only be one per.
	 * @param aTiger TigerObject
	 */
	public void addTiger(TigerObject aTiger) {
		theTiger = aTiger;
	}

	/**
	 * Removes the Tiger on this terrain.
	 * @return TigerObject Null if no Tiger on terrain.
	 */
	public TigerObject removeTiger() {
		TigerObject result = theTiger;
		theTiger = null;
		return result;
	}

	/**
	 * Adds a Tiger to the terrain. Can only be one per.
	 * @param aTiger TigerObject
	 */
	public void addCrocodile(CrocodileObject aCrocodile) {
		theCrocodile = aCrocodile;
	}

	/**
	 * Removes the Tiger on this terrain.
	 * @return TigerObject Null if no Tiger on terrain.
	 */
	public CrocodileObject removeCrocodile() {
		CrocodileObject result = theCrocodile;
		theCrocodile = null;
		return result;
	}
	
	public void setOrientation(int orientation) {

		for (int i = 0; i < theTileConnections.size(); i++) {
			int newConnect = Math.floorMod((theTileConnections.get(i) - 2 * orientation),8);
			theTileConnections.set(i, newConnect);
		}
//		this.orientation = orientation;
		int newMin = zoneListMin(theTileConnections);
		zoneMin = newMin;
	}
	
	//METHODS
	public boolean equals(Terrain terrain) {
		if (this.theType == terrain.getType()) return true;
		return false;
	}

	public boolean hasTiger() {
		if (theTiger == null) return false;
		return true;
	}

	public boolean hasCrocodile() {
		if (theCrocodile == null) return false;
		return true;
	}

	public boolean hasAnimal() {
		if (theAnimal == null) return false;
		return true;
	}

	@Override
	public String toString() {
		String terrainID = String.valueOf(theTerrainID);
		char terrainType = theType;
		String hasTiger = "no";
		if (hasTiger()) {
			hasTiger = "a";
		}
		String numberOfConnections = String.valueOf(theTileConnections.size());
		return "The terrain " + terrainID + " of type " + terrainType + " in region " + theRegionID + " has " +
		hasTiger + " Tigers and " + numberOfConnections + " tile connection(s) (Min INDEX= " + getZoneMin() + ")";
	}
}
