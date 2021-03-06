package entities;

import java.util.*;

public class artificialIntelligence {
	private static int movenumber = 0;
	BoardObject currentBoard;
	//int choiceX, choiceY, orientation;
	//TileStack currentStack;
	TigerTile currentTile;
	private static String ourMove = null;
	ArrayList<Region> descendingRegions = new ArrayList<Region>();
	protected int moveCount; 

	public artificialIntelligence(BoardObject currentBoard) {
		this.currentBoard = currentBoard;
	}

	public ArrayList<Region> orderedListOfRegions() {
		//currentBoard.allRegions;
		ArrayList<Region> descendingRegions = new ArrayList<Region>();
		for(Map.Entry<Integer, Region> entry : currentBoard.incompleteRegions.entrySet()) {
			Region tempRegion = entry.getValue();
			//int pX = tempRegion.getPotential
			descendingRegions.add(tempRegion);
		}
		//Collections.sort(descendingRegions, Collections.reverseOrder());
		int n = descendingRegions.size();
       	Region temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){          
               	if(descendingRegions.get(j-1).getPotential() < descendingRegions.get(j).getPotential()){
                    //swap the elements!
                    temp = descendingRegions.get(j-1);
                    descendingRegions.add((j-1), descendingRegions.get(j));
                    descendingRegions.add(j,temp);
                }               
            }
        }
        return descendingRegions;
	}

	public int[] checkOurTigers(Region tempRegion) {
		int ourTigers = 0;
		if(tempRegion.hasTigers()) {
			for(int i = 0; i < tempRegion.getNumOfTigers(); i++) {
				if(tempRegion.theTigers.get(i).getTigerOwner() == currentBoard.getPlayer(0)) {
					ourTigers++;
				}
			}
		}
		int theirTigers = tempRegion.getNumOfTigers() - ourTigers;
		int theArray[] = {ourTigers, theirTigers};
		return theArray;

	}
