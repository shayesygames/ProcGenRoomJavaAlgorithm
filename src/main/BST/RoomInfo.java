package BST;

public class RoomInfo {

    int roomId;
    int height;
    int width;
    //global coords of room origin (local 0,0)
    int originX;
    int originY;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return "" + roomId;
    }
}
