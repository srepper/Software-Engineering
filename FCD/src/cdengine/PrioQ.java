package cdengine;

import java.util.Vector;

public class PrioQ {
	private Vector<Node> prioQ;
	
	public PrioQ()
	{
		prioQ = new Vector<Node>();
	}
	
	public void insert(Node n)
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
	
	public Node get(int i)
	{
		return prioQ.get(i);
	}
	
	public void remove(int i)
	{
		prioQ.remove(i);
	}
	
	public int size()
	{
		return prioQ.size();
	}
}
