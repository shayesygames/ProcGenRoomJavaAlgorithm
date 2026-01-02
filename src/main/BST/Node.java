package BST;

import java.util.List;

//Implementation of BST, stores typical BST left, right and data
//also stores connection nodes, which represent rooms that can
//have a connection to this node on the final map, only used
//for leaves on the tree
public class Node {

    RoomInfo roomInfo;
    Node leftNode;
    Node rightNode;
    //should connections be owned by the rooms instead?
    //does it matter?
    Node connectionNode1;
    Node connectionNode2;

    public Node(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public Node getConnectionNode1() {
        return connectionNode1;
    }

    public Node getConnectionNode2() {
        return connectionNode2;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setConnectionNode1(Node connectionNode) {
        this.connectionNode1 = connectionNode;
    }

    public void setConnectionNode2(Node connectionNode) {
        this.connectionNode2 = connectionNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    //connect new node to this node to available node
    public void addConnectingRoom(Node nodeToConnect) {
        if (connectionNode1 != null && connectionNode2 != null) {
            return;
        }

        if (connectionNode1 == null) {
            this.setConnectionNode1(nodeToConnect);
        } else {
            this.setConnectionNode2(nodeToConnect);
        }
    }

    //check if nodes are already connected
    public boolean isAlreadyConnected(Node nodeToConnect) {
        return (this.getConnectionNode1() != null && this.getConnectionNode1().equals(nodeToConnect))
                || (this.getConnectionNode2() != null && this.getConnectionNode2().equals(nodeToConnect));
    }

    //check for node equivalence based on room id
    public boolean equals(Node candidateNode) {
        RoomInfo thisRoom = this.roomInfo;
        RoomInfo candidateRoom = candidateNode.getRoomInfo();
        return thisRoom.getRoomId().equals(candidateRoom.getRoomId());
    }

    //prints info about this room and id's of connections
    public void printRoomInfo() {
        RoomInfo roomInfo = this.getRoomInfo();
        System.out.println("Room info: ");
        System.out.println("Room id: " + roomInfo.getRoomId());
        System.out.println("origin : (" + roomInfo.getOriginX() + ", " + roomInfo.getOriginY() + ")");
        System.out.println("Height x Witdth (" + roomInfo.getHeight() + ", " + roomInfo.getWidth() + ")");

        if (connectionNode1 != null || connectionNode2 != null) {
            System.out.println("connections: " + getRoomIdFromConnections());
        }
    }

    //get rooms connected to this node
    public String getRoomIdFromConnections() {
        String result = "";
        if (connectionNode1 != null) {
            result = result + connectionNode1.getRoomInfo().getRoomId() + ", ";
        }
        if (connectionNode2 != null) {
            result = result + connectionNode2.getRoomInfo().getRoomId();
        }
        return result;
    }

    //check if there are rooms available
    public boolean hasFreeRoom() {
        return connectionNode1 == null || connectionNode2 == null;
    }

    //Returns a list of 4 Edges, which represent the 4 walls of the room
    //based off of origin, height, and width
    public List<Edge> getEdges() {
        RoomInfo currentRoom = this.getRoomInfo();
        Edge edge1 = new Edge(currentRoom.getOriginX(),
                currentRoom.getOriginX() + currentRoom.getWidth(),
                currentRoom.getOriginY(),
                currentRoom.getOriginY());
        Edge edge2 = new Edge(currentRoom.getOriginX(),
                currentRoom.getOriginX(),
                currentRoom.getOriginY(),
                currentRoom.getOriginY() + currentRoom.getHeight());
        Edge edge3 = new Edge(currentRoom.getOriginX() + currentRoom.getWidth(),
                currentRoom.getOriginX(),
                currentRoom.getOriginY() + currentRoom.getHeight(),
                currentRoom.getOriginY() + currentRoom.getHeight());
        Edge edge4 = new Edge(currentRoom.getOriginX() + currentRoom.getWidth(),
                currentRoom.getOriginX() + currentRoom.getWidth(),
                currentRoom.getOriginY() + currentRoom.getHeight(),
                currentRoom.getOriginY());

        return List.of(edge1, edge2, edge3, edge4);
    }
}
