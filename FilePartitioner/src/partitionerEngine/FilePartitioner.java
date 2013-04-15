package partitionerEngine;

import java.util.Vector;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import javax.swing.JOptionPane;
public class FilePartitioner {

	public static void main(String[] args) throws Exception
	{
//		partitionFile(Paths.get("project3.pdf"),10);
		Vector<Path>fileArray = new Vector<Path>(0);
		for(int i = 9; i >= 0; i --)
		{
			fileArray.add(Paths.get("project3.pdf" + i +".part"));
		}
		FileDePartitioner.departitionFile(fileArray);
	}
		static byte[] fileArray;
		static Vector<byte[]> outputArray;
		static Vector<Path> outputFiles;
	
	//need file manager
	static boolean partitionFile(Path  file, int numPartitions) throws IOException
	{
		outputFiles = new Vector<Path>(0);
		/***********************************************************************
		 * Convert file to byte array
		 **********************************************************************/
		try {
			fileArray = Files.readAllBytes(file);
		} catch (IOException e) {
			fileReadError();
		}
		
//		System.out.println("filearray length: " + fileArray.length);
	
		outputArray = new Vector<byte[]>(numPartitions);
		
		//get all partitions
		for(int i = 0; i < numPartitions; i ++)
		{
			byte[] temp = new byte[0];
			temp = new byte[((i==numPartitions-1)?fileArray.length:(1+i)*(fileArray.length/numPartitions)) - i*(fileArray.length/numPartitions)];
			//
//			System.out.println("for( j = " + i*(fileArray.length/numPartitions)+ "; j < " + ((i==numPartitions-1)?fileArray.length:(i+1)*(fileArray.length/numPartitions)) + "; j++)");
			for(int j = i*(fileArray.length/numPartitions); j < ((i==numPartitions-1)?fileArray.length:(i+1)*(fileArray.length/numPartitions)); j++)
			{
				temp[j-i*(fileArray.length/numPartitions)] = fileArray[j];
			}
			//add to outputArray
			outputArray.add(temp);
		}

		//add 2 bytes to 1st one, 1 byte for sequence nuber,and 1 byte for number of numbers.
		//add 1 byte to the rest
		
		
		byte[] tempbyte = new byte[outputArray.get(0).length+2];
		tempbyte[0] = Byte.valueOf(0+"", 10);//numPartitions + "");
		tempbyte[1] = Byte.valueOf(numPartitions+ "", 10);
		for(int i = 2; i < outputArray.get(0).length+2; i ++)
		{
			tempbyte[i] = outputArray.get(0)[i-2];
		}
		outputArray.remove(0);
		outputArray.add(0,tempbyte);
		
		//1byte sequence number added
		for(int i = 1; i < numPartitions; i ++)
		{
			tempbyte = new byte[outputArray.get(i).length+1];
			tempbyte[0] = Byte.valueOf(i+"", 10);
			for(int j = 1; j < outputArray.get(i).length+1; j ++)
			{
				tempbyte[j] = outputArray.get(i)[j-1];
			}
			outputArray.remove(i);
			outputArray.add(i,tempbyte);
		}
		
		//write to file
		for(int i = 0; i < numPartitions; i ++)
		{
			String fileName = file.toString();
			String filePrefix;
			Pattern p = Pattern.compile("(.*)\\.(.*)");
			Matcher m = p.matcher(fileName);
			if(m.matches()){
				filePrefix = m.group(1);
				String filePostFix = m.group(2);
				outputFiles.add(Paths.get(filePrefix + "." +  filePostFix + i+ ".part"));
				Files.write(outputFiles.get(i), outputArray.get(i));
			}
		}
		
//		Path testFile = Paths.get("test1.txt0.part");
//		byte[] testByte = Files.readAllBytes(testFile);
//		System.out.println("testByte[0]: " + testByte[0] + ". testByte[1]: " + testByte[1]);
		
		
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
