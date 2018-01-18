import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;



/*author Ishita*/

public class Graph {

	public static Map<String, Vertex> uniqueVertexCollector=new TreeMap<>();
	static String inputFileName ="";
	static String inputString="";
	static String queryFileName="";
	static String outputFileName="";
	Heap heap = null;

	public static void main(String[] args){

		Graph g = new Graph();
		if(args== null){
			System.out.println("argument cannot be null");
			return;
		}
		else if(args.length <3){
			System.out.println("Invalid number of arguments passed");
			return;
		}
		else
		{
			 inputFileName= args[0];
			 queryFileName=args[1];
			 outputFileName=args[2];

			 //Delete the output file from earlier run
			 File file= new File (outputFileName);
			 if(file.exists() && !file.isDirectory()) {
					file.delete();
			}


		}
		inputString=g.printFileAndCreateGraph(inputFileName);
		g.readqueryfile(queryFileName);

	}


	public String printFileAndCreateGraph(String fileName)
	{

        String line = null;
        String[] splitValues;
        try {
        	URL url = Graph.class.getClassLoader().getResource(fileName);
    		File file = new File(url.getPath());
    	    FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
			    splitValues=line.split(" ");
			    populateUniqueMap(splitValues);

			}
			bufferedReader.close();

        }
        catch(Exception ex) {
            System.out.println( "Error in reading file data");
        }

