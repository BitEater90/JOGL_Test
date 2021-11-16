package jogltest.calculations.tsp.metrics;
import jogltest.calculations.tsp.*;
import jogltest.calculations.tsp.Point.Coords.CoordUndefinedException;

/**
 *
 * @author Bartosz Krawczyk
 */
public class GeographicalMetrics extends Metrics {
        
   
    @Override
    public double distance(Point p1, Point p2)
    {
        if (!checkPoints(p1, p2))
            return 0.0;
        
        int n = p1.getSize();
        
        if (n != 2)
        {
            return 0.0;
        }
        
        Point.Coords coord1 = p1.getCoords();
        Point.Coords coord2 = p2.getCoords();
        
        try
        {
            //geographical coordinates for point 1
            double x = coord1.getCoord(0);
            double deg = Math.round(x);
            double min = x - deg;
            double latitude1 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
            double y = coord1.getCoord(1);
            deg = Math.round(y);
            min = y - deg;
            double longitude1 = Math.PI * (deg + 5.0 * min /3.0) / 180.0;
            
            //geographical coordinates for point 2
            x = coord2.getCoord(0);
            deg = Math.round(x);
            min = x - deg;
            double latitude2 = Math.PI * (deg + 5.0 * min / 3.0) / 180.0;
            y = coord2.getCoord(1);
            deg = Math.round(y);
            min = y - deg;
            double longitude2 = Math.PI * (deg + 5.0 * min /3.0) / 180.0;
            
            //calculating the distance
            
            final double RRR = 6378.388;
            double q1, q2, q3;
            q1 = Math.cos(longitude1 - longitude2);
            q2 = Math.cos(latitude1 - latitude2);
            q3 = Math.cos(latitude1 + latitude2);
            
            double distance = RRR * Math.acos(0.5*((1.0+q1)*q2 - (1.0 - q1) * q3)) + 1.0;
            return distance;
            
        }
        catch(CoordUndefinedException cude)
        {
            return 0.0;
        }
                
    }
    
}
