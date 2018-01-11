package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class QuizFrame extends JFrame
{
	private static final long serialVersionUID = 2205503599667227092L;
	private JTextField answerField;
	private Quiz quiz;
	private QuizPanel quizPanel;
	private QuizHistoryFrame quizHistory;
	private QuizDB quizDB;
	private JFrame history, settings, database, appendDB;
	
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
		JButton restartButton = AddButton("(Re)start", "restart", "Click here to (re)start the round.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				answerField.setText("");
				quiz.RestartRound();
				quiz.genNewQuestionId();
				quizPanel.displayNewQuestion();
			}
		});
		bPanel.add(restartButton);
		JButton resultsButton = AddButton("Show results", "results", "Click here to show the results. Doing so will end the round.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		resultsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (quiz.isPlaying) {
					answerField.setText("");
					quiz.isPlaying = false;
					quizPanel.clearDisplayQuestion();
					displayHistory();
				} else {
					JOptionPane.showMessageDialog(null, "Why?", "Unsupported event", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		bPanel.add(resultsButton);
		JButton settingsButton = AddButton("Settings", "settings", "Click here to modify the settings.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settings.setVisible(true);
				settings.pack();
			}
		});
		bPanel.add(settingsButton);
		JButton databaseButton = AddButton("Database", "database", "Click here to modify the database.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				appendDB.setVisible(true);
				appendDB.pack();
			}
		});
		bPanel.add(databaseButton);
		add(bPanel, BorderLayout.NORTH);
		
		// textfield to put answer in
		JPanel answerPanel = new JPanel(new BorderLayout());
		answerField = new JTextField(20);
		answerField.setName("answer");
		answerField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!quiz.isPlaying && answerField.getText().equals("")) {
					answerField.setText("");
					quiz.RestartRound();
					quizPanel.displayNewQuestion();
				}
			}
		});
		addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	answerField.requestFocus();
		    }
		});  // set focus to answer field
		answerPanel.add(answerField, BorderLayout.CENTER);
		Dimension d = new Dimension(75, 25);
		JButton answerButton = AddButton("Send", "answer", "Click to Answer.", true, Color.LIGHT_GRAY, Color.BLACK, d);
		answerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quiz.checkAnswer(answerField.getText());
				answerField.setText("");
				if (quiz.isPlaying) {
					quiz.genNewQuestionId();
					quizPanel.displayNewQuestion();
				} else {
					quizPanel.clearDisplayQuestion();
					displayHistory();
				}
			}
		});
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
		JPanel appendDBPanel = new JPanel(new GridLayout(4, 1));
		JPanel qPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		qPanel.add(new JLabel("Question:"));
		JTextArea questionArea = new JTextArea(3, 30);
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		JScrollPane questionScrollArea = new JScrollPane(questionArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		qPanel.add(questionScrollArea);
		appendDBPanel.add(qPanel);
		
		JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		aPanel.add(new JLabel("Answer:"));
		JTextArea answerArea = new JTextArea(3, 30);
		answerArea.setLineWrap(true);
		answerArea.setWrapStyleWord(true);
		JScrollPane answerScrollArea = new JScrollPane(answerArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		aPanel.add(answerScrollArea);
		appendDBPanel.add(aPanel);
		
		JPanel iPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		iPanel.add(new JLabel("Image path:"));
		JTextArea imageArea = new JTextArea(3, 30);
		imageArea.setLineWrap(true);
		JScrollPane imageScrollArea = new JScrollPane(imageArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		iPanel.add(imageScrollArea);
		appendDBPanel.add(iPanel);
		
		JPanel iPanelEx = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		iPanelEx.add(new JLabel("Example path:"));
		JTextArea imageAreaEx = new JTextArea(1, 30);
		imageAreaEx.setEditable(false);
		imageAreaEx.setText(QuizDB.DB_PATH+"default.png");
		imageAreaEx.setBackground(Color.LIGHT_GRAY);
		imageAreaEx.setLineWrap(true);
		iPanelEx.add(imageAreaEx);
		
		appendDBPanel.add(iPanelEx);
		JPanel appendDBButtons = new JPanel();
		JButton appendDBAppendButton = AddButton("Append", "appenddbappend", "Click here to append to database.", true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(75, 25));
		appendDBAppendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!questionArea.getText().equals("") && !answerArea.getText().equals("")) {
					if (quizDB.appendDatabase(questionArea.getText(), answerArea.getText(), imageArea.getText())) {
						questionArea.setText("");
						answerArea.setText("");
						imageArea.setText("");
						quizDB.readDatabase();
					} else {
						JOptionPane.showMessageDialog(null, "Check if the image path is correct.\nRemember to use the absolute path.", "Error writing to database", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		appendDBButtons.add(appendDBAppendButton);
		JButton appendDBCloseButton = AddButton("Close", "appenddbcancel", "Click here to discard any changes.", true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(75, 25));
		appendDBCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				questionArea.setText("");
				answerArea.setText("");
				imageArea.setText("");
				appendDB.setVisible(false);
				appendDB.dispose();
			}
		});
		appendDBButtons.add(appendDBCloseButton);
		JButton openDBButton = AddButton("Open database", "opendatabase", "Click here to open the database. Useful for debugging the database.", true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(130, 25));
		openDBButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quizDB.displayDatabase();
				database.setVisible(true);
				database.pack();
			}
		});
		appendDBButtons.add(openDBButton);
		JButton galleryButton = AddButton("Browse images", "opengallery", "Click here to open the gallery and browse the images in the database.", true, Color.LIGHT_GRAY, Color.BLACK, new Dimension(130, 25));
		galleryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		appendDBButtons.add(galleryButton);
		
		JFrame frame = new JFrame("Append to database");
		frame.setLayout(new BorderLayout());
		frame.add(appendDBPanel, BorderLayout.CENTER);
		frame.add(appendDBButtons, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JFrame databaseFrame()
	{
		Dimension dim = new Dimension(130, 25);
		JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton databaseWriteButton = AddButton("Write", "databaseSave", "Click here to Write to database.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseWriteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quizDB.writeDatabase();
				quizDB.readDatabase();
				database.setVisible(false);
				database.dispose();
			}
		});
		dbPanel.add(databaseWriteButton);
		JButton databaseDiscardButton = AddButton("Discard", "databaseDiscard", "Click here to discard changes.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseDiscardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				database.setVisible(false);
				database.dispose();
			}
		});
		dbPanel.add(databaseDiscardButton);
		
		JFrame frame = new JFrame("Database Editor");
		frame.setLayout(new BorderLayout());
		frame.add(quizDB, BorderLayout.CENTER);
		frame.add(dbPanel, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JFrame settingsFrame()
	{
		Dimension dim = new Dimension(75, 25);
		JPanel settingsPanel = new JPanel();
		settingsPanel.add(new JLabel("Rounds:"));
		JTextField nRoundsField = new JTextField(10);
		nRoundsField.setText(""+quiz.getNbrRounds());
		settingsPanel.add(nRoundsField);
		
		JPanel settingsButtons = new JPanel();
		JButton settingsApplyButton = AddButton("Apply", "settingsapply", "Click here to apply settings.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsApplyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int newRounds  = Integer.parseInt(nRoundsField.getText());
					quiz.setNbrRounds(newRounds);
					settings.setVisible(false);
					settings.dispose();
					answerField.setText("");
					quiz.RestartRound();
					quizPanel.displayNewQuestion();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Invalid input",
							"Not an integer", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		settingsButtons.add(settingsApplyButton);
		JButton settingsCancelButton = AddButton("Cancel", "settingscancel", "Click here to discard any changes.", true, Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settings.setVisible(false);
			}
		});
		settingsButtons.add(settingsCancelButton);

		JFrame frame = new JFrame("Quiz settings");
		frame.setLayout(new BorderLayout());
		frame.add(settingsPanel, BorderLayout.CENTER);
		frame.add(settingsButtons, BorderLayout.SOUTH);
		frame.setResizable(false);
		return frame;
	}
	
	public JButton AddButton(String buttonText, String nameSet, String tooltip,
			boolean opaque, Color background, Color border, Dimension d)
	{
		JButton button = new JButton(buttonText);
		button.setName(nameSet);
		button.setToolTipText(tooltip);
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
}
