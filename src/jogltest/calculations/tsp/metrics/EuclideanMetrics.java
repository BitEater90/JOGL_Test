package jogltest.calculations.tsp.metrics;
import jogltest.calculations.tsp.*;

/**
 *
 * @author Bartosz Krawczyk
 */
public class EuclideanMetrics extends Metrics {
        
   
    @Override
    public double distance(Point p1, Point p2)
    {
        if (!checkPoints(p1, p2))
            return 0.0;
        
        int n = p1.getSize();
        Point.Coords coord1 = p1.getCoords();
        Point.Coords coord2 = p2.getCoords();
        
        double total = 0.0;
        
        try
        {
            for (int i=0; i<n; i++)
            {
                double diff = coord1.getCoord(i) - coord2.getCoord(i);
                double el = Math.pow(diff, 2.0);
                total += el;
            }
            
            return Math.sqrt(total);
        }
        catch(Point.Coords.CoordUndefinedException cde)
        {
            return 0.0;
        }
        
    }
    
}
