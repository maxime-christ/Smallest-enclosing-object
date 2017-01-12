package model;

import java.util.ArrayDeque;

public class MelkmansAlgorithm {
    private Vector2[] pointSet;
    private ArrayDeque<Vector2> hull;
    private int nextPointToConsider;
    private boolean hullComputed;

    public MelkmansAlgorithm() {
        super();
        hull = new ArrayDeque<Vector2>();
        nextPointToConsider = 0;
        hullComputed = false;
    }

    public void execute() {
        while (!hullComputed) {
            Vector2 p3 = pointSet[nextPointToConsider++];
            if (nextPointToConsider == pointSet.length) {
                nextPointToConsider--;
                hullComputed = true;
            }

            Vector2 p2 = hull.pollLast();
            Vector2 p1 = hull.pollLast();
            while (vectProduct(p1, p2, p3) < 0) {
                p2 = p1;
                p1 = hull.pollLast();
            }
            hull.addLast(p1);
            hull.addLast(p2);
            hull.addLast(p3);

            p1 = p3;
            p2 = hull.pollFirst();
            p3 = hull.pollFirst();
            while (vectProduct(p1, p2, p3) < 0) {
                p2 = p3;
                p3 = hull.pollFirst();
            }
            hull.addFirst(p3);
            hull.addFirst(p2);
            hull.addFirst(p1);
        }
    }

    public void setPoints(Vector2[] aPointSet) {
        pointSet = aPointSet;
        mergeSort(pointSet);

        // init algorithm
        Vector2 p1 = pointSet[0];
        Vector2 p2 = pointSet[1];
        Vector2 p3 = pointSet[2];

        if (vectProduct(p1, p2, p3) > 0) {
            hull.addLast(p1);
            hull.addLast(p2);
        } else {
            hull.addLast(p2);
            hull.addLast(p1);
        }
        hull.addFirst(p3);
        hull.addLast(p3);
        nextPointToConsider = 3;
    }

    public Object[] getHull() {
        return hull.toArray();
    }

    public double vectProduct(Vector2 p1, Vector2 p2, Vector2 p3) {
        Vector2 p1p2 = new Vector2(p1, p2);
        Vector2 p1p3 = new Vector2(p1, p3);
        return p1p2.crossProduct(p1p3);
    }
    
    
    private void mergeSort(Vector2[] unsortedArray) {
        Vector2[] tmp = new Vector2[unsortedArray.length];
        mergeSort(unsortedArray, tmp, 0, unsortedArray.length - 1);
    }
            
    private void mergeSort(Vector2[] unsortedArray, Vector2[] tmp, int start, int end) {
        if(start < end) {
            int center = (start + end)/2;
            mergeSort(unsortedArray, tmp, start, center);
            mergeSort(unsortedArray, tmp, center+1, end);
            merge(unsortedArray, tmp, start, center+1, end);
        }
    }
    
    private void merge(Vector2[] unsortedArray, Vector2[] tmp, int left, int right, int rightEnd) {
        int tmpIndex = left;
        int leftEnd = right - 1;
        int objectSorted = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd) {
            if(unsortedArray[left].getX() < unsortedArray[right].getX()) {
                tmp[tmpIndex++] = unsortedArray[left++];
            } else if (unsortedArray[left].getX() == unsortedArray[right].getX()){
                if(unsortedArray[left].getY() < unsortedArray[right].getY()) {
                    tmp[tmpIndex++] = unsortedArray[left++];
                } else {
                    tmp[tmpIndex++] = unsortedArray[right++];
                }
            } else {
                tmp[tmpIndex++] = unsortedArray[right++];
            }
        }
        
        while(left <= leftEnd)
            tmp[tmpIndex++] = unsortedArray[left++];
        while(right <= rightEnd)
            tmp[tmpIndex++] = unsortedArray[right++];
        
        for(int i = 0; i < objectSorted; i++)
            unsortedArray[rightEnd-i] = tmp[rightEnd-i]; // reverse copy because we still know rightEnd but not left
    }
}
