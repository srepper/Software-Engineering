package cdengine;

import gui.mainWindow;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JOptionPane;

public class FileCompressor {
	private byte[] fileArray;
	private byte[] output;
	private byte[] canonLengths;
	private byte[] fileExtension;
	private int[] count;
	private int aryIndex;
	private int percent;
	private HuffmanTree huffTree;
	private PrioQ prioQ;
	private String[] codes;
	private StringBuilder sb;
	
	public FileCompressor(byte[] file)
	{
		fileArray = file;
		output = new byte[1000];
		count = new int[50];
		canonLengths = new byte[256];
	}
	
	public byte[] compressFile()
	{	
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
		prioQ = new PrioQ(sortCodes(codes));
		makeCanonicalCodes(prioQ);
		appendCanonCodeLengths();
		
		sb = new StringBuilder();
		percent = 1;
		
		reportPercent(0);
		for(int i = 0; i < fileArray.length; i++)
		{
			reportPercent(i);
			buildOutput(codes.length / 2, i);
			if(sb.length() >= 8)
			{
				addOutputByte();
				sb = new StringBuilder(sb.substring(8));
			}
		}
		
		if(sb.length() > 0)
			addOutputByte();
		
		byte[] temp = output; 
		output = new byte[aryIndex];
		System.arraycopy(temp, 0, output, 0, output.length);
		
		return output;
	}
	
	/***************************************************************************
	 * Sets the appropriate file extension in bytes
	 **************************************************************************/
	public void setFileExtension(String fileName)
	{
		String ext = fileName.substring(fileName.lastIndexOf('.'));
		
		try {
			fileExtension = ext.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null,
					"Failed to set file extension.",  
					"Results", JOptionPane.ERROR_MESSAGE);
		}
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
		
		b = (sb.length() < 8? 
				fromBinary(sb.toString().substring(0)): 
				fromBinary(sb.toString().substring(0,8)));
		output[aryIndex++] = b[0];	
	}
	
	/***************************************************************************
	 * Calculate length for each code and put the codes on the output file
	 **************************************************************************/
	private void appendCanonCodeLengths()
	{
		int index = 0;
		for(int i = 0; i <  canonLengths.length; i++)
		{
			if(codes[i] == null)
				canonLengths[i] = 0;
			else
				canonLengths[i] = (byte)(codes[i].length());
			
			output[i] = canonLengths[i];
			index++;
		}
		
		if(fileExtension != null)
		{
			output[index++] = (byte)fileExtension.length;
			for(int i = 0; i < fileExtension.length; i++)
			{
				output[index++] = fileExtension[i];
			}
		}
		else
			output[index++] = (byte)0;
		
		aryIndex = index;
	}
	
	/***************************************************************************
	 * Build binary string output from Huffman codes
	 **************************************************************************/
	private void buildOutput(int index, int i)
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
	 * Create canonical Huffman codes from the dynamic codes
	 **************************************************************************/
	private void makeCanonicalCodes(PrioQ p)
	{
		String s = "0";
		while(s.length() < p.get(0).getCode().length())
		{
			s += "0";
		}
		
		p.get(0).setCanCode(s);
		codes[p.get(0).getData()] = s;
		
		int n = 1;
		boolean x = false;
		for(int i = 1; i < p.size(); i++)
		{
			while(Integer.toBinaryString(n).length() < 
					p.get(i).getCode().length())
			{
				n = n << 1;
			}
			
			p.get(i).setCanCode(Integer.toBinaryString(n));
			codes[p.get(i).getData()] = Integer.toBinaryString(n);
			
			/*s = Integer.toBinaryString(n+1);
			if(!s.contains("0"))//(Integer.parseInt(s, 2) < 0)
				n = n << 1; //TODO: Modified
			else*/
				n++;
		}
		
		String out = "";
		File file = new File("stringsFileCompressor.txt");
		try{
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
		for(int i = 0; i < p.size(); i++)
		{
			String num = Integer.toBinaryString(p.get(i).getData());
			while(num.length() < 8)
				num = "0" + num;
			out += num + "\t  :" + 
				p.get(i).getCode() + "\n";
			bw.write(out);
			bw.newLine();
			out = "";
		}
	
		bw.close();
		}catch(Exception exc){}
		
		int asldkf = 5;
		asldkf = asldkf + 5;
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
			Node node = new Node(i, count[i]);
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
	 * Update progress bar on UI
	 **************************************************************************/
	private void reportPercent(int i)
	{
		if(i == fileArray.length / 100 * percent)
		{
			mainWindow.setProgress(percent);
			percent++;
		}
	}
	
	/***************************************************************************
	 * Sort the dynamic Huffman codes
	 **************************************************************************/
	private Vector<Node> sortCodes(String[] s)
	{
		Vector<Node> nodes = new Vector<Node>();
		for(int i = 0; i < s.length; i++)
		{
			if(s[i] != null)
			{
				Node n = new Node(i, s[i].length(), s[i]);
				nodes.add(n);
			}
		}
	
		Collections.sort(nodes, new Comparator() {
	        public int compare(Object o1, Object o2) {
	            Integer x1 = ((Node) o1).getCount();
	            Integer x2 = ((Node) o2).getCount();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } else {
	               Integer a1 = ((Node) o1).getData();
	               Integer a2 = ((Node) o2).getData();
	               return a1.compareTo(a2);
	            }
	        }
	    });
		
		return nodes;
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
