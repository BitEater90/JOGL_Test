package jogltest.calculations;
import java.util.ArrayList;
import java.util.Random;

public class Vector<T> implements Cloneable {
    
    protected ArrayList<T> elements;
    
    public Vector(){
        elements = new ArrayList<T>();
     }
    
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    
    public Vector randomVector(Class<T> cl, int size, float max)
    {
        if (size <= 0)
            return null;
        
        Vector v = new Vector();
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        
        for (int i=0; i<size; i++)
        {
            Object el = null;
            if (cl.isAssignableFrom(Float.class))
            {
                el = r.nextFloat();
            }
            else if (cl.isAssignableFrom(Integer.class))
            {
                el = r.nextInt();
            }
            v.addElement(el);
        }
        return v;
    }
    
    
    public Vector(Vector<T> ob)
    {
        elements = new ArrayList<T>();
        
        for (int i=0; i<ob.getSize(); i++)
        {
            T el = (T)(ob.get(i));
            elements.add(el);
        }
    }
    
    public Vector(T[] ob)
    {
        elements = new ArrayList<T>();
        
        for (int i=0; i<ob.length; i++)
        {
            elements.add(ob[i]);
        }
    }
    
    public void invert()
    {
        int size = elements.size();
        if (size == 0)
            return;
        
        ArrayList<T> temp = new ArrayList();
        for (int i=(size-1); i>=0; i--)
        {
            temp.add(elements.get(i));
        }
        
        elements.clear();
        for(int i=0; i<size; i++)
        {
            elements.add(temp.get(i));
        }
        temp.clear();
        
    }
    
    public void addElement(T el)
    {
        elements.add(el);
    }
    
    public void clear()
    {
        elements.clear();
    }
        
    public int getSize()
    {
        return elements.size();
    }
    
    public Object get(int i)
    {
        if ((i<0) || (i>=elements.size()))
            return null;
        return elements.get(i);
    }
    
    public void set(int i, T el)
    {
        if ((i<0) || (i>=elements.size()))
        {
            elements.set(i, el);
        }
    }
    
    public void replaceSection(Vector<T> section, int start)
    {
        int sectionSize = section.getSize();
        int size = this.getSize();
        
        int index = start;
        for (int i=0; i < sectionSize; i++)
        {
            T el = (T)section.get(i);
            this.elements.set(index, el);
            index ++;
            if (index >= size)
                index = 0;
        }
    }
    
    public Vector<T> section(int start, int stop)
    {
        int size = elements.size();
        if (size == 0)
            return null;
        Vector<T> v = new Vector();
        if (start < stop)
        {
            for (int i=start; i<=stop; i++)
            {
                v.addElement(elements.get(i));
            }
        }
        else if (start > stop)
        {
            for (int i=stop; i<size; i++)
            {
                v.addElement(elements.get(i));
            }
            
            for (int i=0; i<=start; i++)
            {
                v.addElement(elements.get(i));
            }
        }
        else
        {
            v.addElement(elements.get((start)));
        }
       
        return v;
    }
    
    public boolean equals(Vector<Float> X)
    {
        return equals(X, 0.0f);
    }
    
    public boolean equalsInt(Vector<Integer> X)
    {
        if (X == null)
            return false;
        int size = X.getSize();
        for (int i=0; i<size; i++)
        {
            int Xi = (int)X.get(i);
            int it = (int)this.get(i);
            if (Xi != it)
                return false;
        }
        return true;
    }
    
    public boolean equals(Vector<Float> X, float diff)
    {
        if (X == null)
            return false;
        int size = X.getSize();
        for (int i=0; i<size; i++)
        {
            float Xi = (float)X.get(i);
            float it = (float)this.get(i);
            if (Math.abs(Xi - it) > diff)
                return false;
        }
        return true;
    }
       
    
}
