package jogltest;
import java.awt.DisplayMode;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.graph.curve.opengl.RenderState;
import com.jogamp.graph.font.FontFactory;
import java.awt.Font;
import com.jogamp.graph.geom.SVertex;
import com.jogamp.nativewindow.util.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.awt.TextRenderer;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.jogamp.opengl.glu.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Random;
import jogltest.calculations.Arithmetics;
import jogltest.calculations.EuclideanArithmetics;
import jogltest.calculations.Vector;

public class Simulation implements GLEventListener, KeyListener {
    
   public static final int MAX = 18;
   public static final float SCALE = 0.1f;
   public static DisplayMode dm, dm_old;
   private GLU glu = new GLU();
   private float rquad = 30.0f;  
   private HashMap<Integer, Integer> textures;
   private Arithmetics arithmetics;
   private GreyWolf gw;
   
   public final static String PATH = "digits";
   
   public static enum Ax {
        X,  Y, Z       
   };
      
   public static enum Color {
       RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA, WHITE, GRAY;
   };
   
   private Color[] populationColors;
   
   private GreyWolf.PointsTree pointsLocation;
      
   public void writeText(String text)
   {
       TextRenderer renderer = new TextRenderer(new Font("Verdana", Font.BOLD, 30));
       renderer.beginRendering(900, 700);
       renderer.setColor(java.awt.Color.WHITE);
       renderer.setSmoothing(true);
       
       Point pt = new Point(20, 20);
       renderer.draw(text, (int)(pt.getX()), (int)(pt.getY()));
       renderer.endRendering();
   }
         
   public void switchColor(GL2 gl, Color color)
   {
       switch(color)
       {
           case RED :    gl.glColor3f(1.0f, 0.0f, 0.0f); break;
           case YELLOW : gl.glColor3f(1.0f, 1.0f, 0.0f); break;
           case GREEN :  gl.glColor3f(0.0f, 1.0f, 0.0f); break;
           case CYAN :   gl.glColor3f(0.0f, 1.0f, 1.0f); break;
           case BLUE :   gl.glColor3f(0.0f, 0.0f, 1.0f); break;
           case MAGENTA :gl.glColor3f(1.0f, 0.0f, 1.0f); break;
           case WHITE :  gl.glColor3f(1.0f, 1.0f, 1.0f); break;
           case GRAY :   gl.glColor3f(0.5f, 0.5f, 0.5f); break;
       }
   }
   
   @Override
   public void keyTyped(KeyEvent e){}
   
   @Override
   public void keyPressed(KeyEvent e)
   {
       char pressed = e.getKeyChar();
       
       if (pressed == KeyEvent.VK_R)
       {
           pointsLocation = gw.nextIterationSimulated(MAX);
       }
       else if (pressed == KeyEvent.VK_L)
       {
           pointsLocation = gw.previousIterationSimulated(MAX);
       }
   }
   
   @Override
   public void keyReleased(KeyEvent e)
   {}   
        
   public void drawNumbersAx(GL2 gl, Ax ax)
   {
       float transX = 0.0f, transY = 0.0f, transZ = 0.0f;
       final float TRANS = -0.07f;
       
       switch (ax)
       {
           case X : transX = TRANS; break;
           case Y : transY = TRANS; break;
           case Z : transZ = TRANS; break;       
       }
       
       gl.glTranslatef(transX, transY, transZ);
       gl.glPushMatrix();
             
       for (int i = 0; i <= MAX; i++)
       {
           drawNumber(gl, ax, i);
       }
       gl.glPopMatrix();
       gl.glPushMatrix();
       gl.glTranslatef(-0.1f, 0.0f, 0.0f);
       for (int i = -1; i >= -MAX; i--)
       {
           drawNumber(gl, ax, i);
       }
       
       gl.glPopMatrix();
   }
   
   public void drawNumber(GL2 gl, Ax ax, int number)
   {
       if ((number < -99) || (number > 99))
           return;
       
       float step = 0.1f;
       if (number < 0)
            step = -step;
       
       float transX = 0.0f, transY = 0.0f, transZ = 0.0f;
       
       switch (ax)
       {
           case X : transX = step; break;
           case Y : transY = step; break;
           case Z : transZ = step; break;       
       }

       if ((number >= -9) && (number <= 9))
       {
           drawDigit(gl, number);
           gl.glTranslatef(transX, transY, transZ);
       }
       else if ((Math.abs(number) >= 10) || (Math.abs(number) < 20))
       {
           int rest = Math.abs(number % 10);
           int nmb = (number > 0) ? 1 : -1;
           gl.glTranslatef(-0.03f, 0.0f, 0.0f);
           drawDigit(gl, nmb);
           gl.glTranslatef(0.03f, 0.0f, 0.0f);
           drawDigit(gl, rest);
           
           gl.glTranslatef(transX, transY, transZ);
       }
   }
   
