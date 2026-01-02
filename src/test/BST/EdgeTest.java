package BST;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class EdgeTest {

    //test major scenarios for any possible comparison
    //Case 1: walls match exactly
    //Case 2: walls overlap
    //Case 3: walls only meet (diagonal/corner connection)
    //Case 4: walls do not touch at all
    @Test
    void areWallsSeparatedScenarios() {
        Edge edge = new Edge(1,1,1,1);

        //two walls are touching and have same length (identical)
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,4,2,4);

        //two walls are overlapping, same length but offset
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,4,3,5);

        //two walls touch at ends but do not overlap
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeparated(2,3,3,5);

        //two walls do not touch at all
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeparated(2,3,4,5);

        //one wall fully contained by the other
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,3,1,5);
    }

    //test same scenarios to confirm that altering the order of the
    //coords (but still preserving the same edge) does not affect
    //output
    @Test
    void areWallsSeperatedMixedCoords() {
        Edge edge = new Edge(1,1,1,1);

        //////////////
        //switch order of A values

        //two walls are touching and have same length (identical)
        //(x,4) (x,2) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(4,2,2,4);

        //two walls are overlapping, same length but offset
        //(x,4) (x,2) (x,4) (x,5)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(4,2,3,5);

        //two walls touch at ends but do not overlap
        //(x,3) (x,2) (x,3) (x,5)
        //assert walls are seperated
        assert edge.areWallsSeparated(3,2,3,5);

        //two walls do not touch at all
        //(x,3) (x,2) (x,4) (x,5)
        //assert walls are seperated
        assert edge.areWallsSeparated(3,2,4,5);

        //one wall fully contained by the other
        //(x,3) (x,2) (x,1) (x,5)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(3,2,1,5);

        //////////////
        //switch order of B values

        //two walls are touching and have same length (identical)
        //(x,2) (x,4) (x,4) (x,2)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,4,4,2);

        //two walls are overlapping, same length but offset
        //(x,2) (x,4) (x,5) (x,3)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,4,3,5);

        //two walls touch at ends but do not overlap
        //(x,2) (x,3) (x,5) (x,3)
        //assert walls are seperated
        assert edge.areWallsSeparated(2,3,5,3);

        //two walls do not touch at all
        //(x,2) (x,3) (x,5) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeparated(2,3,5,4);

        //one wall fully contained by the other
        //(x,2) (x,3) (x,5) (x,1)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(2,3,5,1);

        //////////////
        //switch order of both

        //two walls are touching and have same length (identical)
        //(x,4) (x,2) (x,4) (x,2)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(4,2,4,2);

        //two walls are overlapping, same length but offset
        //(x,4) (x,2) (x,5) (x,3)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(4,2,3,5);

        //two walls touch at ends but do not overlap
        //(x,3) (x,2) (x,5) (x,3)
        //assert walls are seperated
        assert edge.areWallsSeparated(3,2,5,3);

        //two walls do not touch at all
        //(x,3) (x,2) (x,5) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeparated(3,2,5,4);

        //one wall fully contained by the other
        //(x,3) (x,2) (x,5) (x,1)
        //assert walls are not seperated
        assert !edge.areWallsSeparated(3,2,5,1);
    }

    @ParameterizedTest
    @MethodSource("roomArgs")
    void testDiagonalConnectionShouldReturnSeperation(RoomInfo roomInfo1, RoomInfo roomInfo2) {
        //create 2 rooms that have a corner touching
        Node node1 = new Node(roomInfo1);
        List<Edge> edges1 = node1.getEdges();

        Node node2 = new Node(roomInfo2);
        List<Edge> edges2 = node2.getEdges();

        boolean isSeperated = true;

        //compare all edges between the two rooms
        System.out.println("start evaluation for Rooms " + roomInfo1.getRoomId() + " and " + roomInfo2.getRoomId());
        for (Edge edge1 : edges1) {
            for (Edge edge2 : edges2) {
                isSeperated = !edge1.hasAdjacentWall(edge2);
            }
        }

        //assert that wallsAreSeperated for all
        assert isSeperated;
    }

    //all of these cases are rooms that touch only at a corner
    static Stream<Arguments> roomArgs() {
        return Stream.of(
                Arguments.of(new RoomInfo(1, 0, 2, 1, 1), new RoomInfo(2, 2, 1, 2, 2)),
                Arguments.of(new RoomInfo(2, 0, 1, 3, 3), new RoomInfo(0, 1, 1, 2, 4)),
                Arguments.of(new RoomInfo(4, 0, 2, 1, 5), new RoomInfo(3, 2, 2, 1, 6)),
                Arguments.of(new RoomInfo(2, 3, 2, 1, 7), new RoomInfo(3, 0, 3, 1, 8)),
                Arguments.of(new RoomInfo(9, 4, 4, 1, 9), new RoomInfo(0, 8, 1, 9, 10))
                );
    }
}