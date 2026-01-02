import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import BST.Node;
import BST.Edge;
import BST.RoomInfo;
import renderer.MapRenderer;

import javax.swing.*;


public class Main {
    int currentRoomId;
    int gridSize;

    void main() {
        currentRoomId = 0;
        //create root room at 0,0 on coord plane with starting size
        gridSize = 10;
        RoomInfo roomInfo = new RoomInfo(0, 0, gridSize, gridSize);
        roomInfo.setRoomId(currentRoomId++);

        //create rooms from root area
        Node root = new Node(roomInfo);
        createRooms(root);

        //print rooms made without connections
        List<Node> leaves = getLeaves(root);
        System.out.println("Pre connections Rooms");
        for (Node node : leaves) {
            node.printRoomInfo();
            System.out.println();
        }

        //attempt to connect leaf rooms to each other, attempts to connect to up to 2 adjacent rooms
        buildConnectionsForRooms(leaves);

        //print text output of final rooms and their connections
        System.out.println("Post connections");
        for (Node node : leaves) {
            node.printRoomInfo();
            System.out.println();
        }

        //render results to visual
        //currently (due to reversing to Y axis in JPanel), Y coords are inverted so that
        //the displayed result looks like a math coord plane (origin at bottom left instead
        //of top left), will need to be updated for different sizes
        JFrame map = new JFrame("Graph result");
        map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        map.add(new MapRenderer(leaves, gridSize));
        map.setSize(750, 750);
        map.setVisible(true);

    }

    //Takes each room and converts into 4 Edges, edges are represented
    //by way of the two vertexes, then uses the edges to determine if
    //two rooms share an edge, which indicates a possible connection
    private void buildConnectionsForRooms(List<Node> leaves) {
        List<Node> remainingAvailableConnections = new ArrayList<>(leaves);

        for (Node currentNode : leaves) {
            List<Edge> currentNodeEdgeList = currentNode.getEdges();
            for (Node connectionCandidate : remainingAvailableConnections) {
                //check if new rooms can be connected (are adjacent) and do not
                //already have connections
                if (!currentNode.equals(connectionCandidate)) {
                    List<Edge> eligibleCandidateEdgeList = connectionCandidate.getEdges();
                    createConnections(currentNode, connectionCandidate, currentNodeEdgeList, eligibleCandidateEdgeList);
                }
            }
        }
    }

    //takes an initial space as a root and divides the root into
    //smaller sub rooms until divisions cannot go further
    void createRooms(Node root) {
        List<Node> newNodesList = new ArrayList<>();
        newNodesList.add(root);

        while (!newNodesList.isEmpty()) {
            //pop node off list, attempt to create two new rooms from node
            Node currentNode = newNodesList.getFirst();
            newNodesList.removeFirst();

            //check if remaining space is enough for a new room
            boolean canDivide = checkDimensions(currentNode);
            if (canDivide) {
                //divide current node into two new nodes, set those as the Node children
                List<Node> newNodes = divideNodes(currentNode);
                currentNode.setLeftNode(newNodes.get(0));
                currentNode.setRightNode(newNodes.get(1));

                //add newly created nodes to list to be checked themselves later
                newNodesList.addAll(newNodes);
            }
        }
    }

    //attempts to create a connection between two Nodes, including checking
    //if connection is possible
    //compares all edges for rooms, represented as two connected vertexes (x1, y1), (x2, y2)
    private void createConnections(Node currentNode, Node connectionCandidate, List<Edge> currentEdges, List<Edge> candidateEdges) {
        if (currentNode.hasFreeRoom() && connectionCandidate.hasFreeRoom()) {
            for (Edge edge1 : currentEdges) {
                for (Edge edge2 : candidateEdges) {
                    //check that nodes aren't already connected
                    //check that nodes share a wall that can have a connection door
                    if (!currentNode.isAlreadyConnected(connectionCandidate)
                            && edge1.hasAdjacentWall(edge2)) {
                        //make each room connected to each other
                        currentNode.addConnectingRoom(connectionCandidate);
                        connectionCandidate.addConnectingRoom((currentNode));
                    }
                }
            }
        }

    }

