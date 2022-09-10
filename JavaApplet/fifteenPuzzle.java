import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class fifteenPuzzle extends JApplet 
{
	static final int beginner = 11;
	static final int amateur = 21;
	static final int professional = 41;
	
	// difficulty comboBox
	private String[] levels = {"BEGINNER", "AMATEUR", "PROFESSIONAL"};

	private JComboBox<String> jcbLevels = new JComboBox<String>(levels);
	
	// Reset button
	private JButton btnReset = new JButton("Reset");
	
	// Puzzle board panel
	private PuzzleBoard puzzleBoard = new PuzzleBoard();
	
	// Game mode
	private boolean won = false;

	public void init() 
	{
		// Add Puzzle board
		add(puzzleBoard, BorderLayout.CENTER);

		// Make ComboBox and Button part
		JPanel pNorth = new JPanel();
		pNorth.add(jcbLevels);
		pNorth.add(btnReset);
		add(pNorth, BorderLayout.NORTH);
		
		// Register listener
		btnReset.addActionListener(new ActionListener() 
		{ // Reset Button
			// When Reset button is clicked
			public void actionPerformed(ActionEvent e) 
			{
				if(jcbLevels.getSelectedItem().toString().equals("BEGINNER")) 
				{
					puzzleBoard.shuffleTiles(beginner);
				} 
				
				else if(jcbLevels.getSelectedItem().toString().equals("AMATEUR")) 
				{
					puzzleBoard.shuffleTiles(amateur);
				} 
				
				else if(jcbLevels.getSelectedItem().toString().equals("PROFESSIONAL")) 
				{
					puzzleBoard.shuffleTiles(professional);
				} 
				
				won = false;
				repaint(); // Show new board
			}
		});
	}
	
	public void parentRepaint() 
	{
		repaint();
	}
	
	/***************************************************************
	 * Puzzle board panel
	 **************************************************************/
	class PuzzleBoard extends JPanel  implements MouseListener
	{
		int puzzle[][] = new int[4][4]; // tiles[x][y]
	
		/**
		 * Constructor
		 */
		PuzzleBoard() 
		{	
			addMouseListener(this);
			shuffleTiles(beginner);
		}
		
		/**
		 * Reset the Puzzle Board
		 */
		public void resetPuzzleBoard() 
		{
			// Make tiles
			for(int y = 0; y < 4; y++) 
			{
				for(int x = 0; x < 4; x++) 
				{
					puzzle[x][y] = (y * 4 + x) + 1; 
				}
			}

			// Change into 0, which means empty 
			puzzle[3][3] = 0;
		}
		
		/**
		 * Get color for tiles
		 */
		public Color getColor(int num) 
		{
			Color color = null;

			switch(num) 
			{
				case 0: color = new Color(255, 255, 255);
						break;

				case 1: case 2: case 3:

				case 4: color = new Color(235, 75, 83);
						break;

				case 5: case 6: case 7:

				case 8: color = new Color(117, 99, 255);
						break;

				case 9: case 10: case 11:

				case 12: color = new Color(103, 224, 103);
						break;

				case 13: case 14:

				case 15: color = new Color(212, 83, 237);
						break;
			}

			return color;
		}
	
		/**
		 * Shuffle tiles
		 * Point(x, y) means the place of tiles[x][y]
		 */
		public void shuffleTiles(int time) 
		{
			resetPuzzleBoard();

			Point current = new Point(3, 3); // point of the place of 0 (empty place)
			Point old = new Point(current); // to avoid going back to the old place
			
			for(int i=0; i<time; i++) 
			{
				randomMove(current, old);
			}
		}
	
		/**
		 * Move a tile randomly
		 * Point(x, y) means the place of tiles[x][y]
		 */
		public void randomMove(Point current, Point old) 
		{
			ArrayList<Point> players = new ArrayList<Point>();
			
			// Check candidates
			// Check Up place
			Point up = new Point(current);

			up.translate(0, -1); // Up point

			if(current.y > 0 && !(up.equals(old))) 
			{
				players.add(up);
			}

			// Check Down place
			Point down = new Point(current);

			down.translate(0, 1); // Down point

			if(current.y < 3 && !(down.equals(old))) 
			{
				players.add(down);
			}

			// Check Left place
			Point left = new Point(current);

			left.translate(-1, 0); // Left point

			if(current.x > 0 && !(left.equals(old))) 
			{
				players.add(left);
			}

			// Check Right place
			Point right = new Point(current);

			right.translate(1, 0); // Right point

			if(current.x < 3 && !(right.equals(old))) 
			{
				players.add(right);
			}
			
			// Choose one from candidates
			int randomPlayers = (int)(Math.random() * players.size());

			Point newplayers = players.get(randomPlayers);

			// Move tile
			puzzle[current.x][current.y] = puzzle[newplayers.x][newplayers.y];
			puzzle[newplayers.x][newplayers.y] = 0;
			
			// Update information
			old.setLocation(current);
			current.setLocation(newplayers);
		}
	
		/**
		 * Check the end of game
		 */
		public boolean End() 
		{
			for(int y=0; y<4; y++) 
			{
				for(int x=0; x<4; x++) 
				{
					int num = (y * 4 + x) + 1;
					if( !(puzzle[x][y] == num) && (num < 16)) 
					{
						return false;
					}
				}
			}

			return true;
		}
	
		/**
		 * Paint the Puzzle Board
		 */
		public void paint(Graphics g) 
		{
			int puzzleWidth = getWidth() / 4;   // define tile width
			int puzzleHeight = getHeight() / 4; // define tile height

			for(int y=0; y<4; y++) 
			{
				for(int x=0; x<4; x++) 
				{
					// draw tiles
					g.setColor(getColor(puzzle[x][y]));
					g.fillRoundRect(puzzleWidth * x, puzzleHeight * y, puzzleWidth - 1, puzzleHeight - 1, 20, 20);

					// draw tile numbers
					g.setColor(new Color(255, 255, 255));
		    		g.setFont(new Font("",Font.ROMAN_BASELINE, 80));
		    		g.drawString(String.valueOf(puzzle[x][y]), puzzleWidth * x + puzzleWidth / 3, puzzleHeight * y + puzzleHeight * 2 /3);
				}
			}
			
			if(won)
			{
				/*g.setColor(new Color(0, 0, 0));
				g.fillRoundRect(puzzleWidth / 2, puzzleHeight / 2, puzzleWidth * 3, puzzleHeight * 3, 20, 20);*/
                
				/*g.setColor(new Color(163, 5, 5));
	    		g.drawString("CONGRATULATIONS!", puzzleWidth, puzzleHeight * 2);*/

				Image pic = getImage(getDocumentBase(), "images.jpg");
				g.drawImage(pic, 460, 150, this);
				g.drawString("CONGRATULATIONS!", 525, 500);
			}
		}
	
		/**
		 * When Mouse Released
		 */
		public void mouseReleased(MouseEvent e) 
		{
			// Check game mode
			if(won) 
			{
				return; // ignore mouse event
			}
			
			int click_X = e.getX();
			int click_Y = e.getY();
			
			// Figure out the x and y
			int y = (click_Y * 4) / getHeight();
			int x = (click_X * 4) / getWidth();
			
			// Move the clicked tile
			if(y > 0 && puzzle[x][y - 1] == 0)
			{ // Go Up
				puzzle[x][y - 1] = puzzle[x][y];
				puzzle[x][y] = 0;
			} 
			
			else if(y < 3 && puzzle[x][y + 1] == 0) 
			{ // Go Down
				puzzle[x][y + 1] = puzzle[x][y];
				puzzle[x][y] = 0;
			} 
			
			else if(x > 0 && puzzle[x - 1][y] == 0) 
			{ // Go Left
				puzzle[x - 1][y] = puzzle[x][y];
				puzzle[x][y] = 0;
			} 
			
			else if(x < 3 && puzzle[x + 1][y] == 0) 
			{ // Go Right
				puzzle[x + 1][y] = puzzle[x][y];
				puzzle[x][y] = 0;
			}
			
			// Check won or not
			if(End())
			{
				won = true;
			}
			
			parentRepaint();
		}
	
		public void mousePressed(MouseEvent e) 
		{}
	
		public void mouseClicked(MouseEvent e) 
		{}
	
		public void mouseEntered(MouseEvent e) 
		{}
	
		public void mouseExited(MouseEvent e) 
		{}
	}
}
