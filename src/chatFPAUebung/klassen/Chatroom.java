package chatFPAUebung.klassen;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.layout.VBox;

public class Chatroom implements interfaces.BlackListInterface, Serializable //Ihr habt vergessen des einzubinden, hoffe des ist okay wenn ich's mach :D
{
    private String name;
    private static int idNew = 0;
    private int id;
    private int maxTeilnehmer;
    private String passwort;
    private DefaultListModel Chatmodel;
    private VBox container;

    private ArrayList<ClientProxy> teilnehmer = new ArrayList<ClientProxy>();
    private ArrayList<String> blacklist = new ArrayList<String>();   //Methode zum blacklisten

    //Hinzufuegen eines Clients sowie abprüfen der Bannliste
    public String hinzufuegen(ClientProxy c)
    {
        String grund;
        boolean gebannt=false;
        for(String s : blacklist)
        {
            if(s.equals(getName()))
            {
                gebannt=true;
            }
        }
        if(gebannt==true)
        {
            grund = "gebannt";
        }
        else if(maxTeilnehmer<=teilnehmer.size())
        {
            grund = "voll";
        }
        else
        {
            teilnehmer.add(c);
            grund = "beitritt";
        }

        return grund;
    }

    //Austreten eines Clients aus dem Chatroom
    public void entfernen(ClientProxy c)
    {
        teilnehmer.remove(c);
    }

    //Bannen eines Users
    public void addUserToBlackList(String username)
    {
        blacklist.add(username);
    }

    //Aufheben eines Banns von einem User
    public void removeUserFromBlackList(String username)
    {
        blacklist.remove(username);
    }

    //Konstruktor
    public Chatroom(String name, int maxTeilnehmer)
    {
        this.id = idNew;
        idNew ++;
        this.name = name;
        this.maxTeilnehmer = maxTeilnehmer;
    }

    //Getter und Setter
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ArrayList<ClientProxy> getTeilnehmer()
    {
        return teilnehmer;
    }

    public void setTeilnehmer(ArrayList<ClientProxy> teilnehmer)
    {
        this.teilnehmer = teilnehmer;
    }

    public int getMaxTeilnehmer() {
        return maxTeilnehmer;
    }

    public void setMaxTeilnehmer(int maxTeilnehmer) {
        this.maxTeilnehmer = maxTeilnehmer;
    }

    public String getPasswort() { return passwort; }

    public void setPasswort(String passwort) { this.passwort = passwort; }

    public ArrayList<String> getBlacklist() { return blacklist; }

    public void setBlacklist(ArrayList<String> blacklist) { this.blacklist = blacklist; }

    public DefaultListModel getChatmodel()
    {
        return Chatmodel;
    }

    public void setChatmodel(DefaultListModel chatmodel)
    {
        Chatmodel = chatmodel;
    }

    public VBox getContainer()
    {
        return container;
    }

    public void setContainer(VBox container)
    {
        this.container = container;
    }
}
