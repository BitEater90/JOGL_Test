package jogltest.calculations.tsp;
import org.apache.commons.collections4.map.MultiKeyMap;
import jogltest.calculations.Vector;
import jogltest.calculations.tsp.metrics.Metrics;

/**
 *
 * @author Bartosz Krawczyk
 */
public class DistanceMatrix {
        
    private MultiKeyMap<Integer, Double> matrix;
    private Metrics metrics;
    
    protected DistanceMatrix(Metrics metrics){
        this.metrics = metrics;
    }
    
    public MultiKeyMap<Integer, Double> getMatrix()
    {
        return this.matrix;
    }
    
    public DistanceMatrix(Metrics metrics, Vector<Point> points)
    {
        this.metrics = metrics;
        matrix = calculateDistanceMatrix(points);
    }
    
    protected MultiKeyMap<Integer, Double> calculateDistanceMatrix(Vector<Point> points)
    {
        int size = points.getSize();
        MultiKeyMap<Integer, Double> m = new MultiKeyMap();
                
        for (int i=0; i<size; i++)
        {
            for (int j=0; j<size; j++)
            {
                if (i == j)
                {
                    Point p = (Point)points.get(i);
                    int id = p.getId();
                    m.put(id, id, 0.0);
                }
                else
                {
                    Point p1 = (Point)points.get(i);
                    Point p2 = (Point)points.get(j);
                    
                    double d = metrics.distance(p1, p2);
                    m.put(p1.getId(), p2.getId(), d);
                }
            }
        }
        
        return m;
        
    }
        
}
