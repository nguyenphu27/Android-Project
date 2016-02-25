package com.example.gameninja;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.pool.GenericPool;

public class TargetsPool extends GenericPool<AnimatedSprite>{

	private TiledTextureRegion mTextureRegion;
	
	public TargetsPool(TiledTextureRegion pTextureRegion){
		if(pTextureRegion == null){
			throw new IllegalArgumentException(
					"Texture region not be null");
			}
		mTextureRegion = pTextureRegion;
	}
		
	@Override
	protected AnimatedSprite onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return new AnimatedSprite(0, 0, mTextureRegion.clone());
	}

	protected void onHandleRecycleItem(final AnimatedSprite target){
		target.clearEntityModifiers();
		target.clearUpdateHandlers();
		target.setVisible(true);
		target.detachSelf();
		target.reset();
	}
}

