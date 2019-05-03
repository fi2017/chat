package chatFPAUebung.klassen;

import java.io.File;

public class NachrichtBild extends Nachricht
{
    private File bild;

    public NachrichtBild(File bild)
    {
        this.bild = bild;
    }

    public File getBild() {
        return bild;
    }

    public void setBild(File bild) {
        this.bild = bild;
    }

    //TODO: ToString für Bild
}
