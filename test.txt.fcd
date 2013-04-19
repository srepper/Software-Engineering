import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.Vector;

public class Test
{
	static int[] count = new int[50];
	static Vector<Node> prioQ;
	static String[] codes;
	static Path file = Paths.get("song.mp3");
	static Path outFile = Paths.get("song.fcd");
	static Path restoredFile = Paths.get("song2.mp3");
	static int originalSize;
	
	public static void main(String[] args) throws Exception
	{
		/**************************************************
		 * Convert file to byte array
		 *************************************************/
		byte[] fileArray;
		fileArray = Files.readAllBytes(file);
		originalSize = fileArray.length;
		file = null;
		
		
		/**********************************************************************
		 * Count each byte in the count[] array
		 *********************************************************************/
		for(int i = 0; i < fileArray.length; i++)
			addToCount(fileArray[i] & 0x0FF);
		
		/**********************************************************************
		 * Total the count & verify against fileArray length
		 *********************************************************************/
		int total = 0;
		for(int i = 0; i < count.length; i++)
		{
			total += count[i];
		}
		if(total != fileArray.length)
			System.out.println("Somethin' ain't right here, yo.");
		
		/**********************************************************************
		 * Create & fill priority queue with Nodes
		 *********************************************************************/
		prioQ = new Vector<Node>();
		for(int i = 0; i < count.length; i++)
		{
			if(count[i] == 0)
				continue;
			Node node = new Node((byte)(i & 0x0FF), count[i]);
			pInsert(node);
		}
		
		/*for(int i = 0; i < prioQ.size(); i++)     // Check byte occurrence counts
		{
			System.out.println(prioQ.get(i).getCount());
		}*/
		
		/**********************************************************************
		 * Combine smallest nodes by count until root remains; set tree root
		 *********************************************************************/
		Node node = null;
		while(prioQ.size() > 1)
		{
			node = new Node(prioQ.get(0).getCount() + 
					prioQ.get(1).getCount());
			node.setLeftChild(prioQ.get(0));
			prioQ.remove(0);
			node.setRightChild(prioQ.get(0));
			prioQ.remove(0);
			pInsert(node);
		}
		Node tree = node;
		
		codes = makeCode(tree);
		prioQ = null;
		
		outputFile(fileArray);
	}
	
	public static void outputFile(byte[] fileArray) throws Exception
	{
		StringBuilder sb = new StringBuilder(/*fileArray.length * Byte.SIZE*/);
		byte[] output = new byte[1000];
		int aryIndex = 0;
		int step;
		
		int percent = 1;
		for(int i = 0; i < fileArray.length; i++)
		{
			if(i == fileArray.length / 100 * percent)
			{
				System.out.println("Compression " + percent + " percent complete.");
				percent++;
			}
			/*if(i % 100000 == 0)
				System.out.println("Not frozen.");*/
			
			int index = codes.length / 2;
			if((step = index/2) < 1)
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
			
			if(sb.length() >= 8)
			{
				if(aryIndex >= output.length)
				{
					byte[] temp = output;
					output = new byte[temp.length * 2];
					System.arraycopy(temp, 0, output, 0, temp.length);
				}
				/*String s = sb.toString();
				int asdf = Integer.parseInt(s);
				output[aryIndex++] = (byte)asdf;
				*/
				byte[] b = new byte[1];
				b = fromBinary(sb.toString().substring(0,8));
				output[aryIndex++] = b[0];
				sb = new StringBuilder(sb.substring(8));
			}
		}
		
		if(sb.length() > 0)
		{
			byte[] b = new byte[1];
			b = fromBinary(sb.toString());
			output[aryIndex++] = b[0];
		}
		
		byte[] temp = output; 
		output = new byte[aryIndex];
		System.arraycopy(temp, 0, output, 0, output.length);
		
		Files.write(outFile, output);
		
		output = null;
				
		byte[] compressedFile;
		compressedFile = Files.readAllBytes(outFile);
		
		decompress(compressedFile);
	}
	
