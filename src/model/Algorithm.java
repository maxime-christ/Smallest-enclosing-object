package model;

public interface Algorithm {
    public enum Type {
        RECTANGLE, CIRCLE
    }
    
    public void execute();
    public void setInputs(Vector2[] aPointSet);
    public Type getAlgorithmType();
    public Vector2[] getPointSet();
    public Object getResult();
}
