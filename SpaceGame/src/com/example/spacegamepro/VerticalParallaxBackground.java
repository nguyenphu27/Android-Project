package com.example.spacegamepro;


import android.annotation.SuppressLint;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 15:36:26 - 19.07.2010
 */
public class VerticalParallaxBackground extends ParallaxBackground{
	private final ArrayList<VerticalParallaxEntity> mParallaxEntities = new ArrayList<VerticalParallaxEntity>();
 	
	private int mParallaxEntityCount;
	protected float mParallaxValue;
	private float mParallaxChangePerSecond;
	// ===========================================================
	// Constructors
	// ===========================================================

	public VerticalParallaxBackground(final float pRed, final float pGreen, final float pBlue,
			final float pParallaxChangePerSecond) {
		super(pRed, pGreen, pBlue);
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float pParallaxValue) {
		this.mParallaxValue = pParallaxValue;
	}
	
	public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@SuppressLint("WrongCall")
	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		super.onDraw(pGL, pCamera);

		final float parallaxValue = this.mParallaxValue;
		final ArrayList<VerticalParallaxEntity> parallaxEntities = this.mParallaxEntities;

		for(int i = 0; i < this.mParallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(pGL, parallaxValue, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
    public void onUpdate(float pSecondsElapsed) {
    	// TODO Auto-generated method stub
    	super.onUpdate(pSecondsElapsed);
    	this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
    }
	
	public void attachVerticalParallaxEntity(final VerticalParallaxEntity pParallaxEntity) {
		this.mParallaxEntities.add(pParallaxEntity);
		this.mParallaxEntityCount++;
	}

	public boolean detachVerticalParallaxEntity(final VerticalParallaxEntity pParallaxEntity) {
		this.mParallaxEntityCount--;
		final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
		if(success == false) {
			this.mParallaxEntityCount++;
		}
		return success;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class VerticalParallaxEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		final float mParallaxFactor;
		final Shape mShape;

		// ===========================================================
		// Constructors
		// ===========================================================

		public VerticalParallaxEntity(final float pParallaxFactor, final Shape pShape) {
			this.mParallaxFactor = pParallaxFactor;
			this.mShape = pShape;			
		}
		
		@SuppressLint("WrongCall")
		public void onDraw(final GL10 pGL, final float pParallaxValue, final Camera pCamera) {
			pGL.glPushMatrix();
			{
				final float cameraHeight = pCamera.getHeight();
				final float shapeHeightScaled = this.mShape.getHeightScaled();
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeHeightScaled;

				while(baseOffset > 0) {
					baseOffset -= shapeHeightScaled;
				}
				pGL.glTranslatef(0,baseOffset-1, 0);

				float currentMaxY = baseOffset;

				do {
					this.mShape.onDraw(pGL, pCamera);
					pGL.glTranslatef(0,shapeHeightScaled-1, 0);
					currentMaxY += shapeHeightScaled;
				} while(currentMaxY < (cameraHeight+shapeHeightScaled));
			}
			pGL.glPopMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
