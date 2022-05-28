package com.orangomango.game;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.text.Font;

import java.lang.reflect.*;
import android.os.Build;
import android.os.Vibrator;
import android.content.Context;
import javafxports.android.FXActivity;
import java.io.*;

public class MyApplication extends Application {
	public static Rectangle2D bounds;
	private Paddle paddle;
	private Controller controller;
	private Ball ball;
	public static Timeline loop;
	public static int score;
	public static int highscore;
	private static final int FPS = 40;
	private static final String PREFIX = FXActivity.getInstance().getFilesDir().getAbsolutePath();
	public static Vibrator vibrator;

	@Override
	public void start(Stage stage) throws Exception, IOException{
		if (Build.VERSION.SDK_INT >= 29){
			Method forName = Class.class.getDeclaredMethod("forName", String.class);
			Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
			Class vmRuntimeClass = (Class) forName.invoke(null, "dalvik.system.VMRuntime");
			Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
			Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[] { String[].class} );
			Object vmRuntime = getRuntime.invoke(null);
			setHiddenApiExemptions.invoke(vmRuntime, new String[][] { new String[] { "L" } });
		}
		
		vibrator = (Vibrator) FXActivity.getInstance().getSystemService(Context.VIBRATOR_SERVICE);

		bounds = Screen.getPrimary().getVisualBounds();

		File file = new File(PREFIX, "highscore.txt");
		if (!file.exists()){
			file.createNewFile();
		} else {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			highscore = Integer.parseInt(reader.readLine());
			reader.close();
		}

		TilePane root = new TilePane();
		Canvas canvas = new Canvas(bounds.getWidth(), bounds.getHeight());
		GraphicsContext gc = canvas.getGraphicsContext2D();

		this.paddle = new Paddle(50);
		this.controller = new Controller(gc);
		this.ball = new Ball(this.paddle);

		gc.setFont(new Font("sans-serif", 25));

		loop = new Timeline(new KeyFrame(Duration.millis(1000.0/FPS), evt -> update(gc)));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();

		root.getChildren().add(canvas);
		Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
		stage.setScene(scene);
		stage.show();
	}
	
	public static void updateHighscore(){
		File file = new File(PREFIX, "highscore.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(Integer.toString(highscore));
			writer.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}

	private void update(GraphicsContext gc) {
		gc.clearRect(0, 0, bounds.getWidth(), bounds.getHeight());
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
		this.paddle.draw(gc);
		this.controller.draw();
		this.ball.draw(gc);
		gc.setFill(Color.WHITE);
		gc.fillText(String.format("%s High:%s", score, highscore), 10, 30);
		Boolean dir = this.controller.getDirection();
		if (dir != null){
			this.paddle.x += dir ? 8 : -8;
			if (this.paddle.x+Paddle.WIDTH >= bounds.getWidth()){
				this.paddle.x -= 8;
			} else if (this.paddle.x <= 0){
				this.paddle.x = 0;
			}
		}
	}
}
