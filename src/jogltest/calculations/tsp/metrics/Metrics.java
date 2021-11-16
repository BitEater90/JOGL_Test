package jogltest.calculations.tsp.metrics;

import jogltest.calculations.tsp.Point;

/**
 *
 * @author Bartosz Krawczyk
 */
public abstract class Metrics {
    
    public abstract double distance(Point p1, Point p2);
    
    class InequalSizesException extends Exception
    {}
    
    private boolean equalValues(Point p1, Point p2) throws InequalSizesException
    {
        try
        {
            int size = p1.getSize();
            Point.Coords p1Coords = p1.getCoords();
            Point.Coords p2Coords = p2.getCoords();
            
            for (int i=0; i<size; i++)
            {
                if (p1Coords.getCoord(i) != p2Coords.getCoord(i))
                    return false;
            }
            
            return true;
        }
        catch(Point.Coords.CoordUndefinedException e)
        {
            throw new InequalSizesException();
        }
    }
    
    public boolean checkPoints(Point p1, Point p2)
    {
        if ((p1 == null) || (p2 == null))
            return false;
        if ((p1.getSize() == 0) || (p2.getSize() == 0))
            return false;
        if (p1.getSize() != p2.getSize())
            return false;
        try
        {
            if (equalValues(p1, p2))
            {
                return false;
            }
        }
        catch(Metrics.InequalSizesException ise)
        {
            return false;
        }
        
        return true;
    }

    
}
