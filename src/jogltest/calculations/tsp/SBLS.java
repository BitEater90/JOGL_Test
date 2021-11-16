package jogltest.calculations.tsp;
import java.util.Random;
import jogltest.GreyWolf.PointsTree;
import jogltest.calculations.Vector;
/*
public class SBLS {


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
*/
