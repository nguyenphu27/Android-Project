package com.phuit.zingonline;

public class Utilities {

	public String milliSecondsToTimer(long milliseconds){
		String fTS = "";
		String secondsS = "";
		
		//gio:phut:giay
		int gio = (int)(milliseconds/ (1000*60*60));
		int phut = (int)(milliseconds % (1000*60*60)/(1000*60));
		int giay = (int)(milliseconds % (1000*60*60) % (1000*60) / 1000);
		
		if(gio > 0){
			fTS = gio + ":";
			
		}
		if(giay < 10){
			secondsS = "0" + giay;
		}else{
			secondsS = "" + giay;
		}
		fTS = fTS + phut + ":" + secondsS;
		return fTS;
	} 
	public int getProgressPercentage(long current, long total){
		Double percentage = (double) 0;
		long currents = (int) (current /1000);
		long totals = (int)(total /1000);
		
		percentage =(((double) currents)/totals)*100;
		return percentage.intValue();
	}
	public int progressToTimer(int progress, int total){
		int current = 0;
		total = (int)(total /1000);
		current = (int)((((double)progress)/100)*total);
		return current*1000;
	}
}
