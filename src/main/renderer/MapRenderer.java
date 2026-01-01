package renderer;

import BST.Node;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapRenderer extends JPanel {

    List<Node> rooms;
    int gridSize;

    public MapRenderer(List<Node> rooms, int gridSize) {
        this.gridSize = gridSize;
        this.rooms = rooms;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //draw rooms
        for (Node currentRoom : rooms) {
            int roomWidth = currentRoom.getRoomInfo().getWidth() * 100;
            int roomHeight = currentRoom.getRoomInfo().getHeight() * 100;
            int roomX = currentRoom.getRoomInfo().getOriginX() * 100;
            //here the Y coords are inverted...this may be more trouble than it's worth, may revert
            int roomY = (gridSize - currentRoom.getRoomInfo().getOriginY() - currentRoom.getRoomInfo().getHeight()) * 100;
            drawRectangle(g2d, roomX, roomY, roomWidth, roomHeight, true);
            drawId(g2d, roomX, roomY, roomWidth, roomHeight, currentRoom.getRoomInfo().getRoomId());
        }

        //draw connections
        for (Node currentRoom : rooms) {
            System.out.println("\n\n\nstarting connections drawing");
            if (currentRoom.getConnectionNode1() != null) {
                attemptToDrawConnectionBetweeNodes(currentRoom, currentRoom.getConnectionNode1(), g2d);
            }
            if (currentRoom.getConnectionNode2() != null) {
                attemptToDrawConnectionBetweeNodes(currentRoom, currentRoom.getConnectionNode2(), g2d);
            }

        }
    }

    private void attemptToDrawConnectionBetweeNodes(Node currentRoom, Node connectionRoom, Graphics2D g2d) {
        double currentOriginX = currentRoom.getRoomInfo().getOriginX();
        double currentOriginY = currentRoom.getRoomInfo().getOriginY();
        double currentRoomWidth = currentRoom.getRoomInfo().getWidth();
        double currentRoomHeight = currentRoom.getRoomInfo().getHeight();
        double connectedRoomOriginX = connectionRoom.getRoomInfo().getOriginX();
        double connectedRoomOriginY = connectionRoom.getRoomInfo().getOriginY();
        double connectedRoomWidth = connectionRoom.getRoomInfo().getWidth();
        double connectedRoomHeight = connectionRoom.getRoomInfo().getHeight();

        double connectionOriginX;
        double connectionOriginY;

        connectionOriginX = Math.max(currentOriginX, connectedRoomOriginX);
        connectionOriginY = Math.max(currentOriginY, connectedRoomOriginY);

        System.out.printf("\n\nattempting to draw connection between %s and %s\n",
                currentRoom.getRoomInfo().getRoomId(),
                connectionRoom.getRoomInfo().getRoomId());
        System.out.println("currentRoom origin: " + currentOriginX + ", " + currentOriginY);
        System.out.println("connectionRoom origin: " + connectedRoomOriginX + ", " + connectedRoomOriginY);
        System.out.println("connection origin: " + connectionOriginX + ", " + connectionOriginY);

        List<Double> connectionCoords = updateConnectionOriginBasedOnAvailableConnectionSpace(connectionOriginX,
                connectionOriginY,
                currentOriginX,
                currentOriginY,
                connectedRoomOriginX,
                connectedRoomOriginY,
                currentRoomWidth,
                currentRoomHeight,
                connectedRoomWidth,
                connectedRoomHeight);

        //also inverted y here, again this may be too much trouble...
        drawRectangle(g2d, connectionCoords.get(0) * 100, (gridSize - connectionCoords.get(1)) * 100, 5,5, false);
    }

    private List<Double> updateConnectionOriginBasedOnAvailableConnectionSpace(double newOriginX,
                                                                               double newOriginY,
                                                                               double currentOriginX,
                                                                               double currentOriginY,
                                                                               double connectedRoomOriginX,
                                                                               double connectedRoomOriginY,
                                                                               double currentWidth,
                                                                               double currentHeight,
                                                                               double connectionWidth,
                                                                               double connectionHeight) {
        boolean currentSharedX = false;
        boolean currentSharedY = false;
        boolean connectionSharedX = false;
        boolean connectionSharedY = false;
        if (newOriginX == currentOriginX) {
            currentSharedX = true;
        }
        if (newOriginY == currentOriginY) {
            currentSharedY = true;
        }
        if (newOriginX == connectedRoomOriginX) {
            connectionSharedX = true;
        }
        if (newOriginY == connectedRoomOriginY) {
            connectionSharedY = true;
        }

        //3 Cases for how connecting rooms are arranged
        //Case 1: rooms share a common X or Y, both matching should not be possible by now
            //i.e. for a new origin of (1,3)
            //currentRoomOrigin == (1,2), connectionRoomOrigin = (1,3) shared on X axis
            //i.e. for a new origin of (2,4)
            //or currentRoomOrigin == (1,4), connectionRoomOrigin = (2,4) shared on Y axis
        //Case 2: new coords match directly to either current or connection
            //i.e. new origin == (1,1) currentRoom == (1,1), connectionRoom = (0,0)
        //Case 3: one coord from each room is used in new origin
            //i.e. new origin == (1,3), currentRoomOrigin == (1,2) and connectionRoomOrigin == (0,3)

        //Case 1:
        if (currentSharedX && connectionSharedX) {
            newOriginX = newOriginX + (Math.min(currentWidth, connectionWidth) / 2);
        } else if (currentSharedY && connectionSharedY) {
            newOriginY = newOriginY + (Math.min(currentHeight, connectionHeight) / 2);
        } else {
            //Case 2:
            if (currentSharedX && currentSharedY) {
                //rooms share a vertical wall, modify Y
                if (areDoublesEqual(currentOriginX, connectedRoomOriginX + connectionWidth)) {
                    double connectionMaxY = connectedRoomOriginY + connectionHeight;
                    double currentMaxY = currentOriginY + currentHeight;
                    double maxYForConnection = Math.min(connectionMaxY, currentMaxY);
                    double newHeight = ((maxYForConnection - newOriginY) / 2);
                    //currentY is lower bound, smallest between connectionMaxY and currentMaxY is upper bound
                    newOriginY = newOriginY + newHeight;
                }
                //share a horizontal wall, modify X
                else if (areDoublesEqual(currentOriginY, connectedRoomOriginY + connectionHeight)) {
                    double connectionMaxX = connectedRoomOriginX + connectionWidth;
                    double currentMaxX = currentOriginX + currentWidth;
                    double maxXForConnection = Math.min(connectionMaxX, currentMaxX);
                    double newWidth = ((maxXForConnection - newOriginX) / 2);
                    //currentY is lower bound, smallest between connectionMaxX and currentMaxX is upper bound
                    newOriginX = newOriginX + newWidth;
                }
            } else if (connectionSharedX && connectionSharedY) {
                //share vertical wall, modify Y
                if (areDoublesEqual(connectedRoomOriginX, currentOriginX + currentWidth)) {
                    double connectionMaxY = connectedRoomOriginY + connectionHeight;
                    double currentMaxY = currentOriginY + currentHeight;
                    double maxYForConnection = Math.min(connectionMaxY, currentMaxY);
                    double newHeight = ((maxYForConnection - newOriginY) / 2);
                    //currentY is lower bound, smallest between connectionMaxY and currentMaxY is upper bound
                    newOriginY = newOriginY + newHeight;
                }
                //share a horizontal wall, modify X
                else if (areDoublesEqual(connectedRoomOriginY, currentOriginY + currentHeight)) {
                    double connectionMaxX = connectedRoomOriginX + connectionWidth;
                    double currentMaxX = currentOriginX + currentWidth;
                    double maxXForConnection = Math.min(connectionMaxX, currentMaxX);
                    double newWidth = ((maxXForConnection - newOriginX) / 2);
                    //currentY is lower bound, smallest between connectionMaxX and currentMaxX is upper bound
                    newOriginX = newOriginX + newWidth;
                }
            }

            //Case 3:
            else {
                if (currentSharedX && connectionSharedY) {
                    //vertical connection, modify Y
                    if (connectedRoomOriginX + connectionWidth <= currentOriginX
                        || currentOriginX + currentWidth <= connectedRoomOriginX) {
                        if (connectedRoomOriginY + connectionHeight <= currentOriginY + currentHeight) {
                            newOriginY = newOriginY + (connectionHeight / 2);
                        } else {
                            newOriginY = newOriginY + ((currentOriginY + currentHeight - connectedRoomOriginY - connectionHeight) / 2);
                        }
                    } //horizontal connection, modify X
                    else if (connectedRoomOriginY + connectionHeight <= currentOriginY
                        || currentOriginY + currentHeight <= connectedRoomOriginY) {
                        if (currentOriginX + currentWidth <= connectedRoomOriginX + connectionWidth) {
                            newOriginX = newOriginX + (currentWidth / 2);
                        } else {
                            newOriginX = newOriginX + ((currentOriginX + currentWidth - connectedRoomOriginX - connectionWidth) / 2);
                        }
                    }

                } else if (currentSharedY && connectionSharedX) {
                    //vertical connection, modify Y
                    if (connectedRoomOriginX + connectionWidth <= currentOriginX
                            || currentOriginX + currentWidth <= connectedRoomOriginX) {
                        if (currentOriginY + currentHeight <= connectedRoomOriginY + connectionHeight) {
                            newOriginY = newOriginY + (currentHeight / 2);
                        } else {
                            newOriginY = newOriginY + ((connectedRoomOriginY + connectionHeight - currentOriginY - currentHeight) / 2);
                        }
                    } //horizontal connection, modify X
                    else if (connectedRoomOriginY + connectionHeight <= currentOriginY
                            || currentOriginY + currentHeight <= connectedRoomOriginY) {
                        if (connectedRoomOriginX + connectionWidth <= currentOriginX + currentWidth) {
                            newOriginX = newOriginX + (connectionWidth / 2);
                        } else {
                            newOriginX = newOriginX + ((connectedRoomOriginX + connectionWidth - currentOriginX - currentWidth) / 2);
                        }
                    }
                }
            }

        }
        System.out.println("new newOrigin (" + newOriginX + ", " + newOriginY + ")");
        return List.of(newOriginX, newOriginY);
    }

    private void drawRectangle(Graphics2D g2d, double x, double y, double width, double height, boolean randomColor) {
        if (randomColor) {
            g2d.setColor(new Color((int)(Math.random() * 0x1000000)));
        } else {
            g2d.setColor(Color.BLACK);
        }
        Rectangle2D rectRoom = new Rectangle2D.Double(x, y, width, height);
        g2d.fill(rectRoom);
    }

    private void drawId(Graphics2D g2d, double x, double y, double width, double height, String id) {
        g2d.setColor(Color.BLACK);
        double stringX = x + .5 * width;
        double stringY = y + .5 * height;
        g2d.drawString(id, (int) stringX, (int) stringY);
    }

    private boolean areDoublesEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < .1;
    }

}
