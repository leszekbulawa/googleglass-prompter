package kainos.clientapp;

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
	}
	
	public Typeface getArial(){
		return arialFont;
	}
	
	public Typeface getCalibri(){
		return calibriFont;
	}

}
