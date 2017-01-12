package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

public class MiniDiscAlgorithm extends Observable implements Algorithm{
    private Vector2[] pointSet;
    private Circle minCircle;
    
    public MiniDiscAlgorithm() {
        super();
    }

    @Override
    public void execute() {
        ArrayList<Vector2> pointsToPick = new ArrayList<Vector2>();
        ArrayList<Vector2> usedPoints = new ArrayList<Vector2>();        
        pointsToPick.addAll(Arrays.asList(pointSet));
        
        Vector2 p1 = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
        Vector2 p2 = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
        Circle circle = new Circle(p1, p2);
        
        usedPoints.add(p1);
        usedPoints.add(p2);

        while (!pointsToPick.isEmpty()) {
            Vector2 p = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
            if(!circle.contains(p))
                circle = miniDiscWithPoint(usedPoints, p);
            usedPoints.add(p);
        }
        
        minCircle = circle;
        setChanged();
        notifyObservers();
    }
    
    private Circle miniDiscWithPoint(ArrayList<Vector2> inputPoints, Vector2 toConsider) {
        ArrayList<Vector2> pointsToPick = new ArrayList<Vector2>();
        pointsToPick.addAll(inputPoints);
        ArrayList<Vector2> usedPoints = new ArrayList<Vector2>();
        
        Vector2 p1 = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
        Circle circle = new Circle(p1, toConsider);
        usedPoints.add(p1);
        
        while (!pointsToPick.isEmpty()) {
            Vector2 p = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
            if(!circle.contains(p))
                circle = miniDiscWith2Points(usedPoints, p, toConsider);
            usedPoints.add(p);
        }
        return circle;
    }
    
    private Circle miniDiscWith2Points(ArrayList<Vector2> inputPoints, Vector2 toConsider1, Vector2 toConsider2) {
        ArrayList<Vector2> pointsToPick = new ArrayList<Vector2>();
        pointsToPick.addAll(inputPoints);
        
        Circle circle = new Circle(toConsider1, toConsider2);
        
        while (!pointsToPick.isEmpty()) {
            Vector2 p = pointsToPick.remove((int) (Math.random() * pointsToPick.size()));
            if(!circle.contains(p))
                circle = new Circle(p, toConsider1, toConsider2);
        }
        return circle;
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

    @Override
    public Object getResult() {
            return minCircle;
    }
}
