package cdengine;

import gui.mainWindow;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JOptionPane;

public class FileCompressor {
	private byte[] fileArray;
	private byte[] output;
	private int[] count;
	private int aryIndex;
	private int originalSize;
	private int percent;
	private HuffmanTree huffTree;
	private Path outFile;
	private PrioQ prioQ;
	private String[] codes;
	private StringBuilder sb;
	
	
	public FileCompressor(byte[] file)
	{
		fileArray = file;
		output = new byte[1000];
		count = new int[50];
		originalSize = fileArray.length;
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
		
		sb = new StringBuilder();
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
		
		byte[] temp = output; 
		output = new byte[aryIndex];
		System.arraycopy(temp, 0, output, 0, output.length);
		
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
	
	private String[] makeCanonicalCodes(PrioQ p)
	{
		String[] cCodes = new String[p.size()];
		
		String s = "";
		while(s.length() < p.get(0).getCode().length())
		{
			s += "0";
		}
		cCodes[0] = s;
		
		int n = 1;
		for(int i = 1; i < p.size(); i++)
		{
			
		}
		
		return cCodes;
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
	 * Update progress bar on UI?
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
	 * Update progress bar on UI?
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
	
		/*
		PrioQ pq = new PrioQ();
		for(int i = 0; i < codes.length; i++)
		{
			if(codes[i] != null)
			{
				Node n = new Node(i, codes[i].length(), codes[i]);
				pq.insert(n);
			}
		}
		
		for(int i = 0; i < pq.size() - 1; i++)
		{
			int x = i;
			while(x < pq.size() - 1 && 
					pq.get(x).getCount() == pq.get(x + 1).getCount() && 
					pq.get(x).getData() > pq.get(x + 1).getData())
			{
				Node n = new Node(pq.get(x).getData(), pq.get(x).getCount(),
						pq.get(x).getCode());
				pq.remove(x);
				pq.insert(x + 1, n);
				x++;
			}
		}
		int aslk = 0;
		*/
		/*
		int[][] sorted = new int[pq.size()][2];
		for(int i = 0; i < pq.size(); i++)	
		{
			sorted[i][0] = pq.get(i).getData();
			sorted[i][1] = Integer.parseInt(pq.get(i).getCode(), 2);
		}*/
		//return sorted;
	
	
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
