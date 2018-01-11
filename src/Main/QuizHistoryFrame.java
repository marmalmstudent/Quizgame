package Main;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class QuizHistoryFrame extends JPanel
{
	private static final long serialVersionUID = 3942585985499609747L;
	private Quiz quiz;
	private JTextArea historyLabel;
	private JScrollPane scrollArea;
	private JLabel scoreLabel;
	
	public QuizHistoryFrame(Quiz q)
	{
		quiz = q;
		setLayout(new BorderLayout());
		scoreLabel = new JLabel();
		add(scoreLabel, BorderLayout.NORTH);
		historyLabel = new JTextArea(43, 52);
		historyLabel.setEditable(false);
		historyLabel.setLineWrap(true);
		historyLabel.setWrapStyleWord(true);
		scrollArea = new JScrollPane(historyLabel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollArea, BorderLayout.CENTER);
	}
	
	public void displayHistory()
	{
		StringBuilder s = new StringBuilder();
		String[][] history = quiz.getHistory();
		for (int i=0; i<quiz.getQCount(); i++)
			s.append(history[i][0] + "\n    " + "Input:\t" + history[i][1] + "\n    " + "Correct:\t" + history[i][2] + "\n    " + history[i][3] + "\n\n");
		historyLabel.setText(s.toString());
		scoreLabel.setText("Score: " + quiz.getScore() + " out of "+quiz.getQCount() + ((quiz.getQCount() != 0) ? " (" + 100.0*quiz.getScore()/quiz.getQCount() + " %)" : ""));
		
	}
}
