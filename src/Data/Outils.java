package Data;

import java.util.ArrayList;

public class Outils {
    static boolean compareArray(ArrayList<Integer> _l1, ArrayList<Integer> _l2){
        if(_l1 == null || _l2 == null) return false;
        if(_l1 .size() != _l2.size()) return false;
        int max = _l2.size();
        for(int i = 0; i < max; i++){
            if(_l1.get(i).intValue() != _l2.get(i).intValue()) return false;
        }
        return true;
    }

    static ArrayList<Segment> combinaisonGagnante(ArrayList<Segment> _espacesLibre, ArrayList<Integer> _resteAPlacer){
        if(_espacesLibre == null || _resteAPlacer == null)
            return null;

        ArrayList<Segment> temp1,result1,result2;
        ArrayList<Integer> temp2;
        if(_resteAPlacer.size() == 0) //tout est placé c'est bon :)
            return new ArrayList<>();
        if(_espacesLibre.size() == 0)
            return null;

        if(_resteAPlacer.get(0) > _espacesLibre.get(0).m_longeur){
            temp1 = clonnageSeg(_espacesLibre);
            temp1.remove(0);
            return combinaisonGagnante(temp1, _resteAPlacer);
        }else{
            temp1 = clonnageSeg(_espacesLibre);
            temp1.get(0).reduire(_resteAPlacer.get(0)+1);
            temp2 = clonnageInt(_resteAPlacer);
            temp2.remove(0);
            result1 = combinaisonGagnante(temp1, temp2);
            if(result1 == null){
                return null;
            }
            int debut = _espacesLibre.get(0).m_debut;
            int fin = debut + _resteAPlacer.get(0)- 1;
            result1.add(0, new Segment(debut, fin));

            temp1 = clonnageSeg(_espacesLibre);
            temp1.get(0).reduire(1);
            result2 = combinaisonGagnante(temp1, _resteAPlacer);

            return mergeSeg(result1,result2);
        }
    }

    private static ArrayList clonnageSeg(ArrayList<Segment> _liste){
        if(_liste == null)
            return null;

        ArrayList<Segment> result = new ArrayList<>();
        for(Segment seg : _liste){
            result.add(new Segment(seg.m_debut,seg.m_fin));
        }
        return result;
    }

    private static ArrayList clonnageInt(ArrayList<Integer> _liste){
        if(_liste == null)
            return null;

        ArrayList<Integer> result = new ArrayList<>();
        for(Integer val : _liste){
            result.add(new Integer(val));
        }
        return result;
    }

    public static ArrayList<Segment> mergeSeg(ArrayList<Segment> _liste1, ArrayList<Segment> _liste2){
        if(_liste1 == null)
            return _liste2;
        if(_liste2 == null)
            return _liste1;

        ArrayList<Segment> result = new ArrayList<>();
        int i = 0, j = 0;
        while(i < _liste1.size() && j < _liste2.size()){
            Segment seg1 = _liste1.get(i);
            Segment seg2 = _liste2.get(j);

            if(seg1.m_fin < seg2.m_debut){
                i++;
            }else if(seg2.m_fin < seg1.m_debut) {
                j++;
            }else if(seg1.m_debut == seg2.m_debut){
                if(seg1.m_fin < seg2.m_fin){
                    i++;
                    result.add(new Segment(seg2.m_debut,seg1.m_fin));
                }else{
                    j++;
                    result.add(new Segment(seg1.m_debut,seg2.m_fin));
                }
            }else if(seg1.m_debut < seg2.m_debut){
                if(seg1.m_fin < seg2.m_fin){
                    i++;
                    result.add(new Segment(seg2.m_debut,seg1.m_fin));
                }else{
                    j++;
                    result.add(new Segment(seg2.m_debut,seg2.m_fin));
                }
            }else {
                if(seg1.m_fin < seg2.m_fin){
                    i++;
                    result.add(new Segment(seg1.m_debut,seg1.m_fin));
                }else{
                    j++;
                    result.add(new Segment(seg1.m_debut,seg2.m_fin));
                }
            }
        }
        return result;
    }

    static ArrayList<ArrayList<Segment>> combinaisons(ArrayList<Segment> _espacesLibre, ArrayList<Integer> _resteAPlacer) {
        if(_espacesLibre == null || _resteAPlacer == null)
            return null;

        ArrayList<ArrayList<Segment>> result1, result2;
        ArrayList<Segment> temp1;
        ArrayList<Integer> temp2;
        if (_resteAPlacer.size() == 0) {//tout est placé c'est bon :)
            result1 = new ArrayList<>();
            result1.add(new ArrayList<>());
            return result1;
        }
        if (_espacesLibre.size() == 0)
            return null;

        if (_resteAPlacer.get(0) > _espacesLibre.get(0).m_longeur) {
            temp1 = clonnageSeg(_espacesLibre);
            temp1.remove(0);
            return combinaisons(temp1, _resteAPlacer);
        } else {
            temp1 = clonnageSeg(_espacesLibre);
            temp1.get(0).reduire(_resteAPlacer.get(0) + 1);
            temp2 = clonnageInt(_resteAPlacer);
            temp2.remove(0);
            result1 = combinaisons(temp1, temp2);
            if (result1 == null) {
                return null;
            }
            int debut = _espacesLibre.get(0).m_debut;
            int fin = debut + _resteAPlacer.get(0) - 1;
            ajouterSegmentDebut(result1, debut, fin);

            temp1 = clonnageSeg(_espacesLibre);
            temp1.get(0).reduire(1);
            result2 = combinaisons(temp1, _resteAPlacer);
            if(result2 != null)
                result1.addAll(result2);
            return result1;
        }
    }

    static void ajouterSegmentDebut(ArrayList<ArrayList<Segment>> _listes,int _debut, int _fin){
        if(_listes == null)
            return;

        for(ArrayList<Segment> liste : _listes){
            liste.add(0, new Segment(_debut, _fin));
        }
    }

    public static ArrayList<ArrayList<Segment>> suppressionCombinaisons(ArrayList<ArrayList<Segment>> _listes, ArrayList<Segment> _espacesPlein){
        if(_listes == null ||_espacesPlein == null)
            return null;

        ArrayList<ArrayList<Segment>> result = new ArrayList<>();
        for(ArrayList<Segment> liste : _listes){
            int i = 0, j = 0;
            while(i < _espacesPlein.size() && j < liste.size()) {
                Segment seg1 = _espacesPlein.get(i);
                Segment seg2 = liste.get(j);

                if (seg1.m_debut < seg2.m_debut) {
                    break;
                } else if (seg1.m_fin <= seg2.m_fin) {
                    i++;
                } else {
                    j++;
                }
            }
            if(i == _espacesPlein.size()){
                result.add(liste);
            }
        }
        return result;
    }

    public static ArrayList<ArrayList<Segment>> inversionCombinaisons(ArrayList<ArrayList<Segment>> _listes, int _max){
        if(_listes == null)
            return null;

        ArrayList<ArrayList<Segment>> result = new ArrayList<>();
        for(ArrayList<Segment> liste : _listes){
            ArrayList<Segment> temp = new ArrayList<>();
            int idxDebut = 0;
            for(Segment seg : liste){
                if(idxDebut != seg.m_debut){
                    temp.add(new Segment(idxDebut, seg.m_debut-1));
                }
                idxDebut = seg.m_fin + 1;
            }
            if(idxDebut < _max){
                temp.add(new Segment(idxDebut, _max-1));
            }
            result.add(temp);
        }
        return result;
    }

}
