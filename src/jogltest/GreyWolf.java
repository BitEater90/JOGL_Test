package jogltest;
import jogltest.calculations.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import jogltest.calculations.Arithmetics;
import jogltest.calculations.EuclideanArithmetics;
import jogltest.calculations.tsp.InverOver;

public class GreyWolf<T> {
    
    //reference to selected arithmetics class
    private Arithmetics arithmetics;
    
    //register action of the algorithm
    private boolean registerOn;
    
    //variable determines if test mode is launched or not
    private boolean testMode;
    
    //constant values of random variables in test mode
    private Float constR1, constR2;
            
    //coefficients
    private Float a, r1, r2;
    
    //registry of applied values of random variables 'r1' & 'r2'
    private ArrayList<Float> r1Log, r2Log;
            
    //step indicating the value for diminishing variable 'a' in each iteration
    private float step;
    
    //dimension of vectors containing potential solutions of the problem
    private int dimension;
    
    //vectors of coefficients
    //private Vector<Double> A, C;
    private Vector<T> 
            A1, A2, A3,
            C1, C2, C3;
    
    //vectors of best achieved solutions
    private Vector<T> 
            
            //osobnik alfa
            Xalpha,
            
            //osobnik beta
            Xbeta,
            
            //osobnik delta
            Xdelta;
    
    //vectors of auxiliary calculations
    private T Dalpha, Dbeta, Ddelta;
    private Vector<T> X1, X2, X3;
    
    public class PointsTree extends TreeMap<Integer, Vector<T>>
    {}
    
    //whole population of grey wolves
    //private TreeMap<Integer, Vector<T>> X;
    
    private PointsTree X;
    
    //registry of iterations
    private ArrayList<PointsTree> iterations;
    
    public static class FitnessItem
    {
        private int id;
        private float fitness;
        
        public FitnessItem(int id, float fitness)
        {
            this.id = id;
            this.fitness = fitness;
        }
        
        public void setId(int id)
        {
            this.id = id;
        }
        
        public void setFitness(float fitness)
        {
            this.fitness = fitness;
        }
        
        public int getId()
        {
            return id;
        }
        
        public float getFitness()
        {
            return this.fitness;
        }
        
        public boolean eq(FitnessItem f)
        {
            return ((this.id == f.getId()) && (this.fitness == f.getFitness()));
        }
    }
    
    //map of fitness for all grey wolves
    //private TreeMap<Integer, Double> fitness;
    private ArrayList<FitnessItem> fitness;        
    
    //map assigning identifiers to place names
    private TreeMap<Integer, String> placeNames;
       
    //number of current iteration
    float t;
    
    //number of iterations of algorithm
    int iterationsNumber;
    
    //index of pointed iteration
    int pointedIteration;
    
    //number of all solutions of problem
    int populationSize;
    
    Class<T> cl;
         
    //set of constants restricting boundaries for variables 
    //controlling action of algorithm
    final int MAX_ITERATIONS_NUMBER = 1000000000;
    final int STANDARD_ITERATIONS_NUMBER = 100;
    final int MAX_POPULATION_SIZE = 10000;
    final int MIN_POPULATION_SIZE = 4;
    final int STANDARD_POPULATION_SIZE = 20;
    final int STANDARD_DIMENSION = 3;
              
    public GreyWolf(Class<T> cl, Arithmetics arithmetics, int populationSize, int iterationsNumber, int dimension, 
            boolean testMode, boolean simulationMode)
    {
        setInitialVariables(cl, arithmetics, populationSize, iterationsNumber, dimension, testMode, simulationMode);
    }
    
    public PointsTree getPopulation()
    {
        return X;
    }
    
