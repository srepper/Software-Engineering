package partitionerEngine;

import java.util.Vector;
import java.io.*;
import java.nio.file.*;

import javax.swing.JOptionPane;
public class FilePartitioner {

	public static void main(String[] args) throws IOException
	{
		partitionFile(Paths.get("test1.txt"),5);
	}
public	static byte[] fileArray;
public	static Vector<byte[]> outputArray;
public	static Vector<Path> outputFiles;
	
	//need file manager
	static boolean partitionFile(Path  file, int numPartitions) throws IOException
	{
//		byte[] fileArray = new byte[0];
//			 Vector<byte[]> outputArray;
//			 Vector<Path> outputFiles;
		/***********************************************************************
		 * Convert file to byte array
		 **********************************************************************/
		try {
			fileArray = Files.readAllBytes(file);
		} catch (IOException e) {
			fileReadError();
		}
		
		System.out.println("filearray length: " + fileArray.length);
	
		outputArray = new Vector<byte[]>(numPartitions);
		
		//get all partitions
		for(int i = 0; i < numPartitions; i ++)
		{System.out.println("i: " + i);
			byte[] temp = new byte[0];
			temp = new byte[((i==numPartitions-1)?fileArray.length:(1+i)*(fileArray.length/numPartitions)) - i*(fileArray.length/numPartitions)];
			//
			System.out.println("for( j = " + i*(fileArray.length/numPartitions)+ "; j < " + ((i==numPartitions-1)?fileArray.length:(i+1)*(fileArray.length/numPartitions)) + "; j++)");
			for(int j = i*(fileArray.length/numPartitions); j < ((i==numPartitions-1)?fileArray.length:(i+1)*(fileArray.length/numPartitions)); j++)
			{
				temp[j-i*(fileArray.length/numPartitions)] = fileArray[j];
			}
			//add to outputArray
			outputArray.add(temp);
		}

		
		
		for(int i = 0; i < numPartitions; i ++)
		{
			Path outfile = Paths.get(file.toString() + i);
			Files.write(outfile, outputArray.get(i));
		}
	
		return true;
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
