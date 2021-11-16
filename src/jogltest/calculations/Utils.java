package jogltest.calculations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Bartosz Krawczyk
 */
public class Utils {
    
    private Utils(){}
    
    public static class TrendLine
    {
        public class Coefficients
        {
            public float a, b, avgT, avgY;
            
            protected Coefficients(){}
        }
        
        public Coefficients coefficients;
        
        public TrendLine(){
            coefficients = new Coefficients();
        }
        
        protected void calculateAvg(Set<Map.Entry<Integer, Float>> entries)
        {
            Iterator<Map.Entry<Integer, Float>> it = entries.iterator();
            
            float totalT = 0.0f;
            float totalY = 0.0f;
            int size = entries.size();
            
            while (it.hasNext())
            {
                Map.Entry<Integer, Float> el = it.next();
                int iterationNumber = el.getKey();
                float length = el.getValue();
                
                totalT += iterationNumber;
                totalY += length;
            }
            
            float avgT = totalT / (float)size;
            float avgY = totalY / (float)size;
            
            coefficients = new Coefficients();
            coefficients.avgT = avgT;
            coefficients.avgY = avgY;
            
        }
        
        //map scores containts pairs: iterationNumber -> vectorLength
        public Coefficients calculate(TreeMap<Integer, Float> scores)
        {
            coefficients = new Coefficients();
            float total = 0.0f;
            Set<Map.Entry<Integer, Float>> entries = scores.entrySet();
            calculateAvg(entries);
            
            Iterator<Map.Entry<Integer, Float>> it = entries.iterator();
            float totalUp = 0.0f;
            float totalDown = 0.0f;
            int size = entries.size();
            
            while (it.hasNext())
            {
                Map.Entry<Integer, Float> el = it.next();
                int iterationNumber = el.getKey();
                float length = el.getValue();
                
                //totalT += iterationNumber;
                //totalY += length;
                
                float firstFactor = ((float)iterationNumber - coefficients.avgT);
                float up = firstFactor * (length - coefficients.avgY);
                float down = (float)Math.pow(firstFactor, 2.0);
                
                totalUp += up;
                totalDown += down;
            }
            coefficients.a = totalUp / totalDown;
            coefficients.b = coefficients.avgY - coefficients.a * coefficients.avgT;
            return coefficients;
        }
        
    }
    
    public static Set<Map.Entry<Integer, Float>> createSetFromArrays(Integer[] keys, Float[] values)
    {
        if ((keys == null) || (values == null))
            return null;
        
        int keySize = keys.length;
        int valueSize = values.length;
        
        if (keySize != valueSize)
            return null;
        
        if (keySize == 0)
            return null;
        
        TreeMap<Integer, Float> map = new TreeMap<Integer, Float>();
        for (int i=0; i<keySize; i++)
        {
            Integer key = keys[i];
            Float value = values[i];
            map.put(key, value);
            
        }
        Set<Map.Entry<Integer, Float>> set = map.entrySet();
        return set;
    }
    
    public static boolean isSizeOfVectorsEqual(ArrayList<Vector> vectors)
    {
        if (vectors == null)
            return false;
        if (vectors.isEmpty())
            return false;
        int size = vectors.size();
        
        Vector<Float> v1 = vectors.get(0);
        if (v1 == null)
            return false;
        int firstSize = v1.getSize();
        if (firstSize == 0)
            return false;
        
        for (int i=1; i<size; i++)
        {
            Vector<Float> v = vectors.get(i);
            int comparedSize = v.getSize();
            if (comparedSize != firstSize)
                return false;
        }
       
        return true;
    }
    
    public static boolean arrayListsEqual(ArrayList<Float> firstList, ArrayList<Float> otherList, float precision)
    {
        if ((firstList == null) || (otherList == null))
            return false;
        
        int firstListSize = firstList.size();
        int otherListSize = otherList.size();
        if (firstListSize != otherListSize)
            return false;
        
        precision = Math.abs(precision);
        
        for (int i=0; i<firstListSize; i++)
        {
            if (Math.abs((firstList.get(i) - otherList.get(i))) > precision)
                return false;
        }
        
        return true;
    }
    
}
