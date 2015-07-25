package source;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Run extends JPanel implements MouseListener
{
	static JFrame frame = new JFrame();
	static Run run = new Run();
	int[][] map;
	CaveGenerator caveGenerator;
	
	final static int blockSize = 5;		//size of each cell
	final static int width = 300;		//width of map in cells
	final static int height = 50;		//height of map in cells
	final static int fillPercent = 50;	//percentage of map filled in
	final static int iterate = 100;		//number of iterations of the smoothing algorithm the generator does
	final static int filledMinSize = 50;//removes any wall regions smaller than this value
	final static int emptyMinSize = 50;	//removes any empty regions smaller than this value
	final static boolean seeded = true;	//true: given string, false: random string
	final static String seed = "cat";	//seed used to generate the map, if any
	
	
	public static void main(String[] args)
	{
		if (seeded)
			run.caveGenerator = new CaveGenerator(width, height, fillPercent, iterate, filledMinSize, emptyMinSize, seed);
		else
			run.caveGenerator = new CaveGenerator(width, height, fillPercent, iterate, filledMinSize, emptyMinSize);
		
		run.map = run.caveGenerator.generate();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width*blockSize+16, height*blockSize+39);
		frame.add(run);
		frame.addMouseListener(run);
		frame.setVisible(true);
		
		while (true)
		{
			run.repaint();
		}
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

	@Override
	public void mouseClicked(MouseEvent e)
	{	
			caveGenerator = new CaveGenerator(width, height, fillPercent, iterate, filledMinSize, emptyMinSize);
			run.map = run.caveGenerator.generate();
	}

	@Override
	public void mousePressed(MouseEvent e)	{}

	@Override
	public void mouseReleased(MouseEvent e)	{}

	@Override
	public void mouseEntered(MouseEvent e)	{}

	@Override
	public void mouseExited(MouseEvent e)	{}
}
