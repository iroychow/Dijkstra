/*author Ishita*/

import java.util.*;

//import Graph.Vertex;

public class Vertex implements Comparable<Vertex>{

	
	private String name;
	private ArrayList<Edge> edges;
	
	//variables used for dijkstra algorithm
	private float distance = Float.MAX_VALUE;
	private Vertex previousVertex = null;
	private boolean visited = false;

	public Vertex(String name){
		this.name=name;
		edges=new ArrayList<Edge>();
	}
	
	public void addEdge(Vertex vertex, String weight){
		Edge newEdge=new Edge(vertex, weight);
		edges.add(newEdge);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public ArrayList<Edge> getEdges(){
		return edges;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Vertex getPreviousVertex() {
		return previousVertex;
	}

	public void setPreviousVertex(Vertex previousVertex) {
		this.previousVertex = previousVertex;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int compareTo(Vertex targetVertex)
	{
		if (distance == targetVertex.distance)
			return name.compareTo(targetVertex.name);
 
		return Float.compare(distance, targetVertex.distance);
	}
	
	
}
