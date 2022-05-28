package com.orangomango.game;

import javafx.scene.canvas.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Ball{
	private double x, y;
	public static final double WIDTH = 25;
	private double angle;
	private Paddle paddle;
	private boolean start = false;
	private boolean gotPoint = false;
	
	public Ball(Paddle paddle){
		this.paddle = paddle;
		this.angle = 45;
		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
			this.start = true;
		}).start();
	}
	
	public void draw(GraphicsContext gc){
		if (!start) return;
		gc.setFill(Color.BLUE);
		gc.fillOval(this.x, this.y, WIDTH, WIDTH);
		this.x += 10*Math.cos(Math.toRadians(this.angle));
		this.y += 10*Math.sin(Math.toRadians(this.angle));
		if (this.y >= MyApplication.bounds.getHeight()-Paddle.DISTANCE){
			if (MyApplication.score > MyApplication.highscore){
				MyApplication.highscore = MyApplication.score;
				MyApplication.updateHighscore();
			}
			MyApplication.score = 0;
			this.x = 10;
			this.y = 10;
		}
		boolean onPaddle = (new Rectangle2D(this.x, this.y, WIDTH, WIDTH)).intersects(this.paddle.getCollisionRect());
		if (this.x <= 0 || this.x+WIDTH >= MyApplication.bounds.getWidth()){
			this.angle = 180-this.angle;
			this.x += 20*Math.cos(Math.toRadians(this.angle));
		} else if (this.y <= 0 || this.y+WIDTH >= MyApplication.bounds.getHeight() || onPaddle){
			this.angle = 360-this.angle;
			this.y += 20*Math.sin(Math.toRadians(this.angle));
		}
		if (onPaddle && !this.gotPoint){
			MyApplication.score += 1;
			MyApplication.vibrator.vibrate(300);
			this.gotPoint = true;
			new Thread(() -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException ex){
					ex.printStackTrace();
				}
				this.gotPoint = false;
			}).start();
		}
	}
}
