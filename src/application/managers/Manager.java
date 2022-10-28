package application.managers;

import java.io.Serializable;

public interface Manager extends Serializable
{
    String getName();
    void setName(String newName);
}
