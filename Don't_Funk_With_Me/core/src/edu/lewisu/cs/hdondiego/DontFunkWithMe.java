package edu.lewisu.cs.hdondiego;

import java.util.TimerTask;

import com.badlogic.gdx.ApplicationAdapter;
import edu.lewisu.cs.cpsc41000.common.*;
import edu.lewisu.cs.cpsc41000.common.labels.*;
import edu.lewisu.cs.cpsc41000.common.labels.ActionLabel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;


class FighterInputAdapter extends InputAdapter {
	MobileImageBasedScreenObject mObj;
	AnimationParameters animParameters;
	AnimationParameters revAnimParameters; // reverse animation of animParameters
	boolean isBlocking;
	
	public AnimationParameters getAnimParameters() {
		return animParameters;
	}

	public void setAnimParameters(AnimationParameters animParameters) {
		this.animParameters = animParameters;
	}
	
	public FighterInputAdapter(MobileImageBasedScreenObject mObj) {
		this.mObj = mObj;
		animParameters = null;
	}
	
	public void updateMobileObject(MobileImageBasedScreenObject mObj) {
		this.mObj = mObj;
	}
	
	public FighterInputAdapter(MobileImageBasedScreenObject mObj, AnimationParameters animParameters, AnimationParameters revAnimParameters) {
		this.mObj = mObj;
		this.animParameters = animParameters;
		this.revAnimParameters = revAnimParameters;
		isBlocking = false;
		//int[] tempFrameSequence = new int[animParameters.getFrameSequence().length];//animParameters.getFrameSequence();
		
		//for (int i=0; i < animParameters.getFrameSequence().length)
		
	}
	
	public boolean isBlocking() {
		return isBlocking;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		// player 1 - block
		if (keyCode == Keys.S) {
			/*
			if (player1CurrAnim != johnnyDBlockAnim) {
				player1.setAnimationParameters(johnnyDBlockAnim);
				player1CurrAnim = johnnyDBlockAnim;
			}*/
			
			if (mObj.getAnimationParameters() != animParameters) {
				mObj.setAnimationParameters(animParameters);
			}
			//player1.accelerateAtAngle(270);
			mObj.startDiscreteAnimation();
			isBlocking =  true;
		}
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		// player 1 - undoing block
		if (keyCode == Keys.S) {
			/*
			if (player1CurrAnim != johnnyDBlockAnim) {
				player1.setAnimationParameters(johnnyDBlockAnim);
				player1CurrAnim = johnnyDBlockAnim;
			}
			*/
			if (mObj.getAnimationParameters() != revAnimParameters) {
				mObj.setAnimationParameters(revAnimParameters);
			}
			//player1.accelerateAtAngle(270);
			mObj.startDiscreteAnimation();
			isBlocking = false;
		}
		
		return true;
	}
}



