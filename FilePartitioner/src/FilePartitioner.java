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
		
		System.out.println("filearrya length: " + fileArray.length);
	
		outputArray = new Vector<byte[]>(numPartitions);
		
		//get all partitions
		for(int i = 0; i < numPartitions; i ++)
		{System.out.println("i: " + i);
			byte[] temp = new byte[0];
			temp = new byte[(1+i)*(fileArray.length/numPartitions) - i*(fileArray.length/numPartitions)];
			//
			for(int j = i*(fileArray.length/numPartitions); j < (i+1)*(fileArray.length/numPartitions); j++)
			{
				temp[j-i*(fileArray.length/numPartitions)] = fileArray[i*(fileArray.length/numPartitions) + j];
			}
			//add to outputArray
			outputArray.add(temp);
		}
		
//		
//		byte[] temp = new byte[fileArray.length/2];
//		for(int i = 0; i < fileArray.length/2; i ++)
//			temp[i] = fileArray[i];
//		outputArray.add(temp);
//		temp = new byte[(fileArray.length) - (fileArray.length/2)];
//		for(int i = fileArray.length/2+1; i < fileArray.length-1; i ++)
//			temp[i-fileArray.length/2-1] = fileArray[i];
//		outputArray.add(temp);
		for(int i = 0; i < numPartitions; i ++)
		{
			Path outfile = Paths.get(file.toString() + i);
			Files.write(outfile, outputArray.get(i));
		}
//		Path files = Paths.get(file.toString() + "1");
//		Files.write(files, outputArray.get(0));
//		
//
//		files = Paths.get(file.toString() + "2");
//		Files.write(files, outputArray.get(1));
//		
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
