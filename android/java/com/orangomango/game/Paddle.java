package com.orangomango.game;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;

public class Paddle{
	public double x;
	public static final double WIDTH = 120;
	public static final double HEIGHT = 30;
	public static final double DISTANCE = 100;
	public Paddle(double x){
		this.x = x;
	}
	
	public Rectangle2D getCollisionRect(){
		return new Rectangle2D(this.x, MyApplication.bounds.getHeight()-DISTANCE, WIDTH, HEIGHT);
	}
	
	public void draw(GraphicsContext gc){
		gc.setFill(Color.RED);
		gc.fillRoundRect(this.x, MyApplication.bounds.getHeight()-DISTANCE, WIDTH, HEIGHT, 20, 20);
	}
}
