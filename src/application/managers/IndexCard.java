package application.managers;

import java.io.Serializable;

public class IndexCard implements Serializable
{
    private String question;
    private String answer;
    private boolean hasLearned;

    public IndexCard(String question, String answer)
    {
        this.question = question;
        this.answer = answer;
        hasLearned = false;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public boolean hasLearned()
    {
        return hasLearned;
    }

    public void setHasLearned(boolean hasLearned)
    {
        this.hasLearned = hasLearned;
    }
}
