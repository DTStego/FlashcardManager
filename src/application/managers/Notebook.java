package application.managers;

import java.util.ArrayList;
import java.util.List;

public class Notebook implements Manager
{
    private final List<Course> courseList;
    private String name;

    public Notebook()
    {
        courseList = new ArrayList<>();
        name = "New Notebook";
    }

    public Notebook(List<Course> courseList, String name)
    {
        this.courseList = courseList;
        this.name = name;
    }

    public boolean add(Course course)
    {
        return courseList.add(course);
    }

    public boolean delete(Course course)
    {
        return courseList.remove(course);
    }

    public List<Course> getCourseList()
    {
        return courseList;
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
