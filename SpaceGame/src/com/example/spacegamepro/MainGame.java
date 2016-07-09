package com.example.spacegamepro;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.TickerText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import com.example.spacegamepro.VerticalParallaxBackground.VerticalParallaxEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

public class MainGame extends BaseGameActivity{
	
	//camera and scene
	private static int CAMERA_WIDTH = 0;
	private static int CAMERA_HEIGHT = 0;
	private Camera camera;
	private Scene scene;	
	
	//Font default and score
	private Font mFont;
	private Texture mFontTexture;
	private ChangeableText score;
	private int hitCount = 0;
	
	
	//background
	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundRegionTexture;
	
	//Sound and Music
	private Sound shooting;
	private Sound collisionSound;
	private Music backgroundMusic;
	
	//player
	private Texture mPlayerTexture;
	private TiledTextureRegion mPlayerRegionTexture;
	private AnimatedSprite player;
	
	//target(may bay)	
	private Texture mcharacter;
	private TiledTextureRegion mcharacterRegion;	
	private LinkedList<AnimatedSprite> targetLL;
	private LinkedList<AnimatedSprite> targetTobeAdd;
	private LinkedList<AnimatedSprite> bulletTargetToBeAdd;
	private LinkedList<AnimatedSprite> bulletTargetLL;
	
	//bullet
	private TiledTextureRegion mbulletRegion;
	private Texture mBulletTexture;
	private LinkedList<AnimatedSprite> bulletLL;
	private LinkedList<AnimatedSprite> bulletToBeAdd;
	private AnimatedSprite bulletsprite;
	
	//Object Pool(optimization memory)
	ProjectilesPool pPool;
	TargetsPool tPool;
	
	//collision(va cham)
	private Texture mcollisionEnemyTexture;
	private TiledTextureRegion mcollisionEnemyRegion;
	private AnimatedSprite collisenemy;
	
