package Main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class QuizFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JButton restartButton, answerButton, settingsButton,
	resultsButton, databaseButton, settingsApplyButton, settingsCancelButton,
	databaseWriteButton, databaseDiscardButton, appendDBAppendButton,
	appendDBCloseButton, openDBButton, galleryButton;
	private JTextField answerField, nRoundsField;
	private JTextArea questionArea, answerArea, imageArea;
	private JScrollPane questionScrollArea, answerScrollArea, imageScrollArea;
	private Quiz quiz;
	private QuizPanel quizPanel;
	private QuizHistoryFrame quizHistory;
	private QuizDB quizDB;
	private JFrame history, settings, database, appendDB;
	private JPanel settingsPanel, settingsButtons, appendDBPanel, appendDBButtons;
	
	public QuizFrame(Quiz q, QuizDB qdb)
	{
		super("A simple quiz game, Written by Marcus Malmquist");
		setLayout(new BorderLayout());
		setResizable(false);
		quiz = q;
		quizDB = qdb;

		history = historyFrame(); // quiz history
		appendDB = appendDatabase(); // append database
		database = databaseFrame(); // quiz database
		settings = settingsFrame(); // config window
		
		// place for images and question
		quizPanel = new QuizPanel(q);
		add(quizPanel, BorderLayout.CENTER);
		
		// panel with buttons
		Dimension dim = new Dimension(130, 25);
		JPanel bPanel = new JPanel(new GridLayout(1, 4));
		restartButton = AddButton("(Re)start", "restart", "Click here to (re)start the round.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		resultsButton = AddButton("Show results", "results", "Click here to show the results."
				+ "Doing so will end the round.", true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsButton = AddButton("Settings", "settings", "Click here to modify the settings.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseButton = AddButton("Database", "database", "Click here to modify the database.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		bPanel.add(restartButton);
		bPanel.add(resultsButton);
		bPanel.add(settingsButton);
		bPanel.add(databaseButton);
		add(bPanel, BorderLayout.NORTH);
		
		// textfield to put answer in
		JPanel answerPanel = new JPanel(new BorderLayout());
		answerField = new JTextField(20);
		answerField.setName("answer");
		answerField.addActionListener(this);
		addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	answerField.requestFocus();
		    }
		});  // set focus to answer field
		answerPanel.add(answerField, BorderLayout.CENTER);
		Dimension d = new Dimension(75, 25);
		answerButton = AddButton("Send", "answer", "Click to Answer.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, d);
		answerPanel.add(answerButton, BorderLayout.EAST);
		add(answerPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JFrame historyFrame()
	{
		JFrame frame = new JFrame("Quiz history");
		quizHistory = new QuizHistoryFrame(quiz);
		frame.setLayout(new BorderLayout());
		frame.add(quizHistory, BorderLayout.CENTER);
		frame.setResizable(false);
		return frame;
	}
	
	public JFrame appendDatabase()
	{
		//TODO: 
		JFrame frame = new JFrame("Append to database");
		appendDBPanel = new JPanel(new GridLayout(4, 1));
		JPanel qPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel questionLabel = new JLabel();
		questionLabel.setText("Question:");
		questionArea = new JTextArea(3, 30);
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionScrollArea = new JScrollPane(questionArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		qPanel.add(questionLabel);
		qPanel.add(questionScrollArea);
		appendDBPanel.add(qPanel);
		JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel qAnswerLabel = new JLabel();
		qAnswerLabel.setText("Answer:");
		answerArea = new JTextArea(3, 30);
		answerArea.setLineWrap(true);
		answerArea.setWrapStyleWord(true);
		answerScrollArea = new JScrollPane(answerArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		aPanel.add(qAnswerLabel);
		aPanel.add(answerScrollArea);
		appendDBPanel.add(aPanel);
		JPanel iPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel imageLabel = new JLabel();
		imageLabel.setText("Image path:");
		imageArea = new JTextArea(3, 30);
		imageArea.setLineWrap(true);
		imageScrollArea = new JScrollPane(imageArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		iPanel.add(imageLabel);
		iPanel.add(imageScrollArea);
		appendDBPanel.add(iPanel);
		JPanel iPanelEx = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel imageLabelEx = new JLabel();
		imageLabelEx.setText("Example path:");
		JTextArea imageAreaEx = new JTextArea(1, 30);
		imageAreaEx.setEditable(false);
		imageAreaEx.setText(QuizDB.DB_PATH+"default.png");
		imageAreaEx.setBackground(Color.LIGHT_GRAY);
		imageAreaEx.setLineWrap(true);
		iPanelEx.add(imageLabelEx);
		iPanelEx.add(imageAreaEx);
		appendDBPanel.add(iPanelEx);
		appendDBButtons = new JPanel();
		appendDBAppendButton = AddButton("Append", "appenddbappend", "Click here to append to database.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(75, 25));
		appendDBCloseButton = AddButton("Close", "appenddbcancel", "Click here to discard any changes.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(75, 25));
		openDBButton = AddButton("Open database", "opendatabase", "Click here to open the database. Useful "
				+ "for debugging the database.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(130, 25));
		galleryButton = AddButton("Browse images", "opengallery", "Click here to open the gallery "
				+ "and browse the images in the database.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(130, 25));
		appendDBButtons.add(appendDBAppendButton);
		appendDBButtons.add(appendDBCloseButton);
		appendDBButtons.add(openDBButton);
		appendDBButtons.add(galleryButton);
		frame.setLayout(new BorderLayout());
		frame.add(appendDBPanel, BorderLayout.CENTER);
		frame.add(appendDBButtons, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JFrame databaseFrame()
	{
		Dimension dim = new Dimension(130, 25);
		JFrame frame = new JFrame("Database Editor");
		frame.setLayout(new BorderLayout());
		frame.add(quizDB, BorderLayout.CENTER);
		JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		databaseWriteButton = AddButton("Write", "databaseSave", "Click here to Write to database.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseDiscardButton = AddButton("Discard", "databaseDiscard", "Click here to discard changes.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		dbPanel.add(databaseWriteButton);
		dbPanel.add(databaseDiscardButton);
		frame.add(dbPanel, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JFrame settingsFrame()
	{
		Dimension dim = new Dimension(75, 25);
		JFrame frame = new JFrame("Quiz settings");
		settingsPanel = new JPanel();
		JLabel nRoundsLabel = new JLabel();
		nRoundsLabel.setText("Rounds:");
		nRoundsField = new JTextField(10);
		nRoundsField.setText(""+quiz.getNbrRounds());
		nRoundsField.addActionListener(this);
		settingsPanel.add(nRoundsLabel);
		settingsPanel.add(nRoundsField);
		settingsButtons = new JPanel();
		settingsApplyButton = AddButton("Apply", "settingsapply", "Click here to apply settings.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsCancelButton = AddButton("Cancel", "settingscancel", "Click here to discard any changes.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsButtons.add(settingsApplyButton);
		settingsButtons.add(settingsCancelButton);
		frame.setLayout(new BorderLayout());
		frame.add(settingsPanel, BorderLayout.CENTER);
		frame.add(settingsButtons, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JButton AddButton(String buttonText, String nameSet, String tooltip,
			boolean actionListen, boolean opaque, Color background,
			Color border, Dimension d)
	{
		JButton button = new JButton(buttonText);
		button.setName(nameSet);
		button.setToolTipText(tooltip);
		if(actionListen)
			button.addActionListener(this);
		button.setOpaque(opaque);
		button.setBackground(background);
		button.setBorder(new LineBorder(border));
		if (d != null)
			button.setPreferredSize(d);
		return button;
	}
	
	public void displayHistory()
	{
		quizHistory.displayHistory();
		history.setVisible(true);
		history.pack();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();
			if (b.getName() != null)
			{
				if (b.getName().equals(restartButton.getName()))
				{
					answerField.setText("");
					quiz.RestartRound();
					quiz.genNewQuestionId();
					quizPanel.displayNewQuestion();
				}
				else if (b.getName().equals(resultsButton.getName()) && quiz.isPlaying)
				{
					answerField.setText("");
					quiz.isPlaying = false;
					quizPanel.clearDisplayQuestion();
					displayHistory();
				}
				else if (b.getName().equals(resultsButton.getName()) && !quiz.isPlaying)
				{
					JOptionPane.showMessageDialog(null, "Why?", "Unsupported event", JOptionPane.INFORMATION_MESSAGE);
				}
				else if (b.getName().equals(databaseButton.getName()))
				{
					appendDB.setVisible(true);
					appendDB.pack();
				}
				else if (b.getName().equals(appendDBAppendButton.getName()))
				{
					if (!questionArea.getText().equals("") && !answerArea.getText().equals(""))
					{
						if (quizDB.appendDatabase(questionArea.getText(), answerArea.getText(), imageArea.getText()))
						{
							questionArea.setText("");
							answerArea.setText("");
							imageArea.setText("");
							quizDB.readDatabase();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Check if the image path is correct.\n"
									+ "Remember to use the absolute path.", "Error writing to database", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else if (b.getName().equals(appendDBCloseButton.getName()))
				{
					questionArea.setText("");
					answerArea.setText("");
					imageArea.setText("");
					appendDB.setVisible(false);
					appendDB.dispose();
				}
				else if (b.getName().equals(openDBButton.getName()))
				{
					quizDB.displayDatabase();
					database.setVisible(true);
					database.pack();
				}
				else if (b.getName().equals(databaseWriteButton.getName()))
				{
					quizDB.writeDatabase();
					quizDB.readDatabase();
					database.setVisible(false);
					database.dispose();
				}
				else if (b.getName().equals(databaseDiscardButton.getName()))
				{
					database.setVisible(false);
					database.dispose();
				}
				else if (b.getName().equals(answerField.getName()) && quiz.isPlaying)
				{
					quiz.checkAnswer(answerField.getText());
					answerField.setText("");
					if (quiz.isPlaying)
					{
						quiz.genNewQuestionId();
						quizPanel.displayNewQuestion();
					} else
					{
						quizPanel.clearDisplayQuestion();
						displayHistory();
					}
				}
				else if (b.getName().equals(settingsButton.getName()))
				{
					settings.setVisible(true);
					settings.pack();
				}
				else if (b.getName().equals(settingsApplyButton.getName()))
				{
					try
					{
						int newRounds  = Integer.parseInt(nRoundsField.getText());
						quiz.setNbrRounds(newRounds);
						settings.setVisible(false);
						settings.dispose();
						answerField.setText("");
						quiz.RestartRound();
						quizPanel.displayNewQuestion();
					} catch (NumberFormatException nfe)
					{
						JOptionPane.showMessageDialog(null, "Invalid input",
								"Not an integer", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if (b.getName().equals(settingsCancelButton.getName()))
				{
					settings.setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Nope.", "Missing feature", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else if (e.getSource() instanceof JTextField)
		{
			JTextField t = (JTextField) e.getSource();
			if (t.getName() != null)
			{
				if (t.getName().equals(answerButton.getName()) && quiz.isPlaying)
				{
					quiz.checkAnswer(answerField.getText());
					answerField.setText("");
					if (quiz.isPlaying)
					{
						quiz.genNewQuestionId();
						quizPanel.displayNewQuestion();
					} else
					{
						quizPanel.clearDisplayQuestion();
						displayHistory();
					}
				}
				else if (!quiz.isPlaying && answerField.getText().equals(""))
				{
					answerField.setText("");
					quiz.RestartRound();
					quizPanel.displayNewQuestion();
				}
			}
		}
	}
}
