package jogltest.calculations.tsp;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jogltest.calculations.Vector;
import jogltest.calculations.tsp.metrics.EuclideanMetrics;
import jogltest.calculations.tsp.metrics.Metrics;

/**
 *
 * @author Bartosz Krawczyk
 */
public class Parser {
    
    private BufferedReader in;
    private String text;
    
    //map of fields containing pairs of a field name (keys) and value (values)
    private FieldMap<String, FieldMap.Element> fields;
    
    public static class Field
    {
        public static final String NAME = "NAME:";
        public static final String COMMENT = "COMMENT:";
        public static final String TYPE = "TYPE:";
        public static final String DIMENSION = "DIMENSION:";
        public static final String EDGE_WEIGHT_TYPE = "EDGE_WEIGHT_TYPE:";
        
        private String field;
       
        private Field(){}
        
        public Field(String field)
        {
            this.field = field;
        }
        
        public String getField()
        {
            return this.field;
        }
    }
    
    private Parser(){}
    
    public Parser(String filename){
       
        in = null;
        text = "";
        
        //String -> value of field, 
        //Integer -> start index of value inside block of text
        fields = new FieldMap<String, FieldMap.Element>();
        openFile(filename);       
    }
    
    public String getText()
    {
        return text;
    }
    
    public FieldMap<String, FieldMap.Element> getFields()
    {
        return fields;
    }
    
    public boolean openFile(String filename)
    {
        try
        {
            String currentPath = new java.io.File( "." ).getCanonicalPath() + "\\";
            File file = new File(currentPath + filename);

            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            return true;
        }
        catch(FileNotFoundException fnfe)
        {
            return false;
        }
        catch(IOException ioe)
        {
            return false;
        }
        
    }
    
    protected void addField(String field, String text)
    {
        int pos = readFieldPosition(field, text);
        int length = field.length();
        int endPos = pos + length;
        fields.addElement(field, "", pos, endPos);
    }
    
    protected Point convertToPoint(String element)
    {
        if (element == null)
            return null;
        
        int length = element.length();
        if (length == 0)
            return null;
        
        int spaceIndex = element.indexOf(" ");
        if (spaceIndex <= 0)
            return null;
        
        String substring = element.substring(0, spaceIndex);
        try
        {
            int id = Integer.parseInt(substring);
            ArrayList<Double> coords = new ArrayList();
                        
            String regex = "(-)?[0-9]+(\\.[0-9]+)?";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(element);
            boolean found = false;
            do
            {
                if (spaceIndex + 1 >= length)
                    break;
                
                found = m.find(spaceIndex + 1);
                int startIndex = m.start();
                int endIndex = m.end();
                String f = element.substring(startIndex, endIndex);
                double d = Double.parseDouble(f);
                coords.add(d);
                spaceIndex = endIndex;
            }
            while (found);
            
            Double cd[] = new Double[coords.size()];
            coords.toArray(cd);
            Point.Coords c = new Point.Coords(cd);
            
            Point point = new Point(id, c);
            return point;
        }
        catch (NumberFormatException nfe)
        {
            return null;
        }
        
    }
    
    protected ArrayList<Point> convertToPoints(ArrayList<String> list)
    {
        if (list == null)
            return null;
        if (list.isEmpty())
            return null;
        int size = list.size();
        ArrayList<Point> points = new ArrayList();
        for (int i=0; i<size; i++)
        {
            Point p = convertToPoint(list.get(i));
            points.add(p);
        }
        return points;
    }
    
    protected ArrayList<String> readPoints(String text)
    {
        String regex = "";
        if (fields == null)
            return null;
        if (fields.isEmpty())
            return null;
                
        FieldMap.Element el = (FieldMap.Element)fields.get(Field.DIMENSION);
        String value = el.getValue();
        int dimension = Integer.parseInt(value);
        if (dimension <= 0)
            return null;
        
        int offset = 0;
        ArrayList substrings = new ArrayList();
        
        for (int i=1; i<=dimension; i++)
        {
            //regex = "[0-9]+\\s([0-9]+(\\.[0-9]+)?(\\s)?){2,}";
            String nextStart = (i<dimension) ? ""+(i+1)+"\\s" : "EOF";
            regex = ""+i+"\\s((\\-)?[0-9]+(\\.[0-9]+)?(\\s)?){2}" + nextStart;
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);
            boolean found = m.find(offset);
            int start = m.start();
            int end = m.end();
            end -= nextStart.length();
            String substr = text.substring(start, end);
            substrings.add(substr);
            offset = end;
        }
        
