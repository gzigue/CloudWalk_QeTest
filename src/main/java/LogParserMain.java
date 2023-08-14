import com.quakelogparser.LogParser;

public class LogParserMain {
	
	public static void main(String[] args) {
		
		final String filePath = "src\\main\\resources\\qgames.log";
		
		LogParser.parseLog(filePath, 3);
		
	}

}
