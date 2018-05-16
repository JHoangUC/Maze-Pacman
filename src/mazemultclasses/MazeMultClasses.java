/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazemultclasses;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class MazeMultClasses extends JFrame implements Runnable {
    boolean animateFirstTime = true;
    Image image;
    Graphics2D g;
   Character npc[] = new Character[Character.NUM_NPC];
   Character player ;
   Board Player ;
   Coin coin[] = new Coin[Coin.NUM_COINS];
   Portal portalIn;
   Portal portalOut;
   

    static MazeMultClasses frame1;
    public static void main(String[] args) {
        frame1 = new MazeMultClasses();
        frame1.setSize(Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
    }

    public MazeMultClasses() {

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
//Keys that determine the direction to move the snake.   
if(player.gameOver)
                      return;
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    player.moveRight();

                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                   player.moveLeft();
                    
                }
                if (e.VK_UP == e.getKeyCode())
                {
                     player.moveUp();
                   
                }
                if (e.VK_DOWN == e.getKeyCode())
                {
                     player.moveDown();
                 
                }
                 if (e.VK_SPACE == e.getKeyCode())
                {
                    
                }


                
                repaint();
            }
        });
        init();
        start();
    }




    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || Window.xsize != getSize().width || Window.ysize != getSize().height) {
            Window.xsize = getSize().width;
            Window.ysize = getSize().height;
            image = createImage(Window.xsize, Window.ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

//fill background
        g.setColor(Color.cyan);

        g.fillRect(0, 0, Window.xsize, Window.ysize);

        int x[] = {Window.getX(0), Window.getX(Window.getWidth2()), Window.getX(Window.getWidth2()), Window.getX(0), Window.getX(0)};
        int y[] = {Window.getY(0), Window.getY(0), Window.getY(Window.getHeight2()), Window.getY(Window.getHeight2()), Window.getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
    Player.draw(g,image,gOld, animateFirstTime);
for(int i=0;i<Coin.NUM_COINS;i++)
        {
        coin[i].draw(g);
                } 
for(int i=0;i<Character.NUM_NPC;i++)
        npc[i].draw(g);
        
        player.draw(g);
        portalIn.draw(g);
        
        portalOut.draw(g);
        
//        npc.draw(g);
        
                    if(player.gameOver)
                    {
                        g.setColor(Color.black);
                        g.setFont(new Font("Arial Black",Font.PLAIN,45));
                        g.drawString("Game Over", 100 ,300);
                    }
                        g.setFont(new Font("Arial Black",Font.PLAIN,11));
        gOld.drawImage(image, 0, 0, null);
    }


////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 1/TimeCount.frameRate;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

Board.passageActive=false;
 player = new Character("ADC",Color.red);
// for(int i=0;i<Character.NUM_NPC;i++)

    npc[0] = new Character ("Blitz",Color.orange);
    npc[0].canFill = true;
    npc[0].speed = (int)(.4*TimeCount.frameRate);
    //////////////////////////////////////////////////////
    npc[1] = new Character ("Thresh",Color.black);
    npc[1].speed = (int)(.1*TimeCount.frameRate);
    ///////////////////////////////////////////////////
    npc[2] = new Character ("Taric",Color.magenta);
    npc[2].speed =  (int)(1*TimeCount.frameRate);
    

  for(int i=0;i<Coin.NUM_COINS;i++)
        {
        coin[i] = new Coin();
        }
        
  portalIn = new Portal(true);
  portalOut = new Portal(false);
  player.gameOver=false;
        TimeCount.timeCount = 0;    
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (Window.xsize != getSize().width || Window.ysize != getSize().height) {
                Window.xsize = getSize().width;
                Window.ysize = getSize().height;
            }
        Board.board=Board.boardOrig;
            reset();
        }
        if(player.gameOver)
                      return;
        for( int i=0;i<Character.NUM_NPC;i++)
           {
               if(npc[i].Row == player.Row && npc[i].Column == player.Column)
               {
                    player.gameOver=true;
                  
                         
               }
           }
        for( int a=0;a<Character.NUM_NPC;a++)
           npc[a].realMove();
           player.realMove();
           player.isNpc=false;
           
           for(int i=0;i<Coin.NUM_COINS;i++)
        {
           player.collected(coin[i]);
           for( int a=0;a<Character.NUM_NPC;a++)
           {
           npc[a].collected(coin[i]);
           
           }
        }
           
          player.port(portalIn,portalOut);
          
          
        TimeCount.addTime();   
    }
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }

}

