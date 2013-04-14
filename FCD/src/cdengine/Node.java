package cdengine;

public class Node
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