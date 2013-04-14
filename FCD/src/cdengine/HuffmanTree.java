package cdengine;

public class HuffmanTree
{
	private Node root;
	private PrioQ prioQ;
	
	public HuffmanTree(PrioQ p)
	{
		prioQ = p;
	}
	
	
	public void buildTree()
	{
		Node node = null;
		PrioQ p = prioQ;
		while(p.size() > 1)
		{
			node = new Node(p.get(0).getCount() + 
				p.get(1).getCount());
			node.setLeftChild(p.get(0));
			p.remove(0);
			node.setRightChild(p.get(0));
			p.remove(0);
			p.insert(node);
		}
		
		root = node;
	}
	
	public String[] makeCodes(int[] count)
	{
		if(root == null)
			return null;
		String codes[] = new String[count.length];
		addCode(root, codes);
		
		return codes;
	}
	
	private static void addCode(Node root, String[] code)
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
}
