import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.Vector;

public class Test
{
	static int[] count = new int[50];
	static Vector<Node> prioQ;
	static String[] codes;
	static Path file = Paths.get("shape.exe");
	static Path outFile = Paths.get("shape.fcd");
	static Path restoredFile = Paths.get("shape2.exe");
	
	public static void main(String[] args) throws Exception
	{
		/**************************************************
		 * Convert file to byte array
		 *************************************************/
		//Path file = Paths.get("song.mp3");
		byte[] fileArray;
		fileArray = Files.readAllBytes(file);
		
		/*
		Path file2 = Paths.get("interfaces2.png");
		byte[] buf = fileArray;
		Files.write(file2, buf);
		*/
		
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
			/*
			for(int j = 0; j <= prioQ.size(); j++)
			{
				if(prioQ.isEmpty() || 
				  (j < prioQ.size() && node.getCount() < prioQ.get(j).getCount()))
				{
					prioQ.add(j, node);
					break;
				}
				else if(j == prioQ.size())
				{
					prioQ.add(node);
					break;
				}
			}
			*/
		}
		
		for(int i = 0; i < prioQ.size(); i++)
		{
			System.out.println(prioQ.get(i).getCount());
		}
		
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
		
		outputFile(fileArray);
	}
	
	public static void outputFile(byte[] fileArray) throws Exception
	{
		StringBuilder sb = new StringBuilder(fileArray.length * Byte.SIZE);
		String outputString = "";
		String oStr = "";
		int step;
		
		for(int i = 0; i < fileArray.length; i++)
		{
			/*if(i % 8 == 0)
			{  
				if(output.length > 1)
				{
					byte[] temp = output;
					byte[] fb = fromBinary(outputString);
					output = new byte[temp.length + fb.length];
					System.arraycopy(temp, 0, output, 0, temp.length);
					System.arraycopy(fb, 0, output, temp.length, fb.length);
				}
				else
					output = fromBinary(outputString);
				
				outputString = "";
			}*/
			if(i % 10000 == 0)
				System.out.println(i);
			if(i % 100000 == 0)
				System.out.println("Not frozen.");
			
			int index = codes.length / 2;
			if((step = index/2) < 1)
				step = 1;
			
			while(true)
			{
				if((fileArray[i] & 0x0FF) == index)
				{
					sb.append(codes[index]);
					//outputString += codes[index];
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
		
		/*byte[] temp = output;
		byte[] fb = fromBinary(outputString);
		output = new byte[temp.length + fb.length];
		System.arraycopy(temp, 0, output, 0, temp.length);
		System.arraycopy(fb, 0, output, temp.length, fb.length);*/
		
		/*
		byte[] output = fromBinary(outputString.get(0));
		for(int i = 1; i < outputString.size(); i++)
		{
				byte[] temp = output;
				byte[] fb = fromBinary(outputString.get(i));
				output = new byte[temp.length + fb.length];
				System.arraycopy(temp, 0, output, 0, temp.length);
				System.arraycopy(fb, 0, output, temp.length, fb.length);
		}
		*/
		
		byte[] output = fromBinary(sb.toString());
		Files.write(outFile, output);
		
		/*temp = null;
		fb = null;*/
		output = null;
		outputString = null;
		
		byte[] compressedFile;
		compressedFile = Files.readAllBytes(outFile);
		
		decompress(compressedFile);
	}
	
	public static void decompress(byte[] compressedFile) throws Exception
	{
		String decompressString = "";
		String temp = "";
		String fileString = toBinary(compressedFile);
		StringBuilder sb = new StringBuilder(compressedFile.length);
		//byte[] decompressedFile = new byte[1];
		for(int i = 0; i < fileString.length(); i++)
		{
			temp += fileString.charAt(i);
			
			for(int x = 0; x < codes.length; x++)
			{
				if(temp.contentEquals(codes[x]))
				{
					byte[] me = new byte[1];
					me[0] = (byte)x; 
					sb.append(toBinary(me));
					//decompressString += toBinary(me);
					temp = "";
				}
			}
			
			/*
			if(decompressString.length() % 800 == 0)
			{  
				if(decompressedFile.length > 1)
				{
					byte[] outTemp = decompressedFile;
					byte[] fb = fromBinary(decompressString);
					decompressedFile = new byte[outTemp.length + fb.length];
					System.arraycopy(outTemp, 0, decompressedFile, 0, outTemp.length);
					System.arraycopy(fb, 0, decompressedFile, outTemp.length, fb.length);
				}
				else
					decompressedFile = fromBinary(decompressString);
				
				decompressString = "";
			}
			*/
			if(i % 500000 == 0)
				System.out.println(i);
		}
		
		/*byte[] outTemp = decompressedFile;
		byte[] fb = fromBinary(decompressString);
		decompressedFile = new byte[outTemp.length + fb.length];
		System.arraycopy(outTemp, 0, decompressedFile, 0, outTemp.length);
		System.arraycopy(fb, 0, decompressedFile, outTemp.length, fb.length);
		*/
		
		//byte[] decompressedFile = fromBinary(decompressString);
		
		/*byte[] decompressedFile = fromBinary(decompressString.get(0));
		for(int i = 1; i < decompressString.size(); i++)
		{
			byte[] outTemp = decompressedFile;
			byte[] fb = fromBinary(decompressString.get(i));
			decompressedFile = new byte[outTemp.length + fb.length];
			System.arraycopy(outTemp, 0, decompressedFile, 0, outTemp.length);
			System.arraycopy(fb, 0, decompressedFile, outTemp.length, fb.length);
		}*/
		
		//decompressString = sb.toString();
		byte[] decompressedFile = fromBinary(sb.toString());
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
	
	/*
	static String binaryToString(byte b)
	{
		String binaryString = "";
		byte[] ref = new byte[]{(byte)0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
		for(byte z = 0; z < 8; z++)
		{
			if((ref[z] & b) != 0)
				binaryString += "1";
			else
				binaryString += "0";
		}
		
		return binaryString;
	}
	*/
	
	/*
	static int insert(char[] c, byte b)
	{
		for(int i = 0; i < c.length; i++)
		{
			if(c[i] == 0)
			{
				c[i] = (char) b;
				return i;
			}
			if(c[i] == b)
				return i;				
		}
		return -1;
	}
	*/
	
	
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
		
	
	/*
	static String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
	*/
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
