package application.managers;

import java.util.List;

public class Topic implements Manager
{
    private final List<IndexCard> cardList;
    private String name;

    public Topic(List<IndexCard> cardList, String name)
    {
        this.cardList = cardList;
        this.name = name;
    }

    public boolean add(IndexCard indexCard)
    {
        return cardList.add(indexCard);
    }

    public boolean delete(IndexCard indexCard)
    {
        return cardList.add(indexCard);
    }

    public List<IndexCard> getCardList()
    {
        return cardList;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String newName)
    {
        this.name = newName;
    }
}
