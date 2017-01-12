package model;

public class Line {
    private Vector2 unitVect;
    private Vector2 point;
    
    public Line(Vector2 aPoint, Vector2 aUnitVect) {
        unitVect = aUnitVect;
        point = aPoint;
    }
    
    public Vector2 getUnitVect() {
        return unitVect;
    }
    
    public Vector2 getPoint() {
        return point;
    }

    public void setUnitVect(Vector2 unitVect) {
        this.unitVect = unitVect;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }
}
