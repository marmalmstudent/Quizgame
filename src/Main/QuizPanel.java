package Main;
import java.awt.*;
import javax.swing.*;

public class QuizPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private ImageIcon picture, defaultPicture;
	private JLabel pictureLabel, progressLabel;
	private JTextArea questionLabel;
	private JScrollPane scrollArea;
	private int defaultImageWidth, defaultImageHeight;
	private Quiz quiz;
	
	public QuizPanel(Quiz q)
	{
		quiz = q;
		defaultPicture = new ImageIcon(QuizDB.DB_PATH+"default.png");
		defaultImageWidth = defaultPicture.getIconWidth();
		defaultImageHeight = defaultPicture.getIconHeight();
		setLayout(new BorderLayout());
		progressLabel = new JLabel(quiz.getQCount()+"/"+quiz.getNbrRounds()+" questions answered.");
		pictureLabel = new JLabel();
		pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		pictureLabel.setPreferredSize(new Dimension(defaultImageWidth, defaultImageHeight));
		pictureLabel.setIcon(defaultPicture);
		questionLabel = new JTextArea(5, 20/*(int)(defaultImageWidth/11.0)*/);
		questionLabel.setEditable(false);
		questionLabel.setLineWrap(true);
		questionLabel.setWrapStyleWord(true);
		questionLabel.setText("Hint: Hit return in the answer field to start a new "
				+ "round with current (default) settings.");
		scrollArea = new JScrollPane(questionLabel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(progressLabel, BorderLayout.NORTH);
		add(pictureLabel, BorderLayout.CENTER);
		add(scrollArea, BorderLayout.SOUTH);
	}
	
	public void displayNewQuestion()
	{
		progressLabel.setText(quiz.getQCount()+"/"+quiz.getNbrRounds()+" questions answered.");
		ImageIcon rawPicture = new ImageIcon(quiz.getCurrImage());
		if (rawPicture.getIconHeight() > defaultImageHeight || rawPicture.getIconWidth() > defaultImageWidth)
		{ // image is bigger than Default
			if ((double)(rawPicture.getIconHeight())/rawPicture.getIconWidth() > (double)(defaultImageHeight)/defaultImageWidth)
			{ // image height is limiting factor
				picture = new ImageIcon(rawPicture.getImage().getScaledInstance(-1, defaultImageHeight, Image.SCALE_SMOOTH));
			}
			else 
			{ // image width is limiting factor
				picture = new ImageIcon(rawPicture.getImage().getScaledInstance(defaultImageWidth, -1, Image.SCALE_SMOOTH));
			}
		}
		else
		{ // use original image dimension
			picture = rawPicture;
		}
		pictureLabel.setIcon(picture);
		questionLabel.setText(quiz.getCurrQuestion());
	}
	
	public void clearDisplayQuestion()
	{
		progressLabel.setText(quiz.getQCount()+"/"+quiz.getNbrRounds()+" questions answered.");
		pictureLabel.setIcon(new ImageIcon());
		questionLabel.setText("");
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

}
