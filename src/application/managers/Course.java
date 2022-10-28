package application.managers;

import java.util.List;

public class Course implements Manager
{
    private List<Topic> topicList;

    public Course()
    {

    }

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
    public List<Manager> display()
    {
        return null;
    }
}
