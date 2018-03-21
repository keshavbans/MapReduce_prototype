package operations;

import java.io.*;

public class ReadFile {
	//-------------------------- This section will read the file ----------------------------------------

    public String[] read(String filePath) throws IOException {

        int lineCounter = linesCounter(filePath);
        return LineReader(filePath, lineCounter);
    }
//-------------------- This sectionw will count the lines in the file -----------------------------

    private int linesCounter(String filePath) throws IOException {

        int lines = 0;

        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        while (buffer.readLine() != null) lines++;
        buffer.close();

        return lines;
    }
  //----------------- This section will read line by line -----------------------------------  
    private String[] LineReader(String filePath, int lineCounter) throws IOException {

        String[] data = new String[lineCounter];

        int currentLine = 0;
        String line;

        BufferedReader buffer = new BufferedReader(new FileReader(filePath));
        while ((line = buffer.readLine()) != null) {
            data[currentLine] = line;
            currentLine++;
        }
        buffer.close();
        return data;
    }
// -------------------------------------------------------------------------------------------------    
}