    private void setInitialVariables(Class<T> cl, Arithmetics arithmetics, int populationSize, int iterationsNumber, int dimension,
            boolean testMode, boolean simulationMode)
    {
        //this.registerOn = registerOn;
        this.cl = cl;
        this.testMode = testMode;
        this.pointedIteration = -1;
        if (simulationMode)
            this.iterations = new ArrayList<PointsTree>();
        else
            this.iterations = null;
        
        this.arithmetics = arithmetics;
        if ((populationSize < MIN_POPULATION_SIZE) || (populationSize > MAX_POPULATION_SIZE))
        {
            this.populationSize = STANDARD_POPULATION_SIZE;
        }
        else
        {
            this.populationSize = populationSize;
        }
        
        if ((iterationsNumber <= 0) || (iterationsNumber > MAX_ITERATIONS_NUMBER))
            this.iterationsNumber = STANDARD_ITERATIONS_NUMBER;
        else
            this.iterationsNumber = iterationsNumber;
        if (dimension <= 0)
            this.dimension = STANDARD_DIMENSION;
        else
            this.dimension = dimension;
     
    }
    
    public int getPointedIteration()
    {
        return pointedIteration;
    }
    
    public void testModeSettings(Float a, Float r1, Float r2, Float step)
    {
        testModeSettings(a, r1, r2);
        if (this.testMode)
            this.step = step;
    }
    
    public void testModeSettings(Float a, Float r1, Float r2)
    {
        if (this.testMode)
        {
            this.a = a;
            this.constR1 = r1;
            this.constR2 = r2;
        }
    }       
    
    /**
     * 
     * @param arithmetics
     * @param populationSize
     * @param iterationsNumber
     * @param dimension -> dimension of vector containing a potential solution
     * @param cl -> class of used type
     */
    public GreyWolf(Class<T> cl, Arithmetics arithmetics, int populationSize, int iterationsNumber, int dimension)
    {
        setInitialVariables(cl, arithmetics, populationSize, iterationsNumber, dimension, false, false);
    }
    
    private GreyWolf(){}
            
    //initialization of a given vector
    public Vector<T> initializeVector(int dimension, float max)
    {
        Vector v = new Vector();
        return v.randomVector(cl, dimension, max);
    } 
    
    public void setX(PointsTree p)
    {
        X = p;
    }
    
    public void reset(ArrayList<Float> X)
    {
        if (X == null)
            return;

        int size = X.size();
        for (int i=0; i<size; i++)
        {
            X.set(i, 0.0f);
        }
    }
    
    /*initialization of all necessary variables
        n -> number of possible solutions    
    */
    protected void initialize(float max)
    {
        a = 2.0f;
        step = a / (float)iterationsNumber;
        
        r1Log = new ArrayList<Float>();
        r2Log = new ArrayList<Float>();
        
        initializePopulation(max);
        updateCoefficients();
                
   }
    
    protected void initializeMaps()
    {
        X = new PointsTree();
        //fitness = new TreeMap<Integer, Double>();
        fitness = new ArrayList<FitnessItem>();
        placeNames = new TreeMap<Integer, String>();
    }
    
        
    public void setPopulation(ArrayList<Vector> population)
    {
        if (population == null)
            return;
        if (population.isEmpty())
            return;
        int size = population.size();
        populationSize = size;
        initializeMaps();
        
        for (int i=0; i<populationSize; i++)
        {
            Vector<T> v = population.get(i);
            X.put(i, v);
            //fitness.put(i, 0.0);
            fitness.add(new FitnessItem(i, 0.0f));
            placeNames.put(i, "");
        
        }
        
        if (this.arithmetics == EuclideanArithmetics.getInstance())
            fitness();
    }
    
    public int getPopulationSize()
    {
        return this.populationSize;
    }
    
    protected void initializePopulation(float max)
    {
        initializeMaps();
        
        for (int i=0; i<populationSize; i++)
        {
            Vector<T> v = initializeVector(this.dimension, max);
            
            X.put(i, v);
            fitness.add(new FitnessItem(i, 0.0f));
            placeNames.put(i, "");
        }
    }
    
    //return random floating point number from range <0, 1>
    protected double rand()
    {
        return Math.random();
    }
    
    protected float fitness(Vector<T> v)
    {
        return arithmetics.fitness(v);
    }
    
