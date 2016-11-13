import java.util.ArrayList;

/**
 * Region is the parent class of all the regions: cities, fields,
 * roads, and monasteries. These are a part of the board.
 */
public abstract class Region
{
	// The attributes
	protected double theRegionID;
	protected ArrayList<Terrain> theTerrains;
	protected ArrayList<MeepleObject> theMeeples;
	protected String theType;

	// Constructors
	public Region()
	{
		// Empty for inheretance
	}

	/**
	 * Region is an object of the board that describes cities, fields,
	 * roads, and monasteries. Use this if there is no starting terrain.
	 * @param aRegionID A unique ID derived from the tile and region
	 * @return Region
	 */
	public Region(double aRegionID)
	{
		theRegionID = aRegionID;
		theTerrains = new ArrayList<Terrain>();
		theMeeples  = new ArrayList<MeepleObject>();
	}

	/**
	* Region is an object of the board that describes cities, fields,
	* roads, and monasteries. Use this if there is a single terrain.
	* @param aTerrain Single terrain that is included in the region.
	* @return Region
	*/
	public Region(Terrain aTerrain)
	{
		// Region ID becomes the terrain's ID
		theRegionID = aTerrain.getTerrainID();
		theTerrains = new ArrayList<Terrain>();
		theMeeples  = new ArrayList<MeepleObject>();
		// Add all and update meepels
		addTerain(aTerrain);
	}

	/**
	 * Region is an object of the board that describes cities, fields,
	 * roads, and monasteries. Use this if there is a set of terrain.
	 * @param aTerrains Set of terrain that is included in the region.
	 * @return Region
	 */
	public Region(ArrayList<Terrain> aTerrains)
	{
		// Region ID becomes the first terrain's ID
		theRegionID = aTerrains.get(0).getTerrainID();
		theTerrains = new ArrayList<Terrain>();
		theMeeples  = new ArrayList<MeepleObject>();
		// Add all and update meepels
		addTerain(aTerrains);
	}

	// Getters
	/**
	 * Get region ID
	 * @return double
	 */
	public double getRegionID()
	{
		return theRegionID;
	}

	/**
	 * Returns array list of terrains in this region.
	 * @return ArrayList<Terrain>
	 */
	public ArrayList<Terrain> getTerrains()
	{
		return theTerrains;
	}

	/**
	 * Returns array list of meeples in this region.
	 * @return ArrayList<MeepleObject>
	 */
	public ArrayList<MeepleObject> getMeeples()
	{
		return theMeeples;
	}

	/**
	 * Get type of region
	 * @return String
	 */
	public String getType()
	{
		String result = theType;
		if (theType == null)
		{
			result = "None";
		}
		return result;
	}

	/**
	 * Gets number of terrain in a region
	 * @return int
	 */
	public int getNumberOfTerrains()
	{
		return theTerrains.size();
	}

	// Checks

	/**
	 * Check if there are any meeples in this region.
	 * @return boolean
	 */
	public boolean hasMeeples()
	{
		boolean result = false;
		// Are any meeples in the array
		if (theMeeples.size() != 0)
		{
			result = true;
		}
		return result;
	}

	// Mutator

	// /**
	//  * Adds a meeple to the region. Should only be added
	//  * @param aMeepel
	//  */
	// public void addMeeple(MeepleObject aMeepel)
	// {
	// 	theMeeples.add(aMeepel);
	// }

	/**
	 * Goes through the current train and updates the held meeples.
	 */
	public void updateMeeples()
	{
		// Go through all the Terrain adding meeples
		for (int i = 0; i < theTerrains.size(); i++)
		{
			if (theTerrains.get(i).hasMeeple() == true)
			{
				theMeeples.add(theTerrains.get(i).getMeeple());
			}
		}
	}

	// Remove all meeples
	/**
	 * Removes all meeples from this region. Not the individual tiles.
	 */
	public void removeAllMeeples()
	{
		theMeeples.clear();
	}

	/**
	 * Check if a single terrain is valid, and adds meeples and terrain to region.
	 * @param aTerrain A single terrain
	 */
	public void addTerain(Terrain aTerrain)
	{
		// Check if the type is right
		if (theType != aTerrain.getType())
		{
			throw new IllegalArgumentException("Mismatch terrain");
		}

		// Add terrain
		theTerrains.add(aTerrain);

		// Add meeple
		if (aTerrain.hasMeeple() == true)
		{
			theMeeples.add(aTerrain.getMeeple());
		}
	}

	/**
	 * Check if an array list of terrain is valid, and adds meeples and terrain
	 * to region.
	 * @param aTerrains An arrayList of terrain
	 */
	public void addTerain(ArrayList<Terrain> aTerrains)
	{
		for (int i = 0; i < theTerrains.size(); i++)
		{
			this.addTerain(aTerrains.get(i));
		}
	}

	/**
	 * Merge an existing region into the current one
	 * @param aRegion Of the same type
	 */
	public void addRegion(Region aRegion)
	{
		// Check if the type is right
		if (theType != aRegion.getType())
		{
			throw new IllegalArgumentException("Mismatch Region");
		}

		addTerain(aRegion.getTerrains());
	}

	/**
	 * Prints out the region ID, type, number of meeples, and number of terrains
	 * @return String description
	 */
	public String toString()
	{
		String regionID = String.valueOf(theRegionID);
		String regionType = theType;
		String numberOfMeeples = String.valueOf(theMeeples.size());
		String numberOfTerrain = String.valueOf(theTerrains.size());
		return "The region " + regionID + " of type " + regionType + " has " +
				numberOfMeeples + " Meepel(s) and " + numberOfTerrain + " Terrain(s)";
	}

}
