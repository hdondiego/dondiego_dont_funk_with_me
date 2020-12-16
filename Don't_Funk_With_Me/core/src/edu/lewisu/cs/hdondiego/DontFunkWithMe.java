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
	// player 1 and 2's character that will have its blocking animation changed depending
	// on if the S or K button was pressed or released
	private MobileImageBasedScreenObject player1MobileObject;
	private MobileImageBasedScreenObject player2MobileObject;

	private AnimationParameters animParameters1; // player 1's blocking animation
	private AnimationParameters revAnimParameters1; // player 1's reverse blocking animation - back to the "fighting" sprite
	
	private AnimationParameters animParameters2; // player 2's blocking animation
	private AnimationParameters revAnimParameters2; // player 2's reverse blocking animation - back to the "fighting" sprite
	
	// used to help let an outside class know if the opposing player did inflict damage
	private boolean isPlayer1Blocking, isPlayer2Blocking;
	
	public AnimationParameters getAnimParameters1() {
		return animParameters1;
	}
	
	public AnimationParameters getAnimParameters2() {
		return animParameters2;
	}
	
	public FighterInputAdapter(MobileImageBasedScreenObject player1MobileObject, MobileImageBasedScreenObject player2MobileObject, AnimationParameters animParameters1,
			AnimationParameters revAnimParameters1, AnimationParameters animParameters2, AnimationParameters revAnimParameters2) {
		this.player1MobileObject = player1MobileObject;
		this.player2MobileObject = player2MobileObject;
		this.animParameters1 = animParameters1;
		this.revAnimParameters1 = revAnimParameters1;
		this.animParameters2 = animParameters2;;
		this.revAnimParameters2 = revAnimParameters2;
		isPlayer1Blocking = false;
		isPlayer2Blocking = false;
	}
	
	public void updatePlayer1MobileObject(MobileImageBasedScreenObject player1MobileObject) {
		this.player1MobileObject = player1MobileObject;
	}
	
	public void updatePlayer2MobileObject(MobileImageBasedScreenObject player2MobileObject) {
		this.player2MobileObject = player2MobileObject;
	}
	
	public boolean isPlayer1Blocking() {
		return isPlayer1Blocking;
	}
	
	public boolean isPlayer2Blocking() {
		return isPlayer2Blocking;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		// player 1 - block
		if (keyCode == Keys.S) {
			if (player1MobileObject.getAnimationParameters() != animParameters1) {
				player1MobileObject.setAnimationParameters(animParameters1);
			}
			player1MobileObject.startDiscreteAnimation();
			isPlayer1Blocking =  true;
		}
		
		// player 2 - block
		if (keyCode == Keys.K) {
			if (player2MobileObject.getAnimationParameters() != animParameters2) {
				player2MobileObject.setAnimationParameters(animParameters2);
			}
			player2MobileObject.startDiscreteAnimation();
			isPlayer2Blocking =  true;
		}
		
		return true;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		// player 1 - undoing block
		if (keyCode == Keys.S) {
			if (player1MobileObject.getAnimationParameters() != revAnimParameters1) {
				player1MobileObject.setAnimationParameters(revAnimParameters1);
			}
			player1MobileObject.startDiscreteAnimation();
			isPlayer1Blocking = false;
		}
		
		// player 2 - undoing block
		if (keyCode == Keys.K) {
			if (player2MobileObject.getAnimationParameters() != revAnimParameters2) {
				player2MobileObject.setAnimationParameters(revAnimParameters2);
			}
			player2MobileObject.startDiscreteAnimation();
			isPlayer2Blocking = false;
		}
		
		return true;
	}
}



