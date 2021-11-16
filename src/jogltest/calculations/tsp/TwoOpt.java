package jogltest.calculations.tsp;
import java.util.Random;
import jogltest.GreyWolf.PointsTree;
import jogltest.calculations.Vector;

public class TwoOpt {

      
   public static Vector operate(Vector route, int i, int k)
   {
        if (route == null)
            return null;
        
        int size = route.getSize();
        if (
                    (i < 0) || (i > size-2)
                ||  (k <= i) || (k > size-1)
           )
        {
            //incorrect values of cutting borders
            return null;
        }
        
        Vector newRoute = new Vector();
        for (int j=0; j<i; j++)
        {
            newRoute.addElement(route.get(j));
        }
        
        for (int j=i; j<=k; j++)
        {
            newRoute.addElement(route.get(i+(k-j)));
        }
        
        for (int j=k+1; j<size; j++)
        {
            newRoute.addElement(route.get(j));
        }
        
        
        return newRoute;
    }
        

    
}
