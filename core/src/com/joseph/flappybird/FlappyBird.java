package com.joseph.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.values.RectangleSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	int flapState = 0;
	Texture background;
	Texture gameOver;
	BitmapFont font;
	//ShapeRenderer shapeRenderer ;
	Texture[] birds;
	float birdY = 0;
	Circle birdCircle;
	float velocity = 0;
	int score = 0;
	int scoringTube=0;
	int gameState = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTube = 4;
	float[] tubeX = new float[numberOfTube];
	float[] tubeOffset = new float[numberOfTube];
	float distancebetweenTubes;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	
	@Override
	public void create () {
		gameOver = new Texture("GameOver.png");
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distancebetweenTubes = Gdx.graphics.getWidth()*3/4;
		topTubeRectangle = new Rectangle[numberOfTube];
		bottomTubeRectangle = new Rectangle[numberOfTube];
		startGame();

	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight()/2  - birds[0].getHeight()/2;
		for (int i = 0;i < numberOfTube ;i++){

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth()+ i*distancebetweenTubes;
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState == 1){
            if (tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if (scoringTube<numberOfTube-1)
                     scoringTube++;
                else
                    scoringTube = 0;
            }

			if (Gdx.input.justTouched()){

				velocity = -30;


			}
			for (int i = 0;i < numberOfTube;i++) {

				if (tubeX[i]<-topTube.getWidth()){
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f)*(Gdx.graphics.getHeight() - gap - 200);
					tubeX[i] = numberOfTube * distancebetweenTubes;
				}else {
					tubeX[i]=tubeX[i]-tubeVelocity;


				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			}


			if (birdY > 0 ){
			velocity = velocity + gravity;
			birdY-=velocity;}
			else{
				gameState = 2;
			}
		}else if (gameState == 0){
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}else if (gameState ==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score=0;
				velocity = 0;
				scoringTube=0;
			}
		}
		if (flapState == 0){
			flapState = 1;
		}else {
			flapState = 0;
		}



		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2,birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapState].getHeight()/2,birds[flapState].getHeight()/2);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for (int i = 0;i < numberOfTube;i++){
			//shapeRenderer.rect(topTubeRectangle[i].x,topTubeRectangle[i].y,topTubeRectangle[i].width,topTubeRectangle[i].height);
			//shapeRenderer.rect(bottomTubeRectangle[i].x,bottomTubeRectangle[i].y,bottomTubeRectangle[i].width,bottomTubeRectangle[i].height);
			if (Intersector.overlaps(birdCircle,topTubeRectangle[i])||Intersector.overlaps(birdCircle,bottomTubeRectangle[i])||birdY>Gdx.graphics.getHeight()){
                 gameState = 2;
			}

		}
		//shapeRenderer.end();


	}
	

}
