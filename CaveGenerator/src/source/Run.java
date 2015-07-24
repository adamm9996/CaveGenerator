package source;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Run extends JPanel
{
	static JFrame frame = new JFrame();
	static Run run = new Run();
	int[][] map;
	
	final static int blockSize = 5;		//size of each cell
	final static int width = 200;		//width of map in cells
	final static int height = 100;		//height of map in cells
	final static int fillPercent = 101;	//percentage of map filled in
	final static int iterate = 10;		//number of iterations of the smoothing algorithm the generator does
	final static boolean seeded = true;	//true: given string, false: random string
	final static String seed = "cat";	//seed used to generate the map, if any
	
	
	public static void main(String[] args)
	{
		CaveGenerator caveGenerator;
		if (seeded)
			caveGenerator = new CaveGenerator(width, height, fillPercent, iterate, seed);
		else
			caveGenerator = new CaveGenerator(width, height, fillPercent, iterate);
		
		run.map = caveGenerator.generate();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width*blockSize, height*blockSize);
		frame.add(run);
		frame.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++)
			{
				g.setColor((map[w][h] == 1) ? Color.BLACK : Color.WHITE);
				g.fillRect(blockSize*w, blockSize*h, blockSize, blockSize);
			}
	}
}