	public static void decompress(byte[] compressedFile) throws Exception
	{
		prioQ = new Vector<Node>();		
		StringBuilder fileString = new StringBuilder();
		byte[] decompressedFile = new byte[originalSize];
		
		sortCodes();
		
		byte[] b = new byte[1];
		int i = 0;
		int index = 0;
		int percent = 1;
		while(i < compressedFile.length)
		{
			if(fileString.length() < 16 && i < compressedFile.length)
			{
				b[0] = compressedFile[i++];
				fileString.append(toBinary(b));
				continue;
			}
			
			for(int x = 0; x < prioQ.size(); x++)
			{
				if(fileString.toString().startsWith(prioQ.get(x).getCode()))
				{
					decompressedFile[index++] = (byte)prioQ.get(x).getData(); 
					fileString = new StringBuilder(fileString.substring(prioQ.get(x).getCode().length()));
					break;
				}
			}
			
			if(decompressedFile.length / 100 * percent == index)
			{
				System.out.println("Decompression " + percent + " percent complete.");
				percent++;
			}
			
			
		}
			
		/*
		int i = 0;
		boolean exit = false;
		//for(int i = 0; i < fileString.length(); i++)//(!fileString.isEmpty())
		while(fileString.length() != 0)//fileString.isEmpty())
		{
			if(fileString.length() <= prioQ.get(0).getCode().length())
				exit = true;
			//temp += fileString.charAt(i);
			
			for(int x = 0; x < prioQ.size(); x++)
			{
				if(fileString.toString().startsWith(prioQ.get(x).getCode()))
					//codes[x] != null && fileString.substring(i).startsWith(codes[x]))
						//temp.contentEquals(codes[x]))
				{
					byte[] me = new byte[1];
					me[0] = (byte)prioQ.get(x).getData();//x; 
					sb.append(toBinary(me));
					//decompressString += toBinary(me);
					//temp = "";
					//fileString = fileString.substring(codes[x].length());
					fileString = new StringBuilder(fileString.substring(prioQ.get(x).getCode().length()));
					//fileString = fileString.substring(prioQ.get(x).getCode().length());//codes[x].length() - 1;
					break;
				}
			}
			
			//i++;
			if(i++ % 1000 == 0)
				System.out.println(i);
			
			if(exit)
				break;
		}
				
		decompressString = sb.toString();
		byte[] decompressedFile = fromBinary(sb.toString());
		*/
		
		Files.write(restoredFile, decompressedFile);
	}
	
	public static void pInsert(Node n)
	{
		if(n.getCount() == 0)
			return;
		
		int step;
		int x = prioQ.size() / 2;
		
		if((step = x/2) < 1)
			step = 1;
		
		while(true)
		{
			if(prioQ.isEmpty())
			{
				prioQ.add(n);
				break;
			}
			else if(x == 0 && n.getCount() < prioQ.get(x).getCount())
			{
				prioQ.add(0, n);
				break;
			}
			else if(n.getCount() == prioQ.get(x).getCount() || 
			  (n.getCount() < prioQ.get(x).getCount() && n.getCount() >= prioQ.get(x-1).getCount()))
			{
				prioQ.add(x, n);
				break;
			}
			else if(n.getCount() > prioQ.get(x).getCount() && x < prioQ.size()-1)
			{
				x += step;
				if(step > 1)
					step /= 2;
			}
			else if(n.getCount() < prioQ.get(x).getCount())
			{
				x -= step;
				if(step > 1)
					step /= 2;
			}
			else
			{
				prioQ.add(n);
				break;
			}
				
		}
	}
	
	public static String[] makeCode(Node root)
	{
		if(root == null)
			return null;
		String[] code = new String[count.length];
		addCode(root, code);
		return code;
	}
	
	public static Vector<Node> sortCodes()
	{
		Vector<Node> sorted = new Vector<Node>();
		for(int i = 0; i < codes.length; i++)
		{
			if(codes[i] != null)
			{
				Node n = new Node((byte)(i & 0x0FF), codes[i].length(), codes[i]);
				pInsert(n);
			}
		}
		return sorted;
	}
	
	public static void addCode(Node root, String[] code)
	{
		if(root.getLeftChild() != null)
		{
			root.getLeftChild().setCode(root.getCode() + "0");
			addCode(root.getLeftChild(), code);
			
			root.getRightChild().setCode(root.getCode() + "1");
			addCode(root.getRightChild(), code);
		}
		else
			code[(int)root.getData() & 0x0FF] = root.getCode();
	}
	
	static void addToCount(int b)
	{
		if(b > count.length-1)
		{
			int[] temp = count;
			count = new int[b+1];
			System.arraycopy(temp, 0, count, 0, temp.length);
		}
		
		
		count[b] += 1;
	}
	
	static String toBinary( byte[] bytes )
	{
 		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
			sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
		return sb.toString();
	}
		
	static byte[] fromBinary(String s)
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
}

class Node
{
	Node leftChild = null;
	Node rightChild = null;
	byte data;
	int count;
	String code = "";
	
	public Node(int c)
	{
		count = c;
	}
	
	public Node(byte b, int c)
	{
		data = b;
		count = c;
	}
	
	public Node(byte b, int c, String s)
	{
		data = b;
		count = c;
		code = s;
	}
	
	public void setLeftChild(Node n)
	{
		leftChild = n;
	}
	
	public void setRightChild(Node n)
	{
		rightChild = n;
	}
	
	public void setCode(String s)
	{
		code = code + s;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public Node getLeftChild()
	{
		return leftChild;
	}
	
	public Node getRightChild()
	{
		return rightChild;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public byte getData()
	{
		return data;
	}
}