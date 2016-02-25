package com.example.gameninja;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.pool.GenericPool;

public class ProjectilesPool extends GenericPool<Sprite>{

	private TextureRegion mTextureRegion;
	
	public ProjectilesPool(TextureRegion pTextureRegion){
		if(pTextureRegion == null){
			throw new IllegalArgumentException("not be null");
		}
		mTextureRegion = pTextureRegion;
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return new Sprite(0, 0, mTextureRegion.clone());
	}
	
	protected void onHandleRecycleItem(final Sprite projectile){
		projectile.clearEntityModifiers();
		projectile.clearUpdateHandlers();
		projectile.setVisible(false);
		projectile.detachSelf();
		projectile.reset();
	}
}