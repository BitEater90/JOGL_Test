package jogltest.calculations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import jogltest.GreyWolf;

public class EuclideanArithmetics extends Arithmetics {
    
    private static EuclideanArithmetics instance;
    
    EuclideanArithmetics(){}
    
    public static Arithmetics getInstance()
    {
        if (instance == null)
            instance = new EuclideanArithmetics();
        return instance;
    }
    

    public int compare(Map.Entry<Integer, Object> v1, Map.Entry<Integer, Object> v2) {
        float d1 = (float)v1.getValue();
        float d2 = (float)v2.getValue();

        return (d1 > d2) ? 1 : (d1 == d2) ? 0 : -1;
    }
    
    @Override
    public int compare(GreyWolf.FitnessItem f1, GreyWolf.FitnessItem f2) {
        float d1 = (float)f1.getFitness();
        float d2 = (float)f2.getFitness();
        return (d1 > d2) ? 1 : (d1 == d2) ? 0 : -1;
    }

    @Override
    public Vector addVectors(Vector v1, Vector v2) {
        
        if (!equalSizes(v1, v2))
            return null;
        
        Vector v = new Vector();
        
        for (int i=0; i<v1.getSize(); i++)
        {
            v.addElement((float)v1.get(i) + (float)v2.get(i));
        }
        
        return v;        
    }
    
    public boolean equalSizes(Vector v1, Vector v2)
    {
        return (v1.getSize() == v2.getSize());
    }

    @Override
    public Vector subtractVectors(Vector v1, Vector v2) {
        
        Vector v = new Vector();
        if (!equalSizes(v1, v2))
            return null;
        
        for (int i=0; i<v1.getSize(); i++)
        {
            v.addElement((float)v1.get(i) - (float)v2.get(i));
        }
        
        return v;  
    }
    
    /* mnozenie skalarne - wynikiem bedzie wektor jednoelementowy
       zawierajacy skalar - liczbe bedaca wynikiem mnozenia
    */
    @Override 
    public Vector scalarMultiply(Vector v1, Vector v2)
    {
        if (v1.getSize() != v2.getSize())
            return null;
        Vector v = new Vector();
        int size = v1.getSize();
        float total = 0.0f;
        
        for (int i=0; i<size; i++)
        {
            total += (float)(v1.get(i)) * (float)(v2.get(i));
        }
        v.addElement(total);
        return v;
    }
    
    @Override
    public Vector multiplyByScalar(float scalar, Vector v)
    {
        if ((v == null) || (v.getSize() ==0))
            return null;
        int size = v.getSize();
        for (int i=0; i<size; i++)
        {
            float el = (float)v.get(i);
            el *= scalar;
            v.set(i, el);
        }
        return v;
    }
    
    /*
    iloczyn wektorowy - wynikiem bedzie wektor prostopadly do plaszczyzny
    rozpinanej przez wektory podane przez parametry
    */
    @Override
    public Vector product(Vector v1, Vector v2)
    {
        
        return null;
    }
    
    //minimize length of vector
    @Override
    public float fitness(Vector v)
    {
        return vectorLength(v);
    }
    
    //wyliczenie długości wektora
    @Override
    public float vectorLength(Vector v)
    {
        float total = 0.0f;
        int size = v.getSize();
        for (int i=0; i<size; i++)
        {
            float elem = (float)v.get(i);
            total += Math.pow(elem, 2.0);
        }
        
        return (float)Math.sqrt(total);
    }
    
    @Override
    public Object addElements(Object el1, Object el2)
    {
        return (float)el1 + (float)el2;
    }
    
    @Override
    public Object subtractElements(Object el1, Object el2)
    {
        return (float)el1 - (float)el2;
    }
    
    @Override
    public Object multiplyElements(Object el1, Object el2)
    {
        return (float)el1 * (float)el2;
    }
    
    @Override
    public Object divideElements(Object el1, Object el2)
    {
        return (float)el1 / (float)el2;
    }
    
    @Override
    public Object abs(Object el)
    {
        return Math.abs((float)el);
    }
    
    @Override
    public Object meanElements(ArrayList<Object> list) {
        if (list == null)
            return null;
        int size = list.size();
        if (size == 0)
            return null;
        float total = 0.0f;
        for (int i=0; i<size; i++)
        {
            total = (float)addElements(total, list.get(i));
        }
        
        return divideElements(total, (float)size);
    }
}
