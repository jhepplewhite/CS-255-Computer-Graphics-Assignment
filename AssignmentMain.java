import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.Math.*;
//Jonathan Hepplewhite - 827328
//"This work is my own, individual effort."

public class AssignmentMain extends JFrame 
{
    JButton invert_button, slow_gamma_button, fast_gamma_button, correlate_button, equal_button, contrast_stretch_button;
    JLabel image_icon;
    BufferedImage image;
	double gamma;
	
	int[] contrastStretchValues = new int[4];
    int r1;
    int s1;
    int r2;
    int s2;

    /*
        This function sets up the GUI and reads an image
    */
    public void Example() throws IOException 
    {
        File image_file = new File("raytrace.jpg");
        
        // Open the file and read it into a BufferedImage
        image = ImageIO.read(image_file);

        JPanel p1 = new JPanel ();
        JPanel p2 = new JPanel ();
        
        // Set up the simple GUI
        // First the container:
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        
        // Then our image (as a label icon)
        image_icon=new JLabel(new ImageIcon(image));
        p1.add(image_icon);
 
        // Then the invert button
        invert_button = new JButton("Invert");
        p2.add(invert_button);

        // Then the equalize button
        equal_button = new JButton("Equalize");
        p2.add(equal_button);
        
        // Gamma button
        slow_gamma_button = new JButton("Slow Gamma");
        p2.add(slow_gamma_button);
        fast_gamma_button = new JButton("Fast Gamma");
        p2.add(fast_gamma_button);
        
        // Correlate button
        correlate_button = new JButton("Correlate");
        p2.add(correlate_button);
        
        //Contrast stretching button
        contrast_stretch_button = new JButton("Contrast Stretch");
        p2.add(contrast_stretch_button);
        		
		container.add(p1, BorderLayout.NORTH);
		container.add(p2, BorderLayout.SOUTH);
        // Now all the handlers
        GUIEventHandler handler = new GUIEventHandler();

        // Button handlers
        invert_button.addActionListener(handler);
        slow_gamma_button.addActionListener(handler);
        fast_gamma_button.addActionListener(handler);
        correlate_button.addActionListener(handler);
        equal_button.addActionListener(handler);
        contrast_stretch_button.addActionListener(handler);
        // Display everything
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public double getGammaValue()
    {
    	return gamma;
    }
    
    public Boolean setGammaValue()
    {
    	try
    	{
    		gamma = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter a value between 0.1 and 10"));
    		if(gamma < 0.1)
    		{
    			gamma = 0.1;
    		}
    		if(gamma > 10)
    		{
    			gamma = 10;
    		}
    		return true;
    	}
    	catch(NullPointerException e)
    	{
    		return false;
    	}
    	catch(NumberFormatException e2)
    	{
    		System.out.println("Please try again");
    		setGammaValue();
    	}
		return false;
    }
    
    
    public int getR1()
    {
    	return r1;
    }
    
    public int getS1()
    {
    	return s1;
    }
    
    public int getR2()
    {
    	return r2;
    }
    
    public int getS2()
    {
    	return s2;
    }
    
    public void setR1(int r1)
    {
    	this.r1 = r1;
    }
    
    public void setS1(int s1)
    {
    	this.s1 = s1;
    }
    
    public void setR2(int r2)
    {
    	this.r2 = r2;
    }
    
    public void setS2(int s2)
    {
    	this.s2 = s2;
    }
    
    public boolean ContrastStretchingInput()
    {  	
    	ContrastStretchGraph g = new ContrastStretchGraph(585,585);
    	g.setPreferredSize(new Dimension(585,585));

    	try
    	{
    		int result = JOptionPane.showConfirmDialog(null, g, "Contrast Stretching", JOptionPane.OK_CANCEL_OPTION);
    		if(result == JOptionPane.OK_OPTION)
        	{
    			contrastStretchValues = g.getContrastStretchValues();
        		setR1(contrastStretchValues[0]);
        		setS1(contrastStretchValues[1]);
        		setR2(contrastStretchValues[2]);
        		setS2(contrastStretchValues[3]);
        		return true;
        	}
        	else
        	{
        		return false;
        	}
    	}
    	catch(NumberFormatException e2)
    	{
    		System.out.println("Input Invalid. Please try again");
    		ContrastStretchingInput();
    	}
    	return false;
    	
    }
    
    /*
        This is the event handler for the application
    */
    private class GUIEventHandler implements ActionListener 
    {
         public void actionPerformed(ActionEvent event) 
         {
             if (event.getSource()==invert_button) 
             {
                // Call image processing function
                image=Invert(image);

                // Update image
                image_icon.setIcon(new ImageIcon(image));
             } 
             else if (event.getSource()==slow_gamma_button) 
             {
                // Call image processing function
                image=SlowGamma(image);

                // Update image
                image_icon.setIcon(new ImageIcon(image));
             }
             else if (event.getSource()==fast_gamma_button)
             {
                // Call image processing function
                image=FastGamma(image);

                // Update image
                image_icon.setIcon(new ImageIcon(image));
             } 
             else if (event.getSource()==correlate_button)
             {
                // Call image processing function
                image=CrossCorrelation(image);

                // Update image
                image_icon.setIcon(new ImageIcon(image));
             } 
             else if (event.getSource()==equal_button) 
             {
				//Call function
				image=Equalise(image);
				
                // Update image
                image_icon.setIcon(new ImageIcon(image));
			}
            else if(event.getSource()==contrast_stretch_button)
            {
	        	 //Call function
	        	 image = ContrastStretching(image);
	        	 //Update image
	        	 image_icon.setIcon(new ImageIcon(image));
             }
         }
    }

    /*
        This function will return a pointer to an array
        of bytes which represent the image data in memory.
        Using such a pointer allows fast access to the image
        data for processing (rather than getting/setting
        individual pixels)
    */
    public static byte[] GetImageData(BufferedImage image) 
    {
        WritableRaster WR=image.getRaster();
        DataBuffer DB=WR.getDataBuffer();
        if (DB.getDataType() != DataBuffer.TYPE_BYTE)
            throw new IllegalStateException("That's not of type byte");
      
        return ((DataBufferByte) DB).getData();
    }


    public BufferedImage Equalise(BufferedImage image) 
    {
        //Get image dimensions, and declare loop variables
        int w=image.getWidth(), h=image.getHeight(), i, j, c;
        //Obtain pointer to data for fast processing
        byte[] data = GetImageData(image);
        
        Histogram hg = new Histogram(w,h,data);
        hg.setPreferredSize(new Dimension(585,585));
        Object[] options = {"Cancel", "Equalise"};
        int result = JOptionPane.showOptionDialog(null, hg, "Histogram Equalisation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[1]);
        
        if(result == 1)
        {
        	int[] histogram, mapping;
            int g_levels = 256;
            histogram = new int[g_levels];
            mapping = new int[g_levels];

        	//make image grey
            for(int k = 0; k<(h*w)*3; k=k+3)
            {
            	int avg = (((data[k]&255)+(data[k+1]&255)+(data[k+2]&255))/3);
              	data[k] = (byte) avg;
              	data[k+1] = (byte) avg;
              	data[k+2] = (byte) avg;
            }
            
            //Find number of pixel with values between 0-255 in grey image
            for (j=0; j<h; j++)
            {
                for (i=0; i<w; i++) 
                {
                    for (c=0; c<1; c++) 
                    {
                    	histogram[data[c+3*i+3*j*w]&255]++;
                    }
                }
            }
            
            int[] t = new int[256];
                 
            //Find the cumulative distribution and apply the mapping
            for(i = 0; i<g_levels; i++)
            {
            	if(i == 0)
            	{
            		t[i] += histogram[i];
            	}
            	else
            	{
            		t[i] += t[i-1] +  histogram[i];
            	}
            	//Create the new mapping
            	mapping[i]= Math.max(0,Math.round((int)(double)(g_levels*((double)(t[i])/(double)(h*w))))-1);
            }
            
            //Apply the new mapping
	        for (j=0; j<h; j++)
	        {
	            for (i=0; i<w; i++) 
	            {
					for (c=0; c<3; c++) 
					{
						data[c+3*i+3*j*w] = (byte) mapping[(data[c+3*i+3*j*w]&255)];
					} // colour loop
	            } // column loop
	        } // row loop
        }
		
        return image;
    }
    

    /*
        This function shows how to carry out an operation on an image.
        It obtains the dimensions of the image, and then loops through
        the image carrying out the invert.
    */
    public BufferedImage Invert(BufferedImage image) 
    {
        //Get image dimensions, and declare loop variables
        int w=image.getWidth(), h=image.getHeight(), i, j, c;
        //Obtain pointer to data for fast processing
        byte[] data = GetImageData(image);
        
        //Shows how to loop through each pixel and colour
        //Try to always use j for loops in y, and i for loops in x
        //as this makes the code more readable
        for (j=0; j<h; j++) 
        {
            for (i=0; i<w; i++) 
            {
                for (c=0; c<3; c++) 
                {
                	data[c+3*i+3*j*w] = (byte) (255-(data[c+3*i+3*j*w]&255));
                } // colour loop
            } // column loop
        } // row loop
        return image;
    }

    public BufferedImage SlowGamma(BufferedImage image) 
    {
        //Get image dimensions, and declare loop variables
        int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);
		double pixel;
		
		Boolean isGammaChanged = setGammaValue();

		//If ok button clicked
		if(isGammaChanged)
		{
			for (j=0; j<h; j++) 
			{
				for (i=0; i<w; i++) 
				{
		        	for (c=0; c<3; c++) 
		        	{
		        		pixel = data[c+3*i+3*j*w] &255;
		        		pixel = pixel/255;
		        		pixel = Math.pow(pixel, 1/this.getGammaValue());
		        		pixel = pixel *255;
		        		data[c+3*i+3*j*w] = (byte) pixel;
		        	} // colour loop
		    	} // column loop
			} // row loop
		}
		return image;
    }
	
    public BufferedImage FastGamma(BufferedImage image) 
    {
            //Get image dimensions, and declare loop variables
            int w=image.getWidth(), h=image.getHeight(), i, j, c;
			byte[] data = GetImageData(image);
            //Obtain pointer to data for fast processing
			
			Boolean isGammaChanged = setGammaValue();
			
			//If a valid value is entered and ok button pressed
			if(isGammaChanged)
			{
				double[] gammaValues = new double[256];
				//Create a lookup table for gamma values
				for(int p = 0; p < 256; p++)
				{
					gammaValues[p] =  (p/255.0);
					gammaValues[p] =  Math.pow(gammaValues[p], 1/this.getGammaValue());
					gammaValues[p] = (gammaValues[p]*255);			
				}
				
				for (j=0; j<h; j++) 
				{
            		for (i=0; i<w; i++) 
            		{
                        for (c=0; c<3; c++) 
                        {
                    		data[c+3*i+3*j*w] = (byte) gammaValues[(data[c+3*i+3*j*w]&255)];
                    	} // colour loop
                	} // column loop
				} // row loop
			}
            return image;
    }
    
    public BufferedImage CrossCorrelation(BufferedImage image)
    {
        //Get image dimensions, and declare loop variables
        int w=image.getWidth(), h=image.getHeight(), i, j, c;
        //Obtain pointer to data for fast processing
        byte[] data = GetImageData(image);
        int[][][] int_image;
        //Used to store new values as they are calculated so other values not affected
        int[][][] int_image_copy = new int[h][w][3];
        
        int_image = new int[h][w][3]; 
        
        // Copy byte data to new image taking care to treat bytes as unsigned
        for (j=0; j<h; j++)
        {
            for (i=0; i<w; i++) 
            {
                for (c=0; c<3; c++) 
                {
                    int_image[j][i][c]=data[c+3*i+3*j*w]&255;
                    int_image_copy[j][i][c]=data[c+3*i+3*j*w]&255;
                } // colour loop
            } // column loop
        } // row loop
        
        int min = 0;
        int max = 0;

        //Calculate cross correlated values given a Laplacian matrix
        for (j=0; j<h; j++) 
        {
        	for (i=0; i<w; i++)
        	{
                if(j>2&&j<(h-3)&&i>2&&i<(w-3))
                {       
                	//Perform cross correlation on the blue channel
	                int_image_copy[j][i][0]=(int_image[j-2][i-2][0] * -4) + (int_image[j-2][i-1][0] * -1) +(int_image[j-2][i+1][0] * -1) + (int_image[j-2][i+2][0] * -4)
	                        + (int_image[j-1][i-2][0] * -1) + (int_image[j-1][i-1][0] * 2) +  (int_image[j-1][i][0] * 3) +(int_image[j-1][i+1][0] * 2) + (int_image[j-1][i+2][0] * -1)
	                        +  (int_image[j][i-1][0] * 3) +  (int_image[j][i][0] * 4) +(int_image[j][i+1][0] * 3)
	                        + (int_image[j+1][i-2][0] * -1) + (int_image[j+1][i-1][0] * 2) +  (int_image[j+1][i][0] * 3) +(int_image[j+1][i+1][0] * 2) + (int_image[j+1][i+2][0] * -1)
	                        + (int_image[j+2][i-2][0] * -4) + (int_image[j+2][i-1][0] * -1) +(int_image[j+2][i+1][0] * -1) + (int_image[j+2][i+2][0] * -4); //BLUE
	
	              //Perform cross correlation on the green channel
	                int_image_copy[j][i][1]=(int_image[j-2][i-2][1] * -4) + (int_image[j-2][i-1][1] * -1) +(int_image[j-2][i+1][1] * -1) + (int_image[j-2][i+2][1] * -4)
	                        + (int_image[j-1][i-2][1] * -1) + (int_image[j-1][i-1][1] * 2) +  (int_image[j-1][i][1] * 3) +(int_image[j-1][i+1][1] * 2) + (int_image[j-1][i+2][1] * -1)
	                        +  (int_image[j][i-1][1] * 3) +  (int_image[j][i][1] * 4) +(int_image[j][i+1][1] * 3)
	                        + (int_image[j+1][i-2][1] * -1) + (int_image[j+1][i-1][1] * 2) +  (int_image[j+1][i][1] * 3) +(int_image[j+1][i+1][1] * 2) + (int_image[j+1][i+2][1] * -1)
	                        + (int_image[j+2][i-2][1] * -4) + (int_image[j+2][i-1][1] * -1) +(int_image[j+2][i+1][1] * -1) + (int_image[j+2][i+2][1] * -4);; //GREEN
	                
	                //Perform cross correlation on the red channel
	                int_image_copy[j][i][2]=(int_image[j-2][i-2][2] * -4) + (int_image[j-2][i-1][2] * -1) +(int_image[j-2][i+1][2] * -1) + (int_image[j-2][i+2][2] * -4)
	                        + (int_image[j-1][i-2][2] * -1) + (int_image[j-1][i-1][2] * 2) +  (int_image[j-1][i][2] * 3) +(int_image[j-1][i+1][2] * 2) + (int_image[j-1][i+2][2] * -1)
	                        +  (int_image[j][i-1][2] * 3) +  (int_image[j][i][2] * 4) +(int_image[j][i+1][2] * 3)
	                        + (int_image[j+1][i-2][2] * -1) + (int_image[j+1][i-1][2] * 2) +  (int_image[j+1][i][2] * 3) +(int_image[j+1][i+1][2] * 2) + (int_image[j+1][i+2][2] * -1)
	                        + (int_image[j+2][i-2][2] * -4) + (int_image[j+2][i-1][2] * -1) +(int_image[j+2][i+1][2] * -1) + (int_image[j+2][i+2][2] * -4);; //RED

                        //Find the min and max values after after applying filter
	                    if(min > int_image_copy[j][i][2])
	                    {
	                    	min = int_image_copy[j][i][2];
	                    }
	                    if(min > int_image_copy[j][i][1])
	                    {
	                    	min = int_image_copy[j][i][1];
	                    }
	                    if(min > int_image_copy[j][i][0])
	                    {
	                    	min = int_image_copy[j][i][0];
	                    }
	                    if(max < int_image_copy[j][i][2])
	                    {
	                    	max = int_image_copy[j][i][2];
	                    }
	                    if(max < int_image_copy[j][i][1])
	                    {
	                    	max = int_image_copy[j][i][1];
	                    }
	                    if(max < int_image_copy[j][i][0])
	                    {
	                    	max = int_image_copy[j][i][0];
	                    }
	                        
                	}
                	else //make outer border black
                	{
                		int_image_copy[j][i][0]=0;
                		int_image_copy[j][i][1]=0;
                		int_image_copy[j][i][2]=0;
                	}
                } // column loop
        } // row loop

        //Normalise values
        for (j=0; j<h; j++) 
        {
            for (i=0; i<w; i++) 
            {
            	if(j>2&&j<(h-3)&&i>2&&i<(w-3))
            	{
            		int_image_copy[j][i][2] = (((int_image_copy[j][i][2]-min)*255)/(max-min))&255;
            		int_image_copy[j][i][1] = (((int_image_copy[j][i][1]-min)*255)/(max-min))&255;
            		int_image_copy[j][i][0] = (((int_image_copy[j][i][0]-min)*255)/(max-min))&255;
            	}
            } // column loop
        } // row loop
        
        // Now copy the processed image back to the original
        for (j=0; j<h; j++) 
        {
                for (i=0; i<w; i++) {
                        for (c=0; c<3; c++) {
                                data[c+3*i+3*j*w]=(byte) int_image_copy[j][i][c];
                        } // colour loop
                } // column loop
        } // row loop
        
        return image;
}

    public BufferedImage ContrastStretching(BufferedImage image) {
        //Get image dimensions, and declare loop variables
        int w=image.getWidth(), h=image.getHeight(), i, j, c;
        //Obtain pointer to data for fast processing
        byte[] data = GetImageData(image);

        Boolean isContrastStretched = ContrastStretchingInput();
        
        //If user has selected to perform contrast stretching
        if(isContrastStretched)
        {
			for (j=0; j<h; j++) 
			{
	    		for (i=0; i<w; i++) 
	    		{
                	for (c=0; c<3; c++) 
                	{
                		if((data[c+3*i+3*j*w]&255) < getR1())
                		{
                			data[c+3*i+3*j*w] = (byte) ((data[c+3*i+3*j*w]&255) * ((double)getS1()/(double)getR1()));
                		}
                		else if(getR1()<= (data[c+3*i+3*j*w]&255) && (data[c+3*i+3*j*w]&255)<=getR2())
                		{
                			data[c+3*i+3*j*w] = (byte) (((double)(getS2()-getS1())/(double)(getR2()-getR1())) * ((data[c+3*i+3*j*w]&255)-getR1()) + getS1());
                		}
                		else if ((data[c+3*i+3*j*w]&255) >= getR2())
                		{
                			data[c+3*i+3*j*w] = (byte) (((double)(255-getS2())/(double)(255-getR2())) * ((data[c+3*i+3*j*w]&255) - getR2()) + getS2());
                		}
                		
                	} // colour loop
	        	} // column loop
			} // row loop
        }
        
        return image;
}

    public static void main(String[] args) throws IOException {
 
       AssignmentMain a = new AssignmentMain();
       a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       a.Example();
    }
}