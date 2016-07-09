package com.example.spacegamepro;


public class AutoVerticalParallaxBackground extends VerticalParallaxBackground{
	
	private final float mParallaxChangePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AutoVerticalParallaxBackground(final float pRed, final float pGreen, final float pBlue,final float pParallaxChangePerSecond) {
		
		super(pRed, pGreen, pBlue,pParallaxChangePerSecond);
		this.mParallaxChangePerSecond = pParallaxChangePerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		super.onUpdate(pSecondsElapsed);

		this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

