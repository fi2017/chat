package chatFPAUebung.gui.client.loginMenu.settingsStage;

public class Theme
{

    public String bezeichnung;
    public String dateiname;

    public Theme()
    {

    }

    public Theme(String bezeichnung, String dateiname)
    {
        this.bezeichnung = bezeichnung;
        this.dateiname = dateiname;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getDateiname() {
        return dateiname;
    }
}
