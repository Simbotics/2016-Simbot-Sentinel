package org.simbotics.robot.io;

import java.io.*;

public class Mouse {
	
	public Mouse(String args[])
	{
		String MOUSE_STREAM = "/dev/input/mice";
		BufferedInputStream bis;
		
		try
		{
			bis = new BufferedInputStream(new FileInputStream(new File (MOUSE_STREAM)));
			
			while (true)
			{
			try
			{
			int input = bis.read();
			if (input != -1)
			System.out.println("" + input);
			}
			catch (IOException iox)
			{
			iox.printStackTrace();
			}
			}
			}
			catch (FileNotFoundException fnfx)
			{
			fnfx.printStackTrace();
		}
	}
}