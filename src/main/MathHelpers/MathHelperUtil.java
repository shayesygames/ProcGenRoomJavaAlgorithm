package MathHelpers;

public class MathHelperUtil {

    public static double case2Calculation(double newOrigin,
                                          double currentCoord,
                                          double connectionCoord,
                                          double currentDistance,
                                          double connectionDistance) {

        double connectionMaxDistance = connectionCoord + connectionDistance;
        double currentMaxDistance = currentCoord + currentDistance;
        double newDistance = Math.min(connectionMaxDistance, currentMaxDistance);
        return ((newDistance - newOrigin) / 2) + newOrigin;
    }

    public static double case3Calculation(double newOrigin,
                                          double room1Coord,
                                          double room1Distance,
                                          double room2Coord,
                                          double room2Distance) {
        if (room1Coord + room1Distance <= room2Coord + room2Distance) {
            newOrigin = newOrigin + (room1Distance / 2);
        } else {
            newOrigin = newOrigin + ((room2Coord + room2Distance - room1Coord - room1Distance) / 2);
        }

        return newOrigin;
    }
}
