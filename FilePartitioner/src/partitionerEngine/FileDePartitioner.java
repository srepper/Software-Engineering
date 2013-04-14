package partitionerEngine;
import java.util.Vector;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import javax.swing.JOptionPane;


public class FileDePartitioner {
	static Vector<Path> fileArray;
	static Vector<byte[]> fileArrayByte;
	static byte[] outputArray;
	static Path outputFile;
	
	static boolean departitionFile(Vector<Path>fileArray)
	{
		for(int i = 0; i < fileArray.size(); i++)
		{
			try {
				fileArrayByte.add(Files.readAllBytes(fileArray.get(i)));
			} catch (IOException e) {
				fileReadError();
			}
		}
		
		
		for(int i = 0 ; i < fileArrayByte.size(); i ++)
		{
			if(fileArrayByte.get(i)[0] == 1)
			{
				
			}
		}
		
		
		return false;
	}
	/***************************************************************************
	 * Error when file cannot be read to fileArray
	 **************************************************************************/
	private static void fileReadError()
	{
		JOptionPane.showMessageDialog(null, "Failed to read input file.",  
				"Results", JOptionPane.ERROR_MESSAGE);
	}

}