public class DontFunkWithMe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture theKingMapTex, johnnyDTex, theKingTex, healthBar1Tex, healthBar2Tex, theKingPlatformTex; // player2Tex, 
	Texture health95, health90, health85, health80, health75, health70, health65, health60, health55, health50, health45, health40, health35, health30, health25, health20, health15, health10, health5, health0;
	Texture titleTex, titleBackgroundTex, startFightSelectedTex, startFightUnselectedTex, exitSelectedTex, exitUnselectedTex;
	HashMap<Integer, Texture> player1HealthBars, player2HealthBars;
	//AnimatedImageBasedScreenObject player1, player2;
	//MobileImageBasedScreenObject player1, player2;
	PlatformCharacter player1, player2;
	ImageBasedScreenObject platform;
	ImageBasedScreenObjectDrawer drawer1, drawer2, platformDrawer;
	OrthographicCamera fightCam, titleCam;
	int WIDTH, HEIGHT, seconds, player1Health, player2Health;
	float FRAME_WIDTH, FRAME_HEIGHT, ANIM_DELAY;
	int[] player1WalkSeq = {0,0,1,0,2,0}; // follows the order for each different movement - only for Johnny Dansalot
	int[] player1PunchSeq = {0,1,1,1,2,1};
	int[] player1KickSeq = {0,2,1,2,2,2};
	int[] player1BlockSeq = {0,3,1,3,2,3};
	int[] player1RevBlockSeq = {2,3,1,3,0,3}; //2,3,1,3,0,3
	
	int[] deltaList = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233};//{0,1,4,9,16,25,36,49,64,81,100,121}{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
	
	ArrayList<ImageBasedScreenObject> platforms = new ArrayList<ImageBasedScreenObject>();
	
	int[] player2WalkSeq = {0,0,1,0,2,0,3,0}; // only for The King
	ActionLabel player1Label, player2Label;//, countdownTimer;
	Music titleMusic, fightMusic; // the background fight music
	boolean player1MovingLeft, inFight, isRoundOver, player2MovingLeft, isPlayer1Blocking, isPlayer2Blocking;//player1OnGround, player1ReachedMax;
	int deltaY = 0;
	int mode = 0; // 0 - title, 1 - settings, 2 - fight
	int selectIncrementVal = 0; // used to increment when player loops through title screen buttons
	Timer timer;
	TimerTask timerTask;
	LabelStyle style = new LabelStyle();
	Label countDownLabel;
	AnimationParameters johnnyDWalkAnim, johnnyDPunchAnim, johnnyDKickAnim, johnnyDBlockAnim, johnnyDRevBlockAnim, player1CurrAnim;
	AnimationParameters theKingWalkAnim, player2CurrAnim;
	
	EdgeHandler edgeHandler;
	InputMultiplexer plexi;
	FighterInputAdapter fiaPlayer1, fiaPlayer2;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		theKingMapTex = new Texture("the_king_map.png"); // one of the maps for one of the characters - The King
		theKingPlatformTex = new Texture("the_king_platform.png");
		platform = new ImageBasedScreenObject(theKingPlatformTex, 0,0,0,0,0,1,1,false,false);
		platforms.add(platform);
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		FRAME_WIDTH = 320f; // 32f
		FRAME_HEIGHT = 320f; // 32f
		ANIM_DELAY = 0.1f;
		
		titleTex = new Texture("dont_funk_with_me_title.png");
		titleBackgroundTex = new Texture("dance_floor.jpeg");
		startFightSelectedTex = new Texture("start_selected.png");
		startFightUnselectedTex = new Texture("start_unselected.png");
		exitSelectedTex = new Texture("exit_selected.png");
		exitUnselectedTex = new Texture("exit_unselected.png");
		
		titleCam = new OrthographicCamera(WIDTH, HEIGHT);
		titleCam.translate(WIDTH/2, HEIGHT/2);
		titleCam.update();
		batch.setProjectionMatrix(titleCam.combined);
		
		fightCam = new OrthographicCamera(WIDTH, HEIGHT);
		fightCam.translate(WIDTH/2, HEIGHT/2);
		fightCam.update();
		//batch.setProjectionMatrix(fightCam.combined);
		drawer1 = new ImageBasedScreenObjectDrawer(batch);
		drawer2 = new ImageBasedScreenObjectDrawer(batch);
		platformDrawer = new ImageBasedScreenObjectDrawer(batch);
		isRoundOver = false;
		inFight = false; // start at title menu - not actively fighting -> lock the same keys used to fight and only use to navigate title menu
		//player1OnGround = true;
		//player1ReachedMax = false;
		isPlayer1Blocking = false;
		isPlayer2Blocking = false;
		
		// creating labels for each fighter on the top part of the screen - in this case, Johnny Dansalot (main character) and The King
		player1Label = new ActionLabel("Johnny Dansalot", 10, 620, "fonts/agency_fb_35pt_black.fnt");
		player2Label = new ActionLabel("The King", 820, 620, "fonts/agency_fb_35pt_black.fnt");
		
		// adding the health bars for each character
		healthBar1Tex = new Texture("health_bar.png"); // 100 percent health bar for Player 1
		healthBar2Tex = new Texture("health_bar.png"); // 100 percent health bar for Player 2
		
		health95 = new Texture("decreasing_health_bars/95.png");
		health90 = new Texture("decreasing_health_bars/90.png");
		health85 = new Texture("decreasing_health_bars/85.png");
		health80 = new Texture("decreasing_health_bars/80.png");
		health75 = new Texture("decreasing_health_bars/75.png");
		health70 = new Texture("decreasing_health_bars/70.png");
		health65 = new Texture("decreasing_health_bars/65.png");
		health60 = new Texture("decreasing_health_bars/60.png");
		health55 = new Texture("decreasing_health_bars/55.png");
		health50 = new Texture("decreasing_health_bars/50.png");
		health45 = new Texture("decreasing_health_bars/45.png");
		health40 = new Texture("decreasing_health_bars/40.png");
		health35 = new Texture("decreasing_health_bars/35.png");
		health30 = new Texture("decreasing_health_bars/30.png");
		health25 = new Texture("decreasing_health_bars/25.png");
		health20 = new Texture("decreasing_health_bars/20.png");
		health15 = new Texture("decreasing_health_bars/15.png");
		health10 = new Texture("decreasing_health_bars/10.png");
		health5 = new Texture("decreasing_health_bars/5.png");
		health0 = new Texture("decreasing_health_bars/0.png");

		player1HealthBars = new HashMap<Integer, Texture>();
		player1HealthBars.put(95, health95);
		player1HealthBars.put(90, health90);
		player1HealthBars.put(85, health85);
		player1HealthBars.put(80, health80);
		player1HealthBars.put(75, health75);
		player1HealthBars.put(70, health70);
		player1HealthBars.put(65, health65);
		player1HealthBars.put(60, health60);
		player1HealthBars.put(55, health55);
		player1HealthBars.put(50, health50);
		player1HealthBars.put(45, health45);
		player1HealthBars.put(40, health40);
		player1HealthBars.put(35, health35);
		player1HealthBars.put(30, health30);
		player1HealthBars.put(25, health25);
		player1HealthBars.put(20, health20);
		player1HealthBars.put(15, health15);
		player1HealthBars.put(10, health10);
		player1HealthBars.put(5, health5);
		player1HealthBars.put(0, health0);
		
		player2HealthBars = new HashMap<Integer, Texture>();
		player2HealthBars.put(95, health95);
		player2HealthBars.put(90, health90);
		player2HealthBars.put(85, health85);
		player2HealthBars.put(80, health80);
		player2HealthBars.put(75, health75);
		player2HealthBars.put(70, health70);
		player2HealthBars.put(65, health65);
		player2HealthBars.put(60, health60);
		player2HealthBars.put(55, health55);
		player2HealthBars.put(50, health50);
		player2HealthBars.put(45, health45);
		player2HealthBars.put(40, health40);
		player2HealthBars.put(35, health35);
		player2HealthBars.put(30, health30);
		player2HealthBars.put(25, health25);
		player2HealthBars.put(20, health20);
		player2HealthBars.put(15, health15);
		player2HealthBars.put(10, health10);
		player2HealthBars.put(5, health5);
		player2HealthBars.put(0, health0);
		
		player1Health = 100;
		player2Health = 100;
		
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/title_screen_02_disco_bass_loop_120_bpm_josefpres.wav"));
		titleMusic.setLooping(true);
		
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
		johnnyDTex = new Texture("johnny_dansalot.png");//"johnny_dansalot_OLD4.png"
		theKingTex = new Texture("the_king.png");
		player1MovingLeft = false;
		player2MovingLeft = false;
		
		//player1 = new AnimatedImageBasedScreenObject(johnnyDTex, 600, 130, 0, 0, 0, 10, 10, player1MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY);
		//player1 = new MobileImageBasedScreenObject(johnnyDTex, 600, 130, 0, 0, 0, 10, 10, player1MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY);
		//player1 = new PlatformCharacter(johnnyDTex, 600, 140, 0, 0, 0, 1, 1, player1MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY, 50, 100, 150,platforms);//platforms
		player1 = new PlatformCharacter(johnnyDTex, 600,140,true);
		player1.setAcceleration(500);
		player1.setDeceleration(1000);
		player1.setMaxSpeed(300);
		johnnyDWalkAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1WalkSeq, ANIM_DELAY);
		johnnyDPunchAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1PunchSeq, ANIM_DELAY);
		johnnyDPunchAnim.setDiscrete(true);
		johnnyDKickAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1KickSeq, 0.1f); // 0.2f
		johnnyDKickAnim.setDiscrete(true);
		johnnyDBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1BlockSeq, ANIM_DELAY);
		johnnyDBlockAnim.setDiscrete(true);
		johnnyDRevBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player1RevBlockSeq, ANIM_DELAY);
		johnnyDRevBlockAnim.setDiscrete(true);
		player1.setAnimationParameters(johnnyDWalkAnim);
		//player1.setScaleX(10);
		//player1.setScaleY(10);
		player1.setPlatforms(platforms);
		player1CurrAnim = johnnyDWalkAnim;
		//player1.centerOriginGeometrically();
		
		//player2 = new AnimatedImageBasedScreenObject(theKingTex, 800, 130, 0, 0, 0, 10, 10, player2MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player2WalkSeq, ANIM_DELAY);
		//player2 = new MobileImageBasedScreenObject(theKingTex, 800, 130, 0, 0, 0, 10, 10, player2MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player2WalkSeq, ANIM_DELAY);
		//player2.centerOriginGeometrically();
		//player2 = new PlatformCharacter(theKingTex, 800, 140, 0, 0, 0, 1, 1, player2MovingLeft, false, FRAME_WIDTH, FRAME_HEIGHT, player2WalkSeq, ANIM_DELAY, 1000,400,10,platforms);
		player2 = new PlatformCharacter(theKingTex, 800, 140, true);
		player2.setAcceleration(500);
		player2.setDeceleration(1000);
		player2.setMaxSpeed(300);
		theKingWalkAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, player2WalkSeq,ANIM_DELAY);
		player2.setAnimationParameters(theKingWalkAnim);
		player2.setPlatforms(platforms);
		
		/*
		 * TODO
		 * Must have EdgeHandler change the camera's position
		 * to wherever the "action is happening"
		 * */
		
		edgeHandler = new EdgeHandler(player1, fightCam, batch, 0, theKingMapTex.getWidth(), 0, theKingMapTex.getHeight(), 0, EdgeHandler.EdgeConstants.PAN, EdgeHandler.EdgeConstants.PAN);
		
		fiaPlayer1 = new FighterInputAdapter(player1, johnnyDBlockAnim, johnnyDRevBlockAnim);
		//fiaPlayer2 = new FighterInputAdapter(player2);
		//plexi = new InputMultiplexer();
		//plexi.addProcessor(fiaPlayer1);
		//plexi.addProcessor(fiaPlayer2);
		Gdx.input.setInputProcessor(fiaPlayer1);
		//fightMusic.play();
		start();
		
		System.out.println(player1.getDrawStartX());
		System.out.println(player2.getDrawStartX());
	}
	
	public void start() {
		// notifying the Timer object about the task it will do
		// Three second delay, before the countdown timer begins
		// every one second, count down by one second (total of 60 seconds per round)
		
		timer.scheduleAtFixedRate(timerTask, 3000, 1000);
	}
	
	public int delta() {
		return deltaList[deltaY];
	}
	
	/*
	 * Title
	 * 
	 * Three buttons - the first one is already highlighted:
	 * Start Fight
	 * Settings
	 * Exit
	 * */
	
	public void renderTitle() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(titleBackgroundTex, -160, 0);
		batch.draw(titleTex, 200, 490);
		if (selectIncrementVal % 2 == 0) { // start fight button selected
			batch.draw(startFightSelectedTex, 330, 300);
			batch.draw(exitUnselectedTex, 330, 100);
		} else if (selectIncrementVal % 2 == 1) { // exit button selected
			batch.draw(startFightUnselectedTex, 330, 300);
			batch.draw(exitSelectedTex, 330, 100);
		}
	
		if ((Gdx.input.isKeyJustPressed(Keys.W)) || (Gdx.input.isKeyJustPressed(Keys.S))) {
			selectIncrementVal++;
		} /*else if (Gdx.input.isKeyJustPressed(Keys.S)) {
			selectIncrementVal++;
		}*/
		
		// selected "start fight"
		if ((Gdx.input.isKeyJustPressed(Keys.C)) && (selectIncrementVal % 2 == 0)){
			mode = 1;
			inFight = true;
		} else if ((Gdx.input.isKeyJustPressed(Keys.C)) && (selectIncrementVal % 2 == 1)) {
			Gdx.app.exit();
		}
		batch.end();
	}
	
	public void renderFight() {
		float dt = Gdx.graphics.getDeltaTime();
		player1Label.setPosition(20+(fightCam.position.x-WIDTH/2), 620+(fightCam.position.y-HEIGHT/2));
		player2Label.setPosition(850+(fightCam.position.x-WIDTH/2), 620+(fightCam.position.y-HEIGHT/2)); // adjust the x depending on character name lengths
		
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			mode = 0;
			inFight = false;
		}
		
		// must have input handler only detect S when inFight is true
		if (inFight) {
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
				if (player1CurrAnim != johnnyDWalkAnim) {
					player1.setAnimationParameters(johnnyDWalkAnim);
					player1CurrAnim = johnnyDWalkAnim;
				}
				player1MovingLeft = true;
				player1.setFlipX(player1MovingLeft);
				player1.accelerateAtAngle(180);
				System.out.printf("Player1: (%f, %f)\nPlayer2: (%f, %f)\n", player1.getXPos(), player1.getYPos(), player2.getXPos(), player2.getYPos());
			}
			
			// move to the right
			if (Gdx.input.isKeyPressed(Keys.D)) {
				if (player1CurrAnim != johnnyDWalkAnim) {
					player1.setAnimationParameters(johnnyDWalkAnim);
					player1CurrAnim = johnnyDWalkAnim;
				}
				player1MovingLeft = false;
				player1.setFlipX(player1MovingLeft);
				player1.accelerateAtAngle(0);
				System.out.printf("Player1: (%f, %f)\nPlayer2: (%f, %f)\n", player1.getXPos(), player1.getYPos(), player2.getXPos(), player2.getYPos());
			}
			
			// to jump
			if (Gdx.input.isKeyJustPressed(Keys.W)) {
				player1.jump();
				//player1.applyPhysics(dt);
				//player1.moveUp(deltaY);
				//player1OnGround = false;
				//deltaY = 0;
				System.out.printf("Player1: (%f, %f)\nPlayer2: (%f, %f)\n", player1.getXPos(), player1.getYPos(), player2.getXPos(), player2.getYPos());
			}
			
			// to block
			if (Gdx.input.isKeyJustPressed(Keys.S)) {
				if (player1CurrAnim != johnnyDBlockAnim) {
					player1.setAnimationParameters(johnnyDBlockAnim);
					
					fiaPlayer1.updateMobileObject(player1);
					player1CurrAnim = johnnyDBlockAnim;
				}
				
				//if (player1.is)
				//player1.accelerateAtAngle(270);
				//player1.startDiscreteAnimation();
			}
			
			// to punch
			if (Gdx.input.isKeyJustPressed(Keys.C)) {
				if (player1CurrAnim != johnnyDPunchAnim) {
					player1.setAnimationParameters(johnnyDPunchAnim);
					player1CurrAnim = johnnyDPunchAnim;
				}			
				player1.startDiscreteAnimation();
				//player1.accelerateAtAngle(0);
				if (player1.overlaps(player2) && !isPlayer2Blocking) {
					System.out.println("Player 1 got 'em with the Dab");
					player2Health -= 5;
					//Vector2 collide = player1.preventOverlap(player2);
					//player2.rebound(collide.angle(), 1.0f);
					
					if (player1MovingLeft) {
						//int tempSpeed = player2;
						player2.rebound(180, 0.5f);
						//player2.accelerateForward();
						//player2.applyPhysics(dt);
						//player2.accelerateAtAngle(180);
					} else {
						player2.rebound(0, 0.5f);
						//player2.accelerateForward();
						//player2.applyPhysics(dt);
						//player2.accelerateAtAngle(0);
					}
					
					//boolean kicking
					// boolean punching
					// initboundingpolygon -> initboundingpolygon with diff array (kicking leg out and standing tall)
					
				}
			}
			
			// to kick
			if (Gdx.input.isKeyJustPressed(Keys.V)) {
				if (player1CurrAnim != johnnyDKickAnim) {
					player1.setAnimationParameters(johnnyDKickAnim);
					player1CurrAnim = johnnyDKickAnim;
				}
				player1.startDiscreteAnimation();
				player1.accelerateAtAngle(0);
				if (player1.overlaps(player2) && !isPlayer2Blocking) {
					System.out.println("Player 1 got 'em with that Superman kick");
					player2Health -= 10;
					if (player1MovingLeft) {
						player2.rebound(180, 1.0f);
					} else {
						player2.rebound(0, 1.0f);
					}
				}
			}
			
			// applyPhysics is needed for Jump animation
			player1.applyPhysics(dt);
			player2.applyPhysics(dt);
			//player1.preventOverlap(platform);
			
			/* TODO
			 * Player 2 Controls:
			 * J - Move left
			 * L - Move right
			 * I - Jump
			 * K - Duck (technically, the block button)
			 * . - Punch
			 * / - Kick
			 * */
			
			// move to the left
			if (Gdx.input.isKeyPressed(Keys.J)) {
				if (player2CurrAnim != theKingWalkAnim) {
					player2.setDiscreteAnimation(false);
					player2.setAnimationParameters(theKingWalkAnim);
					player2CurrAnim = theKingWalkAnim;
				}
				player2MovingLeft = true;
				player2.setFlipX(player2MovingLeft);
				player2.accelerateAtAngle(180);
				//player2.animate(dt);
				//player2.setXPos(player2.getXPos() - 5);
			}
			
			// move to the right
			if (Gdx.input.isKeyPressed(Keys.L)) {
				if (player2CurrAnim != theKingWalkAnim) {
					player2.setDiscreteAnimation(false);
					player2.setAnimationParameters(theKingWalkAnim);
					player2CurrAnim = theKingWalkAnim;
				}
				player2MovingLeft = false;
				player2.setFlipX(player2MovingLeft);
				//player2.animate(dt);
				//player2.setXPos(player2.getXPos() + 5);
				player2.accelerateAtAngle(0);
			}
		}
		
		
		
		edgeHandler.enforceEdges();
		
		batch.begin();
		batch.draw(theKingMapTex, 0, 0); // display the map the players will fight at
		player1Label.draw(batch, 1); // display player 1's character name
		player2Label.draw(batch, 1); // display player 2's character name
		//batch.draw(healthBar1Tex, 10, 660);
		batch.draw(healthBar1Tex, 20+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		//batch.draw(healthBar2Tex, 600, 660);
		batch.draw(healthBar2Tex, 590+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		if (player2Health <= 0) {
			player2Health = 0;
		}
		
		if (player2Health < 100 && player2Health >= 0) {
			batch.draw(player2HealthBars.get(player2Health), 590+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		}
		//countdownTimer.draw(batch, 1);
		countDownLabel.setPosition(470+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		countDownLabel.draw(batch, 1);
		platformDrawer.draw(platform);
		drawer1.draw(player1); // display the sprite on the map
		drawer2.draw(player2);
		batch.end();
	}
	
	@Override
	public void render () {
		 if (mode == 0){
		 	renderTitle();
		 	
		 	if (fightMusic.isPlaying()) {
		 		fightMusic.stop();
		 	}
		 	
		 	titleMusic.play();
		 	batch.setProjectionMatrix(titleCam.combined);
		 } else if (mode == 1){ // if there is time, mode == 1 is for Settings
		 	renderFight();
		 	
		 	if (titleMusic.isPlaying()) {
		 		titleMusic.stop();
		 	}
		 	
		 	fightMusic.play();
		 } /*else {
		 	renderFight();
		 }*/
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		theKingMapTex.dispose();
		fightMusic.dispose();
		titleTex.dispose();
		titleBackgroundTex.dispose();
		startFightSelectedTex.dispose();
		startFightUnselectedTex.dispose();
		exitSelectedTex.dispose();
		exitUnselectedTex.dispose();
		johnnyDTex.dispose();
		theKingTex.dispose();
		healthBar1Tex.dispose();
		healthBar2Tex.dispose();
		theKingPlatformTex.dispose(); 
		health95.dispose();
		health90.dispose();
		health85.dispose();
		health80.dispose();
		health75.dispose();
		health70.dispose();
		health65.dispose();
		health60.dispose();
		health55.dispose();
		health50.dispose();
		health45.dispose();
		health40.dispose();
		health35.dispose();
		health30.dispose();
		health25.dispose();
		health20.dispose();
		health15.dispose();
		health10.dispose();
		health5.dispose();
		health0.dispose();
	}
}
