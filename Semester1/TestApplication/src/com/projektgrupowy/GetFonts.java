package com.projektgrupowy;

import android.content.Context;
import android.graphics.Typeface;
import android.renderscript.Type;

public class GetFonts {

	public static String ARIAL 		= "Arial";
	public static String CALIBRI 	= "Calibri";
	public static String IMPACT 	= "Impact";
	public static String TIMES 		= "Times New Roman";
	
	public Typeface arialFont;
	public Typeface calibriFont;
	public Typeface impactFont;
	public Typeface timesNewRomanFont;
	public Context context;

	public GetFonts(Context context) {
		this.context = context;
		arialFont = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
		calibriFont = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
		impactFont = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
		timesNewRomanFont = Typeface.createFromAsset(context.getAssets(), "fonts/timeroman.ttf");
	}

	public Typeface getFontByID(int id) {
		Typeface fontToReturn;	
		switch (id) {
		case 0:
			fontToReturn = arialFont;
			break;
		case 1:
			fontToReturn = calibriFont;
			break;
		case 2:
			fontToReturn = impactFont;
			break;
		case 3:
			fontToReturn = timesNewRomanFont;
			break;

		default:
			fontToReturn = arialFont;
			break;
		}		
		return fontToReturn;
	}
	
	public String getFontNameByID(int id) {
		String fontNameToReturn;
		switch (id) {
		case 0:
			fontNameToReturn = ARIAL;
			break;
		case 1:
			fontNameToReturn = CALIBRI;
			break;
		case 2:
			fontNameToReturn = IMPACT;
			break;
		case 3:
			fontNameToReturn = TIMES;
			break;

		default:
			fontNameToReturn = ARIAL;
			break;
		}
		return fontNameToReturn;
	}

}
