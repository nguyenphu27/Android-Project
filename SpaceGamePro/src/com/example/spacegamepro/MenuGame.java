package com.example.spacegamepro;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class MenuGame extends BaseGameActivity{
	private static int CAMERA_WIDTH;
	private static int CAMERA_HEIGHT;
	private Scene scene;	
	
	private Texture mbackgroundTexture;
	private TextureRegion mbackgroundRegion;
	
	private Texture mPlayButtonTexture;
	private TextureRegion mPlayButtonRegion;
	
	private Texture mQuitButtonTexture;
	private TextureRegion mQuitButtonRegion;
	
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
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mbackgroundTexture = new Texture(512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mbackgroundRegion = TextureRegionFactory.createFromAsset(this.mbackgroundTexture, this, "homeBG.jpg",0,0);
		
		this.mPlayButtonTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayButtonRegion = TextureRegionFactory.createFromAsset(this.mPlayButtonTexture, this, "menu_ok.png",0,0);
		
		this.mQuitButtonTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mQuitButtonRegion = TextureRegionFactory.createFromAsset(this.mQuitButtonTexture, this, "menu_quit.png",0,0);
		
		this.mEngine.getTextureManager().loadTextures(this.mbackgroundTexture,this.mPlayButtonTexture,this.mQuitButtonTexture);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		
		final AutoParallaxBackground back = new AutoParallaxBackground(0, 0, 0, 5);
		back.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, 0, this.mbackgroundRegion)));		
		scene.setBackground(back);
		
		ButtonPlayGame(this.mPlayButtonRegion, CAMERA_WIDTH/3, CAMERA_HEIGHT*2/3 - this.mPlayButtonRegion.getHeight());
		ButtonQuitGame(this.mQuitButtonRegion, CAMERA_WIDTH/3, CAMERA_HEIGHT*2/3 + 100 - this.mQuitButtonRegion.getHeight());
		
		return scene;
	}

	

	public void ButtonPlayGame(TextureRegion mtextureregion, int x, int y) {
		// TODO Auto-generated method stub
		final Sprite playbutton = new Sprite(x, y, mtextureregion){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub					
					runOnUpdateThread(new Runnable() {				
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent maingame = new Intent("com.example.spacegamepro.MAINGAME");
							startActivity(maingame);
						}
					});							
				return true;
			}
			
		};
		scene.attachChild(playbutton);
		scene.registerTouchArea(playbutton);
	}
	
	public void ButtonQuitGame(TextureRegion mQuitButtonRegion2, int i, int j) {
		// TODO Auto-generated method stub
		final  Sprite quitbutton = new Sprite(i, j, mQuitButtonRegion2){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				if(pSceneTouchEvent.isActionDown()){
					
					new AlertDialog.Builder(context)
						.setIcon(R.drawable.error)
						.setTitle("Quit Game")
						.setMessage("Are you sure quit game?")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								moveTaskToBack(true);
								System.exit(1);								
							}
						}).setNegativeButton("No", null)
						  .show();
				}
				return true;
			}
			
		};
		scene.attachChild(quitbutton);
		scene.registerTouchArea(quitbutton);		
	}
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

}
