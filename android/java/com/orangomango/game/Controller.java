package com.orangomango.game;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.TouchEvent;
import javafx.geometry.Rectangle2D;

public class Controller{
	private GraphicsContext gc;
	private double x = 40;
	private double y = MyApplication.bounds.getHeight()-150;
	private double extraX, extraY;
	private Boolean direction;
	
	public Controller(GraphicsContext gc){
		this.gc = gc;
		EventHandler<TouchEvent> event = new EventHandler<TouchEvent>(){
			@Override
			public void handle(TouchEvent evt){
				if ((new Rectangle2D(x, y, 100, 100)).contains(evt.getTouchPoint().getX(), evt.getTouchPoint().getY())){
					extraX = evt.getTouchPoint().getX()-x-50;
					extraY = evt.getTouchPoint().getY()-y-50;
				} else {
					double angle = Math.atan2(evt.getTouchPoint().getY()-(y+50), evt.getTouchPoint().getX()-(x+50));
					extraX = 50*Math.cos(angle);
					extraY = 50*Math.sin(angle);
					// This will be set later on
					//x = evt.getTouchPoint().getX()-extraX-30;
					//y = evt.getTouchPoint().getY()-extraY-30;
				}
				if (evt.getTouchPoint().getX() >= x+50){
					direction = true;
				} else {
					direction = false;
				}
			}
		};
		this.gc.getCanvas().setOnTouchPressed(event);
		this.gc.getCanvas().setOnTouchMoved(event);
		this.gc.getCanvas().setOnTouchReleased(e -> {
			this.extraX = 0;
			this.extraY = 0;
			this.direction = null;
		});
	}
	
	public Boolean getDirection(){
		return this.direction;
	}
	
	public void draw(){
		this.gc.setStroke(Color.web("#FF7500"));
		this.gc.setLineWidth(4);
		this.gc.strokeOval(x, y, 100, 100);
		this.gc.setFill(Color.web("#FF7500"));
		this.gc.fillOval(x+30+extraX, y+30+extraY, 40, 40);
	}
}