        return substrings;
    }
    
    protected boolean readFields(String text)
    {
        if (text == null)
            return false;
        if (text.length() == 0)
            return false;
        
        fields.clear();
        addField(Field.NAME, text);
        addField(Field.TYPE, text);
        addField(Field.DIMENSION, text);
        addField(Field.EDGE_WEIGHT_TYPE, text);
        addField(Field.COMMENT, text);
      
        Set<Map.Entry<String, FieldMap.Element>> fieldSet = sortDescending(fields);
        Object[] fieldsArray = fieldSet.toArray();
          
        int size = fieldsArray.length;
        for (int i=0; i<size; i++)
        {
            Map.Entry<String, FieldMap.Element> field = (Map.Entry<String, FieldMap.Element>)fieldsArray[i];
            int j = (i<size-1) ? (i+1) : 0;
            Map.Entry<String, FieldMap.Element> field2 = (Map.Entry<String, FieldMap.Element>)fieldsArray[j];
            String subText = "";
            if (j>0)
            {
                subText = text.substring(field.getValue().getValueIndex(), field2.getValue().getStartIndex());
            }
            else
            {
                subText = text.substring(field.getValue().getValueIndex());
                subText = subText.trim();
                int startIndex = 0;
                int tempIndex = -1;
                boolean stopCondition = false;
                
                do
                {
                    tempIndex = subText.indexOf(" ", startIndex);
                    char[] temp = new char[1];
                    temp[0] = subText.charAt(tempIndex+1);
                    String number = new String(temp);
                    if ((subText.charAt(tempIndex-1) != ':') && (number.matches("[0-9]")))
                    {
                        subText = subText.substring(0, tempIndex);
                        stopCondition = true;
                    }
                    else
                    {
                        startIndex = tempIndex + 1;
                    }
                }
                while (!stopCondition);
            }
            
            subText = subText.trim();
                        
            String key = field.getKey();
            FieldMap.Element el = field.getValue();
            el.setValue(subText);
            
            
        }
        return true;
    }
    
    protected int readFieldPosition(String field, String text)
    {
        if ((text == null) || (field == null))
            return -1;
        
        int index = text.indexOf(field);
        return index;
    }
    
    protected String readField(Field field, String text)
    {
        return readField(field, text, 0);
    }
    
    protected String readField(Field field, String text, int startIndex)
    {
        if ((text == null) || (field == null))
            return null;
        
        int index = text.indexOf(field.getField(), startIndex);
        return null;
    }
        
    public String readText()
    {
        try
        {
            if (!in.ready())
                return null;
                     
            text = "";
            String line = null;
            while ((line = in.readLine()) != null)
            {
                text += line;            
            }
        }
        catch(IOException ioe)
        {
            return null;
        }
        
        return text;
    }
    
    public DistanceMatrix buildDistanceMatrix(Metrics metrics)
    {
        Vector<Point> points = parse();
        if (points == null)
            return null;
        
        DistanceMatrix matrix = new DistanceMatrix(metrics, points);
        return matrix;
    }
    
    public Vector<Point> parse()
    {
        if (in == null)
            return null;
        
        readText();
        
        if (!readFields(text))
            return null;
        
        ArrayList<String> lines = readPoints(text);
        ArrayList<Point> points = convertToPoints(lines);
        
        Vector<Point> v = new Vector();
        for (Point p : points)
        {
            v.addElement(p);
        }
                
        return v;
    }
    
    public static Set<Map.Entry<Field, FieldMap.Element>> sortDescending(TreeMap<Field, FieldMap.Element> map)
    {
        Set<Map.Entry<Field, FieldMap.Element>> sortedEntries = new TreeSet<Map.Entry<Field, FieldMap.Element>>(
                new Comparator<Map.Entry<Field, FieldMap.Element>>() {

                    @Override
                    public int compare(Map.Entry<Field, FieldMap.Element> v1, Map.Entry<Field, FieldMap.Element> v2) {
                        int d1 = v1.getValue().getStartIndex();
                        int d2 = v2.getValue().getStartIndex();

                        return (d1 > d2) ? 1 : (d1 == d2) ? 0 : -1;
                    }
        });
        sortedEntries.addAll(map.entrySet());

        List<Integer> values = new LinkedList<Integer>();
        for (Map.Entry<Field, FieldMap.Element> entry : sortedEntries) {
            //values.add(entry.getValue());
            values.add(entry.getValue().getStartIndex());
        }
        
        return sortedEntries;    
    }
    
}
