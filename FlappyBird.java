/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author charan
 */
public class FlappyBird implements ActionListener,MouseListener {

    public static FlappyBird flappyBird; // create a variable falppybird of type FlappyBird
    public final int WIDTH = 800, HEIGHT = 800;
    public boolean starts = false, gameOver = false; 
    public Renderer render;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public Random rand;
    public int ticks, yMotion, score;
    //public ImageObserver observer;
    
    public BufferedImage img = null;

    
    
    public FlappyBird(){                     //Constructor
    
        Timer timer = new Timer(20,this);
        render = new Renderer();
        
        
        JFrame jFrame = new JFrame();
        
        
        jFrame.setSize(this.WIDTH,this.HEIGHT);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.add(render);
        jFrame.addMouseListener(this);
        jFrame.setTitle("Flappy Bird");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer.start();
        
        bird = new Rectangle(WIDTH/2 -10, HEIGHT/2 - 10, 20, 20);
        
        columns = new ArrayList<Rectangle>();
        rand = new Random();
        
        LoadBird();

    }
    
    
    private void LoadBird() {
        try {
        img = ImageIO.read(new File("C:\\Users\\ecslogon\\Documents\\NetBeansProjects\\FlappyBird\\src\\birdImg.jpg"));
        } catch (IOException e) {
        }//To change body of generated methods, choose Tools | Templates.
    }

    
    public void addColumns(boolean start){
    
        int width = 100;
        int space = 300;
        int height = 50 + rand.nextInt(300);
        
        if (start){
           
            columns.add(new Rectangle(WIDTH + width + columns.size()*300, HEIGHT - height - 100,width,height));
            columns.add(new Rectangle(WIDTH + width + (columns.size()-1)*300, 0, width, HEIGHT - height - space));

        }
        
        else{
        
            columns.add(new Rectangle(columns.get((columns.size()-1)).x + 600, HEIGHT - height - 100, width, height));
            columns.add(new Rectangle(columns.get((columns.size()-1)).x, 0, width, HEIGHT - height - space));

        }
        

    }
    
    public void birdjump(){

        if (gameOver){
            
           bird = new Rectangle(WIDTH/2 -10, HEIGHT/2 - 10, 20, 20);
           columns.clear();
            
           starts = true;
           gameOver = false;
           yMotion = 0;
           score = 0;
            
        }
        
         if (!starts){
            
            starts = true;
        
        }
         else if(!gameOver){
         
            if (yMotion > 0){
            
                yMotion = 0;
            
            }
            yMotion -= 10;
         
         
         }

    
    }
    public void paintColumn(Graphics g, Rectangle column){
    
        g.setColor(Color.GREEN);
        g.fillRect(column.x,column.y,column.width,column.height);
    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {          //actionPerformed is an abstract method from ActionListener interface.
     
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    if(starts){    
        int speed = 10;
        ++ticks;
        
        if(!gameOver){
        
            for(int i=0; i<columns.size(); i++){
            
                Rectangle column = columns.get(i);
                column.x -= speed;            

            }
        
            if (ticks % 2 == 0 && yMotion < 15){
        
                yMotion += 1;

            }
            bird.y += yMotion;

            for(int i=0; i<columns.size(); i++){
            
                Rectangle column = columns.get(i);
                
                if ( (bird.x - (bird.width/2)) > (column.x + (column.width)) ){
                
                    score = i/2 + 1;

                }
                if (bird.intersects(column)){
            
                    gameOver = true;
                   // starts = false;
                    bird.x = column.x - column.width - speed;
                
                }

            }
        
            if (bird.y < 0 || bird.y > HEIGHT - 100){
            
                 gameOver = true;
                 //starts = false;

                }
            
            if (gameOver){
        
                bird.y = HEIGHT - 100 - bird.height;

            }
        
        
        }
        

    }
        render.repaint();
         
    }
    

     public void repaint(Graphics g) {                  // repaint is an abstract method from JPanel Class i.e overridden here.         
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
       g.setColor(Color.CYAN.darker());
       g.fillRect(0, 0, WIDTH, HEIGHT);
       
       //g.setColor(Color.red);
       //g.fillRect(bird.x, bird.y, bird.width, bird.height);
       g.drawImage(img, bird.x, bird.y, bird.width, bird.height, null);
       
       g.setColor(Color.ORANGE);
       g.fillRect(0, HEIGHT - 100, WIDTH, 80);
       
       g.setColor(Color.GREEN.darker());
       g.fillRect(0, HEIGHT - 100, WIDTH, 20);
       
        for (Rectangle column : columns){
           paintColumn(g, column);
        }
        
        g.setFont(new Font("Calibri",1,100));
        
        if (starts == false){
            
            g.setFont(new Font("Calibri",1,100));
            g.setColor(Color.WHITE);
            g.drawString("Click to Start", 150, HEIGHT/2 - 100);
        
        }
        
        if(starts){
            
            g.setFont(new Font("Calibri",1,25));
            g.setColor(Color.WHITE);
            g.drawString("Score: " + Integer.toString(score), 650, 60);
 
        }
        
        if (gameOver == true){
            
            g.setFont(new Font("Calibri",1,100));
            g.setColor(Color.RED);
            g.drawString("Game Over!", 150, HEIGHT/2 - 100);

        }
        
        
        
        if(gameOver){
        
            g.setFont(new Font("Calibri",1,50));
            g.setColor(Color.WHITE);
            g.drawString("Click to Restart", 200, HEIGHT/2 + 100);
        
        }
       
     }
     
    
    public static void main(String[] args) {
        // TODO code application logic here
        flappyBird = new FlappyBird(); // Assignt the flappybird object to the FlappyBird constructor
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        birdjump();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}
