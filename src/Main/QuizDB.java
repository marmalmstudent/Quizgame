package Main;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.awt.image.*;

import javax.imageio.*;
import javax.swing.*;

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
		scrollArea = new JScrollPane(databaseDisplay,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollArea, BorderLayout.CENTER);
	}
	
	public static String getClassPath()
	{
		URL url = QuizDB.class.getProtectionDomain().getCodeSource().getLocation();
		String jarPath = null;
		try {
			jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new File(jarPath).getParentFile().getPath() + File.separator;
	}
	
	public void displayDatabase()
	{
		readDatabase();
		StringBuilder s = new StringBuilder();
		if (nDatabaseEntry > 0)
		{
			for (int i=0; i<nDatabaseEntry-1; i++)
			{
				s.append(database[i] + "\n");
			}
			s.append(database[nDatabaseEntry-1]);
		}
		databaseDisplay.setText(s.toString());
	}
	
	public void readDatabase()
	{
		nDatabaseEntry = 0;
		rawDatabase = new String[10000];
		try {
			InputStream inFile = new FileInputStream(DB_PATH+"quiz.txt");
		    BufferedReader in = new BufferedReader(new InputStreamReader(inFile, StandardCharsets.UTF_8));
		    String str;
		    while ((str = in.readLine()) != null)
		    {
				rawDatabase[nDatabaseEntry] = str;
				nDatabaseEntry++;
		    }
			this.database = new String[nDatabaseEntry];
			quizentry = new String[nDatabaseEntry][3];
			String[] thisLineEntry;
			for (int i=0; i<nDatabaseEntry; i++)
			{
				thisLineEntry = rawDatabase[i].split(" __ ");
				if (thisLineEntry.length == 3)
				{
					this.database[i] = rawDatabase[i];
					quizentry[i] = thisLineEntry;
					quizentry[i][0] = questionFixMultilineRead(quizentry[i][0]);
				}
			}
		    in.close();
		    inFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDatabase()
	{
		database = databaseDisplay.getText().split("\n");
        try {
        	OutputStream out = new FileOutputStream(DB_PATH+"quiz.txt");
        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    		for (int i=0; i<database.length-1; i++)
    		{
    			if (database[i].split(" __ ").length == 3)
    			{
    				writer.write(database[i]+"\n");
    			}
    			else
    			{
    				System.err.println("Corrupt database entry at line " + i + ". Skipping...");
    			}
    		}
			if (database[database.length-1].split(" __ ").length == 3)
			{
	    		writer.write(database[database.length-1]);
			}
			else
			{
				System.err.println("Corrupt database entry at line " + (database.length-1) + ". Skipping...");
			}
    		writer.close();
    		out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public boolean appendDatabase(String question, String answer, String imagePath)
	{
		question = questionFixMultilineWrite(question);
		//String[] imageName = imagePath.split(File.separator); // does not seem to work on windows
		String[] imageName = imagePath.split("/");
		String image = imageName[imageName.length-1];
		if (image.equalsIgnoreCase("default.png"))
		for (int i=0; i<quizentry.length; i++)
		{
			if (question.equalsIgnoreCase(quizentry[i][0])
					&& (image.equalsIgnoreCase(quizentry[i][2])
							|| (image.equals("") && quizentry[i][2].equalsIgnoreCase("default.png"))))
			{ // question and image are the same
				if (!answer.equalsIgnoreCase(quizentry[i][1]))
				{
					System.out.println("This question with this image exists in the database "
							+ "but the database holds a different answer, skipping to avoid ambiguity.\n"
							+ "Check database at line "+(i+1));
				}
				else
				{
					System.out.println("This question exists in the database, skipping");
				}
				return true;
			}
		}
		boolean success = true;
		try
		{
			if (!imagePath.equals("") && !image.equalsIgnoreCase("default.png"))
			{ // write image
				File imageSource = new File(imagePath);
				BufferedImage bi = ImageIO.read(imageSource);
				File imageDest = new File(DB_PATH+image);
				if (!imageDest.isDirectory()
						&& (!imageDest.exists()
								|| (imageDest.exists() && (DB_PATH+image).equalsIgnoreCase(DB_PATH+"default.png"))))
				{
					ImageIO.write(bi, "png", imageDest);
				}
				else
				{
					System.err.println("An image with that name already exists.");
					success = false;
				}
			}
			else
			{
				System.out.println("I won't copy this image!");
			}
			// write database
			if (success)
			{
				if (image.equals(""))
				{
					image = "default.png";
				}
	        	OutputStream out = new FileOutputStream(DB_PATH+"quiz.txt");
	        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
	    		for (int i=0; i<database.length; i++)
	    		{
	    			writer.write(database[i]+"\n");
	    		}
	    		writer.write(question+" __ "+answer+" __ "+image);
	    		writer.close();
	    		out.close();
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
		for (int i=0; i < q.length-1; i++) {
			builder.append(q[i]+"<br>");
		}
		builder.append(q[q.length-1]);
		return builder.toString();
	}
	
	public String questionFixMultilineRead(String multiline)
	{
		String[] q = multiline.split("<br>");
		StringBuilder builder = new StringBuilder();
		for (int i=0; i < q.length-1; i++) {
			builder.append(q[i]+"\n");
		}
		builder.append(q[q.length-1]);
		return builder.toString();
	}
	
	public String[][] getQuizEntry()
	{
		return quizentry;
	}
}
