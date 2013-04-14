import java.util.Vector;
import java.io.*;
import java.nio.file.*;

import javax.swing.JOptionPane;
public class FilePartitioner {

	static byte[] fileArray;
	static Vector<byte[]> outputArray;
	static Vector<Path> outputFiles;
	
	//need file manager
	static boolean partitionFile(Path  file)
	{
		/***********************************************************************
		 * Convert file to byte array
		 **********************************************************************/
		try {
			fileArray = Files.readAllBytes(file);
		} catch (IOException e) {
			fileReadError();
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