        return line;
	}

	public void readqueryfile(String fileName) {
        String line = null;
        String[] splitValues;
        try {

        	//read input file
        	URL url = Graph.class.getClassLoader().getResource(fileName);
    		File file = new File(url.getPath());
    	    FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
			    splitValues=line.split(" ");
			    if(splitValues[0] !=null){
			    	 switch (splitValues[0].toLowerCase()) {
						case "addedge":
							addEdgeToGraph(splitValues[1], splitValues[2], splitValues[3]);
							break;
						case "deleteedge":
							deleteEdgeFromGraph(splitValues[1], splitValues[2]);
							break;
						case "edgedown":
							edgeDown(splitValues[1], splitValues[2]);
							break;
						case "edgeup":
							edgeUp(splitValues[1], splitValues[2]);
							break;
						case "path":
							runDijkstra(splitValues[1], splitValues[2]);
							break;
						case "print":
							printGraph();
							break;
						case "vertexdown":
							vertexDown(splitValues[1]);
							break;
						case "vertexup":
							vertexUp(splitValues[1]);
							break;
						case "reachable":
							reachable();
							break;
						case "quit":
							System.exit(0);
							break;
						default:
							Utility.writeToFile(outputFileName, "Incorrect query provided "+splitValues[0]);
							break;
						}
			    }



			}
			bufferedReader.close();

        }
        catch(Exception ex) {
            System.out.println( "Error in running program"+ ex.getMessage());
            ex.printStackTrace();
        }


	}

	Map<String,ArrayList<String>> reachableMatrix = new TreeMap<>();

	@SuppressWarnings("unchecked")
	public void reachable(){

		Iterator iterator = uniqueVertexCollector.entrySet().iterator();
		 while (iterator.hasNext()) {

			 for(String key:uniqueVertexCollector.keySet()){
					Vertex v = uniqueVertexCollector.get(key);
					v.setVisited(false);
				}

			 Map.Entry pair = (Map.Entry)iterator.next();
			 String key = pair.getKey().toString();
		        if(key.contains("DOWN"))
		        	continue;
		        else{
		        	reachableMatrix.put(key,new ArrayList<String>());
		        	//Utility.writeToFile(outputFileName, key);
		        	((Vertex)pair.getValue()).setVisited(true);
		        	ArrayList<Edge> edges = ((Vertex)pair.getValue()).getEdges();
		        	 edges.sort(Edge.edgeComparator);
		        	reachable(edges,key);
		        }
		 }


		 Iterator reachableIterator = reachableMatrix.entrySet().iterator();
		 while (reachableIterator.hasNext()) {
			 Map.Entry pair = (Map.Entry)reachableIterator.next();
			 Utility.writeToFile(outputFileName, pair.getKey().toString());
			 ArrayList<String> items = (ArrayList<String>)pair.getValue();
			 Collections.sort(items);
			 for(String str :items){
				 Utility.writeToFile(outputFileName, "  "+str);
			 }
		 }
	}

	private void reachable( ArrayList<Edge> edgeList,String key){
		 for(Edge eachEdge :edgeList){
        	 if(!(eachEdge.getWeight().contains("DOWN") ||
        			 eachEdge.getDestinationVertex().getName().contains("DOWN"))
        			 &&  eachEdge.getDestinationVertex().isVisited() == false){
        			 reachableMatrix.get(key).add(eachEdge.getDestinationVertex().getName());
        		// Utility.writeToFile(outputFileName, "  "+eachEdge.getDestinationVertex().getName());
        		 eachEdge.getDestinationVertex().setVisited(true);
        		 reachable(eachEdge.getDestinationVertex().getEdges(),key);
        	 }
        }
	}

	public void runDijkstra(String sourceVertexName,String targetVertexName){
		String output ="";
		Float weightFromNeighbour = 0F;
		ArrayList<String> visited = new ArrayList<>();
		//get the vertex element from unique map
		if(!uniqueVertexCollector.containsKey(targetVertexName)){
			Utility.writeToFile(outputFileName, "Either invalid vertex provided or target vertex is down. Target vertex name is = "+targetVertexName);
			return;
		}
		if(uniqueVertexCollector.containsKey(sourceVertexName)){
			//source exist - get the entire vertex for traversal
			for(String key:uniqueVertexCollector.keySet()){
				Vertex v = uniqueVertexCollector.get(key);
				v.setDistance(Float.MAX_VALUE);
				v.setVisited(false);
			}
			Vertex traversalVertex = uniqueVertexCollector.get(sourceVertexName);

			NavigableSet<Vertex> queue = new TreeSet<Vertex>();

			//initialize the heap
        //	heap = new Heap(uniqueVertexCollector.size());

			//add to heap
			for(Vertex eachVertex : uniqueVertexCollector.values()){
				if(traversalVertex.compareTo(eachVertex) == 0){
					traversalVertex.setDistance(0);
					// heap.add(counter,traversalVertex);
					// visited.add(traversalVertex.getName());
				}
			//	 heap.add(eachVertex);
				queue.add(eachVertex);
			}

			while(!queue.isEmpty()){
				Vertex smallestVertex = queue.pollFirst(); //heap.remove(); ////
				for(Edge eachNeighbour : smallestVertex.getEdges()){
					if(eachNeighbour.getDestinationVertex().isVisited() ||
							eachNeighbour.getWeight().equalsIgnoreCase("0"))
						continue;
					if(eachNeighbour.getDestinationVertex().getName().contains("DOWN")){
						//heap.moveUp(eachNeighbour.getDestinationVertex());
						continue;
					}
					try{
						weightFromNeighbour = Float.valueOf(eachNeighbour.getWeight());
					}catch (NumberFormatException e) {
						//check the edge if it is down
						Utility.writeToFile(outputFileName,"Edge is down. So skipping smallest path for edge connection to "+eachNeighbour.getDestinationVertex().getName());
						//heap.moveUp(eachNeighbour.getDestinationVertex());
						continue;
					}

					Float distanceOfNeighbour = Float.valueOf(eachNeighbour.getDestinationVertex().getDistance());
					Float newDistance = smallestVertex.getDistance() == 0.0 ?smallestVertex.getDistance()
										:smallestVertex.getDistance() + weightFromNeighbour;
					if (newDistance < distanceOfNeighbour) {
						  queue.remove(eachNeighbour.getDestinationVertex());
			               eachNeighbour.getDestinationVertex().setDistance(newDistance == 0.0 ?weightFromNeighbour :newDistance);
			               eachNeighbour.getDestinationVertex().setPreviousVertex(smallestVertex);
			               queue.add(eachNeighbour.getDestinationVertex());
			               //heap.moveUp(eachNeighbour.getDestinationVertex());
			            }
					/*else{
			            	if(!visited.contains(eachNeighbour.getDestinationVertex().getName())){
			            		//add
			            		heap.addToHeap(eachNeighbour.getDestinationVertex());
			            		//visited add
			            		visited.add(eachNeighbour.getDestinationVertex().getName());
			            	}
			            }*/

				}
				smallestVertex.setVisited(true);

			}

			 output = printGraphDijkstra(sourceVertexName,targetVertexName);


		}else{
			Utility.writeToFile(outputFileName, "Either invalid vertex provided or source vertex is down. Vertex name is = "+sourceVertexName);
		}

		//output file
		Utility.writeToFile(outputFileName,output);
	}

	public void populateUniqueMap(String[] splitStr){
		String source=splitStr[0];
		String destination=splitStr[1];
		String weight=splitStr[2];
		Vertex sourceVertex;
		Vertex destVertex;
		if(uniqueVertexCollector.containsKey(source)){
			sourceVertex= uniqueVertexCollector.get(source);

		}
		else{
			sourceVertex=new Vertex(source);
			uniqueVertexCollector.put(source, sourceVertex);

		}

		if(uniqueVertexCollector.containsKey(destination)){
			destVertex= uniqueVertexCollector.get(destination);
		}
		else{
			destVertex=new Vertex(destination);
			uniqueVertexCollector.put(destination, destVertex);

		}

		sourceVertex.addEdge(destVertex, weight);
		destVertex.addEdge(sourceVertex, weight);
	}

	public void addEdgeToGraph(String tailvertex, String headvertex, String weight){
		boolean tailExists=false;
		boolean headExists=false;
		if(uniqueVertexCollector.containsKey(tailvertex)){
			tailExists= true;
		}
		if(uniqueVertexCollector.containsKey(headvertex)){
			headExists= true;
		}
		if(!tailExists){
			Vertex addNewVertex= new Vertex(tailvertex);
			uniqueVertexCollector.put(tailvertex, addNewVertex);

		}
		if(!headExists){
			Vertex addNewVertex= new Vertex(headvertex);
			uniqueVertexCollector.put(headvertex, addNewVertex);
		}

			Edge newEdge=new Edge(uniqueVertexCollector.get(headvertex), weight);
			Vertex existingVertex= uniqueVertexCollector.get(tailvertex);
			boolean flag = false;
			for (Edge edge : existingVertex.getEdges()){
				if(edge.getDestinationVertex().getName().equalsIgnoreCase(headvertex)){
					edge.setWeight(weight);
					flag = true;
					break;
				}
			}
			if(!flag){
				 existingVertex.getEdges().add(newEdge);
			}
	}

	public void deleteEdgeFromGraph(String tailvertex, String headvertex){

		if(uniqueVertexCollector.containsKey(tailvertex)){
			Vertex existingVertex= uniqueVertexCollector.get(tailvertex);

			for (Edge edge : existingVertex.getEdges()){
				if(edge.getDestinationVertex().getName().equalsIgnoreCase(headvertex)){
					edge.setWeight("0");
					break;
				}
			}

		}
		else{
			Utility.writeToFile(outputFileName, "Tailvertex not available: Cannot delete edge for vertex="+tailvertex);
		}

	}
	public void edgeDown(String tailvertex, String headvertex){

		if(uniqueVertexCollector.containsKey(tailvertex)){
			Vertex existingVertex= uniqueVertexCollector.get(tailvertex);
			for (Edge edge : existingVertex.getEdges()){
				if(edge.getDestinationVertex().getName().equalsIgnoreCase(headvertex)){

					if(edge.getWeight().indexOf("DOWN")== -1){ // edge is not down

						edge.setWeight(edge.getWeight() + "DOWN");
					}
					break;
				}
			}

		}
		else{
			Utility.writeToFile(outputFileName, "Vertex not available: cannot bring down edge for vertex ="+tailvertex);
		}
	}

	public void edgeUp(String tailvertex, String headvertex){

		if(uniqueVertexCollector.containsKey(tailvertex)){
			Vertex existingVertex= uniqueVertexCollector.get(tailvertex);
			for (Edge edge : existingVertex.getEdges()){
				if(edge.getDestinationVertex().getName().equalsIgnoreCase(headvertex)){

					if(edge.getWeight().indexOf("DOWN")!= -1){ // edge is down

						edge.setWeight(edge.getWeight().substring(0,edge.getWeight().indexOf("DOWN")));
					}
					break;
				}
			}

		}
		else{
			Utility.writeToFile(outputFileName, "Vertex not available: cannot bring up edge for vertex="+tailvertex);
		}
	}

	public void vertexDown(String vertexName){
		String downVertexName = vertexName + "DOWN";
		if(uniqueVertexCollector.containsKey(vertexName)){
			Vertex v= uniqueVertexCollector.get(vertexName);
			v.setName(downVertexName);
			uniqueVertexCollector.remove(vertexName);
			uniqueVertexCollector.put(downVertexName, v);
		}
		else{
			Utility.writeToFile(outputFileName, "Invalid vertex. Cannot run Vertex down operation on  "+vertexName);
		}
	}

	public void vertexUp(String vertexName){
		String downVertexName = vertexName + "DOWN";

		if(uniqueVertexCollector.containsKey(downVertexName)){
			Vertex existingVertex= uniqueVertexCollector.get(downVertexName);
			existingVertex.setName(vertexName);
			uniqueVertexCollector.remove(downVertexName);
			uniqueVertexCollector.put(vertexName, existingVertex);
		}
		else{
			Utility.writeToFile(outputFileName, "Vertex  "+vertexName +"is not down");
		}
	}


	public void printGraph(){
		String result ="";
		    Iterator iterator = uniqueVertexCollector.entrySet().iterator();
		    while (iterator.hasNext()) {
		        Map.Entry pair = (Map.Entry)iterator.next();
		        result += ("\n"+pair.getKey());
		        //print the adjacent matrix
		        ArrayList<Edge> edges = ((Vertex)pair.getValue()).getEdges();
		        edges.sort(Edge.edgeComparator);
		         for(Edge eachEdge :edges){
		        	 String name = eachEdge.getDestinationVertex().getName();
		        	 result += ("\n  "+(name.contains("DOWN")?name.substring(0,name.indexOf("DOWN")): name)+" "+eachEdge.getWeight());
		         }
		    }
		    result += ("\n");
		    Utility.writeToFile(outputFileName,result);
	}


	public String printGraphDijkstra (String sourceVertexName,String tailVertexName){
		ArrayList<String> resultNodes = new ArrayList<>();
		Boolean isTraversing = true;
		Vertex currentVertex = uniqueVertexCollector.get(tailVertexName);
		if(currentVertex!=null)
			resultNodes.add(currentVertex.getName());
		while(isTraversing && currentVertex!=null && currentVertex.getPreviousVertex()!=null){
			resultNodes.add(currentVertex.getPreviousVertex().getName());
			currentVertex.getEdges().sort(Edge.edgeComparator);

			if(currentVertex.getPreviousVertex().getName().equals(sourceVertexName))
				isTraversing= false;
			else
				currentVertex = uniqueVertexCollector.get(currentVertex.getPreviousVertex().getName());

		}
		String result = "";
		for(int i=resultNodes.size()-1;i>=0;i--){
			result = result +resultNodes.get(i)+ "  ";
		}

		/*for(int i=0;i<resultNodes.size();i++){
			result = result +resultNodes.get(i)+ "  ";
		}*/
		if(!result.equalsIgnoreCase(""))
			result = result + ( " "+ uniqueVertexCollector.get(tailVertexName).getDistance() + "\n");
	    return result;
	}
}
