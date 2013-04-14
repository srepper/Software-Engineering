package cdengine;

import gui.mainWindow;

import java.io.IOException;
import java.nio.file.*;

import javax.swing.JOptionPane;

public class FileCompressor {
	private byte[] fileArray;
	private byte[] output;
	private int[] count;
	private int aryIndex;
	private int originalSize;
	private int percent;
	private HuffmanTree huffTree;
	private Path file;
	private Path outFile;
	private PrioQ prioQ;
	private String[] codes;
	private StringBuilder sb;
	
	
	public FileCompressor(String s)
	{
		file = Paths.get(s);
		s = s.substring(0, s.lastIndexOf('.'));
		outFile = Paths.get(s + ".fcd");
		sb = new StringBuilder();
		output = new byte[1000];
		count = new int[50];
	}
	
	public byte[] compressFile()
	{
		/***********************************************************************
		 * Convert file to byte array
		 **********************************************************************/
		try {
			fileArray = Files.readAllBytes(file);
		} catch (IOException e) {
			fileReadError();
		}
		
		originalSize = fileArray.length;
		file = null;
		
		/***********************************************************************
		 * Add each byte count to the count array
		 **********************************************************************/
		for(int i = 0; i < fileArray.length; i++)
			addToCount(fileArray[i] & 0x0FF);
		
		if(!verifyCount(fileArray.length))
		{
			fileMatchError();
			return null;
		}
		
		makePrioQ();
		huffTree = new HuffmanTree(prioQ);
		huffTree.buildTree();
		codes = huffTree.makeCodes(count);
		
		percent = 1;
		aryIndex = 0;
		for(int i = 0; i < fileArray.length; i++)
		{
			reportPercent(i);
			buildOutputString(codes.length / 2, i);
			if(sb.length() >= 8)
			{
				addOutputByte();
				sb = new StringBuilder(sb.substring(8));
			}
		}
		
		if(sb.length() > 0)
			addOutputByte();
		
		byte[] b = new byte[1];
		b = fromBinary(sb.toString());
		output[aryIndex++] = b[0];
		/*
		try {
			Files.write(outFile, output);
		} catch (IOException e) {
			fileWriteError();
			return false;
		}
		*/
		return output;
	}
	
	/***************************************************************************
	 * Count the occurrence of a specific byte in the count array
	 **************************************************************************/
	private void addToCount(int b)
	{
		if(b > count.length-1)
		{
			int[] temp = count;
			count = new int[b+1];
			System.arraycopy(temp, 0, count, 0, temp.length);
		}
		
		count[b] += 1;
	}
	
	/***************************************************************************
	 * Convert 8 binary characters to one byte for output array
	 **************************************************************************/
	private void addOutputByte()
	{
		if(aryIndex >= output.length)
		{
			byte[] temp = output;
			output = new byte[temp.length * 2];
			System.arraycopy(temp, 0, output, 0, temp.length);
		}
		
		byte[] b = new byte[1];
		//if(sb.length() < 8)
		b = (sb.length() < 8? 
				fromBinary(sb.toString().substring(0)): 
				fromBinary(sb.toString().substring(0,8)));
		output[aryIndex++] = b[0];	
	}
	
	/***************************************************************************
	 * Build binary string output from Huffman codes
	 **************************************************************************/
	private void buildOutputString(int index, int i)
	{
		int step;
		if((step = index / 2) < 1)
			step = 1;
		while(true)
		{
			if((fileArray[i] & 0x0FF) == index)
			{
				sb.append(codes[index]);
				break;
			}
			else if((fileArray[i] & 0x0FF) < index)
			{
				index -= step;
				if(step > 1)
					step /= 2;
			}
			else if((fileArray[i] & 0x0FF) > index)
			{
				index += step;
				if(step > 1)
					step /= 2;
			}
		}
	}

	/***************************************************************************
	 * Error when fileArray length and number of codes don't match
	 **************************************************************************/
	private void fileMatchError()
	{
		JOptionPane.showMessageDialog(null, "File conversion error.",  
				"Results", JOptionPane.ERROR_MESSAGE);
	}
		
	/***************************************************************************
	 * Error when file cannot be read to fileArray
	 **************************************************************************/
	private void fileReadError()
	{
		JOptionPane.showMessageDialog(null, "Failed to read input file.",  
				"Results", JOptionPane.ERROR_MESSAGE);
	}
	
	/***************************************************************************
	 * Creates the local priority queue
	 **************************************************************************/
	private void makePrioQ()
	{
		prioQ = new PrioQ();
		
		for(int i = 0; i < count.length; i++)
		{
			if(count[i] == 0)
				continue;
			Node node = new Node((byte)(i & 0x0FF), count[i]);
			prioQ.insert(node);
		}
	}
	
	/***************************************************************************
	 * Creates a byte array from binary string input
	 **************************************************************************/
	private byte[] fromBinary(String s)
	{
		int sLen = s.length();
		byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
		char c;
		for( int i = 0; i < sLen; i++ )
			if( (c = s.charAt(i)) == '1' )
				toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
			else if ( c != '0' )
				throw new IllegalArgumentException();
		return toReturn;
	}
	
	/***************************************************************************
	 * Update progress bar on UI?
	 **************************************************************************/
	private void reportPercent(int i)
	{
		if(i == fileArray.length / 100 * percent)
		{
			mainWindow.setProgress(percent);
			//TODO:  Interface with progress bar
			percent++;
		}
	}
	
	/***************************************************************************
	 * Total the count & verify against fileArray length
	 **************************************************************************/
	private boolean verifyCount(int fileLength)
	{
		int total = 0;
		for(int i = 0; i < count.length; i++)
			total += count[i];
		
		return (total == fileLength? true : false);
	}
	
}
