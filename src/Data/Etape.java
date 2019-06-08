package Data;

public class Etape {
    public enum Sens{
        LIGNE,
        COLONNE
    }

    public Sens m_sens;
    public int m_index;

    public Etape(Sens _sens, int _index){
        m_sens = _sens;
        m_index = _index;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Etape) && m_sens == ((Etape) obj).m_sens && m_index == ((Etape) obj).m_index;
    }

    @Override
    public String toString() {
        switch(m_sens){
            case LIGNE :
                return "Ligne n° " + m_index;
            case COLONNE :
                return "Colonne n° " + m_index;
        }
        return super.toString();
    }
}
