package jogltest.calculations.tsp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jogltest.GreyWolf.PointsTree;
import jogltest.calculations.Vector;

public class InverOverTested {

    protected final static double P = 0.5;
    protected final static int NOT_FOUND = -1;
    protected static boolean testMode = false;
    protected static int Cindex;
    protected static int CprimIndex;
    protected static int Cprim;
    protected static int C;
    protected static int stCprimIndex, stCindex;
    protected static List<Integer> CprimIndices, Cindices;
    protected static double randDouble;
    protected static List<Integer> routeIndices;
    
    public static boolean setTestMode(boolean mode)
    {
        testMode = mode;
        return testMode;
    }       
    
    public static void setRouteIndices(List<Integer> rind)
    {
        routeIndices = new ArrayList();
        int routeSize = rind.size();
        
        for (int i=0; i<routeSize; i++)
        {
            routeIndices.add(rind.get(i));
        }
    }
    
    public static void setIndicesInTestMode(List<Integer> Cin, List<Integer> CprimIn)
    {
        CprimIndices = new ArrayList();
        Cindices = new ArrayList();
        
        int Csize = Cin.size();
        int CprimSize = CprimIn.size();
        
        for (int i=0; i<Csize; i++)
        {
            Cindices.add(Cin.get(i));
        }
        
        for (int i=0; i<CprimSize; i++)
        {
            CprimIndices.add(CprimIn.get(i));
        }
    }
    
    public static void setIndicesInTestMode(int Cin, int CprimIn)
    {
        if (testMode)
        {
            stCprimIndex = CprimIn;
            stCindex = Cin;
        }
    }
    
    public static void setRandomDouble(double rd)
    {
        randDouble = rd;
    }
    
    private static List<Double> doubleList = null;
    private static int randomDoubleIndex = -1;
    
    public static void setRandomDoubles(List<Double> doubles)
    {
        doubleList = new ArrayList();
        int size = doubles.size();
        for (int i=0; i<size; i++)
        {
            doubleList.add(doubles.get(i));
        }
        randomDoubleIndex = -1;
    }
       
           
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
    
    private static int CprimIndexCounter = 0;
    
    private static double randomDouble()
    {
        if (doubleList == null)
            return randDouble;
        
        randomDoubleIndex++;
        return doubleList.get(randomDoubleIndex);
    }
    
    private static int routeIndex = -1;
    
    private static int getRouteIndex()
    {
        return ++routeIndex;
    }
    
    private static void randomCprimIndex(Vector route)
    {
        int routeLength = route.getSize();
        Random r = new Random();
        
        if (testMode)
        {
            if (CprimIndexCounter == 0)
            {
                CprimIndex = stCprimIndex;
                Cprim = (int)route.get(CprimIndex);
            }
            CprimIndexCounter++;
        }
        else
        {
             do
             {
                 CprimIndex = r.nextInt(routeLength);
                 Cprim = (int)route.get(CprimIndex);
             }
             while(C == Cprim);
        }
    }
   
    public static TSPVector operate(PointsTree population)
    {
        int size = population.size();
        TSPVector route = null;
        
        try
        {
            for (int i=0; i<size; i++)
            {
               TSPVector routei = (TSPVector)population.get(i);
               route = (TSPVector)routei.clone();

               Random r = new Random();
               int routeLength = route.getSize();
               int Cindex;
               if (testMode)
               {
                   Cindex = stCindex;
               }
               else
               {
                    Cindex = r.nextInt(routeLength);
               }
               route.get(Cindex);
               C = (int)route.get(Cindex);

               while (true)
               {
                   double ran = randomDouble();
                   //double ran = r.nextDouble();             

                   if (ran < P)
                   {
                       //int CprimIndex;
                       randomCprimIndex(route);                       
                   }
                   else
                   {
                       int routeIndex = -1;
                       int populationSize = population.size();
                       if (populationSize == 1)
                           routeIndex = 0;
                       else
                       {
                            do
                            {
                                if (testMode)
                                {
                                    routeIndex = routeIndices.get(getRouteIndex());
                                }
                                else
                                {
                                    routeIndex = r.nextInt(populationSize);
                                }
                            }
                            while (routeIndex == i);
                       }

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
                  //cIndex++;
                  if (cIndex >= routeLength)
                      cIndex = 0;
                  route.replaceSection(section, cIndex);
                  C = Cprim;
                }

               if (route.getSize() < routei.getSize())
               {
                   routei = (TSPVector)route.clone();
               }
            }
        }
        catch(CloneNotSupportedException cnse)
        {
            return null;
        }
        
        return route;
    }
        

    
}
