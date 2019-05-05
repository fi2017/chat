package chatFPAUebung.gui.client.loginMenu.settingsStage;

public class Theme
{

    public String bezeichnung;
    public String dateipfadLogin;
    public String dateipfadClient;

    public Theme()
    {

    }

    public Theme(String bezeichnung, String dateipfadLogin, String dateipfadClient)
    {
        this.bezeichnung = bezeichnung;
        this.dateipfadLogin = dateipfadLogin;
        this.dateipfadClient = dateipfadClient;
    }

    @Override
    public String toString() {
        return bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getDateipfadLogin() { return dateipfadLogin; }

    public String getDateipfadClient() { return dateipfadClient; }
}