public class DontFunkWithMe extends ApplicationAdapter {
	SpriteBatch batch;
	Texture theKingMapTex, johnnyDTex, theKingTex, healthBar1Tex, healthBar2Tex, theKingPlatformTex; // player2Tex, 
	Texture health95, health90, health85, health80, health75, health70, health65, health60, health55, health50, health45, health40, health35, health30, health25, health20, health15, health10, health5, health0;
	Texture titleTex, titleBackgroundTex, startFightSelectedTex, startFightUnselectedTex, exitSelectedTex, exitUnselectedTex, playerOneFirstWin, playerOneSecondWin, playerTwoFirstWin, playerTwoSecondWin;
	HashMap<Integer, Texture> player1HealthBars, player2HealthBars;
	PlatformCharacter player1, player2;
	ImageBasedScreenObject platform;
	ImageBasedScreenObjectDrawer drawer1, drawer2, platformDrawer;
	OrthographicCamera fightCam, titleCam;
	int WIDTH, HEIGHT, seconds, player1Health, player2Health;
	float FRAME_WIDTH, FRAME_HEIGHT, ANIM_DELAY;
	
	// identifying the different animation names and the frame sequences for each animation
	AnimationParameters johnnyDWalkAnim, johnnyDPunchAnim, johnnyDKickAnim, johnnyDBlockAnim, johnnyDRevBlockAnim, johnnyDPunchLandedAnim, johnnyDKickLandedAnim, player1CurrAnim;
	int[] johnnyDWalkSeq = {0,0,1,0,2,0};
	int[] johnnyDPunchSeq = {3,0,0,1,1,1};
	int[] johnnyDKickSeq = {2,1,3,1,0,2};
	int[] johnnyDBlockSeq = {1,2,2,2,3,2};
	int[] johnnyDRevBlockSeq = {3,2,2,2,1,2};
	int[] johnnyDPunchLandedSeq = {0,3,1,3};
	int[] johnnyDKickLandedSeq = {0,3,1,3,2,3,3,3};
	
	AnimationParameters theKingWalkAnim, theKingPunchAnim, theKingKickAnim, theKingBlockAnim, theKingRevBlockAnim, theKingPunchLandedAnim, theKingKickLandedAnim, player2CurrAnim;
	int[] theKingWalkSeq = {0,0,1,0,2,0,3,0}; // only for The King
	int[] theKingPunchSeq = {0,1,1,1,2,1};
	int[] theKingKickSeq = {3,1,0,2,1,2,2,2};
	int[] theKingBlockSeq = {3,2,0,3,1,3};
	int[] theKingRevBlockSeq = {1,3,0,3,3,2};
	int[] theKingPunchLandedSeq = {2,3,3,3};
	int[] theKingKickLandedSeq = {2,3,3,3,0,4,1,4};
	
	// used to help let the PlatformCharacter class know what platforms the character will jump and land on
	// for the purpose of this ArrayList, it will only hold the floor's ImageBasedScreenObject
	ArrayList<ImageBasedScreenObject> platforms = new ArrayList<ImageBasedScreenObject>();
	
	ActionLabel player1Label, player2Label; // the names of each character used
	Music titleMusic, fightMusic; // the music used in the title screen, and the background fight music
	boolean player1MovingLeft, player2MovingLeft, drawReadyLabel, drawDanceLabel, isRoundOver, inFight;
	int screen = 0; // 0 -> title, 1 -> fight; used to help identify what type of screen to render
	int selectIncrementVal = 0; // used to increment when player loops through title screen buttons
	
	Timer timer; // used to help execute the task of changing the countdown's value
	TimerTask timerTask; // used to help establish the steps of changing the countdown value
	LabelStyle style = new LabelStyle(); // holds the font style to be used for the countdown
	Label countDownLabel; // the label that displays the number of seconds left in the fight
	
	EdgeHandler edgeHandler; // controls how far out the camera will go (was used for player 1 to dictate where the camera will go)
	FighterInputAdapter fightInputHandler;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		theKingMapTex = new Texture("the_king_map.png"); // The King's map
		theKingPlatformTex = new Texture("the_king_platform.png"); // used to establish the ground that the players will jump on
		platform = new ImageBasedScreenObject(theKingPlatformTex, 0,0,0,0,0,1,1,false,false);
		platforms.add(platform); // for the PlatformCharacter class
		
