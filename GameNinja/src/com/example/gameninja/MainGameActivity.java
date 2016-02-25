package com.example.gameninja;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

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
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.DelayModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.IModifier.IModifierListener;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainGameActivity extends BaseGameActivity implements IOnSceneTouchListener{
	private static int CAMERA_WIDTH;
	private static int CAMERA_HEIGHT;
	
	private Scene scene;
	private Camera camera;
	
	//background
	private Texture mbackgroundTexture;
	private TextureRegion mbackgroundRegion;
	
	//object pools
	ProjectilesPool pPool;
	TargetsPool tPool;
	
	//player
	private Texture mFaceTexture;
	private TiledTextureRegion mFaceRegionTexture;
	public static AnimatedSprite layer;
	
	//target
	private Texture mTargetTexture;
	private TiledTextureRegion mTargetRegion;
	
	private LinkedList<AnimatedSprite> targetLL;
	private LinkedList<AnimatedSprite> TargetsTobeAdd;

	//bullet
	private LinkedList<Sprite> projectLL;
	private LinkedList<Sprite> projectToBeAdd;
	private TextureRegion mProjectRegion;
	private Texture mProjectTexture;
	
	//Sound and Music
	private Sound shooting;
	private Music backgroundMusic;
	
	//pause game
	private Texture mPauseTexture;
	private TextureRegion mPauseRegion;
	private CameraScene mPauseScene;
	
	//score-------------------------
	private CameraScene mResultScene;
	private boolean runningFlag = false;
	private boolean pauseFlag = false;
	private Texture mFontTexture;
	private Font mFont;
	private ChangeableText score;
	private int hitCount;
	private final int maxScore = 10;
	private Sprite winSprite;
	private Sprite failSprite;
	private Texture mWinTexture;
	private Texture mFailTexture;
	private TextureRegion mWinTextureRegion;
	private TextureRegion mFailTextureRegion;
	
	//--------------------------
	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		CAMERA_WIDTH = dm.widthPixels;
		CAMERA_HEIGHT = dm.heightPixels;
		
		this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);		
		final Engine mEngine = new Engine(new EngineOptions(true,ScreenOrientation.LANDSCAPE, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera).setNeedsSound(true).setNeedsMusic(true));
		
		
		if(MultiTouch.isSupported(this)){
			try {
				mEngine.setTouchController(new MultiTouchController());
			} catch (MultiTouchException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Du ma Sai gon!", Toast.LENGTH_LONG).show();
			}
			if(MultiTouch.isSupportedDistinct(this)){
				Toast.makeText(this, "Cam ung da diem hoat dong tot!", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this, "Du ma lam nay gio!", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(this, "Du ma lam nay gio!", Toast.LENGTH_LONG).show();
		}
		
		return mEngine;	
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mFaceTexture = new Texture(2048, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		this.mFaceRegionTexture = TextureRegionFactory.createTiledFromAsset(this.mFaceTexture, this, "hero.png", 0, 0,11,1);
		
		this.mTargetTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);		
		this.mTargetRegion = TextureRegionFactory.createTiledFromAsset(this.mTargetTexture, this, "zombie.png",0,0,3,1);

		this.mProjectTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mProjectRegion = TextureRegionFactory.createFromAsset(this.mProjectTexture, this, "bullet.png", 0,0);
		
		this.mPauseTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPauseRegion = TextureRegionFactory.createFromAsset(this.mPauseTexture, this, "paused.png",0,0);
		
		this.mbackgroundTexture = new Texture(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mbackgroundRegion = TextureRegionFactory.createFromAsset(this.mbackgroundTexture, this, "background.png",0,0);
		//Pool
		pPool = new ProjectilesPool(mProjectRegion);
		tPool = new TargetsPool(mTargetRegion);
		
		//Sound and Music
		SoundFactory.setAssetBasePath("mfx/");
		try {
			shooting = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), 
					this, "Hit1.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MusicFactory.setAssetBasePath("mfx/");
		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(),
					this, "MattOglseby - 1.ogg");
			backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//score
		this.mWinTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mWinTextureRegion = TextureRegionFactory.createFromAsset(this.mWinTexture, this, "win.png",0,0);
		
		this.mFailTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFailTextureRegion = TextureRegionFactory.createFromAsset(this.mFailTexture, this, "fail.png",0,0);
		
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);
		
		this.mEngine.getTextureManager().loadTextures(this.mFaceTexture,this.mTargetTexture, 
				this.mProjectTexture,this.mPauseTexture,this.mWinTexture,this.mFailTexture,
				this.mFontTexture,this.mbackgroundTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		//scene.setBackground(new ColorBackground(0.1f, 0.6f, 0.9f));
		this.backgroundMusic.play();
		//auto parallax
		final AutoParallaxBackground parallax = new AutoParallaxBackground(0, 0, 0, 5);
		parallax.attachParallaxEntity(new ParallaxEntity(-4.0f, new Sprite(0, CAMERA_HEIGHT - this.mbackgroundRegion.getHeight(), this.mbackgroundRegion)));
		scene.setBackground(parallax);
		
		float pX = 15;
		float pY = CAMERA_HEIGHT/2 - this.mFaceRegionTexture.getHeight();
		
		//layer
		layer = new AnimatedSprite(pX, pY, this.mFaceRegionTexture){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				this.setPosition(this.getX(), pSceneTouchEvent.getY() - this.getHeight()/2);
				return true;
			}
			
		};
		scene.attachChild(layer);
		scene.registerTouchArea(layer);
		scene.setTouchAreaBindingEnabled(true);
		
		//target
		targetLL = new LinkedList<AnimatedSprite>();
		TargetsTobeAdd = new LinkedList<AnimatedSprite>();
		createSprite();	
		
		//bullet
		projectLL = new LinkedList();
		projectToBeAdd = new LinkedList();
		scene.registerUpdateHandler(detect);
		scene.setOnSceneTouchListener(this);
		
		//CameraScene
		mPauseScene = new CameraScene(0,camera);
		final int x = (int) (camera.getWidth()/2 - mPauseRegion.getWidth());
		final int y = (int) (camera.getHeight()/2 - mPauseRegion.getHeight());
		final Sprite pauseSprite = new Sprite(x, y, this.mPauseRegion);
		mPauseScene.attachChild(pauseSprite);
		mPauseScene.setBackgroundEnabled(false);
		
		//score
		this.mResultScene = new CameraScene(0,this.camera);
		winSprite = new Sprite(x, y, this.mWinTextureRegion);
		failSprite = new Sprite(x, y, this.mFailTextureRegion);
		mResultScene.attachChild(winSprite);
		mResultScene.attachChild(failSprite);
		mResultScene.setBackgroundEnabled(false);
		
		winSprite.setVisible(false);
		failSprite.setVisible(false);
		score = new ChangeableText(0, 0, this.mFont, String.valueOf(maxScore));
		score.setPosition(this.camera.getWidth() - score.getWidth() - 5 , 5);
		restart();
		return scene;
	}

	private void createSprite() {
		// TODO Auto-generated method stub
		TimerHandler timehandler;
		
		timehandler = new TimerHandler(2f, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				addTarget();
			}
		});
		getEngine().registerUpdateHandler(timehandler);
	}
	public void addTarget() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int x = (int) camera.getWidth() + mTargetRegion.getWidth();
		int minY = mTargetRegion.getHeight();
		int maxY = (int) (camera.getHeight() - mTargetRegion.getHeight());
		
		int randY = maxY - minY;
		int y = rand.nextInt(randY) + minY;
		
		final AnimatedSprite target;
		target = tPool.obtainPoolItem();
		target.setPosition(x, y);
		target.setScale(2f);
		target.animate(200);
		scene.attachChild(target);
		
		int actualdur = rand.nextInt(2) + 10;
		MoveXModifier mod = new MoveXModifier(20, target.getX(), - target.getWidth());
		target.registerEntityModifier(mod.clone());
		TargetsTobeAdd.add(target);
		
	}
	
	//-----------------------------------------------------------
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
		
	// detect collision and cleaning up
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
				if(target2.getX() <= - target2.getWidth() ){
					tPool.recyclePoolItem(target2);
					targets.remove();					
					//fail();
					break;
				}
			Iterator<Sprite> project3 = projectLL.iterator();
			Sprite project4;
			while(project3.hasNext()){
				project4 = project3.next();
				
				if(project4.getX() >= camera.getWidth()
						|| project4.getY() >= camera.getHeight() + project4.getHeight()
						|| project4.getY() <= - project4.getHeight()){
					pPool.recyclePoolItem(project4);
					project3.remove();					
					continue;
				}
				
				if(target2.collidesWith(project4)){
					pPool.recyclePoolItem(project4);
					project3.remove();					
					hit = true;
					break;
				}
			}
			if(hit){	
				tPool.recyclePoolItem(target2);
				targets.remove();				
				hit =  false;
				hitCount++;
				score.setText(String.valueOf(hitCount));
			}	
				
			}
			if(hitCount >= maxScore){
				win();
			}
			projectLL.addAll(projectToBeAdd);
			projectToBeAdd.clear();
			
			targetLL.addAll(TargetsTobeAdd);
			TargetsTobeAdd.clear();
		}
	};
	
	private void shootProject(final float pX, final float pY){
		
		if (!CoolDown.sharedCoolDown().checkValidity()) {
		    return;
		}
		
		int offX = (int) (pX - layer.getX());
		int offY = (int) (pY - layer.getY());
		if(offX <= 0) return;
		
		final Sprite project2;
		
		project2 = pPool.obtainPoolItem();
		project2.setPosition(layer.getX() + layer.getWidth(), layer.getY());
		//scene.attachChild(project2);
		
		int realX = (int) (camera.getWidth() + project2.getWidth()/2.0f);
		float ratio = (float) offY / (float) offX;
		int realY = (int)((realX * ratio) + project2.getY());
		
		int offRealX = (int) (realX - project2.getX());
	    int offRealY = (int) (realY - project2.getY());
	    float length = (float) Math.sqrt((offRealX * offRealX) + (offRealY * offRealY));
	    float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
	    float realMoveDuration = length / velocity;
	    
	    MoveModifier mod = new MoveModifier(realMoveDuration, project2.getX(),
	    		realX, project2.getY(), realY);
	    //rotation bullet
	    LoopEntityModifier loopentity = new LoopEntityModifier(new RotationModifier(0.5f, 0, -360));
	    final ParallelEntityModifier par = new ParallelEntityModifier(mod, loopentity);	    
	    DelayModifier delaymod = new DelayModifier(0.55f);
	    
	    delaymod.setModifierListener(new IModifierListener<IEntity>() {
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
				shooting.play();
		        project2.setVisible(false);
		        project2.setPosition(layer.getX() + layer.getWidth(), layer.getY()+ layer.getHeight() / 3);
		        projectToBeAdd.add(project2);
			}
		});
	    SequenceEntityModifier seq = new SequenceEntityModifier(delaymod, par);
	    project2.registerEntityModifier(seq);
	    project2.setVisible(true);
	    scene.attachChild(project2);
	    
	    project2.registerEntityModifier(par.clone()); 
	    projectToBeAdd.add(project2);
	    layer.animate(50,false);
	    this.shooting.play();
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if(pSceneTouchEvent.isActionDown()){
			final float touchX = pSceneTouchEvent.getX();
			final float touchY = pSceneTouchEvent.getY();
			shootProject(touchX, touchY);
		}
		return false;
	}
	
	//--------------------
	public void pauseGame(){
		scene.setChildScene(mPauseScene,false,true,true);
		this.mEngine.stop();
	}
	public void unPauseGame(){
		scene.clearChildScene();
		this.mEngine.start();
	}
	public void pauseMusic(){
		if(runningFlag)
		if(this.backgroundMusic.isPlaying())
			this.backgroundMusic.pause();
	}
	public void resumeMusic(){
		if(runningFlag)
		if(!this.backgroundMusic.isPlaying())
			this.backgroundMusic.resume();
	}

	public void restart(){
		runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scene.detachChildren();
				scene.attachChild(layer);
				scene.attachChild(score);
			}
		});
		hitCount = 0;
		score.setText(String.valueOf(hitCount));
		projectLL.clear();
		projectToBeAdd.clear();
		TargetsTobeAdd.clear();
		targetLL.clear();
	}
	
	public void fail(){
		if(this.mEngine.isRunning()){
			winSprite.setVisible(false);
			failSprite.setVisible(true);
			scene.setChildScene(this.mResultScene,false,true,true);
			this.mEngine.stop();
		}
	}
	
	public void win(){
		if(this.mEngine.isRunning()){
			winSprite.setVisible(true);
			failSprite.setVisible(false);
			scene.setChildScene(this.mResultScene,false,true,true);
			this.mEngine.stop();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN){
			if(this.mEngine.isRunning() && backgroundMusic.isPlaying()){
				pauseMusic();
				pauseFlag = true;
				pauseGame();
				Toast.makeText(this, "Nhấn Menu để resume!", Toast.LENGTH_SHORT).show();
			}else{
				if(!backgroundMusic.isPlaying()){
					unPauseGame();
					pauseFlag = false;
					resumeMusic();
					this.mEngine.start();
				}
				return true;
			}
		}else if(keyCode == event.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN){
			if(!this.mEngine.isRunning() && backgroundMusic.isPlaying()){
				scene.clearChildScene();
				this.mEngine.start();
				restart();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (runningFlag) {
	        pauseMusic();
	        if (mEngine.isRunning()) {
	            pauseGame();
	            pauseFlag = true;
	        }
	    }
		super.onPause();
	}

	@Override
	public void onResumeGame() {
		// TODO Auto-generated method stub
		super.onResumeGame();
		if (runningFlag) {
	        if (pauseFlag) {
	            pauseFlag = false;
	            Toast.makeText(this, "Menu button to resume",
	            Toast.LENGTH_SHORT).show();
	        } else {
	            resumeMusic();
	            mEngine.stop();
	        }
	    } else {
	        runningFlag = true;
	    }
	}
	

	
}
