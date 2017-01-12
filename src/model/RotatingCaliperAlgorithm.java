package model;

import java.awt.Point;

import java.util.Observable;

public class RotatingCaliperAlgorithm extends Observable implements Algorithm {
    private Vector2[] pointSet;
    private Vector2[] hull;
    private Line[] contactPoints;
    private Vector2[] minRectangle;
    private int[] contactIndex;

    public RotatingCaliperAlgorithm() {
        super();
    }

    @Override
    public void execute() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        contactPoints = new Line[4]; // point and unit vector
        contactIndex = new int[4];

        // Initialization step
        for (int i = 0; i < hull.length; i++) {
            if (hull[i].getX() < minX) {
                minX = hull[i].getX();
                contactPoints[0] = new Line(hull[i], new Vector2(0, 1));
                contactIndex[0] = i;
            }
            if (hull[i].getY() > maxY) {
                maxY = hull[i].getY();
                contactPoints[1] = new Line(hull[i], new Vector2(1, 0));
                contactIndex[1] = i;
            }
            if (hull[i].getX() > maxX) {
                maxX = hull[i].getX();
                contactPoints[2] = new Line(hull[i], new Vector2(0, -1));
                contactIndex[2] = i;
            }
            if (hull[i].getY() < minY) {
                minY = hull[i].getY();
                contactPoints[3] = new Line(hull[i], new Vector2(-1, 0));
                contactIndex[3] = i;
            }
        }

        minRectangle = new Vector2[4];
        minRectangle[0] = new Vector2(minX, minY);
        minRectangle[1] = new Vector2(maxX, minY);
        minRectangle[2] = new Vector2(maxX, maxY);
        minRectangle[3] = new Vector2(minX, maxY);

        // Loop through the convex hull
        while (contactPoints[0].getUnitVect().crossProduct(new Vector2(-1,0)) > 0) {
            double smallestAngle = -Double.MAX_VALUE;
            double currentAngle;
            int reference = -1; // Need to initialize to remove compiler error...

            // Min theta
            for (int i = 0; i < 4;
                 i++) {
                //System.out.println("current vect for " + i + " is " + contactPoints[i].getUnitVect());
                Vector2 nextEdge =
  new Vector2(hull[contactIndex[i]], hull[(contactIndex[i] - 1 + hull.length) % hull.length]);
                currentAngle = nextEdge.dotProduct(contactPoints[i].getUnitVect()) / nextEdge.norm();
                //System.out.println("current angle for " + i + " is " + currentAngle);

                if (currentAngle > smallestAngle) {
                    reference = i;
                    smallestAngle = currentAngle;
                }
            }
            //System.out.println("smallest angle is " + smallestAngle + " for " + reference);

            // Update (one of) the reference
            Vector2 updateEdge =
                new Vector2(hull[contactIndex[reference]],
                            hull[(contactIndex[reference] - 1 + hull.length) % hull.length]);
            updateEdge.scale(1 / updateEdge.norm());
            contactPoints[reference].setUnitVect(updateEdge);
            contactIndex[reference] = (contactIndex[reference] - 1 + hull.length) % hull.length;
            contactPoints[reference].setPoint(hull[contactIndex[reference]]);

            // Rotate calipers / Update others
            for (int i = 0; i < 4; i++) {
                if (i == reference)
                    continue;
                Vector2 nextEdge =
                    new Vector2(hull[contactIndex[i]], hull[(contactIndex[i] - 1 + hull.length) % hull.length]);
                nextEdge.scale(1 / nextEdge.norm());
                if (contactPoints[i].getUnitVect().crossProduct(nextEdge) == smallestAngle) {
                    // Same angle than reference
                    contactPoints[i].setUnitVect(nextEdge);
                    contactIndex[i] = (contactIndex[reference] - 1 + hull.length) % hull.length;
                    contactPoints[i].setPoint(hull[contactIndex[i]]);
                } else {
                    // Update wrt reference
                    switch (reference - i) {
                    case 1:
                    case -3:
                        contactPoints[i].setUnitVect(contactPoints[reference].getUnitVect().orthogonalClockWise().opposite());
                        break;
                    case 2:
                    case -2:
                        contactPoints[i].setUnitVect(contactPoints[reference].getUnitVect().opposite());
                        break;
                    case -1:
                    case 3:
                        contactPoints[i].setUnitVect(contactPoints[reference].getUnitVect().orthogonalClockWise());
                        break;
                    }
                }
            }

            // Compute enclosing rectangle
            Vector2[] currentRectangle = new Vector2[4];
            for (int i = 0; i < 4; i++) {
                Vector2 p2 = contactPoints[i].getPoint();
                Vector2 p1 = contactPoints[(i + 1) % 4].getPoint();
                Vector2 v2 = contactPoints[i].getUnitVect();
                Vector2 v1 = contactPoints[(i + 1) % 4].getUnitVect();

                Vector2 p1p2 = new Vector2(p1, p2);
                double k = v1.crossProduct(p1p2);

                currentRectangle[i] = new Vector2(p2.getX() - k * v2.getX(), p2.getY() - k * v2.getY());
            }
            // Update min rect
            Vector2 c1 = currentRectangle[0];
            Vector2 c2 = currentRectangle[1];
            Vector2 c3 = currentRectangle[2];
            Vector2 m1 = minRectangle[0];
            Vector2 m2 = minRectangle[1];
            Vector2 m3 = minRectangle[2];
            if (minRectangle == null ||
                2 * c1.distance(c2) + 2 * c2.distance(c3) < 2 * m1.distance(m2) + 2 * m2.distance(m3))
                minRectangle = currentRectangle;
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public void setInputs(Vector2[] aPointSet) {
        pointSet = aPointSet;
        MelkmansAlgorithm CHComputer = new MelkmansAlgorithm();
        CHComputer.setPoints(aPointSet);
        CHComputer.execute();
        Object[] tmp = CHComputer.getHull();
        hull = new Vector2[tmp.length - 1];
        for (int i = 0; i < tmp.length - 1; i++) {
            hull[i] = (Vector2) tmp[i];
        }
        minRectangle = null;

        setChanged();
        notifyObservers();
    }

    @Override
    public Algorithm.Type getAlgorithmType() {
        return Type.RECTANGLE;
    }

    @Override
    public Vector2[] getPointSet() {
        return hull;
    }

    @Override
    public Object getResult() {
        return minRectangle;
    }
}
