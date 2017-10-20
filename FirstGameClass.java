
import javafx.util.Duration;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;;

public class FirstGameClass extends Application{
    //numbers add to 360, cause the player to wobble          
    double width = 150.0;
    double height = 50.0;
    
	public FirstGameClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

    //set up player position and speed (position based on top right corner)
    double xSpeed = 0;
    double ySpeed = 0;
    double xPos = 400;
    double yPos = 400;
    
    //used to figure out how long it's been since start of game
    final long timeStart = System.currentTimeMillis();
    
	//steps of rotation of rectangle per cycle
    double t = (System.currentTimeMillis() - timeStart) / 250.0;
    
	@Override
	public void start(Stage firstStage) throws Exception {
		// This method always needs to be implemented to make the 'extends Application' thing work
		// It creates the main window
		firstStage.setTitle("Stage1");
		
		//setting up some preliminary stuff (gotta undertand this a bit more)
		Group root = new Group();
		Scene scene1 = new Scene( root );
		firstStage.setScene(scene1);
		Canvas canvas = new Canvas( 800, 800 );
	    root.getChildren().add(canvas);
	    
	    //set up the display graphics
	    GraphicsContext gc = canvas.getGraphicsContext2D();
        firstStage.show();
        
        //keep track of what the current key press is by storing it in here
        ArrayList<String> input = new ArrayList<String>();
        
        scene1.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
     
                        // only add once... prevent duplicates
                        if ( !input.contains(code) )
                            input.add(code);
                    }
                });
        
            scene1.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove(code);
                    }
                });
            
            Timeline gameLoop = new Timeline();
            gameLoop.setCycleCount( Timeline.INDEFINITE );
            
            
        KeyFrame kf = new KeyFrame(
            Duration.seconds(0.017),                // 60 FPS
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                	
                    //check if x position needs to change based on input (arrowkeys)
                    if (input.contains("LEFT")){
                    	if (xPos-width/2 >= 0){
                    		if (xSpeed > -10){
                    			xSpeed  += -0.5;
                    		}
                    		xPos += xSpeed;
                    	}
                    	if (xPos-width/2 <= 0){
                    		xPos = width/2;
                    		xSpeed = 0;
                    	}
                    }
                    
                    if (input.contains("RIGHT")){
                    	if (xPos-width/2 <= 800){
                    		if (xSpeed < 10){
                    			xSpeed  += 0.5;
                    		}
                    		xPos += xSpeed;
                    	}
                    	if (xPos+width/2 >= 800){
                    		xPos = 800-width/2;
                    		xSpeed = 0;
                    	}
                    }
                    
                    // if neither horizontal arrow key pressed, slow down
                    if (!input.contains("RIGHT") && !input.contains("LEFT")){
                    	if (xSpeed > 0)
                    		xSpeed  += -0.5;
                    	if (xSpeed < 0)
                    		xSpeed += 0.5;
                    	xPos += xSpeed;
                    	if (xPos+width/2 >= 800){
                    		xPos = 800-width/2;
                    		xSpeed = 0;
                    	}
                    	if (xPos-width/2 <= 0){
                    		xPos = width/2;
                    		xSpeed = 0;
                    	}
                    }
                    
                    //check if y position needs to change based on input (arrowkeys)
                    if (input.contains("UP"))
                    	yPos += -6;
                    if (input.contains("DOWN"))
                    	yPos += 6;
                    
                    if (input.contains("SPACE")){
                    	if (height < 150){
                    		height += 10;
                    		width += -10;
                    	}
                    }
                    else{
                    	if (height > 50){
                    		height += -10;
                    		width += 10;
                    	}
                    } //near misses
                    
                    // Clear the canvas
                    gc.clearRect(0, 0, 800,800);
                    
                    // background image clears canvas
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, 800, 400);
                    
                    if (yPos < 400){
                    //draw character  if in black half of screen (position adjusted to center the player, moved with speed variables)
                    	gc.setFill(Color.WHITE);
                    	gc.fillRect(xPos-width/2, yPos-height/2, width, height);
                    }
                    else{
                    //draw character if in white half of screen (position adjusted to center the player)
                    	gc.setFill(Color.BLACK);
                    	gc.fillRect(xPos-width/2, yPos-height/2, width, height); //change to draw with height=0 when fully in top half, height=y in bottom half, exact middle if on center line.
                    }

                }
            });
        
        gameLoop.getKeyFrames().add( kf );
        gameLoop.play();
	}

}
