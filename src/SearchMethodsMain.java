import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class SearchMethodsMain {
	/** Anthony Bluff_City 
		Bluff_City Kiowa
	 */
    private static Map<String, List<String>> adjacencies = new HashMap();

    
    /**	Abilene, 38.9220277, -97.2666667
		Andover, 37.6868403, -97.1657752
     */
    private static Map<String, double[]> coordinates = new HashMap();

    public static void main(String[] args) {
    	
    	loadAdjacenciesFileIntoAdjancencies(adjacencies);
    	loadCoordinatesFileIntoCoordinates(coordinates);
    	
    	SearchMethods searchMethods = new SearchMethods(adjacencies, coordinates);

    	Scanner scanner = new Scanner(System.in);
    	
        while (true) {
        	System.out.println("Enter the starting City: ");
            String startCity = scanner.nextLine();
            
        	System.out.println("Enter the ending City: ");
            String endCity = scanner.nextLine();
           
            if (!coordinates.containsKey(startCity) || !coordinates.containsKey(endCity)) {
                System.out.println("Invalid cities. Please enter valid city names.");
                continue;
            }
            System.out.println("Select a search method:");
            System.out.println("1. Brute-force (Undirected)");
            System.out.println("2. Breadth-first search");
            System.out.println("3. Depth-first search");
            System.out.println("4. ID-DFS search");
            System.out.println("5. Best-first search");
            System.out.println("6. A* search");
            
        	System.out.println("Enter the method number (1-6): ");
        	int method = scanner.nextInt();
            
            long startTime = System.currentTimeMillis();
            List<String> path;
            switch (method) {
                case 1:
                    path = searchMethods.bruteForceSearch(startCity, endCity);
                    break;
                case 2:
                    path = searchMethods.breathFirstSearch(startCity, endCity);
                    break;
                case 3:
                    path = searchMethods.depthFirstSearch(startCity, endCity);
                    break;
                case 4:
                    path = searchMethods.idDfs(startCity, endCity);
                    break;
                case 5:
                    path = searchMethods.bestFirstSearch(startCity, endCity);
                    break;
                case 6:
                    path = searchMethods.aStarSearch(startCity, endCity);
                    break;
                default:
                	System.out.println("Invalid input. Please enter a valid method number (1-6).");
                    continue;
            }

            if (path != null) {
                System.out.println("Route found: " + String.join(" -> ", path));
                double totalDistance = 0;
                for (int i = 0; i < path.size() - 1; i++) {
                    double[] coord1 = coordinates.get(path.get(i));
                    double[] coord2 = coordinates.get(path.get(i + 1));
                    totalDistance += searchMethods.haversine(coord1, coord2);
                }
                System.out.println("Total distance: " + String.format("%.2f", totalDistance) + " km");
                
                double totalTime = (totalDistance/65) * 60;
                
                System.out.println("Time taken: " + totalTime + " minutes");
            } else {
                System.out.println("No route found.");
            }
            
            System.out.println("--------------------------------------");
        	System.out.println("Do you want to search again? (yes/no): ");
        	
        	String choice = scanner.nextLine();
        	
            if (!choice.equalsIgnoreCase("yes")) {
                break;
            }
        }
        scanner.close();
    }

    private static void loadAdjacenciesFileIntoAdjancencies(Map<String, List<String>> adjacencyGraph) {
  
        try {
            BufferedReader adjacenciesReader = new BufferedReader(new FileReader("Adjacencies.txt"));
            String adjacenciesLine;
            while ((adjacenciesLine = adjacenciesReader.readLine()) != null) {
                String[] adjacenciesData = adjacenciesLine.split(" ");
                String city1 = adjacenciesData[0];
                String city2 = adjacenciesData[1];
                adjacencyGraph.putIfAbsent(city1, new ArrayList<>());
                adjacencyGraph.putIfAbsent(city2, new ArrayList<>());
                adjacencyGraph.get(city1).add(city2);
                adjacencyGraph.get(city2).add(city1);
            }
            adjacenciesReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
  }
    
    private static void loadCoordinatesFileIntoCoordinates(Map<String, double[]> cityCoordinates){   	
        try {
            BufferedReader coordinatesReader = new BufferedReader(new FileReader("coordinates.csv"));
            String coordinatesLine;
            while ((coordinatesLine = coordinatesReader.readLine()) != null) {
                String[] coordinatesData = coordinatesLine.split(",");
                String city = coordinatesData[0];
                double latitude = Double.parseDouble(coordinatesData[1]);
                double longitude = Double.parseDouble(coordinatesData[2]);
                cityCoordinates.put(city, new double[]{latitude, longitude});
            }
            coordinatesReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}