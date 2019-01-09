package Data;

import java.util.ArrayList;

public class Outils {
    static boolean compareArray(ArrayList<Integer> _l1, ArrayList<Integer> _l2){
        if(_l1 .size() != _l2.size()) return false;
        int max = _l2.size();
        for(int i = 0; i < max; i++){
            if(_l1.get(i).intValue() != _l2.get(i).intValue()) return false;
        }
        return true;
    }

    static ArrayList<Segment> combinaisonGagnante(ArrayList<Segment> _espacesLibre, ArrayList<Integer> _resteAPlacer){
        ArrayList<Segment> result = new ArrayList<>(),temp1,result1,result2;
        ArrayList<Integer> temp2;
        if(_resteAPlacer.size() == 0) //tout est placé c'est bon :)
            return result;
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
        ArrayList<Segment> result = new ArrayList<>();
        for(Segment seg : _liste){
            result.add(new Segment(seg.m_debut,seg.m_fin));
        }
        return result;
    }

    private static ArrayList clonnageInt(ArrayList<Integer> _liste){
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer val : _liste){
            result.add(new Integer(val));
        }
        return result;
    }

    private static ArrayList<Segment> mergeSeg(ArrayList<Segment> _liste1, ArrayList<Segment> _liste2){
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
                i++;
                result.add(new Segment(seg2.m_debut,seg1.m_fin));
            }else {
                j++;
                result.add(new Segment(seg1.m_debut,seg2.m_fin));
            }
        }
        return result;
    }
}
