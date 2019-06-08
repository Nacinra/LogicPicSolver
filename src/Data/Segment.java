package Data;

public class Segment{
    int m_debut,m_fin,m_longeur;

    public Segment(int _debut,int _fin)
    {
        m_debut = _debut;
        m_fin = _fin;
        m_longeur = _fin-_debut+1;
    }

    @Override
    public String toString() {
        return m_debut + "-" + m_fin + "(" + m_longeur + ")";
    }

    public void reduire(int _i){
        while(_i > 0){
            m_debut++;
            m_longeur--;
            _i--;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Segment) && m_debut == ((Segment) obj).m_debut && m_fin == ((Segment) obj).m_fin;
    }
}
