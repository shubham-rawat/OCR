import java.sql.*;
import java.time.*;
import java.io.*;
import net.sourceforge.tess4j.TesseractException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.format.DateTimeFormatter;
public class Reader{
    
    public static String original;
    public static String name = "Someone";
    public static String image = "current.jpg";
    public static String path = "C:\\Users\\dell\\Desktop\\test images\\";

    
    public static void main(String[] args) throws TesseractException, ClassNotFoundException, IOException, InterruptedException, SQLException{
    
        Path faxFolder = Paths.get("C:\\Users\\dell\\Desktop\\test images");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					original = event.context().toString();
					if(original.endsWith(".jpg")||original.endsWith(".png"))
					{
					System.out.println("New File Found:" + original);
					processImage();
					}
				}
			}
			valid = watchKey.reset();
		} while (valid);
    }
    
    public static void processImage() throws IOException, TesseractException, ClassNotFoundException, SQLException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        ConvertToGray ctg = new ConvertToGray(new File(path+original));
        BlackPoints.drawBinaryImage();
        String input = OCR.scan();
        
        System.out.println("Registration No --> "+input);
        db(input);
        System.out.println("Registered To --> "+name);
        
        File yourFile = new File("C:\\Users\\dell\\Desktop\\test images\\chalans\\"+name+".txt");
        yourFile.createNewFile(); 
        
        try(FileWriter fw = new FileWriter("C:\\Users\\dell\\Desktop\\test images\\chalans\\"+name+".txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            out.println("File Name   : "+original);
            out.println("Date Time   : "+ dtf.format(now));
            out.println("Reg. No.    : "+input);
            out.println("");
        } catch (IOException e) {
            System.out.println("Error in the file");
        }
    }
    
    public static void db(String reg) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/numberplate","root","");
        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from registration where regno='"+reg+"'");
        while(rs.next())
            name=rs.getString("name");
        con.close();
    }
}