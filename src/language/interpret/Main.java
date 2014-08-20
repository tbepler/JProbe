package language.interpret;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	
	public static void main(String[] args){
		Interpreter interp = new Interpreter();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			while((line = in.readLine()) != null){
				if(line.equals("q")){
					break;
				}
				try{
					interp.interpret(new ByteArrayInputStream(line.getBytes()));
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
}
