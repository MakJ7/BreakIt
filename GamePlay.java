import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import javax.swing.Timer;

/**
 * Write a description of class GamePlay here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposx = 120;
    private int ballposy = 350;
    private int ballxdir = -1;
    private int ballydir = -2;

    private MapGenerator map;

    public GamePlay()
    {
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g)
    {
        // background
        g.setColor(Color.BLACK);
        g.fillRect(1, 2, 692, 592);

        // Map
        map.draw((Graphics2D)g);

        // scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score, 590, 30);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(683, 0, 3, 592);       //here its different 683 instead 691

        // paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // ball
        g.setColor(Color.yellow);
        g.fillOval(ballposx, ballposy, 20, 20);

        // Win message
        if(totalBricks<=0)
        {
            play = false;
            ballxdir=0;
            ballydir=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You Won!", 260, 300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to restart!", 230, 350);
        }

        //Game Over message
        if(ballposy>570){
            play = false;
            ballxdir=0;
            ballydir=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over! Score: "+score, 190, 300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to restart!", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play)
        {

            if(new Rectangle(ballposx,ballposy,20,20).intersects(new Rectangle(playerX,550,100,8)))
            {
                ballydir=-ballydir;
            }

            br: for(int i=0;i<map.map.length;i++)
            {
                for(int j=0;j<map.map[i].length;j++)
                {
                    if(map.map[i][j]>0)
                    {
                        int brickX=j*map.brickwidth+80;
                        int brickY=i*map.brickheight+50;
                        int brickwidth=map.brickwidth;
                        int brickheight=map.brickheight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickwidth,brickheight);
                        Rectangle ballRect = new Rectangle(ballposx,ballposy,20,20);

                        if(rect.intersects(ballRect))
                        {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score+=5;

                            if(ballposx+19<=rect.x || ballposx+1 >=rect.x+rect.width)
                            {
                                ballxdir=-ballxdir;
                            }else{
                                ballydir=-ballydir;
                            }
                            break br;
                        }
                    }
                }
            }

            ballposx+=ballxdir;
            ballposy+=ballydir;
            if(ballposx<0)
            {
                ballxdir=-ballxdir;
            }
            if(ballposy<0)
            {
                ballydir=-ballydir;
            }
            if(ballposx>670)
            {
                ballxdir=-ballxdir;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(playerX > 580)                  // Limits needs to be set. It isn't working properly.
            {
                playerX=580;
            }else{
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(playerX < 20)                  // Limits needs to be set. It isn't working properly.
            {
                playerX = 20;
            }
            else{
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play)
            {
                play=true;
                ballposx=120;
                ballposy=350;
                ballxdir=-1;
                ballydir=-2;
                playerX=310;
                score=0;
                totalBricks=21;
                map=new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight()
    {
        play=true;
        playerX+=20;
    }

    public void moveLeft()
    {
        play=true;
        playerX-=20;
    }
}
