package serverapp.frame;

import java.io.File;
import java.io.FileInputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFComments;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class Extractor {
	
	private List<XSLFSlide> slideList;
	
	private FileInputStream inputPresentationFile;
	
	private XMLSlideShow slideshow;
	private XSLFSlide [] slidesArray;
	private XSLFNotes notes;
	private XSLFComments comments;
	public String [] notesArray;
	
	int slideNumber;
	
	public Extractor(){
		
	}
	
	public void OpenPresentation(String path){
		try{
			inputPresentationFile = new FileInputStream(path);
			slideshow = new XMLSlideShow(inputPresentationFile);
			System.out.println("Success!");
		}catch (Exception e){
			System.out.println("Could not open presentation.");
			System.out.println(e);
		}	
	}
	
	public int GetSlides(){
		if (slideshow != null){ 
			slideList = slideshow.getSlides();
			slideNumber = slideList.size();
			//System.out.println(SlideNumber);
			slidesArray = slideList.toArray(new XSLFSlide[slideList.size()]);
			notesArray = new String[slidesArray.length];
		}else{
			System.out.println("Select and open presentation.");
		}
		return slideNumber;
	}
	
	public void GetNotes(){
		/*for (XSLFSlide slide : slidesArray){
			notes = slide.getNotes();			
		}*/
		if (slideNumber != 0){
			for (int i=0; i<slidesArray.length; i++){	
				try{
					notes = slidesArray[i].getNotes();	
					for (XSLFShape shape : notes){
						if (shape instanceof XSLFTextShape){
							XSLFTextShape txShape = (XSLFTextShape) shape;
						
							for (XSLFTextParagraph xslfParagraph : txShape.getTextParagraphs()){
								System.out.println(xslfParagraph.getText());
								//notesArray[i] = xslfParagraph.getText();
							}
						}
					}
				}catch (Exception e){
					System.out.println(e);
				}
			}
		}else{
			System.out.println("There are no slides.");
		}
		//for (int i=0; i<notesArray.length; i++){
		//	System.out.println(notesArray[i]);
		//}
	}
}
