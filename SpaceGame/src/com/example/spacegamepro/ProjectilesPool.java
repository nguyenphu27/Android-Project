package com.example.spacegamepro;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.pool.GenericPool;

public class ProjectilesPool extends GenericPool<AnimatedSprite>{

	private TiledTextureRegion mTextureRegion;
	
	public ProjectilesPool(TiledTextureRegion pTextureRegion) {
		// TODO Auto-generated constructor stub
		if(pTextureRegion == null){
			throw new IllegalArgumentException("Texture not null");
		}
		mTextureRegion = pTextureRegion;
	}
	@Override
	protected AnimatedSprite onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return new AnimatedSprite(0, 0, mTextureRegion.clone());
	}
	protected void onHandleRecycleItem(final AnimatedSprite projectile){
		projectile.clearEntityModifiers();
		projectile.clearUpdateHandlers();
		projectile.setVisible(false);
		projectile.detachSelf();
		projectile.reset();
	}
}
