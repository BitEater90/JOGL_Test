package jogltest.calculations.tsp.metrics;
import jogltest.calculations.tsp.*;

/**
 *
 * @author Bartosz Krawczyk
 */
public class PseudoEuclideanMetrics extends Metrics {
        
   
    @Override
    public double distance(Point p1, Point p2)
    {
        if (!checkPoints(p1, p2))
            return 0.0;
        
        int n = p1.getSize();
        Point.Coords coord1 = p1.getCoords();
        Point.Coords coord2 = p2.getCoords();
        
        
        try
        {
            double x1, y1, x2, y2;
            double xd, yd, rij, tij, dij;
            
            x1 = coord1.getCoord(0);
            y1 = coord1.getCoord(1);
            x2 = coord2.getCoord(0);
            y2 = coord2.getCoord(1);
            
            xd = x1 - x2;
            yd = y1 - y2;
            rij = Math.sqrt((xd*xd + yd*yd) / 10.0);
            tij = Math.round(rij);
            if (tij < rij) dij = tij + 1.0;
            else dij = tij;
            return dij;
        }
        catch(Point.Coords.CoordUndefinedException cde)
        {
            return 0.0;
        }
        
    }
    
}
