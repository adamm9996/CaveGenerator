package source;

import java.util.Random;

public class CaveGenerator
{
	private int width, height;
	private int[][] map;
	private int fillPercent;
	private int iterate;
	private Random pseudoRand;
	
	public CaveGenerator(int width, int height, int fillPercent, int iterate)
	{
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		this.fillPercent = (fillPercent > 100 || fillPercent < 0) ? 50 : fillPercent;
		this.pseudoRand = new Random();
		this.iterate = iterate;
	}

	public CaveGenerator(int width, int height, int fillPercent, int iterate, String seed)
	{
		this.width = width;
		this.height = height;
		this.map = new int[width][height];
		this.fillPercent = (fillPercent > 100 || fillPercent < 0) ? 50 : fillPercent;
		this.pseudoRand = new Random(seed.hashCode());
		this.iterate = iterate;
	}
	
	public int[][] generate()
	{
		randomFillMap();
		for (int t = 0; t < iterate; t++)
			smoothMap();
		
		return map;
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
				if (neighbourX >= 0 && neighbourX < width && neighbourY >= 0 && neighbourY < height)
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
