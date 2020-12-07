package edu.lewisu.cs.hdondiego;

import com.badlogic.gdx.ApplicationAdapter;
import edu.lewisu.cs.cpsc41000.common.*;
import edu.lewisu.cs.cpsc41000.common.labels.*;
import edu.lewisu.cs.cpsc41000.common.labels.ActionLabel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DontFunkWithMe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture theKingMap, player1Tex, healthBar1, healthBar2; // player2Tex
	AnimatedImageBasedScreenObject player1; // player2
	ImageBasedScreenObjectDrawer drawer1;
	OrthographicCamera cam, titleCam;
	int WIDTH, HEIGHT, seconds;
	int[] player1Seq = {0,0,1,0,2,0,0,1,1,1,2,1}; // follows the order for each different movement
	ActionLabel player1Label, player2Label, countdownTimer;
	Music fightMusic; // the background fight music
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		theKingMap = new Texture("the_king_map.png"); // one of the maps for one of the characters - The King
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		drawer1 = new ImageBasedScreenObjectDrawer(batch);
		
		// creating labels for each fighter on the top part of the screen - in this case, Johnny Dansalot (main character) and The King
		player1Label = new ActionLabel("Johnny Dansalot", 10, 620, "fonts/agency_fb_35pt_black.fnt");
		player2Label = new ActionLabel("The King", 820, 620, "fonts/agency_fb_35pt_black.fnt");
		
		// adding the health bars for each character
		healthBar1 = new Texture("health_bar.png");
		healthBar2 = new Texture("health_bar.png");
		
		// creating the label for the countdown timer
		seconds = 60;
		countdownTimer = new ActionLabel(Integer.toString(seconds), 470, 660, "fonts/agency_fb_35pt_black.fnt");
		
		// instantiating the main character's sprite
		player1Tex = new Texture("johnny_dansalot.png");
		player1 = new AnimatedImageBasedScreenObject(player1Tex, 600, 130, 0, 0, 0, 10, 10, false, false, 32f, 32f, player1Seq, 0.1f);
		
		// instantiating the background fight music for The King's map
		// the music continually loops
		fightMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/the_king_kb11_BSB.wav"));
		fightMusic.setLooping(true);
		fightMusic.play();
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// move to the right
		if (Gdx.input.isKeyPressed(Keys.D)) {
			player1.animate(dt);
			player1.setXPos(player1.getXPos() + 5);
		}
		
		// move to the left
		if (Gdx.input.isKeyPressed(Keys.A)) {
			player1.animate(dt);
			player1.setXPos(player1.getXPos() - 5);
		}
		
		batch.begin();
		batch.draw(theKingMap, 0, 0); // display the map the player's will fight at
		player1Label.draw(batch, 1); // display player 1's character name
		player2Label.draw(batch, 1); // display player 2's character name
		batch.draw(healthBar1, 10, 660);
		batch.draw(healthBar2, 600, 660);
		countdownTimer.draw(batch, 1);
		drawer1.draw(player1); // display the sprite on the map
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		theKingMap.dispose();
		fightMusic.dispose();
	}
}