    protected void fitness()
    {
        if (populationSize == 0)
            return;
                
        fitness.clear();
        for (int i=0; i<populationSize; i++)
        {
            float f = fitness(X.get(i));
            fitness.add(new FitnessItem(i, f));
        }
    }
    
    public ArrayList<Float> getFitness()
    {
        ArrayList<Float> list = new ArrayList<Float>();
        int size = fitness.size();
        for (int i=0; i<size; i++)
        {
            //double el = fitness.get(i);
            float el = fitness.get(i).getFitness();
            list.add(el);
        }
        return list;
    }
    
    protected Vector<T> calculateA(int dimension)
    {
        Vector<T> v = new Vector();
        if (testMode)
            r1 = constR1;
        else
            r1 = (float)rand();
        T el = (T)arithmetics.subtractElements(
                arithmetics.multiplyElements(arithmetics.multiplyElements(2.0f, a), r1),
                a);
        //double el = (2.0 * a * r1 - a);
        for (int i=0; i<dimension; i++)
        {
            v.addElement(el);
        }
        
        return v;
    }
    
    protected Vector<T> calculateC(int dimension)
    {
        Vector<T> v = new Vector();
        if (testMode)
            r2 = constR2;
        else
            r2 = (float)rand();
        
        T el = (T)arithmetics.multiplyElements(2.0f, r2);
        //double el = (2.0 * r2);
        for (int i=0; i<dimension; i++)
        {
            v.addElement(el);
        }
        //r2Log.add(r2);
        return v;
    }
    
    protected Vector<T> calculateD(int dimension, Vector<T> xBest, Vector<T> x)
    {
        Vector<T> v = new Vector();
        Vector<T> C = calculateC(dimension);
        for (int i=0; i<dimension; i++)
        {
            T el = (T)arithmetics.abs(
                    arithmetics.subtractElements(arithmetics.multiplyElements(C.get(i), xBest.get(i)),
                                                 x.get(i))
            );
            //double el = Math.abs((double)C.get(i) * (double)xBest.get(i) - (double)x.get(i));
            v.addElement(el);
        }
        return v;
    }
   
    protected void updateCoefficients()
    {
        A1 = calculateA(dimension);
        C1 = calculateC(dimension);
        A2 = calculateA(dimension);
        C2 = calculateC(dimension);
        A3 = calculateA(dimension);
        C3 = calculateC(dimension);  
    }
    
    protected Vector<T> calculateAuxiliarySolution(int dimension, Vector<T> Xbest, Vector<T> A, Vector<T> D)
    {
        Vector<T> v = new Vector();
       
        for (int i=0; i<dimension; i++)
        {
            T el = (T)arithmetics
                    .subtractElements(Xbest.get(i),
                                     arithmetics.multiplyElements(A.get(i), D.get(i)));
            v.addElement(el);
        }
        return v;
    }
   
    protected Vector<T> calculateMeanSolution()
    {
        Vector<T> v = new Vector();
        
        if ((X1 == null) || (X2 == null) || (X3 == null))
            return null;
        
        int x1Size = X1.getSize();
        int x2Size = X2.getSize();
        int x3Size = X3.getSize();
        
        if (!((x1Size == x2Size) && (x1Size == x3Size) && (x2Size == x3Size)))
            return null;
        
        ArrayList<Vector> vectors = new ArrayList();

        vectors.add(X1);
        vectors.add(X2);
        vectors.add(X3);
        
        v = arithmetics.meanVector(vectors);
        
        return v;
    }
    
    
    protected void calculateAuxiliarySolutions(Vector<T> Xi)
    {
        Vector<T> Dalpha = calculateD(dimension, Xalpha, Xi);
        X1 = calculateAuxiliarySolution(dimension, Xalpha, A1, Dalpha);

        Vector<T> Dbeta = calculateD(dimension, Xbeta, Xi);
        X2 = calculateAuxiliarySolution(dimension, Xbeta, A2, Dbeta);

        Vector<T> Ddelta = calculateD(dimension, Xdelta, Xi);
        X3 = calculateAuxiliarySolution(dimension, Xdelta, A3, Ddelta);
    }
    
