package renderer;

import BST.Node;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapRenderer extends JPanel {

    List<Node> rooms;

    public MapRenderer(List<Node> rooms) {
        this.rooms = rooms;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //draw rooms
        for (Node currentRoom : rooms) {
            int roomX = currentRoom.getRoomInfo().getOriginX() * 100;
            int roomY = currentRoom.getRoomInfo().getOriginY() * 100;
            int roomWidth = currentRoom.getRoomInfo().getWidth() * 100;
            int roomHeight = currentRoom.getRoomInfo().getHeight() * 100;
            drawRectangle(g2d, roomX + 25, roomY + 25, roomWidth, roomHeight, true);
        }

        //draw connections
        for (Node currentRoom : rooms) {
            if (currentRoom.getConnectionNode1() != null) {
                double currentOriginX = currentRoom.getRoomInfo().getOriginX();
                double currentOriginY = currentRoom.getRoomInfo().getOriginY();
                double currentRoomWidth = currentRoom.getRoomInfo().getWidth();
                double currentRoomHeight = currentRoom.getRoomInfo().getHeight();
                double connectedRoomOriginX = currentRoom.getConnectionNode1().getRoomInfo().getOriginX();
                double connectedRoomOriginY = currentRoom.getConnectionNode1().getRoomInfo().getOriginY();
                double connectedRoomWidth = currentRoom.getConnectionNode1().getRoomInfo().getWidth();
                double connectedRoomHeight = currentRoom.getConnectionNode1().getRoomInfo().getHeight();

                double connectionOriginX;
                double connectionOriginY;

                connectionOriginX = Math.max(currentOriginX, connectedRoomOriginX);
                connectionOriginY = Math.max(currentOriginY, connectedRoomOriginY);

                List<Double> connectionCoords = chooseHeightWidthModification(connectionOriginX,
                        connectionOriginY,
                        currentOriginX,
                        currentOriginY,
                        connectedRoomOriginX,
                        connectedRoomOriginY,
                        currentRoomWidth,
                        currentRoomHeight,
                        connectedRoomWidth,
                        connectedRoomHeight);

                drawRectangle(g2d, connectionCoords.get(0) * 100 + 25, connectionCoords.get(1) * 100 + 25, 5,5, false);

            }

        }
    }

    //width comes as 0, height comes as 1
    private List<Double> chooseHeightWidthModification(double newOriginX,
                                                       double newOriginY,
                                                       double currentOriginX,
                                                       double currentOriginY,
                                                       double connectedRoomOriginX,
                                                       double connectedRoomOriginY,
                                                       double currentWidth,
                                                       double currentHeight,
                                                       double connectionWidth,
                                                       double connectionHeight) {

        //check if new origin exceeds either greater boundary of either shape
        //i.e. if shape 1 has origin 0,0 with L=1 H=2 and shape 2 has origin 1,1 and L=1 H=1
        //shape 2 origin exceeds X axis of shape 1 boundaries since shape 2 originX = shape 1 originX + shape 1 L
        //but does not exceed on Y axis since shape2 originY < shape 1 origin Y + shape 1 H
        //since X exceeds, we allow the x to stay the same and add .5 shape 2 H to newOriginY

        //if originX/Y + shape1 H/W does not exceed shape2 originX/Y + shape2 H/W then can just use simple
        //originX/Y + .5 shape1 H/W
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
            newOriginX = newOriginX + ((currentWidth < connectionWidth) ? currentWidth * .5 : connectionWidth * .5);
        } else if (currentSharedY && connectionSharedY) {
            newOriginY = newOriginY + ((currentHeight < connectionHeight) ? currentHeight * .5 : connectionHeight * .5);
        } else {
            //Case 2:
            if (currentSharedX && currentSharedY) {
                //rooms share a vertical wall, modify Y
                if (currentOriginX == connectedRoomOriginX + connectionWidth) {
                    double connectionMaxY = connectedRoomOriginY + connectionHeight;
                    double currentMaxY = currentOriginY + currentHeight;
                    double maxHeightForConnection = Math.min(connectionMaxY, currentMaxY);
                    //currentY is lower bound, smallest between connectionMaxY and currentMaxY is upper bound
                    newOriginY = newOriginY + (.5 * (maxHeightForConnection - newOriginY));
                }
                //share a horizontal wall, modify X
                else if (currentOriginY == connectedRoomOriginY + connectionHeight) {
                    double connectionMaxX = connectedRoomOriginX + connectionWidth;
                    double currentMaxX = currentOriginX + currentWidth;
                    double maxWidthForConnection = Math.min(connectionMaxX, currentMaxX);
                    //currentY is lower bound, smallest between connectionMaxX and currentMaxX is upper bound
                    newOriginX = newOriginX + (.5 * (maxWidthForConnection - newOriginX));
                }
            } else if (connectionSharedX && connectionSharedY) {
                //share vertical wall, modify Y
                if (connectedRoomOriginX == currentOriginX + currentWidth) {
                    double connectionMaxY = connectedRoomOriginY + connectionHeight;
                    double currentMaxY = currentOriginY + currentHeight;
                    double maxHeightForConnection = Math.min(connectionMaxY, currentMaxY);
                    //currentY is lower bound, smallest between connectionMaxY and currentMaxY is upper bound
                    newOriginY = newOriginY + (.5 * (maxHeightForConnection - newOriginY));
                }
                //share a horizontal wall, modify X
                else if (connectedRoomOriginY == currentOriginX + currentHeight) {
                    double connectionMaxX = connectedRoomOriginX + connectionWidth;
                    double currentMaxX = currentOriginX + currentWidth;
                    double maxWidthForConnection = Math.min(connectionMaxX, currentMaxX);
                    //currentY is lower bound, smallest between connectionMaxX and currentMaxX is upper bound
                    newOriginX = newOriginX + (.5 * (maxWidthForConnection - newOriginX));
                }
            }

            //Case 3:
            else {
                if (currentSharedX && connectionSharedY) {
                    //figure out vertical or horizontal connections
                    if (newOriginX >= currentOriginX + currentWidth) {

                    }

                    //check against current dimensions

                } else if (connectionSharedX && currentSharedY) {

                }
            }
        }

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

}
