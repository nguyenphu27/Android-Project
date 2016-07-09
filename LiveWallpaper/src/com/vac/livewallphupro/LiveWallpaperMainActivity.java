package com.vac.livewallphupro;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

public class LiveWallpaperMainActivity extends BaseLiveWallpaperService{
	
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGTH = 640;
	
	private ScreenOrientation mScreenOrientation;
	private VelocityInitializer mVelocity;
	
	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundRegion;
	
	private Texture mTexture;
	private TextureRegion mTextureRegion;
	
	
	@Override
	public org.anddev.andengine.engine.Engine onLoadEngine() {
		// TODO Auto-generated method stub
		return new org.anddev.andengine.engine.Engine(
				new EngineOptions(true, this.mScreenOrientation, 
					new FillResolutionPolicy(), 
					new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGTH)));
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub	
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBackgroundTexture = new Texture(512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "livewallpaper.png",0,0);
		
		this.mTexture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "phupro.png",0,0);
		
		this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture,this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		final Scene scene = new Scene(2);
		final Sprite background = new Sprite(0, CAMERA_HEIGTH - this.mBackgroundRegion.getHeight(), this.mBackgroundRegion);
		scene.getFirstChild().attachChild(background);
		
		final ParticleSystem textParticle = new ParticleSystem(this.mBackgroundRegion.getWidth()/3, CAMERA_HEIGTH , 0, 0, 1, 1, 20, this.mTextureRegion);
		
		this.mVelocity = new VelocityInitializer(-40, 40,-50,-100);
		textParticle.addParticleInitializer(this.mVelocity);// register move
		textParticle.addParticleModifier(new ExpireModifier(15,15));//time life
		
		scene.getLastChild().attachChild(textParticle);
		
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPauseGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResumeGame() {
		// TODO Auto-generated method stub
		
	}

	
}