   public void drawDigit(GL2 gl, int digit){
        
        if ((digit < -9) || (digit > 9))
            return;
        
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL2.GL_GREATER, 0.0f);
                
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        int texture = textures.get(digit);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f);
        
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(0.05f, 0.0f, 0.0f);
                
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(0.05f, 0.05f, 0.0f);

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.05f, 0.0f);
        gl.glEnd();
        
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL2.GL_ALPHA_TEST);
        gl.glDisable(GL2.GL_BLEND);
   }
        
     
   public void drawCoordinateSystem(GL2 gl)
   {
      gl.glPushMatrix();
       
      //draw axes for the system of coordinates
      gl.glColor3f(1.0f, 1.0f, 1.0f);
      gl.glBegin(gl.GL_LINES);
      gl.glVertex3f( 0.0f, -2.0f, 0.0f);
      gl.glVertex3f( 0.0f, 2.0f, 0.0f);
      gl.glEnd();      
            
      gl.glColor3f(0.5f, 0.0f, 0.5f);
      gl.glBegin(gl.GL_LINES);
      gl.glVertex3f(-2.0f, 0.0f, 0.0f);
      gl.glVertex3f(2.0f, 0.0f, 0.0f);
      gl.glEnd();
                 
      gl.glColor3f(0.0f, 0.5f, 0.5f);
      gl.glBegin(gl.GL_LINES);
      gl.glVertex3f(0.0f, 0.0f, -2.0f);
      gl.glVertex3f(0.0f, 0.0f, 2.0f);
      gl.glEnd();
      //end drawing system of coordinates
      
      //arrows to finish the axes
      gl.glColor3f(0.5f, 0.0f, 0.5f);
      gl.glBegin(gl.GL_TRIANGLES);
      gl.glVertex3f(2.0f, 0.0f, 0.0f);
      gl.glVertex3f(1.9f, 0.1f, 0.0f);
      gl.glVertex3f(1.9f, -0.1f, 0.0f);
      gl.glEnd();

      float f = -2.0f;
      
      while (f < 1.9f)
      {
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(f, -0.05f, 0.0f);
        gl.glVertex3f(f, 0.05f, 0.0f);
        gl.glEnd();
        
        f += 0.1f;
      }
      
      //grot na osi Y
      gl.glColor3f(1.0f, 1.0f, 1.0f);
      gl.glBegin(gl.GL_TRIANGLES);
      gl.glVertex3f(0.0f, 1.9f, -0.1f);
      gl.glVertex3f(0.0f, 2.0f, 0.0f);
      gl.glVertex3f(0.0f, 1.9f, 0.1f);
      gl.glEnd();
      
      f = -2.0f;
      
      while (f < 1.9f)
      {
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(0.0f, f, -0.05f);
        gl.glVertex3f(0.0f, f, 0.05f);
        gl.glEnd();
        
        f += 0.1f;
      }      
      
      //grot na osi Z
      gl.glColor3f(0.0f, 0.5f, 0.5f);
      gl.glBegin(gl.GL_TRIANGLES);
      gl.glVertex3f(0.0f, -0.1f, 1.9f);
      gl.glVertex3f(0.0f, 0.0f, 2.0f);
      gl.glVertex3f(0.0f, 0.1f, 1.9f);
      gl.glEnd();
      
      f = -2.0f;
      while (f < 1.9f)
      {
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(0.0f, -0.05f, f);
        gl.glVertex3f(0.0f, 0.05f, f);
        gl.glEnd();
        
        f += 0.1f;
      }
      
      gl.glPopMatrix();
   }
      
   @Override
   public void display( GLAutoDrawable drawable ) {
	
      final GL2 gl = drawable.getGL().getGL2();
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
      gl.glLoadIdentity();
      
      gl.glTranslatef( 0f, -1.0f, -3.0f ); 
      //gl.glViewport(0, 0, 0, 0);
      gl.glScalef(1.0f, 1.0f, 1.0f);
      // Rotate The Simulation On X, Y & Z
      gl.glRotatef(rquad, 0.2f, 1.0f, 0.2f); 
      drawCoordinateSystem(gl);
      
      drawNumbersAx(gl, Ax.X);
      drawNumbersAx(gl, Ax.Y);
      drawNumbersAx(gl, Ax.Z);
      
      Vector<Float> v = new Vector();
      v.addElement(0.05f);
      v.addElement(0.1f);
      v.addElement(0.1f);
      drawPoint(gl, v, Color.YELLOW);
      
      simulateSingleIteration(gl);
      writeText("Iteracja " + (gw.getPointedIteration() + 1));

      rquad += 0.60f;
   }
   
   @Override
   public void dispose( GLAutoDrawable drawable ) {}
                
   public Color randomColor(GL2 gl)
   {
       Random rand = new Random();
       Color color;
       do
       {
            int r = rand.nextInt(8);
            Color[] colors = Color.values();
            color = colors[r];
       }
       while (color == Color.YELLOW);
       return color;
   }
   
   public void setPopulationColors(GL2 gl)
   {
       int populationSize = gw.getPopulationSize();
       this.populationColors = new Color[populationSize];
       
       for (int i=0; i<populationSize; i++)
       {
           populationColors[i] = randomColor(gl);
       }
   }
   
   public Color getPopulationColor(int i)
   {
       int populationSize = gw.getPopulationSize();
       if ((i < 0) || (i >= populationSize))
           return null;
       
       return populationColors[i];
   }
   
   public void simulateSingleIteration(GL2 gl)
   {
        int populationSize = gw.getPopulationSize();
        for (int i=0; i<populationSize; i++)
        {
            //Vector<Float> xi = (Vector<Float>)(pointsLocation.get(i));
            
            Vector<Float> xi = gw.getXi(i);
            Color color = getPopulationColor(i);
            drawPoint(gl, xi, color);
        }
   }   
     
   
   public boolean drawPoint(GL2 gl, Vector<Float> point, Color color)
   {
       float max = (color == Color.YELLOW) ? 1.0f : MAX;
       
       float x = Math.abs((float)point.get(0)) * max;
       float y = Math.abs((float)point.get(1)) * max;
       float z = Math.abs((float)point.get(2)) * max;
       
       System.out.println("x = "+x+", y = "+y+", z = " + z);
       
       if ((x < -MAX) || (x > MAX))
       {
           return false;
       }
       if ((y < -MAX) || (y > MAX))
       {
           return false;
       }
       if ((z < -MAX) || (z > MAX))
       {
           return false;
       }
       
       float scale = (color == Color.YELLOW) ? 1.0f : SCALE;
       x = x * scale;
       y = y * scale;
       z = z * scale;
       
       
       GLUquadric quadric = glu.gluNewQuadric();
       glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
       glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
             
       gl.glPushMatrix();
       gl.glColor3f(1.0f, 0.0f, 0.0f);
       gl.glTranslatef(x, y, z);
       
       switchColor(gl, color);
       glu.gluSphere(quadric, 0.02f, 256, 128);
       gl.glPopMatrix();
       
       glu.gluDeleteQuadric(quadric);
       
       return true;
   }
   
   
   public int loadTexture(GL2 gl, String path, String file)
   {
        try {
            File im = new File(path + "\\" + file);
            Texture t = TextureIO.newTexture(im, true);
            return t.getTextureObject(gl);
       }
       catch(IOException e){
         e.printStackTrace();
         return Integer.MAX_VALUE;
      }
   }
   
   public boolean loadTextures(GL2 gl, String path)
   {
       textures = new HashMap<Integer, Integer>();
       
       for (int i=-9; i<=9; i++)
       {
            int tex = loadTexture(gl, path, "" + i + ".png");
            if (tex == Integer.MAX_VALUE)
                return false;
            textures.put(i, tex);
       }
       
       return true;
   }
   
   @Override
   public void init( GLAutoDrawable drawable ) {
	
      final GL2 gl = drawable.getGL().getGL2();
      
      gl.glShadeModel( GL2.GL_SMOOTH );
      gl.glClearColor( 0f, 0f, 0f, 0f );
      gl.glClearDepth( 1.0f );
      gl.glEnable( GL2.GL_DEPTH_TEST );
      gl.glDepthFunc( GL2.GL_LEQUAL );
      gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );

      loadTextures(gl, PATH);
      arithmetics = EuclideanArithmetics.getInstance();
      gw = new GreyWolf(Float.class, arithmetics, 15, 100, 3, false, true);
      setPopulationColors(gl);           
          
      
      //gw.nextIteration(MAX);
      gw.nextIterationSimulated(MAX);
      
   }
      
   @Override
   public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {
	
      // TODO Auto-generated method stub
      final GL2 gl = drawable.getGL().getGL2();
      if( height <= 0 )
         height = 1;
			
      final float h = ( float ) width / ( float ) height;
      gl.glViewport( 0, 0, width, height );
      gl.glMatrixMode( GL2.GL_PROJECTION );
      gl.glLoadIdentity();
		
      glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
      gl.glMatrixMode( GL2.GL_MODELVIEW );
      gl.glLoadIdentity();
   }
      
   public static void main( String[] args ) {
	
      final GLProfile profile = GLProfile.get( GLProfile.GL2 );
      GLCapabilities capabilities = new GLCapabilities( profile );
      
      // The canvas
      final GLCanvas glcanvas = new GLCanvas( capabilities );
      Simulation cube = new Simulation();
		
      glcanvas.addGLEventListener( cube );
      glcanvas.addKeyListener(cube);
      glcanvas.setSize( 1000, 600 );
		
      final JFrame frame = new JFrame ( " Multicolored cube" );
      frame.getContentPane().add( glcanvas );
      frame.setSize( frame.getContentPane().getPreferredSize() );
      frame.setVisible( true );
      final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true);
      animator.start();
   }
	
}