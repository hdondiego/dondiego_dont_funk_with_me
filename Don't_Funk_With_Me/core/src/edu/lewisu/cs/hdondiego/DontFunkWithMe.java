package edu.lewisu.cs.hdondiego;

import java.util.TimerTask;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import java.util.Timer;

public class DontFunkWithMe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture theKingMap, player1Tex, healthBar1, healthBar2; // player2Tex
	AnimatedImageBasedScreenObject player1; // player2
	ImageBasedScreenObjectDrawer drawer1;
	OrthographicCamera cam, titleCam;
	int WIDTH, HEIGHT, seconds;
	float FRAME_WIDTH, FRAME_HEIGHT, ANIM_DELAY;
	int[] player1WalkSeq = {0,0,1,0,2,0}; // follows the order for each different movement
	int[] player1PunchSeq = {0,1,1,1,2,1};
	int[] player1KickSeq = {0,2,1,2,2,2};
	ActionLabel player1Label, player2Label;//, countdownTimer;
	Music fightMusic; // the background fight music
	boolean movingLeft, isRoundOver;
	Timer timer;
	TimerTask timerTask;
	LabelStyle style = new LabelStyle();
	Label countDownLabel;
	AnimationParameters walkAnim, punchAnim, kickAnim, currAnim;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		theKingMap = new Texture("the_king_map.png"); // one of the maps for one of the characters - The King
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		FRAME_WIDTH = 32f;
		FRAME_HEIGHT = 32f;
		ANIM_DELAY = 0.1f;
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		drawer1 = new ImageBasedScreenObjectDrawer(batch);
		isRoundOver = false;
		
		// creating labels for each fighter on the top part of the screen - in this case, Johnny Dansalot (main character) and The King
		player1Label = new ActionLabel("Johnny Dansalot", 10, 620, "fonts/agency_fb_35pt_black.fnt");
		player2Label = new ActionLabel("The King", 820, 620, "fonts/agency_fb_35pt_black.fnt");
		
		// adding the health bars for each character
		healthBar1 = new Texture("health_bar.png");
		healthBar2 = new Texture("health_bar.png");
		
		// instantiating the background fight music for The King's map
		// the music continually loops
		fightMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/the_king_kb11_BSB.wav"));
		fightMusic.setLooping(true);
		
		// creating the label for the countdown timer
		seconds = 60;
		//countdownTimer = new ActionLabel(Integer.toString(seconds), 470, 660, "fonts/agency_fb_35pt_black.fnt");
		style.font = new BitmapFont(Gdx.files.internal("fonts/agency_fb_35pt_black.fnt"));
		countDownLabel = new Label(Integer.toString(seconds),style);
		countDownLabel.setPosition(470,660);
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				if (seconds > 0) {
					seconds--;
					countDownLabel.setText(seconds);
				} else { // 60 seconds have already elapsed
					isRoundOver = true;
					fightMusic.stop();
				}
			}
		};
		
		// instantiating the main character's sprite
		player1Tex = new Texture("johnny_dansalot.png");
		movingLeft = false;
		player1 = new AnimatedImageBasedScreenObject(player1Tex, 600, 130, 0, 0, 0, 10, 10, movingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY);
		walkAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY);
		punchAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1PunchSeq, ANIM_DELAY);
		kickAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1KickSeq, ANIM_DELAY);
		currAnim = walkAnim;
		
		fightMusic.play();
		start();
		
	}

	public void start() {
		// notifying the Timer object about the task it will do
		// Three second delay, before the countdown timer begins
		// every one second, count down by one second (total of 60 seconds per round)
		
		timer.scheduleAtFixedRate(timerTask, 3000, 1000);
	}
	
	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		/*
		 * Player 1 Controls:
		 * A - Move left
		 * D - Move right
		 * W - Jump
		 * S - Duck (technically, the block button)
		 * C - Punch
		 * V - Kick
		 */
				
		// move to the left
		if (Gdx.input.isKeyPressed(Keys.A)) {
			if (currAnim != walkAnim) {
				player1.setAnimationParameters(walkAnim);
				currAnim = walkAnim;
			}
			movingLeft = true;
			player1.setFlipX(movingLeft);
			player1.animate(dt);
			player1.setXPos(player1.getXPos() - 5);
		}
		
		// move to the right
		if (Gdx.input.isKeyPressed(Keys.D)) {
			if (currAnim != walkAnim) {
				player1.setAnimationParameters(walkAnim);
				currAnim = walkAnim;
			}
			movingLeft = false;
			player1.setFlipX(movingLeft);
			player1.animate(dt);
			player1.setXPos(player1.getXPos() + 5);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			if (currAnim != punchAnim) {
				player1.setAnimationParameters(punchAnim);
				currAnim = punchAnim;
			}			
			player1.animate(dt);
			//player1.resetAnimation();
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.V)) {
			if (currAnim != kickAnim) {
				player1.setAnimationParameters(kickAnim);
				currAnim = kickAnim;
			}
			player1.animate(dt);
			//player1.resetAnimation();
		}
		
		/* TODO
		 * Player 2 Controls:
		 * J - Move left
		 * L - Move right
		 * I - Jump
		 * K - Duck (technically, the block button)
		 * . - Punch
		 * / - Kick
		 * */
		
		batch.begin();
		batch.draw(theKingMap, 0, 0); // display the map the player's will fight at
		player1Label.draw(batch, 1); // display player 1's character name
		player2Label.draw(batch, 1); // display player 2's character name
		batch.draw(healthBar1, 10, 660);
		batch.draw(healthBar2, 600, 660);
		//countdownTimer.draw(batch, 1);
		countDownLabel.draw(batch, 1);
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
