package Main;


public class Quiz
{
	private int score;
	private int qCount;
	private int nbrRounds;
	private int currQuestionId;
	private String[][] history;
	private String[][] quizentry;
	public boolean isPlaying;
	private QuizDB qdb;

	public static void main(String[] args)
	{
		Quiz q = new Quiz();
		q.qdb = new QuizDB();
		new QuizFrame(q, q.qdb);
	}

	public Quiz()
	{
		this.score = 0;
		this.qCount = 0;
		this.nbrRounds = 5;
		this.history = new String[nbrRounds][4];
	}
	
	public void checkAnswer(String answer)
	{
		boolean correctAnswer = answer.equalsIgnoreCase(quizentry[currQuestionId][1]);
		String[] currHistEntry = {quizentry[currQuestionId][0], answer, quizentry[currQuestionId][1], ((correctAnswer) ? "CORRECT" : "INCORRECT")};
		if(correctAnswer)
			score++;
		history[qCount] = currHistEntry;
		qCount++;
		if (qCount >= nbrRounds)
			isPlaying = false;
		return;
	}

	public void RestartRound()
	{
		score = 0;
		qCount = 0;
		history = new String[nbrRounds][4];
		quizentry = qdb.getQuizEntry();
		isPlaying = true;
		return;
	}
	
	public void genNewQuestionId() { currQuestionId = (int)(quizentry.length*Math.random()); }
	
	public void setScore(int score) { this.score = score; }
	public int getScore() { return score; }
	public void setQCount(int qCount) { this.qCount = qCount; }
	public int getQCount() { return qCount; }
	public void setNbrRounds(int nbrRounds) { this.nbrRounds = nbrRounds; }
	public int getNbrRounds() { return nbrRounds; }
	public void setHistory(int row, int col, String val) { this.history[row][col] = val; }
	public String[][] getHistory() { return history; }
	public String getCurrQuestion() { return quizentry[currQuestionId][0]; }
	public String getCurrAnswer() { return quizentry[currQuestionId][1]; }
	public String getCurrImage() { return QuizDB.DB_PATH + quizentry[currQuestionId][2]; }
	public int getCurrQuestionID() { return currQuestionId; }
}

