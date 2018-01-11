package Main;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizDB extends JPanel
{
	private static final long serialVersionUID = 1L;
	public static final String DB_PATH = getClassPath()+"Quiz"+File.separator;
	private JTextArea databaseDisplay;
	private JScrollPane scrollArea;
	private String[] database, rawDatabase;
	private String[][] quizentry;
	private int nDatabaseEntry;
	
	public QuizDB()
	{
		readDatabase();
		databaseDisplay = new JTextArea(40, 50);
		databaseDisplay.setEditable(true);
		scrollArea = new JScrollPane(databaseDisplay, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollArea, BorderLayout.CENTER);
	}
	
	public static String getClassPath()
	{
		try {
			String f =QuizDB.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			return new File(URLDecoder.decode(f, "UTF-8")).getParentFile().getPath() + File.separator;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void displayDatabase()
	{
		readDatabase();
		StringBuilder s = new StringBuilder();
		if (nDatabaseEntry > 0) {
			for (int i=0; i<nDatabaseEntry-1; i++)
				s.append(database[i] + "\n");
			s.append(database[nDatabaseEntry-1]);
		}
		databaseDisplay.setText(s.toString());
	}
	
	public void readDatabase()
	{
		nDatabaseEntry = 0;
		rawDatabase = new String[10000];
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(DB_PATH+"quiz.txt"), StandardCharsets.UTF_8))) {
		    for (String str; (str = in.readLine()) != null; ) 
				rawDatabase[nDatabaseEntry++] = str;
			database = new String[nDatabaseEntry];
			quizentry = new String[nDatabaseEntry][3];
			String[] thisLineEntry;
			for (int i=0; i<nDatabaseEntry; i++) {
				thisLineEntry = rawDatabase[i].split(" __ ");
				if (thisLineEntry.length == 3) {
					database[i] = rawDatabase[i];
					quizentry[i] = thisLineEntry;
					quizentry[i][0] = questionFixMultilineRead(quizentry[i][0]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDatabase()
	{
		database = databaseDisplay.getText().split("\n");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DB_PATH+"quiz.txt"), StandardCharsets.UTF_8))) {
    		for (int i=0; i<database.length-1; i++) {
    			if (database[i].split(" __ ").length == 3)
    				writer.write(database[i]+"\n");
    			else
    				System.err.println("Corrupt database entry at line " + i + ". Skipping...");
    		}
			if (database[database.length-1].split(" __ ").length == 3)
	    		writer.write(database[database.length-1]);
			else
				System.err.println("Corrupt database entry at line " + (database.length-1) + ". Skipping...");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public boolean appendDatabase(String question, String answer, String imagePath)
	{
		question = questionFixMultilineWrite(question);
		String[] imageName = imagePath.split("/");
		String image = imageName[imageName.length-1];
		if (image.equalsIgnoreCase("default.png"))
		for (int i=0; i<quizentry.length; i++) {
			if (question.equalsIgnoreCase(quizentry[i][0]) && (image.equalsIgnoreCase(quizentry[i][2]) || (image.equals("") && quizentry[i][2].equalsIgnoreCase("default.png")))) { // question and image are the same
				if (!answer.equalsIgnoreCase(quizentry[i][1]))
					System.out.println("This question with this image exists in the database but the database holds a different answer, skipping to avoid ambiguity.\nCheck database at line "+(i+1));
				else
					System.out.println("This question exists in the database, skipping");
				return true;
			}
		}
		boolean success = true;
		try {
			if (!imagePath.equals("") && !image.equalsIgnoreCase("default.png")) { // write image
				File imageDest = new File(DB_PATH+image);
				if (!imageDest.isDirectory() && (!imageDest.exists() || (imageDest.exists() && (DB_PATH+image).equalsIgnoreCase(DB_PATH+"default.png")))) {
					ImageIO.write(ImageIO.read(new File(imagePath)), "png", imageDest);
				} else {
					System.err.println("An image with that name already exists.");
					success = false;
				}
			} else
				System.out.println("I won't copy this image!");
			// write database
			if (success) {
				if (image.equals(""))
					image = "default.png";
				try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(DB_PATH+"quiz.txt"), StandardCharsets.UTF_8))) {
					for (int i=0; i<database.length; i++)
						writer.write(database[i]+"\n");
					writer.write(question+" __ "+answer+" __ "+image);
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
		return success;
	}
	
	public String questionFixMultilineWrite(String multiline)
	{
		String[] q = multiline.split("\n");
		StringBuilder builder = new StringBuilder();
		for (int i=0; i < q.length-1; i++)
			builder.append(q[i]+"<br>");
		builder.append(q[q.length-1]);
		return builder.toString();
	}
	
	public String questionFixMultilineRead(String multiline)
	{
		String[] q = multiline.split("<br>");
		StringBuilder builder = new StringBuilder();
		for (int i=0; i < q.length-1; i++)
			builder.append(q[i]+"\n");
		builder.append(q[q.length-1]);
		return builder.toString();
	}
	
	public String[][] getQuizEntry() { return quizentry; }
}
