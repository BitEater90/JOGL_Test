package jogltest.calculations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import jogltest.GreyWolf;

public abstract class Arithmetics implements Comparator<GreyWolf.FitnessItem> {
       
    public abstract Vector addVectors(Vector v1, Vector v2);
    public abstract Vector subtractVectors(Vector v1, Vector v2);
    public abstract Vector scalarMultiply(Vector v1, Vector v2);
    public abstract Vector product(Vector v1, Vector v2);
    public abstract Vector multiplyByScalar(float scalar, Vector v);
    public abstract float fitness(Vector v);
    public abstract float vectorLength(Vector v);
    
    public abstract Object addElements(Object el1, Object el2);
    public abstract Object subtractElements(Object el1, Object el2);
    public abstract Object multiplyElements(Object el1, Object el2);
    public abstract Object divideElements(Object el1, Object el2);
    public abstract Object abs(Object el);
    
    public abstract Object meanElements(ArrayList<Object> v);
    
    public Vector meanVector(ArrayList<Vector> list)
    {
        if (list == null)
            return null;
        
        int vectorsNumber = list.size();
        if (vectorsNumber == 0)
            return null;
        
        if (!Utils.isSizeOfVectorsEqual(list))
            return null;
        
        int vectorLength = list.get(0).getSize();
        
        Vector v = new Vector();
        
        //iterate through each element of a vector (assuming that all vectors
        //in the list are of the same length and calculate a mean value
        //of all elements laying at the same position throughout the vector list
        for (int elIndex = 0; elIndex < vectorLength; elIndex++)
        {
            ArrayList<Object> elements = new ArrayList();
            for (int vectorI = 0; vectorI < vectorsNumber; vectorI++)
            {
                elements.add(list.get(vectorI).get(elIndex));
            }
            Object meanElement = meanElements(elements);
            v.addElement(meanElement);
        }
        
        return v;
    }
    
        
}
