public class Heap
{
    private Vertex[] theHeap;
    private int position;
    private int size;
 
    public Heap(int size)
    {
        this.size = size;
        this.position = -1;
        theHeap = new Vertex[this.size + 1];
        //theHeap[0] = Integer.MIN_VALUE;
    }
 
    private int parentIndex(int position)
    {
        return position / 2;
    }
 
    private int leftChildIndex(int position)
    {
        return (2 * position);
    }
 
    private int rightChildIndex(int position)
    {
        return (2 * position) + 1;
    }
 
    private boolean isLeaf(int position)
    {
        if (position >=  (position / 2)  &&  position <= position)
        { 
            return true;
        }
        return false;
    }
 
    private void swap(int firstPosition, int secondPosition)
    {
        Vertex temp;
        temp = theHeap[firstPosition];
        theHeap[firstPosition] = theHeap[secondPosition];
        theHeap[secondPosition] = temp;
    }
 
    private void minHeapify(int pos)
    {
        if (!isLeaf(pos))
        { 

        	Boolean leftPos= theHeap[pos].compareTo(theHeap[leftChildIndex(pos)] ) > 0 ? true : false; 
        	Boolean rightPos= theHeap[pos].compareTo(theHeap[rightChildIndex(pos)] ) > 0 ? true : false; 
        	Boolean leftRightPos = theHeap[leftChildIndex(pos)].compareTo(theHeap[rightChildIndex(pos)]) < 0 ? true :false;
            if (   leftPos || rightPos)
            {
                if (leftRightPos)
                {
                    swap(pos, leftChildIndex(pos));
                    minHeapify(leftChildIndex(pos));
                }else
                {
                    swap(pos, rightChildIndex(pos));
                    minHeapify(rightChildIndex(pos));
                }
            }
        }
    }
 
    public void insert(Vertex element)
    {
        theHeap[++position] = element;
        int current = position;
        Boolean currentPos= true; 
    	
        while (currentPos)
        {
            swap(current,parentIndex(current));
            current = parentIndex(current);
           currentPos =  theHeap[current].compareTo(theHeap[parentIndex(current)] ) < 0 ? true : false;
        }	
    }
 
   
 
    public void minHeap()
    {
        for (int pos = (position / 2); pos >= 1 ; pos--)
        {
            minHeapify(pos);
        }
    }
 
    public Vertex extractMin()
    {
        Vertex minValue = theHeap[1];
        theHeap[1] = theHeap[position--]; 
        minHeapify(0);
        return minValue;
    }
 
    void moveUp(int pos) {
        while (pos > 0) {
            int parent = (pos - 1) >> 1;
            if (theHeap[pos].compareTo(theHeap[parent]) > 0) {
                break;
            }
            swap(pos, parent);
            pos = parent;
        }
    }

    void add(int id, Vertex vertex) {
        theHeap[size] = vertex;
        moveUp(size++);
    }

    void increasePriority(int pos, Vertex vertex) {
        theHeap[pos] = vertex;
        moveUp(pos);
    }
}