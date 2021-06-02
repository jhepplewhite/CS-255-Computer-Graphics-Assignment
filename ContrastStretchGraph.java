import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//Jonathan Hepplewhite - 827328
//"This work is my own, individual effort."


public class ContrastStretchGraph extends JPanel implements MouseListener, MouseMotionListener, KeyListener
{
	
	Rectangle point1;
	Rectangle point2;
	
	int point1X = 195;
	int point1Y = 395;
	int point2X = 395;
	int point2Y = 195;
	
	final int leftSpace = 50;
	final int rightSpace = 25;
	final int bottomSpace = 35;
	final int topSpace = 40;
	
	int activePoint;
	Boolean moving = false;
	
	JTextField r1Field;
	JTextField s1Field;
	JTextField r2Field;
	JTextField s2Field;
	
	public ContrastStretchGraph(int width, int height)
	{
		point1 = new Rectangle(point1X,point1Y,10,10);
		point2 = new Rectangle(point2X,point2Y,10,10);
		
		JLabel label = new JLabel("Press shift to apply an entered value.");
		
		//Set up r1 input textfield
    	r1Field = new JTextField(3);
    	r1Field.setText(String.valueOf(convertLocationToConStrX((point1.getCenterX()))));
    	r1Field.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}
			//Handle and apply text input
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					activePoint = 1;
				try
				{
					double convertedValue = Integer.parseInt(r1Field.getText());
					if(convertedValue < 0)
					{
						convertedValue = 0;
					}
					convertedValue = convertConStrToLocationX(convertedValue);
					if(restrictPointXInput(convertedValue))
					{
						convertedValue = point2.getMinX() -2;
					}
					point1.setLocation((int)convertedValue-(int)point1.getWidth()/2, (int)point1.getY());
					r1Field.setText(String.valueOf(convertLocationToConStrX(convertedValue)));

					repaint();
				}
				catch(NumberFormatException e1)	
				{
					System.out.println("Input error");

				}
				}
		}
    		
    	});
    	//Set up s1 input textfield
    	s1Field = new JTextField(3);
    	s1Field.setText(String.valueOf(convertLocationToConStrY((point1.getCenterY()))));
    	s1Field.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			//Handle and apply text input
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					activePoint=1;
					try
					{
						double convertedValue = Integer.parseInt(s1Field.getText());
						if(convertedValue < 0)
						{
							convertedValue = 0;
						}
						convertedValue = convertConStrToLocationY(convertedValue);
						s1Field.setText(String.valueOf(convertLocationToConStrY(convertedValue)));
						point1.setLocation((int)point1.getX(), (int)convertedValue-(int)point1.getHeight()/2);				
						repaint();
					}
					catch(NumberFormatException e1)	
					{
						System.out.println("Input error");

					}	
				}	
			}
		});
    	//Set up r2 input textfield
    	r2Field = new JTextField(3);
    	r2Field.setText(String.valueOf(convertLocationToConStrX((point2.getCenterX()))));
    	r2Field.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			//Handle and apply text input
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					activePoint = 2;
					try
					{
						double convertedValue = Integer.parseInt(r2Field.getText());
						if(convertedValue < 0)
						{
							convertedValue = 0;
						}
						convertedValue = convertConStrToLocationX(convertedValue);
						if(restrictPointXInput(convertedValue))
						{
							convertedValue = point1.getMaxX();
						}
						
						point2.setLocation((int)convertedValue - (int)point2.getWidth()/2, (int)point2.getY());
						r2Field.setText(String.valueOf(convertLocationToConStrX(convertedValue)));
						repaint();
					}
					catch(NumberFormatException e1)
					{
						System.out.println("Input error");

					}
					
				}
		}
    		
    	});
    	//Set up s2 input textfield
    	s2Field = new JTextField(3);
    	s2Field.setText(String.valueOf(convertLocationToConStrY((point2.getCenterY()))));
    	s2Field.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			//Handle and apply text input
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_SHIFT)
				{
					activePoint = 2;
					try
					{
						double convertedValue = Integer.parseInt(s2Field.getText());
						if(convertedValue < 0)
						{
							convertedValue = 0;
						}
						convertedValue = convertConStrToLocationY(convertedValue);
						s2Field.setText(String.valueOf(convertLocationToConStrY(convertedValue)));
						point2.setLocation((int)point2.getX(), (int)convertedValue - (int)point2.getHeight()/2);
						repaint();
					}
					catch(NumberFormatException e1)	
					{
						System.out.println("Input error");
					}	
				}
			}
		});
    	//Set up panel for text input
    	JPanel panel = new JPanel();    	
    	panel.add(label);
    	panel.add(new JLabel("r1: "));
    	panel.add(r1Field);
    	panel.add(new JLabel("s1: "));
    	panel.add(s1Field);
    	panel.add(new JLabel("r2: "));
    	panel.add(r2Field);
    	panel.add(new JLabel("s2: "));
    	panel.add(s2Field);
    	this.add(panel);
    	
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.WHITE);
        
        
        g.fillRect(leftSpace, topSpace, this.getWidth() - (leftSpace + rightSpace), this.getHeight() - (bottomSpace + topSpace));
        
        g.setColor(Color.BLACK);
        
        g.drawRect(leftSpace, topSpace, this.getWidth()-(leftSpace + rightSpace), this.getHeight() - (bottomSpace + topSpace));
     
        //Draw grid
        for(int i = 1; i<=10; i++)
        {
        	//Vertical lines
        	g.setColor(new Color(200,200,200));
        	//Find next line start x
        	int j = ((this.getWidth() - (leftSpace + rightSpace+10))/10) * i;
        	g.drawLine(leftSpace+j, topSpace, leftSpace+j,this.getHeight()-bottomSpace);
        	//Horizontal lines
        	int k = ((this.getHeight() - (bottomSpace + topSpace+10))/10)*i;
        	g.drawLine(leftSpace, (this.getHeight() - bottomSpace)-k, this.getWidth()-rightSpace, (this.getHeight() - bottomSpace)-k);
        	
        	g.setColor(new Color(0,0,0));
        	//Bottom axis labels
        	g.drawString(String.valueOf(i*25), (leftSpace-10)+j, this.getHeight()-(bottomSpace-10));
        	//Left axis labels
        	g.drawString(String.valueOf(i*25), leftSpace-25, (this.getHeight() - bottomSpace)-k);
        }
        g.drawString("0", leftSpace-10, this.getHeight()-(bottomSpace-10));
        g.drawString("255", leftSpace, topSpace);
        g.drawString("255", this.getWidth() - (rightSpace), this.getHeight()-topSpace);
        
        pointPosCheck(point1);
        g2.draw(point1);
        pointPosCheck(point2);
        g2.draw(point2);
        //Draw lines between points
        g2.drawLine(leftSpace, this.getHeight()-bottomSpace, (int)point1.getCenterX(), (int)point1.getCenterY());
        g2.drawLine((int)point1.getCenterX(), (int)point1.getCenterY(), (int)point2.getCenterX(), (int)point2.getCenterY());
        g2.drawLine((int)point2.getCenterX(), (int)point2.getCenterY(), this.getWidth()-rightSpace, topSpace);

	}
	
	//Restrict points to the graph and each other
	public void pointPosCheck(Rectangle point)
	{
		if(point.getCenterX() < 50)
		{
			point.setLocation((int)(50-point.getWidth()/2), (int)point.getY());
		}
		if(point.getCenterX() > this.getWidth()-rightSpace)
		{
			point.setLocation((int)(this.getWidth() - (rightSpace+point.getWidth()/2)), (int)point.getY());
		}
		if(point.getY() < (topSpace-point.getWidth()/2))
		{
			point.setLocation((int)point.getX(), (int)(topSpace-point.getWidth()/2));
		}
		if(point.getCenterY() > this.getHeight() - bottomSpace)
		{
			point.setLocation((int)point.getX(), (int)(this.getHeight() - (bottomSpace+point.getHeight()/2)));
		}
		if(point1.getMaxX() > point2.getMaxX() && activePoint == 1)
		{
			point.setLocation((int)point2.getMinX()-2, (int)point1.getY());
		}
		if(point2.getMinX() < point1.getMinX() && activePoint == 2)
		{
			point.setLocation((int)point1.getMinX()+2, (int)point.getY());
		}
		//Can be used to restrict Y values between the two points
//		if(point1.getMaxY() < point2.getMaxY() && activePoint == 1)
//		{
//			point.setLocation((int)point.getX(), (int)point2.getMaxY() - (int)point.getHeight());
//		}
//		if(point2.getMinY() > point1.getMinY() && activePoint == 2)
//		{
//			point.setLocation((int)point.getX(), (int)point1.getMinY());
//		}
	}
	
	//Restrict x input for text input
	public boolean restrictPointXInput(double value)
	{
		if(point2.getMinX() < value && activePoint == 1)
		{
			return true;
		}
		if(point1.getMinX() > value && activePoint == 2)
		{
			return true;
		}
		return false;
	}
	
	//Move points using mouse input
	public void movePoint(MouseEvent e, Rectangle point)
	{
		point.setLocation((e.getX()-(int)point.getWidth()/2), (e.getY()-(int)point.getHeight()/2));
		pointPosCheck(point);
		r1Field.setText(String.valueOf(convertLocationToConStrX((point1.getCenterX()))));
		r2Field.setText(String.valueOf(convertLocationToConStrX((point2.getCenterX()))));
		s1Field.setText(String.valueOf(convertLocationToConStrY((point1.getCenterY()))));
		s2Field.setText(String.valueOf(convertLocationToConStrY((point2.getCenterY()))));
		repaint();
	}
	
	//Take x contrast stretch values (0-255) and convert to a location for points
	public double convertConStrToLocationX(double input)
	{
		double conversion = input*2;
		conversion += leftSpace;
		return conversion;
	}
	
	//Take y contrast stretch values (0-255) and convert to a location for points
	public double convertConStrToLocationY(double input)
	{
		double conversion = input*-2;
		conversion += 510;
		conversion += topSpace;
		return conversion;
	}
	
	//Take point x location and convert to a value 0-255
	public int convertLocationToConStrX(double value)
	{
		double newValue = value-leftSpace;
		newValue = newValue/2;
		return (int)newValue;
	}
	
	//Take point y location and convert to a value 0-255
	public int convertLocationToConStrY(double value)
	{
		double newValue = value-topSpace;
		newValue = newValue - 510;
		newValue = newValue/2*-1;
		return (int)newValue;
	}
	
	//Get values to be used in calculation from point
	public int[] getContrastStretchValues()
	{
		int[] values = new int[4];
		
		values[0] = convertLocationToConStrX((int)point1.getCenterX());
		values[1] = convertLocationToConStrY((int)point1.getCenterY());
		values[2] = convertLocationToConStrX((int)point2.getCenterX());
		values[3] = convertLocationToConStrY((int)point2.getCenterY());
		
		return values;
	}
	
	//For testing
	public static void main(String[] args)
	{
		
		JFrame frame = new JFrame();
        frame.pack();
        //frame.setPreferredSize(new Dimension(800, 600));
		ContrastStretchGraph g = new ContrastStretchGraph(585,585);
        g.setPreferredSize(new Dimension(585, 585));
		frame.add(g);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		g.repaint();
	}

	//Moving point
	@Override
	public void mousePressed(MouseEvent e) 
	{
		
		if(point1.contains(e.getX(), e.getY()))
		{
			activePoint = 1;
			moving = true;
		}
		else if(point2.contains(e.getX(), e.getY()))
		{
			activePoint = 2;
			moving = true;
		}
	}
	
	//Moving point
	@Override
	public void mouseDragged(MouseEvent e) {
		if(activePoint == 1 && moving)
		{
			movePoint(e, point1);
		}
		else if(activePoint == 2 && moving)
		{
			movePoint(e, point2);
		}	
	}

	//Moving point
	@Override
	public void mouseReleased(MouseEvent e) {
		if(point1.contains(e.getX(), e.getY()))
		{
			movePoint(e, point1);
		}
		else if(point2.contains(e.getX(), e.getY()))
		{
			movePoint(e, point2);
		}
		else
		{
			moving = false;
		}	
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