	private Context context = this;
	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		CAMERA_WIDTH = dm.widthPixels;
		CAMERA_HEIGHT = dm.heightPixels;
		
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);		
		  final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				  new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera).setNeedsMusic(true).setNeedsSound(true);		  
		  return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		
		//set Music and Sound path
		TextureRegionFactory.setAssetBasePath("gfx/");
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		
		try {
			shooting = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "Hit1.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			collisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "explosion.wav");
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "photonix2.mp3");
			backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//font default
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50, true, Color.WHITE);
		this.mEngine.getFontManager().loadFont(this.mFont);
		
		//background texture
		this.mBackgroundTexture = new Texture(512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundRegionTexture = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "6.jpg",0,200);
		
		//enemy texture - target
		this.mcharacter = new Texture(512, 128,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mcharacterRegion = TextureRegionFactory.createTiledFromAsset(this.mcharacter, this, "enemy11.png", 0, 0,2,1);
		
		//player texture
		this.mPlayerTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayerRegionTexture = TextureRegionFactory.createTiledFromAsset(this.mPlayerTexture, this, "player.png",0,0,3,1);
		
		//bullet 
		this.mBulletTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mbulletRegion = TextureRegionFactory.createTiledFromAsset(this.mBulletTexture, this, "ship_bullet.png", 0, 0,3,1);
		
		//collision enemy 
		this.mcollisionEnemyTexture = new Texture(512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mcollisionEnemyRegion = TextureRegionFactory.createTiledFromAsset(this.mcollisionEnemyTexture, this,"blast6.png", 0, 0,5,1);
		
		//Pool
		pPool = new ProjectilesPool(mbulletRegion);//target
		tPool = new TargetsPool(mcharacterRegion);//bullet
		
		//load texture
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture,this.mcharacter,
				this.mPlayerTexture,this.mBulletTexture,this.mcollisionEnemyTexture,this.mFontTexture);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		this.backgroundMusic.play();
		background();
	
		//control sprite player
		DragPlayer();
		
		//random sprite
		createSprite();
					
		//bullet
		bulletLL = new LinkedList<AnimatedSprite>();
		bulletToBeAdd = new LinkedList<AnimatedSprite>();
				
		//target
		targetLL = new LinkedList<AnimatedSprite>();
		targetTobeAdd = new LinkedList<AnimatedSprite>();
		bulletTargetLL = new LinkedList<AnimatedSprite>();
		bulletTargetToBeAdd = new LinkedList<AnimatedSprite>();
		scene.registerUpdateHandler(detect);
		
		//collision enemy
		collisenemy = new AnimatedSprite(-CAMERA_WIDTH, 100, this.mcollisionEnemyRegion);
		//collisenemy.setScale(1.15f);
		collisenemy.animate(200);
		scene.attachChild(collisenemy);		
		collisionTimer();
		
		//score
		score = new ChangeableText(0, 0, this.mFont, "Score: " + "00000");
		score.setPosition(0, 0);
		score.setText("Score: "+ hitCount);
		scene.attachChild(score);
		
		return scene;
	}

	private void collisionTimer() {
		// TODO Auto-generated method stub
		getEngine().registerUpdateHandler(new TimerHandler(2f, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub				
				collisenemy.setVisible(false);
			}
		}));
	}

	//detect collision and cleaning up
	IUpdateHandler detect = new IUpdateHandler() {
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			Iterator<AnimatedSprite> targets = targetLL.iterator();			
			AnimatedSprite target2;	
			boolean hit = false;
			
			while(targets.hasNext()){
				target2 = targets.next();
				if(target2.getY() >= CAMERA_HEIGHT + target2.getHeight()){
					tPool.recyclePoolItem(target2);
					targets.remove();
					continue;				
				}
				if(target2.collidesWith(player)){
					//gameoverText();
					break;
				}
				
			
			Iterator<AnimatedSprite> bullettargets = bulletTargetLL.iterator();
			AnimatedSprite bulletTarget2;
			while(bullettargets.hasNext()){
				bulletTarget2 = bullettargets.next();
				if(bulletTarget2.getY() >= CAMERA_HEIGHT + bulletTarget2.getHeight()){
					pPool.recyclePoolItem(bulletTarget2);
					bullettargets.remove();
					continue;
				}
				if(bulletTarget2.collidesWith(player)){
//					player.detachSelf();
//					bulletTargetLL.remove();
//					collisenemy.setVisible(true);
//					collisenemy.setPosition(player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2);
//					collisionSound.play();
					//gameoverText();
					break;
				}
			}
				
				
			Iterator<AnimatedSprite> bullets = bulletLL.iterator();
			AnimatedSprite bullets2;
			
			while(bullets.hasNext()){
				bullets2 = bullets.next();				
				if(bullets2.getY() <= - bullets2.getHeight()){
					pPool.recyclePoolItem(bullets2);
					bullets.remove();
					continue;
				}
				if(target2.collidesWith(bullets2)){		
					pPool.recyclePoolItem(bullets2);
					bullets.remove();
					collisenemy.setVisible(true);
					collisenemy.setPosition((target2.getX()+40), target2.getY()+50);										
					collisionSound.play();	
					hit = true;
					break;
				}			
			}	
			if(hit){
				hitCount++;
				score.setText("Score: "+ hitCount);
				tPool.recyclePoolItem(target2);
				targets.remove();
				hit = false;
			}
		}
			bulletTargetLL.addAll(bulletTargetToBeAdd);
			bulletTargetToBeAdd.clear();
			bulletLL.addAll(bulletToBeAdd);
			bulletToBeAdd.clear();
			targetLL.addAll(targetTobeAdd);
			targetTobeAdd.clear();
	}
};		
	
	private void background() {
		// TODO Auto-generated method stub
		final AutoVerticalParallaxBackground auto = new AutoVerticalParallaxBackground(0, 0, 0, 5);
		auto.attachVerticalParallaxEntity(new VerticalParallaxEntity(1.5f, new Sprite(0, CAMERA_HEIGHT-this.mBackgroundRegionTexture.getHeight(), this.mBackgroundRegionTexture)));
		scene.setBackground(auto);
	}

	

	private void createSprite() {
		// TODO Auto-generated method stub
		
		getEngine().registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				addcharacter();				
			}
		}));
	}
	
	public void addcharacter() {
		// TODO Auto-generated method stub
		
		Random rand = new Random();	
		
		int x1 = CAMERA_WIDTH - this.mcharacterRegion.getWidth()/2;		 
		int x = rand.nextInt(x1);
		
		int y =-this.mcharacterRegion.getHeight();
	
		final AnimatedSprite enemy;
		enemy = tPool.obtainPoolItem();
		enemy.setPosition(x, y);
		enemy.setScale(0.5f);
		enemy.animate(100);	
		scene.attachChild(enemy);
		
		//int dur = rand.nextInt(2) + 1;		
		MoveYModifier modifier = new MoveYModifier(10, enemy.getY(), CAMERA_HEIGHT + enemy.getHeight());
		enemy.registerEntityModifier(modifier);
		targetTobeAdd.add(enemy);
		
		AnimatedSprite b;
		b = pPool.obtainPoolItem();
		b.setPosition(enemy.getX() + enemy.getWidth()/2.5f, enemy.getY());
		Log.v("", "Position"+enemy.getX() + enemy.getWidth()/3);
		b.animate(new long[]{100},new int[]{0},1000);
		scene.attachChild(b);
		
		MoveYModifier modbullet = new MoveYModifier(2, enemy.getY(), CAMERA_HEIGHT + enemy.getHeight());
		b.registerEntityModifier(modbullet);
		bulletTargetToBeAdd.add(b);
	}
	
	
