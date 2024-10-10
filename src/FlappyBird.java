import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.random.*;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
 int boardWidth = 360;
 int boardheight = 640;


 //Images
 Image backgroundImage;
 Image birdImg;
 Image topPipeImg;
 Image bottomPipeImage;

 //Bird
 int birdX = boardWidth/8;
 int birdY = boardheight/2;
 int birdhWidth = 34;
 int birdHeight = 24;

 class Bird {
    int x = birdX;
    int y = birdY;
    int width = birdhWidth;
    int height = birdHeight;
    Image img;
    
 

    Bird (Image img){
        this.img = img;
    }
 }


//Pipes
int pipeX =boardWidth;
int pipeY = 0;
int pipeWidth = 64;
int pipeHeight = 512;

class Pipe {
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe (Image img) {
        this.img = img;
    }
}


// gameLogic
Bird bird;
int velocityX = -4;
int velocityY = 0;
int gravity = 1;

ArrayList<Pipe> pipes;
Random random = new Random (); 
Timer gameLoop;
Timer placePipesTimer;
boolean gameOver = false;
double score = 0;


FlappyBird () {
     setPreferredSize(new Dimension(boardWidth,boardheight));
     //setBackground(Color.blue);
     setFocusable(true);
     addKeyListener(this);

     //load images
     backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
     birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
     topPipeImg= new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
     bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

     //bird
     bird = new Bird(birdImg);
     pipes = new ArrayList<Pipe>();



     //place pipes timer
     placePipesTimer = new Timer (1500, new ActionListener() {
        @Override
        public void actionPerformed (ActionEvent e)
        { placePipes ();}
    });

    placePipesTimer.start();




     //game timer
     gameLoop = new Timer (1000/60, this);
     gameLoop.start();}

public void placePipes (){
int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));    
int openingSpace = boardheight/4;
Pipe topPipe = new Pipe (topPipeImg);
topPipe.y = randomPipeY;
pipes.add (topPipe);

Pipe bottomPipe = new Pipe (bottomPipeImage);
bottomPipe.y =  topPipe.y + pipeHeight + openingSpace;
pipes.add(bottomPipe);
}




public void paintComponent (Graphics g) {
    super.paintComponent(g);
    draw (g);
    }

public void draw(Graphics g) {
    System.out.println("Draw");
    //Background
    g.drawImage(backgroundImage, 0, 0,boardWidth, boardheight ,null);

    //bird
    g.drawImage(bird.img, bird.x, bird.y, bird.width,bird.height, null);

    //Pipes
    for (int i= 0; i < pipes.size(); i++){
        Pipe pipe = pipes.get(i);
        g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
 

    //score
    g.setColor(Color.white);
    g.setFont( new Font ("Arial", Font.PLAIN,32));
    if (gameOver) {
        g.drawString("Game Over:" + String.valueOf((int)score), 10, 35);
        }

else {

    g.drawString(String.valueOf((int)score), 10, 35);
     }    
      }


public void move () {
   //bird
   velocityY += gravity;
   bird.y += velocityY;
   bird.y = Math.max(bird.y, 0);

   //pipes
   for (int i=0; i <pipes.size(); i++){
    Pipe pipe =pipes.get (i);
    pipe.x += velocityX;
    if (!pipe.passed && bird.x > pipe.x + pipe.width) {
        pipe.passed =true;
        score += 0.5;
    }

    if (collision(bird,pipe)){
        gameOver = true;
    }
      }
       if (bird.y > boardheight) {
       gameOver =true;
    }}
    
    
    public boolean collision (Bird a , Pipe b) {
    return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner

}



@Override
public void actionPerformed(ActionEvent e) {

    move();
    repaint();
    if (gameOver){
        placePipesTimer.stop();
        gameLoop.stop();
    }}



@Override
public void keyPressed(KeyEvent e) {
    if ( e.getKeyCode()==KeyEvent.VK_SPACE) {
        velocityY =-9;
        if (gameOver){
            bird.y =birdY;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameLoop.start ();
            placePipesTimer.start();

        }
        
    }
    
}
@Override
public void keyTyped(KeyEvent e) {}
   
@Override
public void keyReleased(KeyEvent e) {}

}
    

