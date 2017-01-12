package model;

public class Circle {
    private Vector2 center;
    private double radius;
    
    public Circle() {
        center = new Vector2(0,0);
        radius = Double.MAX_VALUE;
    }
    
    public Circle(Vector2 a, Vector2 b) {
        center = new Vector2((a.getX()+b.getX())/2, (a.getY()+b.getY())/2);
        radius = a.distance(b)/2;
    }
    
    public Circle(Vector2 a, Vector2 b, Vector2 c) {
        double yDelta1 = b.getY() - a.getY();
        double xDelta1 = b.getX() - a.getX();
        double yDelta2 = c.getY() - b.getY();
        double xDelta2 = c.getX() - b.getX();
        
        double slope1 = yDelta1/xDelta1;
        double slope2 = yDelta2/xDelta2;
        
        double centerX = (slope1*slope2*(a.getY()-c.getY()) + slope2*(a.getX()+b.getX()) - slope1*(b.getX()+c.getX()))
                         / (2*(slope2 - slope1));
        double centerY = -1*(centerX - (a.getX()+b.getX())/2)/slope1 + (a.getY()+b.getY())/2;
        center = new Vector2(centerX, centerY);
        radius = 1.001*center.distance(a); // Because of approximations, the two other points are often out of the circle
    }
    
    public Vector2 getCenter() {
        return center;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public boolean contains(Vector2 p) {
        return (center.distance(p) <= radius);
    }
    
    @Override
    public String toString() {
        return "The center is: " + center + " and the radius is " + radius;
    }
}
