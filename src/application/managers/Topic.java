package application.managers;

import java.util.List;

public class Topic implements Manager
{
    List<IndexCard> cardList;

    @Override
    public boolean add(Manager manager)
    {
        return false;
    }

    @Override
    public boolean delete(Manager manager)
    {
        return false;
    }

    @Override
    public boolean rename(Manager manager, String newName)
    {
        return false;
    }

    @Override
    public List<? extends Manager> display()
    {
        return null;
    }
}
