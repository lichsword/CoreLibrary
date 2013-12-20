import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Launcher {
    public static void main(String args[]){
		Lib lib = new Lib();
		System.out.println("show me the money"+lib.getValue());
		try{
		    Process process = Runtime.getRuntime().exec ("ls"); 
		    InputStreamReader ir=new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader (ir);

			String line;
			while ((line = input.readLine ()) != null){
				System.out.println(line);
			}// end while
		}catch (java.io.IOException e){
			System.err.println ("IOException " + e.getMessage());
		}
	}
}