    //check that room is large enough to be divided
    //default is minimum length or width of 1
    boolean checkDimensions(Node currentNode) {
        RoomInfo currentRoom = currentNode.getRoomInfo();
        return currentRoom.getHeight() >= 2 && currentRoom.getWidth() >= 2;
    }

    //divide nodes that can be divided
    List<Node> divideNodes(Node currentNode) {
        RoomInfo currentRoom = currentNode.getRoomInfo();
        List<Node> resultList;

        //pick length or width for division
        int lengthOrWidth = ThreadLocalRandom.current().nextInt(0, 2);
        if (lengthOrWidth == 0) {
            //divide width-wise
            int dividingLine = ThreadLocalRandom.current().nextInt(1, currentRoom.getWidth());
            resultList = createNodes(currentNode, dividingLine, lengthOrWidth);
        } else {
            //divide length wise
            int dividingLine = ThreadLocalRandom.current().nextInt(1, currentRoom.getHeight());
            resultList = createNodes(currentNode, dividingLine, lengthOrWidth);
        }

        return resultList;
    }

    // create new nodes, update coords and length + width of new rooms
    List<Node> createNodes(Node currentNode, int dividingLine, int lengthOrWidth) {
        //initialize new rooms with unique ID for comparison
        RoomInfo currentRoom = currentNode.getRoomInfo();
        RoomInfo newNode1RoomInfo = new RoomInfo();
        RoomInfo newNode2RoomInfo = new RoomInfo();
        newNode1RoomInfo.setRoomId(currentRoomId++);
        newNode2RoomInfo.setRoomId(currentRoomId++);

        //width division
        if (lengthOrWidth == 0) {
            newNode1RoomInfo.setOriginX(currentRoom.getOriginX());
            newNode2RoomInfo.setOriginX(currentRoom.getOriginX() + dividingLine);
            newNode1RoomInfo.setOriginY(currentRoom.getOriginY());
            newNode2RoomInfo.setOriginY(currentRoom.getOriginY());

            newNode1RoomInfo.setWidth(dividingLine);
            newNode2RoomInfo.setWidth(currentRoom.getWidth() - dividingLine);
            newNode1RoomInfo.setHeight(currentRoom.getHeight());
            newNode2RoomInfo.setHeight(currentRoom.getHeight());
        }
        //length division
        else {
            newNode1RoomInfo.setOriginX(currentRoom.getOriginX());
            newNode2RoomInfo.setOriginX(currentRoom.getOriginX());
            newNode1RoomInfo.setOriginY(currentRoom.getOriginY());
            newNode2RoomInfo.setOriginY(currentRoom.getOriginY() + dividingLine);

            newNode1RoomInfo.setWidth(currentRoom.getWidth());
            newNode2RoomInfo.setWidth(currentRoom.getWidth());
            newNode1RoomInfo.setHeight(dividingLine);
            newNode2RoomInfo.setHeight(currentRoom.getHeight() - dividingLine);
        }
        return List.of(new Node(newNode1RoomInfo), new Node(newNode2RoomInfo));
    }

    //traverse tree and extract leaves only, leaves represent the
    //final rooms
    List<Node> getLeaves(Node root) {
        List<Node> resultNodeList = new ArrayList<>();
        List<Node> nodesList = new ArrayList<>();
        nodesList.add(root);

        while (!nodesList.isEmpty()) {
            Node currentNode = nodesList.getFirst();
            nodesList.removeFirst();
            if (currentNode.getRightNode() == null && currentNode.getLeftNode() == null) {
                resultNodeList.add(currentNode);
            }

            if (currentNode.getLeftNode() != null) {
                nodesList.add(currentNode.getLeftNode());
            }
            if (currentNode.getRightNode() != null) {
                nodesList.add(currentNode.getRightNode());
            }
        }
        return resultNodeList;
    }
}
