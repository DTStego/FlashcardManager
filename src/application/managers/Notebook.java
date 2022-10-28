package application.managers;

import java.util.List;

public class Notebook implements Manager
{
    private final List<Course> courseList;

    public Notebook(List<Course> courseList)
    {
        this.courseList = courseList;
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
    public List<Course> display()
    {
        return courseList;
    }
}
