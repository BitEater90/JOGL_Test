package jogltest.calculations.tsp;
import org.apache.commons.collections4.map.MultiKeyMap;
import jogltest.calculations.Vector;

/**
 * @author Bartosz Krawczyk
 */

public class TSPVector extends Vector<Integer> {
    
    private int size;
    
    public boolean equals(TSPVector other)
    {
        if (other == null)
            return false;
        int size = other.size;
        if (size == 0)
            return false;
        for (int i=0; i<size; i++)
        {
            int el = (int)other.get(i);
            if (el != (int)elements.get(i))
                return false;
        }
        return true;
    }
                
    public TSPVector(){}
    
    public TSPVector(int[] ids)
    {
        super();
        if (ids == null)
        {
            this.size = 0;
            return;
        }
        
        if (ids.length == 0)
        {
            this.size = 0;
            return;
        }
        
        this.size = ids.length;
        
        for (int i=0; i<size; i++)
        {
            this.addElement((Integer)ids[i]);
        }
    }
    
    public TSPVector(Vector<Point> points)
    {
        super();
        if (points == null)
        {
            this.size = 0;
            return;
        }
        
        if (points.getSize() == 0)
        {
            this.size = 0;
            return;
        }
        
        this.size = points.getSize();
        
        for (int i=0; i<size; i++)
        {
            this.addElement(((Point)points.get(i)).getId());
        }
    }
               
    public TSPVector(Point[] points)
    {
        super();
        if (points == null)
        {
            this.size = 0;
            return;
        }
        
        if (points.length == 0)
        {
            this.size = 0;
            return;
        }
        
        this.size = points.length;
        
        for (int i=0; i<size; i++)
        {
            this.addElement(points[i].getId());
        }
    }
    
    public double distanceOnEdge(DistanceMatrix matrix, int vertex1Index, int vertex2Index)
    {
        int size = elements.size();
        if ((vertex1Index < 0) || (vertex1Index >= size) || (vertex2Index < 0) || (vertex2Index >= size))
            throw new IndexOutOfBoundsException();
        
        int id1 = (int)(get(vertex1Index));
        int id2 = (int)(get(vertex2Index));
        
        MultiKeyMap<Integer, Double> m = matrix.getMatrix();
        return m.get(id1, id2);
    }
    
    public double totalDistance(DistanceMatrix matrix)
    {
        int size = elements.size();
        double total = 0.0;
        
        for (int i=0; i<size-1; i++)
        {
            double d = distanceOnEdge(matrix, i, i+1);
            total += d;
        }
        
        total += distanceOnEdge(matrix, size-1, 0);
        return total;
    }
    
    @Override
    public Vector randomVector(Class cl, int size, float max){ return new Vector();}
    
}