/*
	public boolean isAdjacent(Location ps, Location r)
	{
		if((ps.getX() == r.getX() && (Math.abs(ps.getY()- r.getY()) == 1)) || (ps.getY()==r.getY() && (Math.abs(ps.getX()-r.getX())==1))) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
*/
	public String getMove(TigerTile currentTile) {
		movenumber++;
		if(!currentBoard.canPlace(currentTile)) {
			ourMove = "TILE " + currentTile.getType() + " ";
			if(currentBoard.getPlayer(0).getNumOfTigers() == 0) { 
			// pop out tiger we own from least valued incomplete region.
				ArrayList<Region> dlist = orderedListOfRegions();
				for(int i = dlist.size() - 1; i >= 0; i--) {	// starting from least valued region
					int tigers[] = checkOurTigers(dlist.get(i));
					if(tigers[0] > 0) {		// if we have tigers in it. 
						//retrieve Tiger
						for(int j = 0; j < dlist.get(i).getNumOfTigers(); j++) {
							if(dlist.get(i).theTigers.get(j).getTigerOwner() == currentBoard.getPlayer(0)) {
								Location removefrom = dlist.get(i).theTigers.get(j).getLocation();
								ourMove += "UNPLACEABLE RETRIEVE TIGER AT " + removefrom.getX() + " " + removefrom.getY();
								break;
							}
						}
					break;
				   }
				}
			}
			else if(currentBoard.getPlayer(0).getNumOfTigers() > 1) {
				ArrayList<Region> desclist = orderedListOfRegions();
				for(int i = 0; i < desclist.size(); i++) {
					int tigers[] = checkOurTigers(desclist.get(i));
					if(tigers[1] - tigers[0] == 1) {
						//place Tiger
						for(int j = 0; j < desclist.get(i).getNumOfTigers(); j++) {
							if(desclist.get(i).theTigers.get(j).getTigerOwner() == currentBoard.getPlayer(0)) {
								Location addhere = desclist.get(i).theTigers.get(j).getLocation();
								ourMove += "UNPLACEABLE ADD ANOTHER TIGER TO" + " " + addhere.getX() + " " + addhere.getY();
							break;
						}
						}
						break;
					}
				}
			}
			else {
				ourMove += "UNPLACEABLE PASS";
     		}
		}
		else {
			ourMove = "PLACE " + currentTile.getType() + " ";	
			//BoardObject currentBoard = new BoardObject(currentBoard);
			//ArrayList<Region> tempArray = orderedListOfRegions();	// O(n^2) so if timeout deal with this.
			ArrayList<TilePair> tempPS = currentBoard.getPossibleSpots();
			int index = -1;
			// Step 0 - override random if tile is a den tile. 
			if(currentTile.getCenter()=='X')
			{
				HashMap<Location, Integer> uniqueLocation = new HashMap<Location, Integer>();
				for(int i = 0; i < tempPS.size(); i++) {
					if(!uniqueLocation.containsKey(tempPS.get(i))) {
						uniqueLocation.put(tempPS.get(i).getLocation(), 0);
					}
					else 
					{
						int curVal = uniqueLocation.get(tempPS.get(i).getLocation());
						uniqueLocation.put(tempPS.get(i).getLocation(), curVal+1);
					}
				}
				int maxMoore = -1;
				Location bestloc = null;;
				for (HashMap.Entry<Location, Integer> entry : uniqueLocation.entrySet()) {
				    Location location = entry.getKey();
				    if(currentBoard.getMoore(location).size() > maxMoore)
				    {
				    	maxMoore = currentBoard.getMoore(location).size();
				    	bestloc = location;
				    }
					}
			   for (int i = 0; i < tempPS.size(); i++)
			   {
				   if(tempPS.get(i).getLocation()==bestloc)
				   {
					   index = i;
				   }
			   }
			}
			// Step 1 - pick random spot out of eligible ones.
			else
			{
			 	Random randomGenerator = new Random();
			 	index = randomGenerator.nextInt(tempPS.size());
			}
			TilePair tempSpot = tempPS.get(index);
			Location tileLoc = tempSpot.getLocation();
			int orientation = tempSpot.getOrientation();
			
			int row = tileLoc.getY();
			int col = tileLoc.getX();
			
			int adjustedY = BoardObject.startY + (BoardObject.COLSIZE/2 - row);
			int adjustedX = BoardObject.startX + (col - BoardObject.ROWSIZE/2);
			
			ourMove += "AT " + adjustedX + " " + adjustedY + " " + orientation; 
			
			currentTile.setOrientation(orientation / 90);
			currentBoard.place(currentTile, new Location(adjustedX, adjustedY));
			TigerTile temp = currentBoard.getRecentTile();
			Terrain[] terrains = temp.getTerrains();
			Map<Integer, Region> allRegions = currentBoard.getAll();
			
			ArrayList<Integer> potentials = new ArrayList<Integer>();
			ArrayList<Integer> potentialRegionID = new ArrayList<Integer>();
			ArrayList<Boolean> prevstatus = new ArrayList<Boolean>();
			
			for (int i = 0; i < terrains.length; i++) { 
				int regionID = terrains[i].getRegionID();
				Region region = allRegions.get(regionID);
				prevstatus.add(region.isCompleted());
				if (!region.hasTigers() && !region.hasCrocodiles()) { 
					potentials.add(region.getPotential());
					potentialRegionID.add(region.getRegionID());
				}
			}
			
			int maxPotential = -1;
			int maxIndex = 0;
			
			for (int i = 0; i < potentials.size(); i++) {
				if(potentials.get(i) > maxPotential) { 
					maxIndex = i;
					maxPotential = potentials.get(i);
				}
			}
			boolean placeprinted = false;
			if(maxPotential != -1) { 
				if(currentBoard.getPlayer(0).getNumOfTigers() == 0) 
				{
					if(currentBoard.getPlayer(0).getNumOfCrocs() == 0)
					{
						ourMove += " NONE";
						placeprinted = true;
					}
					else
					{
						Region theregion = allRegions.get(potentialRegionID.get(maxIndex));
							if(theregion.getType()=='L')
							{
								int tigers[] = checkOurTigers(theregion);
								if(tigers[1] != 0 && tigers[0] == 0) {
								// place croc
								ourMove += " CROCODILE";	
								placeprinted = true;
								}
							}
					}
				}
				else
				{
					Region region = allRegions.get(potentialRegionID.get(maxIndex));
					if((prevstatus.get(maxIndex)==false && region.isCompleted==true) || currentTile.getCenter()=='X' || movenumber%4==0)
						ourMove += " TIGER " + region.getRecentMin();	
					else
						ourMove += " NONE";	
				}	
				if(!placeprinted)
					ourMove += " NONE";	
			}
			else
			{
				ourMove += " NONE";	
			}
						
//			for(int i = 0; i < tempArray.size(); i++) {
//				Region temp = tempArray.get(i);
//				Set<Integer> ourTileList = temp.getTileList();
//				Iterator<Integer> it = ourTileList.iterator();
//				int or = 0;
//     			while(it.hasNext()) {
//
//     				TigerTile tiletoplay = currentBoard.tiles.get(it.next());
//
//     				Location tileLoc = tiletoplay.getCoord();
//     				for(Map.Entry<Location, Integer> entry : uniqueLocation.entrySet()) {
//     					if(isAdjacent(entry.getKey(), tileLoc)) {
//     						currentBoard.place(currentTile, tileLoc);
//     						for(int y = 0; y < tempPS.size(); y++) {
//
//     							if(tempPS.get(i).getLocation() == tileLoc) {
//     								tigerPlacer = temp;
//     								or = tempPS.get(i).getOrientation();
//     								/*int or2;
//     								switch(or)
//     								{
//     									case 0: 
//     										or2 = 0;
//     										break;
//     									case 90:
//     										or2 = 1;
//     										break;
//     									case 180:
//     										or2 = 2;
//     										break;
//     									case 270:
//     										or2 = 3;
//     										break;
//     								}*/
//     								//currentTile.setOrientation(or2);
//     							}
//     						}
//     						
//     						ourMove += "AT " + tileLoc.getX() + " " + tileLoc.getY() + " " + or;
//
//     						break;
//     					}
//     				}
//     				break;
//     			}
//     			break;
//			}
//
//			//check tiles in regions
//			//if any playable spot is adjacent to tile in region place tile
		} 
		
		return ourMove;
	} // end of get move

} // end of class