//drag player
	private void DragPlayer() {
		// TODO Auto-generated method stub
		float px = CAMERA_WIDTH/2;
		float py = CAMERA_HEIGHT - this.mPlayerRegionTexture.getWidth();
		
		player = new AnimatedSprite(px, py, this.mPlayerRegionTexture){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, pSceneTouchEvent.getY()-this.getHeight()/2);
				final float pX = pSceneTouchEvent.getX();
				shoot(pX);								
				return true;
			}			
			
		};
		player.animate(90);
		scene.attachChild(player);
		scene.registerTouchArea(player);
		scene.setTouchAreaBindingEnabled(true);
	}
		
	
	public void shoot(float pX) {
		// TODO Auto-generated method stub		
		if (!CoolDown.sharedCoolDown().checkValidity()) {
		    return;
		}
		final float pY = player.getY();				
		bulletsprite = pPool.obtainPoolItem();
		bulletsprite.setPosition(pX-13, pY);
		
		bulletsprite.animate(new long[]{100}, new int[]{0},10000);
		scene.attachChild(bulletsprite);
		
		MoveYModifier shootBullet = new MoveYModifier(3f, bulletsprite.getY(),-CAMERA_HEIGHT);
		bulletsprite.registerEntityModifier(shootBullet.clone());
		bulletToBeAdd.add(bulletsprite);	
		this.shooting.play();
	}
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
			

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN){									
			new AlertDialog.Builder(context)
				.setCancelable(false)
				.setPositiveButton("Menu game",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						runOnUpdateThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent menu = new Intent("com.example.spacegamepro.MENUGAME");
								startActivity(menu);
							}
						});
					}
				}).setNegativeButton("Resume", null)
				  .show();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	public void restart() {
		// TODO Auto-generated method stub
		runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scene.detachChildren();								
			}
		});
		targetLL.clear();
		targetTobeAdd.clear();
		bulletLL.clear();
		bulletToBeAdd.clear();
		bulletTargetLL.clear();
		bulletTargetToBeAdd.clear();
	}
	
	
}

	