package view;

import java.awt.Color;
import java.awt.Graphics;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Algorithm;
import model.Circle;
import model.Line;
import model.Vector2;

public class DrawingPanel extends JPanel implements Observer {
    private Vector2 pointSet[] = new Vector2[0];
    private double[] range = new double[] { 20, 20 };
    private double[] shift = new double[] { 10, 10 };
    private Algorithm.Type algoType;
    private Circle circle;
    private Vector2[] rectangle;

    public DrawingPanel() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw point set
        for (int i = 0; i < pointSet.length; i++) {
            g.drawOval((int) pointSet[i].getX() - 5, (int) pointSet[i].getY() - 5, 10, 10);
        }

        // Draw enclosing circle
        if (circle != null) {
            int xRadius = (int) (Math.abs(circle.getRadius() / range[0] * getWidth()));
            int yRadius = (int) (Math.abs(circle.getRadius() / range[1] * getHeight()));
            g.drawOval((int) convertCoordinates(circle.getCenter()).getX() - xRadius / 2,
                       (int) convertCoordinates(circle.getCenter()).getY() - yRadius / 2, xRadius, yRadius);
            g.fillOval((int) convertCoordinates(circle.getCenter()).getX() - 5,
                       (int) convertCoordinates(circle.getCenter()).getY() - 5, 10, 10);
        }

        // Draw smallest enclosing rectangle
        if (rectangle != null) {
            for (int i = 0; i < rectangle.length; i++) {
                // lines printing
                /*Vector2 p1 = convertCoordinates(new Vector2(rectangle[i].getPoint().getX() -
                                                   20 * rectangle[i].getUnitVect().getX(),
                                                   rectangle[i].getPoint().getY() -
                                                   20 * rectangle[i].getUnitVect().getY()));
                Vector2 p2 =
                    convertCoordinates(new Vector2(rectangle[i].getPoint().getX() +
                                                   20 * rectangle[i].getUnitVect().getX(),
                                                   rectangle[i].getPoint().getY() +
                                                   20 * rectangle[i].getUnitVect().getY()));*/
                Vector2 p1 = convertCoordinates(rectangle[i]);
                Vector2 p2 = convertCoordinates(rectangle[(i+1)%4]);

                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }
        }
    }

    private Vector2 convertCoordinates(Vector2 input) {
        double x = ((0.5 * input.getX() + shift[0]) / range[0]) * getWidth();
        double y = ((0.5 * input.getY() + shift[1]) / range[1]) * getHeight();
        return new Vector2(x, y);
    }

    private void computeRange(Vector2[] aPointSet) {
        double[] min = new double[2];
        double[] max = new double[2];
        min[0] = aPointSet[0].getX();
        min[1] = aPointSet[0].getY();
        max[0] = min[0];
        max[1] = min[1];
        for (int i = 0; i < aPointSet.length; i++) {
            if (aPointSet[i].getX() < min[0])
                min[0] = aPointSet[i].getX();
            if (aPointSet[i].getY() < min[1])
                min[1] = aPointSet[i].getY();
            if (aPointSet[i].getX() > max[0])
                max[0] = aPointSet[i].getX();
            if (aPointSet[i].getY() > max[1])
                max[1] = aPointSet[i].getY();
        }
        range[0] = max[0] - min[0];
        range[1] = max[1] - min[1];
        shift[0] = -min[0];
        shift[1] = -min[1];
    }

    @Override
    public void update(Observable observable, Object object) {
        algoType = ((Algorithm) observable).getAlgorithmType();
        Vector2[] originalPointSet = ((Algorithm) observable).getPointSet();
        computeRange(originalPointSet);
        pointSet = new Vector2[originalPointSet.length];
        for (int i = 0; i < originalPointSet.length; i++) {
            pointSet[i] = convertCoordinates(originalPointSet[i]);
        }

        switch (algoType) {
        case RECTANGLE:
            circle = null;
            rectangle = (Vector2[]) (((Algorithm) observable).getResult());
            break;
        case CIRCLE:
            circle = (Circle) (((Algorithm) observable).getResult());
            rectangle = null;
            break;
        default:
            System.out.println("Unknown algo type...");
        }

        repaint();
    }
}
