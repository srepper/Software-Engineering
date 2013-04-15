package cdengine;

import gui.mainWindow;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class FileDecompressor {
	private byte[] fileArray;
	private byte[] output;
	private int[] canonLengths;
	private int aryIndex;
	private int outIndex;
	private int percent;
	private PrioQ prioQ;
	private String[] codes;
	private String fileExtension;
	private StringBuilder sb;
	private boolean exit;

	public FileDecompressor(byte[] file)
	{
		fileArray = file;
		output = new byte[1000];
		canonLengths = new int[256];
	}
	
	public byte[] decompressFile()
	{
		splitHuffmanData();
		makePrioQ();
		prioQ = new PrioQ(sortCodes());
		recoverCodes(prioQ);
		
		sb = new StringBuilder();
		percent = 1;
		aryIndex = 0;
		outIndex = 0;
		
		while(aryIndex < fileArray.length)
		{
			reportPercent(aryIndex);
			buildOutput();
			
		}
		
		exit = false;
		while(!exit)
		{
			buildOutput();
		}
		
		byte[] temp = output; 
		output = new byte[outIndex];
		System.arraycopy(temp, 0, output, 0, output.length);
		
		return output;
	}
	
	public String getFileExtension()
	{
		return fileExtension;
	}
	
	/***************************************************************************
	 * Build binary string output from Huffman codes
	 **************************************************************************/
	private void buildOutput()
	{
		if(outIndex >= output.length)
		{
			byte[] temp = output;
			output = new byte[temp.length * 2];
			System.arraycopy(temp, 0, output, 0, temp.length);
		}
		
		if(sb.length() < 16 && aryIndex < fileArray.length)
		{
			byte[] b = new byte[1];
			b[0] = fileArray[aryIndex++];
			sb.append(toBinary(b));
			return;
		}
		
		for(int i = 0; i < prioQ.size(); i++)
		{
			if(sb.toString().startsWith(prioQ.get(i).getCode()))
			{
				output[outIndex++] = (byte)prioQ.get(i).getByteData();
				sb = new StringBuilder(
						sb.substring(prioQ.get(i).getCode().length()));
				return;
			}
		}
		exit = true;
	}
	
	/***************************************************************************
	 * Creates the local priority queue
	 **************************************************************************/
	private void makePrioQ()
	{
		prioQ = new PrioQ();
		
		for(int i = 0; i < canonLengths.length; i++)
		{
			if(canonLengths[i] == 0)
				continue;
			Node node = new Node(i, canonLengths[i]);
			prioQ.insert(node);
		}
	}
	
	/***************************************************************************
	 * Create canonical Huffman codes from the dynamic codes
	 **************************************************************************/
	private void recoverCodes(PrioQ p)
	{
		codes = new String[256];
		
		String s = "";
		while(s.length() < p.get(0).getCount())
		{
			s += "0";
		}
		p.get(0).setCanCode(s);
		codes[p.get(0).getData()] = s;
		
		int n = 0;
		for(int i = 1; i < p.size(); i++)
		{
			n++;
			while(Integer.toBinaryString(n).length() < 
					p.get(i).getCount())
			{
				n = n << 1;
			}
			
			p.get(i).setCanCode(Integer.toBinaryString(n));
			codes[p.get(i).getData()] = Integer.toBinaryString(n);
		}
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
	 * Sort the canonical Huffman code lengths
	 **************************************************************************/
	private Vector<Node> sortCodes()
	{
		Vector<Node> nodes = new Vector<Node>();
		for(int i = 0; i < prioQ.size(); i++)
		{
			Node n = new Node(prioQ.get(i).getData(),
					prioQ.get(i).getCount());
			nodes.add(n);
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
	 * Sort the canonical Huffman code lengths
	 **************************************************************************/
	private void splitHuffmanData()
	{
		byte[] ext = new byte[fileArray[256]];
		int index = 0;
		for(int i = 0; i < canonLengths.length; i++)
		{
			canonLengths[i] = (int)(fileArray[i] & 0x0FF);
			index++;
		}
		index++;
		
		for(int i = 0; i < ext.length; i++)
			ext[i] = fileArray[index++];
		
		fileExtension = new String(ext);
		byte[] temp = fileArray;
		fileArray = new byte[
		            temp.length - canonLengths.length - (ext.length + 1)];
		
		System.arraycopy(temp, index, fileArray, 0, fileArray.length);
	}
	
	/***************************************************************************
	 * Creates a binary string from byte array input
	 **************************************************************************/
	private String toBinary( byte[] bytes )
	{
 		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
			sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
		return sb.toString();
	}
}
