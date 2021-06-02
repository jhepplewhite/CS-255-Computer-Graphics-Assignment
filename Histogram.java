import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

//Jonathan Hepplewhite - 827328
//"This work is my own, individual effort."

public class Histogram extends JPanel implements ActionListener
{
	int[][] histogram = new int[256][3];
	double[] greyChannelValues = new double[256];
	JButton redButton;
	JButton greenButton;
	JButton blueButton;
	JButton greyButton;
	
	int activeChannel = 3;
	
	int topSpace = 15;
	int bottomSpace = 60;
	int leftSpace = 50;
	int rightSpace = 25;
	
	int redMax = 0;
	int greenMax = 0;
	int blueMax = 0;
	int greyMax = 0;
	
	public Histogram(int w,int h,byte[] data)
	{
		histogramCalculation(w, h, data);
        findMinMaxValues();
		JPanel panel = new JPanel();
		redButton = new JButton("Red Channel");
		greenButton = new JButton("Green Channel");
		blueButton = new JButton("Blue Channel");
		greyButton = new JButton("Grey Channel");
		
		redButton.addActionListener(this);
		greenButton.addActionListener(this);
		blueButton.addActionListener(this);
		greyButton.addActionListener(this);
		
		panel.add(redButton);
		panel.add(greenButton);
		panel.add(blueButton);
		panel.add(greyButton);
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.SOUTH);
	}
	
    public void histogramCalculation(int w, int h, byte[] data)
    {
    	int i, j, c;

    	//Count number of each channel intensity values
        for (j=0; j<h; j++) {
            for (i=0; i<w; i++) {
                    for (c=0; c<3; c++) {
                    		histogram[data[c+3*i+3*j*w]&255][c]++;
                    }
            }
        }
		
        //Print out number of values for each channel and intensity
        for(int u = 0; u<256; u++)
        {
        	greyChannelValues[u] = (double)(histogram[u][0]+histogram[u][1]+histogram[u][2])/3;
        	System.out.println(u+" r="+histogram[u][2]+
        			" g="+histogram[u][1]+
        			" b="+histogram[u][0]);
        }

    }
    
    public void findMinMaxValues()
    {
    	for(int i = 0; i < 256; i++)
    	{
    		if(greyChannelValues[i] > greyMax)
    		{
    			greyMax = (int)greyChannelValues[i];
    		}
    	}
    	
    	for(int r = 0; r < 256; r++)
    	{
    		if(histogram[r][2] > redMax)
    		{
    			redMax = histogram[r][2];
    		}
    	}
    	
    	for(int g = 0; g < 256; g++)
    	{
    		if(histogram[g][1] > greenMax)
    		{
    			greenMax = histogram[g][1];
    		}
    	}
    	
    	for(int b = 0; b < 256; b++)
    	{
    		if(histogram[b][0] > blueMax)
    		{
    			blueMax = histogram[b][0];
    		}
    	}
    }
    
    public double[] getGreyValues()
    {
    	return greyChannelValues;
    }
    
    public double getGreyValue(int i)
    {
    	return greyChannelValues[i];
    }
    
    
    @Override
    public void paintComponent(Graphics g)
    {
    	//Setup chart background
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setColor(Color.BLACK);
    	g2.drawString("0", leftSpace-10,  this.getHeight() -(bottomSpace-10));
    	g2.drawString("255", this.getWidth()-rightSpace, this.getHeight() -(bottomSpace-10));
    	g2.setColor(Color.WHITE);
        g2.fillRect(leftSpace-1, topSpace,1+ this.getWidth() - (leftSpace + rightSpace), this.getHeight() - (bottomSpace + topSpace));
        g2.setColor(Color.BLACK);
        g2.drawRect(leftSpace-1, topSpace,1+ this.getWidth()-(leftSpace + rightSpace), this.getHeight() - (bottomSpace + topSpace));
        g2.setStroke(new BasicStroke(3));
        
        //Create barcharts
        for(int i = 0; i < 256; i++)
        {
        	if(activeChannel == 0)
        	{
        		g2.setColor(Color.RED);
        		g2.drawLine(leftSpace + (i*2), topSpace+510, leftSpace + (i*2), (topSpace+510)- (int)(((double)histogram[i][2]/redMax) * 510));
        		g2.setColor(Color.BLACK);
        		String s1 = String.valueOf(redMax);
        		g2.drawString(s1, leftSpace-45, topSpace);
        		String s2 = String.valueOf((int)redMax/2);
        		g2.drawString(s2, leftSpace-45, 255 + topSpace);
        	}
        	else if(activeChannel == 1)
        	{
        		g2.setColor(Color.GREEN);
        		g2.drawLine(leftSpace + (i*2), topSpace+510, leftSpace + (i*2), (topSpace+510)- (int)(((double)histogram[i][1]/greenMax) * 510));
        		g2.setColor(Color.BLACK);
        		String s1 = String.valueOf(greenMax);
        		g2.drawString(s1, leftSpace-45, topSpace);
        		String s2 = String.valueOf((int)greenMax/2);
        		g2.drawString(s2, leftSpace-45, 255 + topSpace);
        	}
        	else if(activeChannel == 2)
        	{
        		g2.setColor(Color.BLUE);
        		g2.drawLine(leftSpace + (i*2), topSpace+510, leftSpace + (i*2), (topSpace+510)- (int)(((double)histogram[i][0]/blueMax) * 510));
        		g2.setColor(Color.BLACK);
        		String s1 = String.valueOf(blueMax);
        		g2.drawString(s1, leftSpace-45, topSpace);
        		String s2 = String.valueOf((int)blueMax/2);
        		g2.drawString(s2, leftSpace-45, 255 + topSpace);
        	}
        	else if(activeChannel == 3)
        	{
        		g2.setColor(Color.GRAY);
        		g2.drawLine(leftSpace + (i*2), topSpace+510, leftSpace + (i*2), (topSpace+510)- (int)(((double)greyChannelValues[i]/greyMax) * 510));
        		g2.setColor(Color.BLACK);
        		String s1 = String.valueOf(greyMax);
        		g2.drawString(s1, leftSpace-45, topSpace);
        		String s2 = String.valueOf((int)greyMax/2);
        		g2.drawString(s2, leftSpace-45, 255 + topSpace);
        	}
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == redButton)
		{
			activeChannel = 0;
			repaint();
		}
		if(e.getSource() == greenButton)
		{
			activeChannel = 1;
			repaint();
		}
		if(e.getSource() == blueButton)
		{
			activeChannel = 2;
			repaint();
		}
		if(e.getSource() == greyButton)
		{
			activeChannel = 3;
			repaint();
		}
	}
}
