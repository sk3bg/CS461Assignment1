import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class SearchMethods {
	/** Anthony Bluff_City 
		Bluff_City Kiowa
	 */
    private  Map<String, List<String>> adjacencies;

    /**	Abilene, 38.9220277, -97.2666667
		Andover, 37.6868403, -97.1657752
     */
    private  Map<String, double[]> coordinates;

    public SearchMethods(Map<String, List<String>> adjacencyGraph, Map<String, double[]> cityCoordinates) {
    	this.adjacencies = adjacencyGraph;
    	this.coordinates = cityCoordinates;
    }

    public  double haversine(double[] coord1, double[] coord2) {
        double lat1 = coord1[0];
        double lon1 = coord1[1];
        double lat2 = coord2[0];
        double lon2 = coord2[1];
        double radius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    public  List<String> bruteForceSearch(String start, String end) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(start);
        return bruteForceSearchHelper(start, end, visited, path);
    }

    public  List<String> bruteForceSearchHelper(String current, String end, Set<String> visited, List<String> path) {
        if (current.equals(end)) {
            return path;
        }
        visited.add(current);
        for (String neighbor : adjacencies.get(current)) {
            if (!visited.contains(neighbor)) {
                List<String> newPath = new ArrayList<>(path);
                newPath.add(neighbor);
                List<String> result = bruteForceSearchHelper(neighbor, end, visited, newPath);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public  List<String> breathFirstSearch(String start, String end) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(start);
        return breathFirstSearchHelper(start, end, visited, path);
    }

    public  List<String> breathFirstSearchHelper(String current, String end, Set<String> visited, List<String> path) {
        if (current.equals(end)) {
            return path;
        }
        visited.add(current);
        List<String> newPath = new ArrayList<>(path);
        for (String neighbor : adjacencies.get(current)) {
            if (!visited.contains(neighbor)) {
                newPath.add(neighbor);
                if (neighbor.equals(end)) {
                    return newPath;
                }
            }
        }
        for (String neighbor : adjacencies.get(current)) {
            if (!visited.contains(neighbor)) {
                List<String> result = breathFirstSearchHelper(neighbor, end, visited, newPath);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public  List<String> depthFirstSearch(String start, String end) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(start);
        return depthFirstSearchHelper(start, end, visited, path);
    }

    public  List<String> depthFirstSearchHelper(String current, String end, Set<String> visited, List<String> path) {
        if (current.equals(end)) {
            return path;
        }
        visited.add(current);
        for (String neighbor : adjacencies.get(current)) {
            if (!visited.contains(neighbor)) {
                List<String> newPath = new ArrayList<>(path);
                newPath.add(neighbor);
                List<String> result = depthFirstSearchHelper(neighbor, end, visited, newPath);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public  List<String> idDfs(String start, String end) {
        int depth = 0;
        while (true) {
            List<String> result = dfsRecursive(start, end, new ArrayList<>(), depth);
            if (result != null) {
                return result;
            }
            depth++;
        }
    }

    public  List<String> dfsRecursive(String current, String end, List<String> path, int depth) {
        if (current.equals(end)) {
            return path;
        }
        if (depth == 0) {
            return null;
        }
        for (String neighbor : adjacencies.get(current)) {
            if (!path.contains(neighbor)) {
                List<String> newPath = new ArrayList<>(path);
                newPath.add(current);
                List<String> result = dfsRecursive(neighbor, end, newPath, depth - 1);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public  List<String> bestFirstSearch(String start, String end) {
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> heap = new PriorityQueue<>();
        heap.add(new Node(start, new ArrayList<>(), 0));
        while (!heap.isEmpty()) {
            Node node = heap.poll();
            String current = node.city;
            List<String> path = node.path;
            if (current.equals(end)) {
                return path;
            }
            visited.add(current);
            for (String neighbor : adjacencies.get(current)) {
                if (!visited.contains(neighbor)) {
                    double distance = haversine(coordinates.get(neighbor), coordinates.get(end));
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(current);
                    heap.add(new Node(neighbor, newPath, distance));
                }
            }
        }
        return null;
    }

    public  List<String> aStarSearch(String start, String end) {
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> heap = new PriorityQueue<>();
        heap.add(new Node(start, new ArrayList<>(), 0, haversine(coordinates.get(start), coordinates.get(end))));
        while (!heap.isEmpty()) {
            Node node = heap.poll();
            String current = node.city;
            List<String> path = node.path;
            if (current.equals(end)) {
                return path;
            }
            visited.add(current);
            for (String neighbor : adjacencies.get(current)) {
                if (!visited.contains(neighbor)) {
                    double g = path.size() + 1;
                    double h = haversine(coordinates.get(neighbor), coordinates.get(end));
                    double f = g + h;
                    List<String> newPath = new ArrayList<>(path);
                    newPath.add(current);
                    heap.add(new Node(neighbor, newPath, f, h));
                }
            }
        }
        return null;
    }

}
