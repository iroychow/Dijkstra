import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*author Ishita*/

public class Utility {
	
	public static void writeToFile(String fileName,String content){
		File file= new File (fileName);
		PrintWriter writer=null;
		boolean appendFlag = false;
		try{
			
			if(file.exists() && !file.isDirectory()) { 
				appendFlag = true;
			}
			
        	FileWriter fw = new FileWriter(file, appendFlag);
			BufferedWriter bw = new BufferedWriter(fw);
        	writer = new PrintWriter(bw);
        	writer.println(content);
        	writer.close();
            bw.close();
            fw.close();
            	
            
        } catch (IOException e) {
           System.out.println("Failed to write to the file");
        }
	}
	
			

}