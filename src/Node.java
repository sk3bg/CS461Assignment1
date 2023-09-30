import java.util.List;

public class Node implements Comparable<Node> {
    String city;
    List<String> path;
    double f; 
    double h;

    public Node(String city, List<String> path, double f) {
        this.city = city;
        this.path = path;
        this.f = f;
    }

    public Node(String city, List<String> path, double f, double h) {
        this.city = city;
        this.path = path;
        this.f = f;
        this.h = h;
    }

    @Override
    public int compareTo(Node other) {
        if (this.f < other.f) {
            return -1;
        } else if (this.f > other.f) {
            return 1;
        } else {
            if (this.h < other.h) {
                return -1;
            } else if (this.h > other.h) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}