		// setting up the OrthographicCamera objects - title and fight cameras
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		titleCam = new OrthographicCamera(WIDTH, HEIGHT);
		titleCam.translate(WIDTH/2, HEIGHT/2);
		titleCam.update();
		batch.setProjectionMatrix(titleCam.combined);
		fightCam = new OrthographicCamera(WIDTH, HEIGHT);
		fightCam.translate(WIDTH/2, HEIGHT/2);
		fightCam.update();
		
		// used to help with instantiating AnimationParameters objects for Johnny Danslot and The King's animations
		FRAME_WIDTH = 320f;
		FRAME_HEIGHT = 320f;
		ANIM_DELAY = 0.1f;
		
		// the Texture objects used for the title screen
		titleTex = new Texture("dont_funk_with_me_title.png");
		titleBackgroundTex = new Texture("dance_floor.jpeg");
		startFightSelectedTex = new Texture("start_selected.png");
		startFightUnselectedTex = new Texture("start_unselected.png");
		exitSelectedTex = new Texture("exit_selected.png");
		exitUnselectedTex = new Texture("exit_unselected.png");
		
		// used to help draw the players and the platform
		drawer1 = new ImageBasedScreenObjectDrawer(batch);
		drawer2 = new ImageBasedScreenObjectDrawer(batch);
		platformDrawer = new ImageBasedScreenObjectDrawer(batch);
		
		// originally intended to help control whether or not the controls can be used 
		isRoundOver = false; // originally intended to stop the controls so players can pay attention to the messages after the round(s)
		inFight = false; // start at title menu - not actively fighting -> lock the same keys used to fight and only use to navigate title menu
		
		// creating labels for each fighter on the top part of the screen - in this case, Johnny Dansalot (main character) and The King
		player1Label = new ActionLabel("Johnny Dansalot", 10, 620, "fonts/agency_fb_35pt_black.fnt");
		player2Label = new ActionLabel("The King", 820, 620, "fonts/agency_fb_35pt_black.fnt");
		
		// adding the health bars for each character
		healthBar1Tex = new Texture("health_bar.png"); // 100 percent health bar for Player 1
		healthBar2Tex = new Texture("health_bar.png"); // 100 percent health bar for Player 2
		
		// the health bars to be used on top of the original one (100% health) to show how much damage the player took
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

		// HashMaps for each character to help with automatically returning the Texture for the amount of damage taken for the player
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
		
		// each player starts with 100 health
		player1Health = 100;
		player2Health = 100;
		
		// instantiating the title screen music
		// the music continually loops
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/title_screen_02_disco_bass_loop_120_bpm_josefpres.wav"));
		titleMusic.setLooping(true);
		
		// instantiating the background fight music for The King's map
		// the music continually loops
		fightMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/the_king_kb11_BSB.wav"));
		fightMusic.setLooping(true);
		
		// creating the label for the countdown timer
		seconds = 60; // only a minute worth of fighting
		style.font = new BitmapFont(Gdx.files.internal("fonts/agency_fb_35pt_black.fnt"));
		countDownLabel = new Label(Integer.toString(seconds),style);
		countDownLabel.setPosition(470,660);
		
