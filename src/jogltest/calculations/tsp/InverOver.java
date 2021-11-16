package jogltest.calculations.tsp;
import java.util.Random;
import jogltest.GreyWolf.PointsTree;
import jogltest.calculations.Vector;

public class InverOver {

    protected final static double P = 0.5;
    protected final static int NOT_FOUND = -1;
            
    private static int findCityIndex(Vector route, int city)
    {
        int size = route.getSize();
        for (int i=0; i<size; i++)
        {
            int selectedCity = (int)route.get(i);
            if (selectedCity == city)
                return i;        
        }
        
        return NOT_FOUND;
    }       
    
    protected static int nextCityIndex(Vector route, int C)
    {
        int Cindex;
        Cindex = findCityIndex(route, C);
        if (Cindex == NOT_FOUND)
            return NOT_FOUND;
        
        int size = route.getSize();
        int nextIndex = NOT_FOUND;
        if (Cindex == (size-1))
        {
            return 0;
        }
        else
        {
            return (Cindex + 1);
        }
    }
    
    protected static int nextCity(Vector route, int C)
    {
        int nextIndex = nextCityIndex(route, C);
        return (int)route.get(nextIndex);
    }
    
    protected static boolean areCitiesNeighbors(Vector route, int C, int Cprim)
    {
        int Cindex;
        Cindex = findCityIndex(route, C);
        if (Cindex == NOT_FOUND)
            return false;
        
        int size = route.getSize();
        int neighbor = NOT_FOUND;
        
        //check next element
        if (Cindex == (size-1))
        {
            neighbor = (int)route.get(0);
        }
        else
        {
            neighbor = (int)route.get(Cindex + 1);
        }
        
        if (neighbor == Cprim)
            return true;
        
        //check previous element
        if (Cindex == 0)
        {
            neighbor = (int)route.get(size-1);
        }
        else
        {
            neighbor = (int)route.get(Cindex -1);
        }
        
        if (neighbor == Cprim)
            return true;
        
        return false;
    }
    
    /*
    protected static boolean areCitiesNeighbors(Vector route, int C, int Cprim)
    {
                
        int Cindex = findCityIndex(route, C);
        if (Cindex == NOT_FOUND)
            return false;
        
        int size = route.getSize();
        int neighbor = NOT_FOUND;
        
        //check next element
        if (Cindex == (size-1))
        {
            neighbor = (int)route.get(0);
        }
        else
        {
            neighbor = (int)route.get(Cindex + 1);
        }
        
        if (neighbor != Cprim)
            return false;
        
        //check previous element
        if (Cindex == 0)
        {
            neighbor = (int)route.get(size-1);
        }
        else
        {
            neighbor = (int)route.get(Cindex -1);
        }
        
        if (neighbor != Cprim)
            return false;
        
        return true;
    }
    */
   
   public static TSPVector operate(PointsTree population)
   {
        int size = population.size();
        try
        {
            for (int i=0; i<size; i++)
            {
               Vector routei = (Vector)population.get(i);
               Vector route = (Vector)routei.clone();

               Random r = new Random();
               int routeLength = route.getSize();
               int Cindex = r.nextInt(routeLength);

               System.out.println("Cindex = " + Cindex);
               route.get(Cindex);
               int C = (int)route.get(Cindex);
               int Cprim;

               while (true)
               {
                   double ran = r.nextDouble();             

                   if (ran < P)
                   {
                       int CprimIndex;
                       
                       do
                       {
                            CprimIndex = r.nextInt(routeLength);
                            Cprim = (int)route.get(CprimIndex);
                       }
                       while(C == Cprim);                     
                       System.out.println("CprimIndex = " + CprimIndex);

                   }
                   else
                   {
                       int routeIndex = -1;
                       do
                       {
                           routeIndex = r.nextInt(population.size());
                       }
                       while (routeIndex == i);

                       Vector selectedRoute = (Vector)population.get(routeIndex);
                       Cprim = nextCity(selectedRoute, C);

                  }

                  if (areCitiesNeighbors(route, C, Cprim))
                       break;

                  int cIndex = nextCityIndex(route, C);
                  int cPrimIndex = nextCityIndex(route, Cprim)-1;
                  if (cPrimIndex == -1)
                      cPrimIndex = routeLength-1;

                  Vector<Integer> section = route.section(cIndex, cPrimIndex);
                  section.invert();
                  cIndex++;
                  if (cIndex >= routeLength)
                      cIndex = 0;
                  route.replaceSection(section, cIndex);
                  C = Cprim;
                }

               if (route.getSize() < routei.getSize())
               {
                   routei = (Vector)route.clone();
               }
            }
        }
        catch(CloneNotSupportedException cnse)
        {
            return null;
        }
        
        return null;
    }
        

    
}