    protected void updateSearchAgent(int i)
    {
        //element X[i] w chwili t
        Vector<T> Xi = X.get(i);
        calculateAuxiliarySolutions(Xi);
    
        //obliczenie elementu X[i] w chwili t+1
        Xi = calculateMeanSolution();
        X.replace(i, Xi);
    }
    
    protected void updateAllSearchAgents()
    {
        int size = X.size();
        for (int i=0; i<size; i++)
        {
            updateSearchAgent(i);
        }
    }
    
    protected void updateBestSearchAgents()
    {
        List<GreyWolf.FitnessItem> sorted = sortAscending(this.fitness);
        int xAlphaId, xBetaId, xDeltaId;

        int size = sorted.size();
        if (size == 1)
        {
            xAlphaId = sorted.get(0).getId();
            Xalpha = new Vector(X.get(xAlphaId));
            Xbeta = new Vector(X.get(xAlphaId));
            Xdelta = new Vector(X.get(xAlphaId));
        }
        else
        {
            xAlphaId = sorted.get(0).getId();
            xBetaId = sorted.get(1).getId();
            xDeltaId = sorted.get(2).getId();
            
            Xalpha = new Vector(X.get(xAlphaId));
            Xbeta = new Vector(X.get(xBetaId));
            Xdelta = new Vector(X.get(xDeltaId));
        }
           
    }
    
    public ArrayList<FitnessItem> sortAscending(ArrayList<FitnessItem> list)
    {
        list.sort((Comparator)this.arithmetics);
        return list;    
    }
    
   
    public void singleIteration()
    {
        updateAllSearchAgents();
        updateCoefficients();
        fitness();
        updateBestSearchAgents();
            
        a -= step;
    
    }
    
    public double getCoefficienta()
    {
        return a;
    }
    
    public Vector<T> getXalpha()
    {
        return Xalpha;
    }
    
    public Vector<T> getXBeta()
    {
        return Xbeta;
    }
    
    public Vector<T> getXdelta()
    {
        return Xdelta;
    }
    
    public Vector<T> getX1(){ return X1; }
    public Vector<T> getX2(){ return X2; }
    public Vector<T> getX3(){ return X3; }
    
    public Vector<T> getXi(int i)
    {
        int size = X.size();
        if ((i<0) || (i>=size))
            return null;
        return X.get(i);
    }
    
    public Vector<T> getA(int i)
    {
        switch(i)
        {
            case 1 : return A1;
            case 2 : return A2;
            case 3 : return A3;
            default : return null;
        }
    }
    
    public Vector<T> getC(int i)
    {
        switch(i)
        {
            case 1 :  return C1;
            case 2 :  return C2;
            case 3 :  return C3;
            default : return null;
        }
    }
    
    public void preparation(float max)
    {
        initialize(max);
        fitness();
        updateBestSearchAgents();
                
        t = 0;
    }
    
    public boolean nextIteration(float max)
    {
        if (t == 0)
            preparation(max);
        
        if (t >= iterationsNumber)
            return false;
        
        singleIteration();
        t++;
        return true;
    }
    
    public PointsTree nextIterationSimulated(float max)
    {
        pointedIteration++;
        if (pointedIteration >= t)
        {
            if (t == 0)
                preparation(max);
        
            if (t >= iterationsNumber)
            {
                pointedIteration--;
                return null;
            }
            
            singleIteration();
            saveIteration();
        }
        
        return this.iterations.get(pointedIteration);
    }
        
    
    public PointsTree previousIterationSimulated(float max)
    {
        pointedIteration--;
        if (pointedIteration < 0)
            pointedIteration = 0;
       
        return this.iterations.get(pointedIteration);
    }
    
    public void saveIteration()
    {
        if (this.iterations != null)
        {
            iterations.add(X);
        }
    }
    
        
    public void run(float max)
    {
        preparation(max);
        while (t < iterationsNumber)
        {
            singleIteration();
            t++;
        }
    }
        
    
}
