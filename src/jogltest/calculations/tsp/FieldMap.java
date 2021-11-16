package jogltest.calculations.tsp;
import java.util.TreeMap;
import jogltest.calculations.tsp.Parser.Field;

/**
 *
 * @author Bartosz Krawczyk
 */
public class FieldMap<K, V> extends TreeMap {
    
    class Element
    {
        private int startIndex, valueIndex;
        private String value;
        
        private Element(){}
        
        public Element(int startIndex, int valueIndex, String value)
        {
            this.startIndex = startIndex;
            this.valueIndex = valueIndex;
            this.value = value;
        }
        
        public int getStartIndex()
        {
            return startIndex;
        }
        
        public int getValueIndex()
        {
            return valueIndex;
        }
        
        public void setValue(String v)
        {
            value = v;
        }
        
        public String getValue()
        {
            return value;
        }
    }
    
    public FieldMap(){}
    
    public void addElement(String key, String value, int startIndex, int valueIndex)
    {
        Parser.Field f = new Parser.Field(key);
        if (startIndex < 0 )
            startIndex = 0;
        this.put(key, new Element(startIndex, valueIndex, value));
        
    }
    
}
