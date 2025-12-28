package BST;

import org.junit.jupiter.api.Test;

class EdgeTest {

    //test major scenarios for any possible comparison
    //Case 1: walls match exactly
    //Case 2: walls overlap
    //Case 3: walls only meet (diagonal/corner connection)
    //Case 4: walls do not touch at all
    @Test
    void areWallsSeperatedScenarios() {
        Edge edge = new Edge(1,1,1,1);

        //two walls are touching and have same length (identical)
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,4,2,4);

        //two walls are overlapping, same length but offset
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,4,3,5);

        //two walls touch at ends but do not overlap
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeperated(2,3,3,5);

        //two walls do not touch at all
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeperated(2,3,4,5);

        //one wall fully contained by the other
        //(x,2) (x,4) (x,2) (x,4)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,3,1,5);
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
        assert !edge.areWallsSeperated(4,2,2,4);

        //two walls are overlapping, same length but offset
        //(x,4) (x,2) (x,4) (x,5)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(4,2,3,5);

        //two walls touch at ends but do not overlap
        //(x,3) (x,2) (x,3) (x,5)
        //assert walls are seperated
        assert edge.areWallsSeperated(3,2,3,5);

        //two walls do not touch at all
        //(x,3) (x,2) (x,4) (x,5)
        //assert walls are seperated
        assert edge.areWallsSeperated(3,2,4,5);

        //one wall fully contained by the other
        //(x,3) (x,2) (x,1) (x,5)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(3,2,1,5);

        //////////////
        //switch order of B values

        //two walls are touching and have same length (identical)
        //(x,2) (x,4) (x,4) (x,2)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,4,4,2);

        //two walls are overlapping, same length but offset
        //(x,2) (x,4) (x,5) (x,3)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,4,3,5);

        //two walls touch at ends but do not overlap
        //(x,2) (x,3) (x,5) (x,3)
        //assert walls are seperated
        assert edge.areWallsSeperated(2,3,5,3);

        //two walls do not touch at all
        //(x,2) (x,3) (x,5) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeperated(2,3,5,4);

        //one wall fully contained by the other
        //(x,2) (x,3) (x,5) (x,1)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(2,3,5,1);

        //////////////
        //switch order of both

        //two walls are touching and have same length (identical)
        //(x,4) (x,2) (x,4) (x,2)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(4,2,4,2);

        //two walls are overlapping, same length but offset
        //(x,4) (x,2) (x,5) (x,3)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(4,2,3,5);

        //two walls touch at ends but do not overlap
        //(x,3) (x,2) (x,5) (x,3)
        //assert walls are seperated
        assert edge.areWallsSeperated(3,2,5,3);

        //two walls do not touch at all
        //(x,3) (x,2) (x,5) (x,4)
        //assert walls are seperated
        assert edge.areWallsSeperated(3,2,5,4);

        //one wall fully contained by the other
        //(x,3) (x,2) (x,5) (x,1)
        //assert walls are not seperated
        assert !edge.areWallsSeperated(3,2,5,1);
    }
}