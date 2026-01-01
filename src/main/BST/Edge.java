package BST;

public class Edge {
    public int x1;
    public int x2;
    public int y1;
    public int y2;

    public Edge(int x1, int x2, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    //checks if an edge shares a wall, done by checking if either
    //x's or y's are shared between edges and the alternate coord
    //does not violate rules that check for space between edges
    public boolean hasAdjacentWall(Edge candidateEdge) {
        if (hasSharedX(candidateEdge)) {
            return !areWallsSeperated(this.y1, this.y2, candidateEdge.y1, candidateEdge.y2);
        } else if (hasSharedY(candidateEdge)) {
            return !areWallsSeperated(this.x1, this.x2, candidateEdge.x1, candidateEdge.x2);
        }

        return false;
    }

    //At this point it has already been determined that a shared x or y
    //exists between these two edges, this method then compares the dimension that
    //is not shared and checks if there is a space between them or if they overlap
    //overlaps signal that the edges can facilitate a connection
    public boolean areWallsSeperated(int An1, int An2, int Bn1, int Bn2) {
        //if this pair are both less than both of the candidate pair
        //or if this pair are both greater than both of the candidate pair
        //then that means that there is not an overlap, at most there is
        //a corner connection, which is still not valid
        return (An1 < Bn1 && An1 < Bn2 && An2 <= Bn1 && An2 < Bn2)
                || (An1 > Bn1 && An1 > Bn2 && An2 >= Bn1 && An2 > Bn2);
    }


    //methods for basic checks on shared values
    private boolean hasSharedX(Edge candidateEdge) {
        return this.selfXMatching() && candidateEdge.selfXMatching() && this.xMatching(candidateEdge);
    }

    private boolean hasSharedY(Edge candidateEdge) {
        return this.selfYMatching() && candidateEdge.selfYMatching() && this.yMatching(candidateEdge);
    }

    public boolean xMatching(Edge comparator) {
        return x1 == comparator.x1;
    }

    public boolean yMatching(Edge comparator) {
        return y1 == comparator.y1;
    }

    public boolean selfXMatching() {
        return x1 == x2;
    }

    public boolean selfYMatching() {
        return y1 == y2;
    }
}
