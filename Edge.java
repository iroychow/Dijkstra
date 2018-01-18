import java.util.Comparator;

/*author Ishita*/

public class Edge implements Comparable<Edge>{
	
	private Vertex destinationVertex;
	private String weight;
	
	public Edge(Vertex vertex, String weight){
		this.destinationVertex=vertex;
		this.weight=weight;
		
	}
	
	public Vertex getDestinationVertex(){
		return destinationVertex;
	}
	
	public String getWeight(){
		return weight;
	}
	
	public void setWeight(String weight){
		this.weight=weight;
	}
	
	public void setDestinationVertex(Vertex destinationVertex){
		this.destinationVertex =  destinationVertex;
	}
	
	
	public static Comparator<Edge> edgeComparator
		    = new Comparator<Edge>() {
		
		public int compare(Edge edge1, Edge edge2) {
		return edge1.getDestinationVertex().getName().compareTo(edge2.getDestinationVertex().getName());
		}
		
	};

	@Override
	public int compareTo(Edge o) {
		return 0;
	}
	
	
	
}
