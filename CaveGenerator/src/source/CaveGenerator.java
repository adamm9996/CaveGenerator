package source;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class CaveGenerator
{
	private class Coord
	{
		public int x;
		public int y;
		
		public Coord(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	private int width, height;
	private int[][] map;
	private int fillPercent;
	private int iterate;
	private Random pseudoRand;
	private int filledMinSize;
	private int emptyMinSize;
	
	public CaveGenerator(int width, int height, int fillPercent, int iterate, int filledMinSize, int emptyMinSize)
	{
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		this.fillPercent = (fillPercent > 100 || fillPercent < 0) ? 50 : fillPercent;
		this.pseudoRand = new Random();
		this.iterate = iterate;
		this.filledMinSize = filledMinSize;
		this.emptyMinSize = emptyMinSize;
	}

	public CaveGenerator(int width, int height, int fillPercent, int iterate, int filledMinSize, int emptyMinSize, String seed)
	{
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		this.fillPercent = (fillPercent > 100 || fillPercent < 0) ? 50 : fillPercent;
		this.pseudoRand = new Random(seed.hashCode());
		this.iterate = iterate;
		this.filledMinSize = filledMinSize;
		this.emptyMinSize = emptyMinSize;
	}
	
	public int[][] generate()
	{
		randomFillMap();
		for (int t = 0; t < iterate; t++)
			smoothMap();
		processMap();		
		
		return map;
	}
	
	private void processMap()
	{
		ArrayList<ArrayList<Coord>> wallRegions = getRegions(1);
		
		for (ArrayList<Coord> wallRegion : wallRegions)
		{
			if (wallRegion.size() < filledMinSize)
				for (Coord tile : wallRegion)
					map[tile.x][tile.y] = 0;
		}
		
		ArrayList<ArrayList<Coord>> emptyRegions = getRegions(0);
		
		for (ArrayList<Coord> emptyRegion : emptyRegions)
		{
			if (emptyRegion.size() < emptyMinSize)
				for (Coord tile : emptyRegion)
					map[tile.x][tile.y] = 1;
		}
	}
	
	private ArrayList<ArrayList<Coord>> getRegions(int tileType)
	{
		ArrayList<ArrayList<Coord>> regions = new ArrayList<ArrayList<Coord>>();
		int[][] mapFlags = new int[width][height];
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (mapFlags[x][y] == 0 && map[x][y] == tileType)
				{
					ArrayList<Coord> newRegion = getRegionTiles(x, y);
					regions.add(newRegion);
					
					for (Coord tile : newRegion)
					{
						mapFlags[tile.x][tile.y] = 1;
					}
				}
		
		
		return regions;
	}

	private ArrayList<Coord> getRegionTiles(int startX, int startY)
	{
		ArrayList<Coord> tiles = new ArrayList<>();
		int[][] mapFlags = new int[width][height];
		int tileType = map[startX][startY];		
		Queue<Coord> queue = new LinkedList<Coord>();
		queue.add(new Coord(startX, startY));
		mapFlags[startX][startY] = 1;
		
		while (queue.size() > 0)
		{
			Coord current = queue.remove();
			tiles.add(current);
			
			for (int x = current.x-1; x <= current.x+1; x++)
				for (int y = current.y-1; y <= current.y+1; y++)
					if (isInMapRange(x, y) && (y == current.y || x == current.x))
						if (mapFlags[x][y] == 0 && map[x][y] == tileType)
						{
							mapFlags[x][y] = 1;
							queue.add(new Coord(x, y));
						}
		}
		
		return tiles;
	}
	
	private boolean isInMapRange(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	private void smoothMap()
	{
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++)
			{
				int surroundingFilledTiles = getSurroundingCount(w,h);
				if (surroundingFilledTiles > 4)
					map[w][h] = 1;
				else if (surroundingFilledTiles < 4)
					map[w][h] = 0;
			}
	}
	
	int getSurroundingCount(int x, int y)
	{
		int wallCount = 0;
		for (int neighbourX = x-1; neighbourX <= x+1; neighbourX++)
			for (int neighbourY = y-1; neighbourY <= y+1; neighbourY++)
				if (isInMapRange(neighbourX, neighbourY))
				{
					if (neighbourX != x || neighbourY != y)
						wallCount += map[neighbourX][neighbourY];
				}
				else 
				{
					wallCount++;
				}
				
		return wallCount;
	}
	
	private void randomFillMap()
	{
		//initially fills the map with 1s and 0s that look like noise. Ratio depends on wallPercentage
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++)
				if (w == 0 || w == width-1 || h == 0 || h == height-1)//ensure the borders are always filled
					map[w][h] = 1;
				else
					map[w][h] = (pseudoRand.nextInt(100) < fillPercent) ? 1 : 0;
	}
		
}
