package jogltest.calculations.tsp.metrics;
import jogltest.calculations.tsp.*;
import jogltest.calculations.tsp.Point.Coords.CoordUndefinedException;

/**
 *
 * @author Bartosz Krawczyk
 */
public class CrystallographiclMetrics extends Metrics {
        
   
    @Override
    public double distance(Point p1, Point p2)
    {
        if (!checkPoints(p1, p2))
            return 0.0;
        
        int n = p1.getSize();
        
        if (n != 3)
        {
            return 0.0;
        }
        
        Point.Coords coord1 = p1.getCoords();
        Point.Coords coord2 = p2.getCoords();
        
        try
        {
            //double dMin1, dMax1, dAbs;
            double distp, distc, distt, cost;
            double phiV, chiV, twothV;
            double phiW, chiW, twothW;
            
            phiV = coord1.getCoord(0);
            chiV = coord1.getCoord(1);
            twothV = coord1.getCoord(2);
            
            phiW = coord2.getCoord(0);
            chiW = coord2.getCoord(1);
            twothW = coord2.getCoord(2);
            
            distp = Math.min(Math.abs(phiV - phiW), Math.abs(Math.abs(phiV)-phiW)-360.0*Math.pow(10.0, 0.0));
            distc = Math.abs(chiV - chiW);
            distt = Math.abs(twothV - twothW);
            double cost_p1 = Math.max(distp/(1.00*Math.pow(10.0, 0.0)), distc/(1.0*Math.pow(10.0, 0)));
            cost = Math.max(cost_p1, distt/1.00*Math.pow(10.0, 0));
            double integralCost = 100.0*Math.pow(10.0, 0.0)*cost+0.5*Math.pow(10.0,0);
            return integralCost;
            
        }
        catch(CoordUndefinedException cude)
        {
            return 0.0;
        }
                
    }
    
}
