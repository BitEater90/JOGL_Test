package jogltest.calculations.tsp;

import java.util.ArrayList;

/**
 *
 * @author Bartosz Krawczyk
 */
public class Point {
    
    private int id;
    private Coords coords;
    
    public int getSize()
    {
        return coords.size();
    }
    
    public boolean equals(Point p)
    {
        if (p == null)
            return false;
        if (p.getId() != id)
            return false;
        
        if (!this.coords.equals(p.getCoords()))
            return false;
        
        return true;
    }
    
    public static class Coords
    {
        
        public static class CoordUndefinedException extends Exception
        {
            public CoordUndefinedException()
            {
                super();
                System.err.println("Demanded coord is undefined");
            }
        }
        
        private ArrayList<Double> coords;
        
        public boolean equals(Coords c)
        {
            if (c == null)
                return false;
            if (coords.size() != c.size())
                return false;
            
            int size = coords.size();
            try
            {
                for (int i=0; i<size; i++)
                {
                    if (this.coords.get(i) != c.getCoord(i))
                        return false;
                }
            }
            catch(CoordUndefinedException cue)
            {
                return false;
            }
            
            return true;
        }
        
        public double getLat() throws CoordUndefinedException
        {
            if (coords.size() < 1)
                throw new CoordUndefinedException();
            return coords.get(0);
        }
        
        public double getLng() throws CoordUndefinedException
        {
            if (coords.size() < 2)
                throw new CoordUndefinedException();
            return coords.get(1);
        }
        
        public double getCoord(int i) throws CoordUndefinedException
        {
            if ((i < 0) || (i >= coords.size()))
                throw new CoordUndefinedException();
            return coords.get(i);
        }
        
        private Coords(){}
    
        public Coords(Double[] c)
        {
            coords = new ArrayList();
            
            if (c == null)
                return;
            
            if (c.length == 0)
                return;
            
            for (int i=0; i<c.length; i++)
            {
                coords.add(c[i]);
            }
        }
        
        public int size()
        {
            return this.coords.size();
        }
        
        public void addCoord(double coord)
        {
            this.coords.add(coord);
        }
                
    }
    
    /*
    class Coords
    {
        private double lng, lat;
        
        public Coords(double lat, double lng)
        {
            this.lng = lng;
            this.lat = lat;
        }
        
        public void setLng(double lng)
        {
            this.lng = lng;
        }
        
        public void setLat(double lat)
        {
            this.lat = lat;
        }
        
        public double getLng()
        {
            return lng;
        }
        
        public double getLat()
        {
            return lat;
        }
    }*/
    
    public Point(int id, Coords coords)
    {
        this.id = id;
        this.coords = coords;
    }
    
    
    public int getId()
    {
        return this.id;
    }
    
    public Coords getCoords()
    {
        return this.coords;
    }
}
