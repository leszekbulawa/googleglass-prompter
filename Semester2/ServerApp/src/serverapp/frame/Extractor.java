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

import org.apache.log4j.Logger;

public class Extractor{

	private FileInputStream inputPresentationFile;
	private StringBuilder tempstr;
	
	private List<XSLFSlide> slideList;
	private XMLSlideShow slideshow;
	private XSLFSlide [] slidesArray;
	private XSLFNotes notes;
	public XSLFPowerPointExtractor extractor;
	public String [] notesArray;
	public String [] notesArrayEmpty;
	public String stringBuff;
	public String noteTemp;
	
	int slideNumber;
	
	private static final Logger log = Logger.getLogger(Extractor.class.getName());
	
	public void openPresentation(String path){
		try{
			inputPresentationFile = new FileInputStream(path);
			slideshow = new XMLSlideShow(inputPresentationFile);
			log.info("Opening successful.");
		}catch (IOException e){
			log.info("Could not open presentation.");
			log.info(e.getMessage());
		}	
	}
	
	public int getSlides(){
		if (slideshow != null){ 
			slideList = slideshow.getSlides();
			slideNumber = slideList.size();
			slidesArray = slideList.toArray(new XSLFSlide[slideList.size()]);
			notesArray = new String[slidesArray.length];
		}else{
			log.info("Presentation not opened");
		}
		return slideNumber;
	}
	
	public String[] getNotes(){
		if (slideNumber != 0){
			for (int i=0; i<slidesArray.length; i++){	
				try{
					notes = slidesArray[i].getNotes();	
					tempstr = new StringBuilder();
					for (XSLFShape shape : notes){
						if (shape instanceof XSLFTextShape){
							XSLFTextShape txShape = (XSLFTextShape) shape;
							
							for (XSLFTextParagraph xslfParagraph : txShape.getTextParagraphs()){
								tempstr.append(xslfParagraph.getText() + "\n");
								stringBuff = xslfParagraph.getText();								
							}
						}
					}										
					tempstr.setLength(tempstr.length()-(stringBuff.length()+1));				
					if(notesArray[i]==null){
						notesArray[i] = "Note empty";
					}				
					notesArray[i] = tempstr.toString();
					
				}catch (Exception e){
					if(e.toString()=="java.lang.NullPointerException"){
						notesArray[i] = "Note empty";
					}
					else{
						log.info(e.getMessage());
						System.out.println(e);
					}
				}
			}
			return notesArray;
		}else{
			notesArrayEmpty[0] = "There are no slides";
			log.info("There are no slides");
			return notesArrayEmpty;
		}
		
	}
	public String getNotes2(){
		extractor = new XSLFPowerPointExtractor(slideshow);
		String notes2 = extractor.getText(true, true);
		return notes2;
	}
}