class Window
{
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 500;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 525;
    static int xsize = -1;
    static int ysize = -1;
    
/////////////////////////////////////////////////////////////////////////
    public static int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public static int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public static int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public static int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public static int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }    
}

class Board
{
    public static boolean passageActive=false;
    final static int numRows = 15;
    final static int numColumns = 20;
    final static int PATH = 0;
    final static int WALL = 1;
    final static int NUNU = 2;
    static int board[][] = new int[numRows][numColumns];
    static int boardOrig[][] = {{WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL},
                            {WALL,PATH,WALL,WALL,WALL,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,WALL},
                            {WALL,PATH,PATH,PATH,WALL,PATH,WALL,WALL,WALL,PATH,WALL,PATH,PATH,WALL,WALL,WALL,PATH,WALL,PATH,WALL},
                            {WALL,PATH,WALL,PATH,WALL,PATH,PATH,PATH,PATH,PATH,WALL,WALL,PATH,PATH,WALL,WALL,PATH,WALL,PATH,WALL},
                            {WALL,PATH,WALL,PATH,PATH,PATH,WALL,WALL,WALL,WALL,WALL,WALL,WALL,PATH,WALL,WALL,PATH,WALL,PATH,WALL},
                            {WALL,PATH,WALL,WALL,WALL,PATH,WALL,WALL,WALL,PATH,PATH,PATH,WALL,PATH,PATH,PATH,PATH,WALL,PATH,WALL},
                            {PATH,PATH,WALL,WALL,WALL,PATH,WALL,WALL,PATH,PATH,WALL,PATH,WALL,PATH,WALL,PATH,WALL,WALL,PATH,PATH},
                            {WALL,PATH,WALL,WALL,WALL,PATH,PATH,PATH,PATH,PATH,WALL,PATH,WALL,PATH,WALL,PATH,WALL,WALL,PATH,WALL},
                            {WALL,PATH,WALL,WALL,WALL,PATH,WALL,WALL,PATH,WALL,WALL,PATH,WALL,PATH,WALL,PATH,WALL,WALL,PATH,WALL},
                            {WALL,PATH,PATH,PATH,PATH,PATH,WALL,WALL,PATH,WALL,WALL,PATH,WALL,PATH,WALL,PATH,PATH,PATH,PATH,WALL},
                            {WALL,PATH,WALL,WALL,WALL,WALL,WALL,WALL,PATH,WALL,WALL,PATH,PATH,PATH,WALL,WALL,WALL,WALL,PATH,WALL},
                            {WALL,PATH,WALL,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,WALL,WALL,WALL,PATH,PATH,PATH,PATH,WALL},
                            {WALL,PATH,PATH,PATH,WALL,WALL,WALL,WALL,PATH,WALL,WALL,WALL,WALL,PATH,PATH,PATH,WALL,WALL,WALL,WALL},
                            {WALL,WALL,WALL,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,PATH,WALL,WALL,WALL,WALL,WALL,WALL},
                            {WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL,WALL}  };
    

