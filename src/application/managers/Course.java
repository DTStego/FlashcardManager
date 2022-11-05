package application.managers;

import java.util.List;

public class Course implements Manager
{
    private final List<Topic> topicList;
    private String name;

    public Course(List<Topic> topicList, String name)
    {
        this.topicList = topicList;
        this.name = name;
    }

    public boolean add(Topic topic)
    {
        return topicList.add(topic);
    }

    public boolean delete(Topic topic)
    {
        return topicList.add(topic);
    }

    public List<Topic> getTopicList()
    {
        return topicList;
    }

    public void rename(String newName) {
        name = newName;
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
