package serverapp.frame;

//import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;
import com.sun.prism.Presentable;
import com.jacob.com.Dispatch;
import com.jacob.activeX.*;

public class Automation {
	//ActiveXComponent present = new ActiveXComponent("PowerPoint.Application");
	//present.setProperty("Visible", new Variant(true));
	//Presentable.
	ActiveXComponent oPower;
	
	
	public Automation(){
		//ActiveXComponent oPower = new ActiveXComponent("PowerPoint.Application");
		//ActiveXComponent oPres = new ActiveXComponent("PowerPoint.Presentation");
		//ActiveXComponent oSlide = new ActiveXComponent("PowerPoint.Slide");
		//ActiveXComponent oSettings = new ActiveXComponent("PowerPoint.SlideShowSettings");
		//oPower.setProperty("ActivePresentation", new Variant(true));
		//int count = oPower.getProperty("ActivePresentation");
		
		/*try{
			oPower.getProperty("Documents");
		}catch(Exception e){
			System.out.println("Brak slide.");
		}*/	
	}
	
	public boolean checkActive(){
		oPower = new ActiveXComponent("PowerPoint.Application");
		try{
			oPower.getProperty("ActivePresentation");
			return true;
		}catch(Exception e){
			System.out.println("Presentation is not running.");
			return false;
		}
		
	}
		
	
}