        //////////////////////////////////////////////////////////
//    g,image,gOld, animateFirstTime
    public static void draw(Graphics2D g,Image image, Graphics gOld, boolean animateFirstTime)
    {
       
        g.setColor(Color.red);
//horizontal lines
        for (int zi=1;zi<Board.numRows;zi++)
        {
            g.drawLine(Window.getX(0) ,Window.getY(0)+zi*Window.getHeight2()/Board.numRows ,
            Window.getX(Window.getWidth2()) ,Window.getY(0)+zi*Window.getHeight2()/Board.numRows );
        }
//vertical lines
        for (int zi=1;zi<Board.numColumns;zi++)
        {
            g.drawLine(Window.getX(0)+zi*Window.getWidth2()/Board.numColumns ,Window.getY(0) ,
            Window.getX(0)+zi*Window.getWidth2()/Board.numColumns,Window.getY(Window.getHeight2())  );
        }

//Display the objects of the board
        for (int zrow=0;zrow<Board.numRows;zrow++)
        {
            for (int zcolumn=0;zcolumn<Board.numColumns;zcolumn++)
            {
                if (Board.board[zrow][zcolumn] == Board.WALL)
                {
                    g.setColor(Color.gray);
                    g.fillRect(Window.getX(0)+zcolumn*Window.getWidth2()/Board.numColumns,
                    Window.getY(0)+zrow*Window.getHeight2()/Board.numRows,
                    Window.getWidth2()/Board.numColumns,
                    Window.getHeight2()/Board.numRows);
                }    
                if (Board.board[zrow][zcolumn] == Board.NUNU)
                {
                    g.setColor(Color.cyan);
                    g.fillRect(Window.getX(0)+zcolumn*Window.getWidth2()/Board.numColumns,
                    Window.getY(0)+zrow*Window.getHeight2()/Board.numRows,
                    Window.getWidth2()/Board.numColumns,
                    Window.getHeight2()/Board.numRows);
                }     
                       
            }
        }
        if(passageActive)
        {
            Board.board[6][2] = Board.NUNU;
            Board.board[6][3] = Board.NUNU;
            Board.board[6][4] = Board.NUNU;
        }
        
    }
    
}

    class Coin
    {  
        public final static int NUM_COINS=13;
        int Row;
        int Column;
        boolean collected = true;
        int value;
        Coin()
        {

                 boolean keepLooping = true; 
                 while(keepLooping)
                 {
                     Row = (int)(Math.random()*Board.numRows);
                     Column = (int)(Math.random()*Board.numColumns);
                     if (Board.board[Row][Column] == Board.PATH)
                     {
                         keepLooping = false;
                     }
                 } 
                 value = (int)(Math.random()*5+1);


        }
         public void draw(Graphics2D g)
                    {
                        if(collected)
                        {
                            g.setColor(Color.yellow);
                            g.fillOval(Window.getX(0)+Column*Window.getWidth2()/Board.numColumns,
                            Window.getY(0)+Row*Window.getHeight2()/Board.numRows,
                            Window.getWidth2()/Board.numColumns,
                            Window.getHeight2()/Board.numRows);
                            g.setColor(Color.black);
                            g.drawString("" + value,Window.getX(0)+Column*Window.getWidth2()/Board.numColumns,
                            Window.getY(15)+Row*Window.getHeight2()/Board.numRows);
                        }
                    }  
    }
    class Character
    {
        boolean gameOver;
        boolean canFill;
        public final static int NUM_NPC=3;
        boolean isNpc;
        int value;
        int Row;
        int Column;
        int rowDir;
        int columnDir;
        int move;
        int right=1;
        int left=2;
        int up=3;
        int down=4;
        int speed=1;
        String name;
        Color color;
    Character(String n, Color c)
    {
        gameOver=false;
        isNpc = true;
        name = n;
        color = c;        
       boolean keepLooping = true; 
        while(keepLooping)
        {
            Row = (int)(Math.random()*Board.numRows);
            Column = (int)(Math.random()*Board.numColumns);
            if (Board.board[Row][Column] == Board.PATH)
            {
                keepLooping = false;
            }
        }
       
    }
    public void draw(Graphics2D g)
    {            
        g.setColor(color);
        g.fillRect(Window.getX(0)+Column*Window.getWidth2()/Board.numColumns,
        Window.getY(0)+Row*Window.getHeight2()/Board.numRows,
        Window.getWidth2()/Board.numColumns,
        Window.getHeight2()/Board.numRows);
        g.setColor(color.black);
        g.drawString("" + name + value,Window.getX(0)+Column*Window.getWidth2()/Board.numColumns,
        Window.getY(0)+Row*Window.getHeight2()/Board.numRows);
    }     
    public void realMove()
    {
        
        if(isNpc)
        {
            if(TimeCount.update(speed))
            {
            move = (int)(Math.random()*4+1);
            if(move == right)
                moveRight();
            else if(move == left)
                moveLeft();
            else if(move == up)
                 moveUp();
            else if(move == down)
                moveDown();
            if(Column + columnDir == -1)
                Column = Board.numColumns;
            else if(Column + columnDir == Board.numColumns)
                Column = -1;
            }
        }
        if(Column + columnDir == -1)
            Column = Board.numColumns;
        else if(Column + columnDir == Board.numColumns)
            Column = -1;
        
        
        if (Board.board[Row+rowDir][Column+columnDir] == Board.PATH|| Board.board[Row+rowDir][Column+columnDir] == Board.NUNU)
        {
            if(canFill)
                Board.board[Row][Column] = Board.WALL;
            
            Row += rowDir;
            Column += columnDir;
        }
        rowDir = 0;
        columnDir = 0;
      
        
    }
    public void moveRight()
    {
        rowDir=0;
        columnDir=1;
    }
     public void moveLeft()
    {
        rowDir = 0;
        columnDir = -1;
    }
      public void moveDown()
    {
        rowDir=1;
        columnDir=0;
    }
       public void moveUp()
    {
        rowDir=-1;
        columnDir=0;
    }
       
       public void port(Portal in,Portal out)
       {
           if(Row == in.Row && Column == in.Column)
           {
               Row = out.Row;
               Column = out.Column;
           }
       }
      
       public void collected(Coin coin)
     {
         for(int i=0;i<Coin.NUM_COINS;i++)
        {
           if(coin.collected&&coin.Column == Column && coin.Row == Row)
           {
//               coin.collected = false;
               
               value += coin.value;
               boolean keepLooping = true; 
                 while(keepLooping)
                 {
                     coin.Row = (int)(Math.random()*Board.numRows);
                     coin.Column = (int)(Math.random()*Board.numColumns);
                     if (Board.board[coin.Row][coin.Column] == Board.PATH)
                     {
                         keepLooping = false;
                     }
                     
                     if(!isNpc)
                     {
                        if(value>5 && value <20)
                        {
                           Board.passageActive=true;
                        }
                     }
                     
                 }  
           }
        }
         
     }
}
class Portal
{
    int Row;
    int Column;
    boolean portalIn;
    Portal(boolean _portalIn)
    {
        portalIn = _portalIn;
        
        boolean keepLooping = true; 
        while(keepLooping)
        {
            Row = (int)(Math.random()*Board.numRows);
            Column = (int)(Math.random()*Board.numColumns);
            if (Board.board[Row][Column] == Board.PATH)
            {
                keepLooping = false;
            }
        }
        
    }
    public void draw(Graphics2D g)
    {            
        
        if(portalIn)
            g.setColor(Color.lightGray);
        else
            g.setColor(Color.pink);
        g.fillRect(Window.getX(0)+Column*Window.getWidth2()/Board.numColumns,
        Window.getY(0)+Row*Window.getHeight2()/Board.numRows,
        Window.getWidth2()/Board.numColumns,
        Window.getHeight2()/Board.numRows);
        if(!portalIn)
        {
            if (TimeCount.timeCount % (int)(TimeCount.frameRate*4)== (int)(TimeCount.frameRate*4) -1)
            {
                boolean keepLooping = true; 
                while(keepLooping)
                {
                    Row = (int)(Math.random()*Board.numRows);
                    Column = (int)(Math.random()*Board.numColumns);
                    if (Board.board[Row][Column] == Board.PATH)
                    {
                        keepLooping = false;
                    }
                }
            }
        }
    }
}
class TimeCount
{
    public static double frameRate = 10.0;
   public static int timeCount;
    static void addTime()
    {
        timeCount++;
    }
    public static boolean update(int _val)
    {
        return(timeCount % _val == _val-1);
    }
}