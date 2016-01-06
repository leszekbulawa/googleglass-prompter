package serverapp.frame;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.lang.*;

import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class Extractor{

	private FileInputStream inputPresentationFile;
	private StringBuilder tempstr;
	
	private List<XSLFSlide> slideList;
	private XMLSlideShow slideshow;
	private XSLFSlide [] slidesArray;
	private XSLFNotes notes;
	private XSLFNotes notestest;
	public XSLFPowerPointExtractor extractor;
	public String [] notesArray;
	public String stringBuff;
	public String noteTemp;
	
	int slideNumber;
	
	public void openPresentation(String path){
		try{
			inputPresentationFile = new FileInputStream(path);
			slideshow = new XMLSlideShow(inputPresentationFile);
			System.out.println("Success!");
		}catch (Exception e){
			System.out.println("Could not open presentation.");
			System.out.println(e);
		}	
	}
	
	public int getSlides(){
		if (slideshow != null){ 
			slideList = slideshow.getSlides();
			slideNumber = slideList.size();
			slidesArray = slideList.toArray(new XSLFSlide[slideList.size()]);
			notesArray = new String[slidesArray.length];
		}else{
			System.out.println("Select and open presentation.");
		}
		return slideNumber;
	}
	
	public void getNotes(){
		if (slideNumber != 0){
			for (int i=0; i<slidesArray.length; i++){	
				try{
					notes = slidesArray[i].getNotes();	
					tempstr = new StringBuilder();
					for (XSLFShape shape : notes){
						if (shape instanceof XSLFTextShape){
							XSLFTextShape txShape = (XSLFTextShape) shape;
							
							for (XSLFTextParagraph xslfParagraph : txShape.getTextParagraphs()){
								tempstr.append(xslfParagraph.getText());
								stringBuff = xslfParagraph.getText();								
							}
						}
					}										
					tempstr.setLength(tempstr.length()-stringBuff.length());				
					if(notesArray[i]==null){
						notesArray[i] = "Note empty";
					}				
					notesArray[i] = tempstr.toString();
					
				}catch (Exception e){
					if(e.toString()=="java.lang.NullPointerException"){
						notesArray[i] = "Note empty";
					}
					else{
						System.out.println(e);
					}
				}
			}
			for (int i=0; i<notesArray.length; i++){
				System.out.println(notesArray[i]);
			}
		}else{
			System.out.println("There are no slides.");
		}
		
	}
	public void getNotes2(){
		extractor = new XSLFPowerPointExtractor(slideshow);
		String notes2 = extractor.getText(true, true);
		System.out.println(notes2);
	}
}
