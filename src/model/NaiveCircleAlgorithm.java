package model;

import java.util.Observable;

public class NaiveCircleAlgorithm extends Observable implements Algorithm {
    private Vector2[] pointSet;
    private Circle minCircle;
    
    public NaiveCircleAlgorithm() {
        super();
    }

    @Override
    public void execute() {
        boolean ok;
        Vector2 a = new Vector2(0,0), b = new Vector2(0,0), c = new Vector2(0,0), x = new Vector2(0,0);
        
        // Two points circle
        for (int i = 0; i < pointSet.length; i++) {
            a = pointSet[i];
            for (int j = i+1; j < pointSet.length; j++) {
                b = pointSet[j];
                Circle circ = new Circle(a, b);
                // Check if points lies in the circle
                ok = true;
                for (int k = 0; k < pointSet.length; k++) {
                    x = pointSet[k];
                    if (x == a || x == b)
                        continue;
                    if (!circ.contains(x)) {
                        ok = false;
                        break;
                    }
                    if(k == 19 && pointSet[19] != x)
                        System.out.println("nikoumouk");
                }
                if (ok && (minCircle == null || circ.getRadius() < minCircle.getRadius())) {
                    minCircle = circ;
                }
            }
        }

        // Three points circle
        for(int i = 0; i < pointSet.length; i++) {
            a = pointSet[i];
            for (int j = i+1; j < pointSet.length; j++) {
                b = pointSet[j];
                for(int k = j+1; k < pointSet.length; k++) {
                    c = pointSet[k];
                    Circle circ = new Circle(a, b, c);
                    
                    // Check if points lies in the circle
                    ok = true;
                    for (int l = 0; l < pointSet.length; l++) {
                        x = pointSet[l];
                        if (x == a || x == b || x == c)
                            continue;
                        if (!circ.contains(x)) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok && (minCircle == null || circ.getRadius() < minCircle.getRadius())) {
                        minCircle = circ;
                    }
                }
            }
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public void setInputs(Vector2[] aPointSet) {
        pointSet = aPointSet;
        minCircle = null;
        setChanged();
        notifyObservers();
    }

    @Override
    public Algorithm.Type getAlgorithmType() {
        return Type.CIRCLE;
    }

    @Override
    public Vector2[] getPointSet() {
        return pointSet;
    }
    
    public Object getResult() {
        return minCircle;
    }
}