		// changing the text on the countdown label
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				if (seconds > 0) {
					inFight = true;
					isRoundOver = false;
					seconds--;
					countDownLabel.setText(seconds);
				} else { // 60 seconds have already elapsed
					isRoundOver = true;
					fightMusic.stop();
				}
			}
		};
		
		// instantiating the textures for Johnny Danslot and The King sprite sheets
		johnnyDTex = new Texture("johnny_dansalot.png");
		theKingTex = new Texture("the_king.png");
		
		// used to help determine if the sprites need to flip their orientation facing left or right
		player1MovingLeft = false;
		player2MovingLeft = false;
		
		// instantiating Johnny Danslot's character
		player1 = new PlatformCharacter(johnnyDTex, 240,140,true);
		player1.setAcceleration(500);
		player1.setDeceleration(1000);
		player1.setMaxSpeed(300);
		johnnyDWalkAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDWalkSeq, ANIM_DELAY);
		johnnyDPunchAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDPunchSeq, ANIM_DELAY);
		johnnyDPunchAnim.setDiscrete(true);
		johnnyDKickAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDKickSeq, 0.1f); // 0.2f
		johnnyDKickAnim.setDiscrete(true);
		johnnyDBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDBlockSeq, ANIM_DELAY);
		johnnyDBlockAnim.setDiscrete(true);
		johnnyDRevBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDRevBlockSeq, ANIM_DELAY);
		johnnyDRevBlockAnim.setDiscrete(true);
		johnnyDPunchLandedAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDPunchLandedSeq, ANIM_DELAY);
		johnnyDPunchLandedAnim.setDiscrete(true);
		johnnyDKickLandedAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, johnnyDKickLandedSeq, ANIM_DELAY);
		johnnyDKickLandedAnim.setDiscrete(true);
		player1.setAnimationParameters(johnnyDWalkAnim);
		player1.setPlatforms(platforms);
		player1CurrAnim = johnnyDWalkAnim;
		
		// instantiating The King's character
		player2 = new PlatformCharacter(theKingTex, 480, 140, true);
		player2.setAcceleration(500);
		player2.setDeceleration(1000);
		player2.setMaxSpeed(300);
		theKingWalkAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingWalkSeq, ANIM_DELAY);
		theKingPunchAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingPunchSeq, ANIM_DELAY);
		theKingPunchAnim.setDiscrete(true);
		theKingKickAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingKickSeq, ANIM_DELAY);
		theKingKickAnim.setDiscrete(true);
		theKingBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingBlockSeq, ANIM_DELAY);
		theKingBlockAnim.setDiscrete(true);
		theKingRevBlockAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingRevBlockSeq, ANIM_DELAY);
		theKingRevBlockAnim.setDiscrete(true);
		theKingPunchLandedAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingPunchLandedSeq, ANIM_DELAY);
		theKingPunchLandedAnim.setDiscrete(true);
		theKingKickLandedAnim = new AnimationParameters(FRAME_WIDTH, FRAME_HEIGHT, theKingKickLandedSeq, ANIM_DELAY);
		theKingKickLandedAnim.setDiscrete(true);
		player2.setAnimationParameters(theKingWalkAnim);
		player2.setPlatforms(platforms);
		player2CurrAnim = theKingWalkAnim;
		
		/*
		 * TODO
		 * Must have EdgeHandler change the camera's position
		 * to wherever the "action is happening"
		 * */
		
		edgeHandler = new EdgeHandler(player1, fightCam, batch, 0, theKingMapTex.getWidth(), 0, theKingMapTex.getHeight(), 0, EdgeHandler.EdgeConstants.PAN, EdgeHandler.EdgeConstants.PAN);
		
		fightInputHandler = new FighterInputAdapter(player1, player2, johnnyDBlockAnim, johnnyDRevBlockAnim, theKingBlockAnim, theKingRevBlockAnim);
		Gdx.input.setInputProcessor(fightInputHandler);
		
		System.out.println(player1.getDrawStartX());
		System.out.println(player2.getDrawStartX());
	}
	
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
		}
		
		// selected "start fight"
		if ((Gdx.input.isKeyJustPressed(Keys.C)) && (selectIncrementVal % 2 == 0)){
			screen = 1;
			inFight = true;
		} else if ((Gdx.input.isKeyJustPressed(Keys.C)) && (selectIncrementVal % 2 == 1)) { // selected to exit the game
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
			screen = 0;
			timer.cancel();
			
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
			inFight = false;
			seconds = 60;
			countDownLabel.setText(seconds);
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
				System.out.printf("Player1: (%f, %f)\nPlayer2: (%f, %f)\n", player1.getXPos(), player1.getYPos(), player2.getXPos(), player2.getYPos());
			}
			
			// to block
			if (Gdx.input.isKeyJustPressed(Keys.S)) {
				if (player1CurrAnim != johnnyDBlockAnim) {
					player1.setAnimationParameters(johnnyDBlockAnim);
					
					fightInputHandler.updatePlayer1MobileObject(player1);
					player1CurrAnim = johnnyDBlockAnim;
				}
			}
			
			// to punch
			if (Gdx.input.isKeyJustPressed(Keys.C)) {
				if (player1CurrAnim != johnnyDPunchAnim) {
					player1.setAnimationParameters(johnnyDPunchAnim);
					player1CurrAnim = johnnyDPunchAnim;
				}			
				player1.startDiscreteAnimation();
				if (player1.overlaps(player2) && !fightInputHandler.isPlayer2Blocking()) {
					System.out.println("Player 1 got 'em with the Dab");
					player2Health -= 5;
					if (player1MovingLeft) {
						player2MovingLeft = false;
						player2.setFlipX(player2MovingLeft);
						player2.rebound(180, 0.5f);
						player2.setAnimationParameters(theKingPunchLandedAnim);
						player2CurrAnim = theKingPunchLandedAnim;
						player2.startDiscreteAnimation();
					} else {
						player2MovingLeft = true;
						player2.setFlipX(player2MovingLeft);
						player2.rebound(0, 0.5f);
						player2.setAnimationParameters(theKingPunchLandedAnim);
						player2CurrAnim = theKingPunchLandedAnim;
						player2.startDiscreteAnimation();
					}
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
				if (player1.overlaps(player2) && !fightInputHandler.isPlayer2Blocking()) {
					System.out.println("Player 1 got 'em with that Superman kick");
					player2Health -= 10;
					if (player1MovingLeft) {
						player2MovingLeft = false;
						player2.setFlipX(player2MovingLeft);
						player2.rebound(180, 1.0f);
						player2.setAnimationParameters(theKingKickLandedAnim);
						player2CurrAnim = theKingKickLandedAnim;
						player2.startDiscreteAnimation();
					} else {
						player2MovingLeft = true;
						player2.setFlipX(player2MovingLeft);
						player2.rebound(0, 1.0f);
						player2.setAnimationParameters(theKingKickLandedAnim);
						player2CurrAnim = theKingKickLandedAnim;
						player2.startDiscreteAnimation();
					}
				}
			}
			
			// applyPhysics is needed for Jump animation
			player1.applyPhysics(dt);
			player2.applyPhysics(dt);
			
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
					player2.setAnimationParameters(theKingWalkAnim);
					player2CurrAnim = theKingWalkAnim;
				}
				player2MovingLeft = true;
				player2.setFlipX(player2MovingLeft);
				player2.accelerateAtAngle(180);
			}
			
			// move to the right
			if (Gdx.input.isKeyPressed(Keys.L)) {
				if (player2CurrAnim != theKingWalkAnim) {
					player2.setAnimationParameters(theKingWalkAnim);
					player2CurrAnim = theKingWalkAnim;
				}
				player2MovingLeft = false;
				player2.setFlipX(player2MovingLeft);
				player2.accelerateAtAngle(0);
			}
			
			// to jump
			if (Gdx.input.isKeyJustPressed(Keys.I)) {
				player2.jump();
			}
			
			// to block
			if (Gdx.input.isKeyJustPressed(Keys.K)) {
				if (player2CurrAnim != theKingBlockAnim) {
					player2.setAnimationParameters(theKingBlockAnim);
					
					fightInputHandler.updatePlayer2MobileObject(player2);
					player2CurrAnim = theKingBlockAnim;
				}
			}
			
			// to punch
			if (Gdx.input.isKeyJustPressed(Keys.PERIOD)) {
				if (player2CurrAnim != theKingPunchAnim) {
					player2.setAnimationParameters(theKingPunchAnim);
					player2CurrAnim = theKingPunchAnim;
				}			
				player2.startDiscreteAnimation();
				if (player2.overlaps(player1) && !fightInputHandler.isPlayer1Blocking()) { // FIX THIS
					System.out.println("Player 2 got 'em with the Hoho");
					player1Health -= 5;
					
					if (player2MovingLeft) {
						player1MovingLeft = false;
						player1.setFlipX(player1MovingLeft);
						player1.rebound(180, 0.5f);
						player1.setAnimationParameters(johnnyDPunchLandedAnim);
						player1CurrAnim = johnnyDPunchLandedAnim;
						player1.startDiscreteAnimation();
					} else {
						player1MovingLeft = true;
						player1.setFlipX(player1MovingLeft);
						player1.rebound(0, 0.5f);
						player1.setAnimationParameters(johnnyDPunchLandedAnim);
						player1CurrAnim = johnnyDPunchLandedAnim;
						player1.startDiscreteAnimation();
					}
				}
			}
			
			// to kick
			if (Gdx.input.isKeyJustPressed(Keys.SLASH)) {
				if (player2CurrAnim != theKingKickAnim) {
					player2.setAnimationParameters(theKingKickAnim);
					player2CurrAnim = theKingKickAnim;
				}
				player2.startDiscreteAnimation();
				if (player2.overlaps(player1) && !fightInputHandler.isPlayer1Blocking()) { // FIX THIS
					System.out.println("Player 2 got 'em with that Flicking Kick");
					player1Health -= 10;
					if (player2MovingLeft) {
						player1MovingLeft = false;
						player1.setFlipX(player1MovingLeft);
						player1.rebound(180, 1.0f);
						player1.setAnimationParameters(johnnyDKickLandedAnim);
						player1CurrAnim = johnnyDKickLandedAnim;
						player1.startDiscreteAnimation();
					} else {
						player1MovingLeft = true;
						player1.setFlipX(player1MovingLeft);
						player1.rebound(0, 1.0f);
						player1.setAnimationParameters(johnnyDKickLandedAnim);
						player1CurrAnim = johnnyDKickLandedAnim;
						player1.startDiscreteAnimation();
					}
				}
			}
		}
		
		
		
		edgeHandler.enforceEdges();
		
		batch.begin();
		batch.draw(theKingMapTex, 0, 0); // display the map the players will fight at
		player1Label.draw(batch, 1); // display player 1's character name
		player2Label.draw(batch, 1); // display player 2's character name
		
		// positioning Player 1's and Player 2's health bars and displaying decreased health (if needed)
		batch.draw(healthBar1Tex, 20+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		if (player1Health < 0) {
			player1Health = 0;
		}
		
		if(player1Health < 100 && player1Health >= 0) {
			int decBarLength = player1HealthBars.get(player1Health).getWidth();
			batch.draw(player1HealthBars.get(player1Health), (370-decBarLength)+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		}
		
		batch.draw(healthBar2Tex, 590+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		if (player2Health < 0) {
			player2Health = 0;
		}
		
		if (player2Health < 100 && player2Health >= 0) {
			batch.draw(player2HealthBars.get(player2Health), 590+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		}

		// placement of countdown
		countDownLabel.setPosition(470+(fightCam.position.x-WIDTH/2), 660+(fightCam.position.y-HEIGHT/2));
		countDownLabel.draw(batch, 1);
		
		// draw the platform and the characters
		platformDrawer.draw(platform);
		drawer1.draw(player1);
		drawer2.draw(player2);

		batch.end();
	}
	
	@Override
	public void render () {
		 if (screen == 0){
		 	renderTitle();
		 	
		 	if (fightMusic.isPlaying()) {
		 		fightMusic.stop();
		 	}
		 	
		 	titleMusic.play();
		 	batch.setProjectionMatrix(titleCam.combined);
		 } else if (screen == 1){ // if there is time, mode == 1 is for Settings
			//seconds = 60;
			//countDownLabel.setText(seconds);
			 /*
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
			*/
			 
			renderFight();
		 	//start();
		 	
		 	if (titleMusic.isPlaying()) {
		 		titleMusic.stop();
		 		//seconds = 60;
		 		//danceTimer.scheduleAtFixedRate(danceTimerTask, 3000, 1000);
				timer.scheduleAtFixedRate(timerTask, 4000, 1000);
		 	}
		 	
		 	fightMusic.play();
		 	batch.setProjectionMatrix(fightCam.combined);
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
