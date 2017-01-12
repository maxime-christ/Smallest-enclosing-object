package model;

// In this lab I would have needed both Point and Line ojects, but they are basically the same
// (a point's coordinate being the vector from the origin to this point). 
// So this class can either represent a point or a line (unit vector).
public class Vector2 {
    private double x;
    private double y;
    
    public Vector2() {
        x = 0;
        y = 0;
    }
    
    public Vector2(double aX, double aY) {
        x = aX;
        y = aY;
    }
    
    // line passing throug point a and b
    public Vector2(Vector2 a, Vector2 b){
        x = b.x - a.x;
        y = b.y - a.y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double distance(Vector2 other) {
        double x2 = (x-other.getX()) * (x-other.getX());
        double y2 = (y-other.getY()) * (y-other.getY());
        return (Math.sqrt(x2+y2));
    }
    
    public double crossProduct(Vector2 other) {
        return (x*other.y - y*other.x);
    }
    
    public double dotProduct(Vector2 other) {
        return (x*other.x + y*other.y);
    }
    
    public Vector2 opposite() {
        return new Vector2(-x, -y);
    }
    
    public Vector2 orthogonalClockWise() {
        return new Vector2(y, -x);
    }
    
    public double norm() {
        return Math.sqrt(x*x + y*y);
    }
    
    public void scale(double k) {
        x*=k;
        y*=k;
    }
    
    @Override
    public String toString(){
        return "x:" + x + ", y:" + y;
    }
}
