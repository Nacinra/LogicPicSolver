package Data;
import GUI.MySwingWorker;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static Data.Outils.combinaisons;

public class LogicPicGrid implements Serializable {

    private static LogicPicGrid s_singleton = null;
    private ArrayList<ArrayList<Integer>> m_colonnes = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> m_lignes = new ArrayList<>();
    transient private ArrayList<ArrayList<Integer>> m_Tcolonnes = new ArrayList<>();
    transient private ArrayList<ArrayList<Integer>> m_Tlignes = new ArrayList<>();
    private static final long serialVersionUID = 2L;
    private static final String s_savePath = "./save/";

    public int m_largeur = 0;
    public int m_hauteur = 0;
    transient public int[][] m_tableau = null;
    transient public static final int s_VIDE = 0;
    transient public static final int s_PLEIN = 1;
    transient public static final int s_BLOQUE = 2;
    transient public static final int s_FINAL = 3;

    public transient MySwingWorker m_swingWorker = null;
    public transient boolean m_modePasAPas = false;
    private transient boolean m_modification = false;

    private transient ArrayList<Etape> m_etapes = new ArrayList<>();
    private transient ArrayList<int[][]> m_savesTable = new ArrayList<>();
    private transient ArrayList<Etape> m_savesEtape = new ArrayList<>();

    //--------------------------------------------------------------------------
    //------              Création Utilisation de la grille               ------
    //--------------------------------------------------------------------------
    private LogicPicGrid(){
    }

    public static void init() {
        if (s_singleton == null) {

            LogicPicGrid result = null;
            Scanner sc = new Scanner(System.in);
            String reponse;
            Boolean finish = false;
            while (!finish) {
                System.out.print("recuperer une configuration [O/N] ? ");
                reponse = sc.nextLine();
                if (reponse.compareTo("O") == 0) {

                    JFileChooser choix = new JFileChooser();
                    choix.setCurrentDirectory(new File(s_savePath));
                    int retour=choix.showOpenDialog(null);

                    if(retour==JFileChooser.APPROVE_OPTION){
                        File file = choix.getSelectedFile().getAbsoluteFile();
                        if (!file.exists()) {
                            System.out.println("fichier inexistant");
                        }
                        try {
                            result = (LogicPicGrid) (new ObjectInputStream((new FileInputStream(file)))).readObject();
                            result.afficherCaracteristique();
                            finish = true;
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("erreur lors du chargement");
                        }
                    }
                } else if (reponse.compareTo("N") == 0) {
                    result = new LogicPicGrid();
                    result.recupInput(sc);
                    finish = true;

                    System.out.print("enregistrer la configuration [O/N] ? ");
                    if (sc.nextLine().compareTo("O") == 0) {
                        System.out.print("nom du fichier : ");
                        String file_name = sc.nextLine();
                        File file = new File(s_savePath + file_name);
                        if (file.exists()) {
                            System.out.println("fichier deja existant");
                        } else {
                            try {
                                if (!file.createNewFile()) {
                                    System.out.println("erreur lors de la sauvegarde");
                                }
                                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
                                stream.writeObject(result);
                                stream.flush();
                            } catch (IOException | SecurityException e) {
                                System.out.println("erreur lors de la sauvegarde");
                            }
                        }
                    }

                } else {
                    System.out.println("reponse incorrect");
                }
            }

            result.m_tableau = new int[result.m_hauteur][result.m_largeur];
            for (int i = 0; i < result.m_hauteur; i++)
                for (int j = 0; j < result.m_largeur; j++)
                    result.set(i, j, s_VIDE);

            if (result.m_Tlignes == null ) result.m_Tlignes = new ArrayList<>();
            for (ArrayList<Integer> ligne : result.m_lignes){
                ArrayList<Integer> Tligne = new ArrayList<>();
                for(int i = ligne.size()-1; i >= 0; i--)
                    Tligne.add(ligne.get(i));
                result.m_Tlignes.add(Tligne);
            }

            if (result.m_Tcolonnes == null ) result.m_Tcolonnes = new ArrayList<>();
            for (ArrayList<Integer> colonne : result.m_colonnes){
                ArrayList<Integer> Tcolonne = new ArrayList<>();
                for(int i = colonne.size()-1; i >= 0; i--)
                    Tcolonne.add(colonne.get(i));
                result.m_Tcolonnes.add(Tcolonne);
            }

            result.resetEtapes();
            result.m_savesTable = new ArrayList<>();
            result.m_savesEtape = new ArrayList<>();
            s_singleton = result;
        }
    }

    public static void destroy(){
        s_singleton = null;
    }

    public static LogicPicGrid getSingleton(){
        return s_singleton;
    }

    private void recupInput(Scanner sc){
        System.out.print("largeur : ");
        m_largeur = sc.nextInt();

        System.out.print("hauteur : ");
        m_hauteur = sc.nextInt();

        sc.nextLine();
        for(int i = 0; i < m_hauteur; i++){
            ArrayList<Integer> ligne = new ArrayList<>();
            System.out.print("ligne n°" + i + " : ");
            String reponse = sc.nextLine();
            if(reponse.compareTo("") != 0) {
                for (String str : reponse.split(" ")) {
                    ligne.add(new Integer(str));
                }
            }
            if( ligne.stream().reduce(0, (res, s)-> res + s) + ligne.size()-1 <= m_largeur){
                m_lignes.add(ligne);
            } else {
                System.out.print("Valeur incorrect ");
                i--;
            }
        }
        Boolean finish;
        System.out.print("Voulez vous corriger une ligne [O/N] ? ");
        finish = sc.nextLine().compareTo("O") != 0;
        while(!finish){
            System.out.print("Indiquer le numéro de ligne : ");
            int i = sc.nextInt();
            if(i < 0 || i >= m_hauteur){
                System.out.print("Numéro de ligne incorrect ! ");
                continue;
            }
            sc.nextLine();
            ArrayList<Integer> ligne = new ArrayList<>();
            System.out.print("ligne n°" + i + " : ");
            String reponse = sc.nextLine();
            if(reponse.compareTo("") != 0) {
                for (String str : reponse.split(" ")) {
                    ligne.add(new Integer(str));
                }
            }
            if( ligne.stream().reduce(0, (res, s)-> res + s) + ligne.size()-1 <= m_largeur){
                m_lignes.set(i, ligne);
            } else {
                System.out.print("Valeur incorrect ");
                continue;
            }
            System.out.print("Voulez vous corriger une autre ligne [O/N] ? ");
            finish = sc.nextLine().compareTo("O") != 0;
        }

        for(int i = 0; i < m_largeur; i++){
            ArrayList<Integer> colonne = new ArrayList<>();
            System.out.print("colonne n°" + i + " : ");
            String reponse = sc.nextLine();
            if(reponse.compareTo("") != 0) {
                for (String str : reponse.split(" ")) {
                    colonne.add(new Integer(str));
                }
            }
            if( colonne.stream().reduce(0, (res, s)-> res + s) + colonne.size()-1 <= m_hauteur){
                m_colonnes.add(colonne);
            } else {
                System.out.print("Valeur incorrect ");
                i--;
            }
        }
        System.out.print("Voulez vous corriger une colonne [O/N] ? ");
        finish = sc.nextLine().compareTo("O") != 0;
        while(!finish){
            System.out.print("Indiquer le numéro de colonne : ");
            int i = sc.nextInt();
            if(i < 0 || i >= m_largeur){
                System.out.print("Numéro de colonne incorrect ! ");
                continue;
            }
            sc.nextLine();
            ArrayList<Integer> colonne = new ArrayList<>();
            System.out.print("colonne n°" + i + " : ");
            String reponse = sc.nextLine();
            if(reponse.compareTo("") != 0) {
                for (String str : reponse.split(" ")) {
                    colonne.add(new Integer(str));
                }
            }
            if( colonne.stream().reduce(0, (res, s)-> res + s) + colonne.size()-1 <= m_largeur){
                m_colonnes.set(i, colonne);
            } else {
                System.out.print("Valeur incorrect ");
                continue;
            }
            System.out.print("Voulez vous corriger une autre colonne [O/N] ? ");
            finish = sc.nextLine().compareTo("O") != 0;
        }
    }

    public void reset() {
        for (int i = 0; i < m_hauteur; i++) {
            for (int j = 0; j < m_largeur; j++)
                set(i, j, s_VIDE);
        }

        m_savesEtape = new ArrayList<>();
        m_savesTable = new ArrayList<>();
        resetEtapes();
    }

    private int get(int _i, int _j){
        return m_tableau[_i][_j];
    }

    private void set(int _i, int _j, int _val){
        if(get(_i, _j) != _val) {
            m_tableau[_i][_j] = _val;
            m_modification = true;
            addEtape(Etape.Sens.LIGNE, _i);
            addEtape(Etape.Sens.COLONNE, _j);
            if (m_swingWorker != null) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m_swingWorker.myPublish(new Quadruplet(_i, _j, _val,m_largeur));
            }
        }
    }

    private void resetEtapes(){
        m_etapes = new ArrayList<>();
        for (int i = 0; i < m_hauteur; i++){
            addEtape(Etape.Sens.LIGNE, i);
        }

        for (int j = 0; j < m_largeur; j++){
            addEtape(Etape.Sens.COLONNE, j);
        }
    }

    private void addEtape(Etape.Sens _sens, int _idex){
        Etape current = new Etape(_sens, _idex);
        if(!m_etapes.contains(current)){
            m_etapes.add(current);
        }
    }

    //--------------------------------------------------------------------------
    //------                     Phases de résolution                     ------
    //--------------------------------------------------------------------------

    public boolean debile(int _i, int _j){
        if(_i == (m_hauteur-1) && _j == (m_largeur-1)){
            if(get(_i, _j) == s_VIDE) {
                set(_i, _j, s_PLEIN);
                if (verification()) return true;
                set(_i, _j, s_VIDE);
                return verification();
            }else{
                return verification();
            }
        }else{
            int i1 = (_j == (m_largeur-1))? _i+1 : _i;
            int j1 = (_j+1)%m_largeur;
            if(get(_i, _j) == s_VIDE) {
                set(_i, _j, s_PLEIN);
                if (debile(i1, j1)) return true;
                set(_i, _j, s_VIDE);
                return debile(i1, j1);
            }else{
                return debile(i1, j1);
            }
        }
    }

    public boolean moindebile(int _i){
        if(_i == m_hauteur){
            return verification();
        } else {
            ArrayList<Integer> valeurs = m_lignes.get(_i);
            ArrayList<Segment> vide = new ArrayList<>();
            vide.add(new Segment(0, m_largeur - 1));
            for (ArrayList<Segment> combinaison : combinaisons(vide, valeurs)) {
                remplir(new Etape(Etape.Sens.LIGNE, _i), vide, s_VIDE);
                remplir(new Etape(Etape.Sens.LIGNE, _i), combinaison, s_PLEIN);
                if (moindebile(_i + 1)){
                    return true;
                }
            }
            return false;
        }
    }

    public void magie(){
        boolean reset = false;
        m_modification = false;
        while(!m_etapes.isEmpty() && !(m_modePasAPas && m_modification)) {
            try {
                Etape current = m_etapes.get(0);
                ArrayList<ArrayList<Segment>> combinaisonsRestante;
                ArrayList<Segment> combinaisonARemplir;
                ArrayList<ArrayList<Segment>> inverseCombiRest;
                reset = false;

                combinaisonsRestante = rechercheCombinaisons(current);
                if(combinaisonsRestante == null || combinaisonsRestante.size() < 1){
                    resetEtapes();
                    current = resetTable();
                    combinaisonsRestante = rechercheCombinaisons(current);
                    combinaisonsRestante.remove(0);
                    reset = true;
                }

                combinaisonARemplir = combinaisonsRestante.stream().reduce(null, Outils::mergeSeg);
                remplir(current, combinaisonARemplir, s_PLEIN);

                inverseCombiRest = Outils.inversionCombinaisons(combinaisonsRestante, m_largeur);
                combinaisonARemplir = inverseCombiRest.stream().reduce(null, Outils::mergeSeg);
                remplir(current, combinaisonARemplir, s_BLOQUE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!reset) {
                m_etapes.remove(0);
            }
        }
        if(m_etapes.isEmpty() && !verification()){
            Etape etape = rechercheMinCombinaison();
            saveTable(etape);

            ArrayList<ArrayList<Segment>> combinaisonMin = rechercheCombinaisons(etape);
            ArrayList<Segment> tentative = combinaisonMin.get(0);
            remplir(etape, tentative, s_PLEIN);
            magie();
        }
    }

    private ArrayList<ArrayList<Segment>> rechercheCombinaisons(Etape _etape){
        ArrayList<Integer> valeurs = recupValeur(_etape);
        ArrayList<Segment> espacesLibre = recupEspace(_etape, s_BLOQUE, true);
        ArrayList<Segment> espacesPlein = recupEspace(_etape, s_PLEIN, false);
        ArrayList<ArrayList<Segment>> combinaisons = Outils.combinaisons(espacesLibre, valeurs);
        return Outils.suppressionCombinaisons(combinaisons, espacesPlein);
    }

    private Etape rechercheMinCombinaison(){
        Etape result = null, current;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < m_hauteur; i++){
            current = new Etape(Etape.Sens.LIGNE, i);
            ArrayList<ArrayList<Segment>> combinaisonsRestante = rechercheCombinaisons(current);

            if(combinaisonsRestante.size() > 1 && combinaisonsRestante.size() < min) {
                result = current;
                min = combinaisonsRestante.size();
            }
        }
        for (int j = 0; j < m_largeur; j++){
            current = new Etape(Etape.Sens.COLONNE, j);
            ArrayList<ArrayList<Segment>> combinaisonsRestante = rechercheCombinaisons(current);

            if(combinaisonsRestante.size() > 1 && combinaisonsRestante.size() < min){
                result = current;
                min = combinaisonsRestante.size();
            }
        }
        return result;
    }

    //--------------------------------------------------------------------------
    //------                  Outils de gestion des Grid                  ------
    //--------------------------------------------------------------------------

    public boolean verification(){
        for(int i = 0; i < m_hauteur;i++){
            if(!verifUnitaire(new Etape(Etape.Sens.LIGNE, i)))
                return false;
        }
        for (int j = 0; j < m_largeur; j++) {
            if(!verifUnitaire(new Etape(Etape.Sens.COLONNE, j)))
                return false;
        }
        for(int i = 0; i < m_hauteur;i++){
            for(int j = 0; j < m_largeur; j++){
                switch(get(i, j)){
                    case s_BLOQUE :
                        set(i, j, s_VIDE);
                        break;
                }
            }
        }
        return true;
    }

    private boolean verifUnitaire(Etape _etape){
        return Outils.compareArray(lectureValeur(_etape), recupValeur(_etape));
    }

    private ArrayList<Integer> recupValeur(Etape _etape){
        switch (_etape.m_sens) {
            case LIGNE:
                return m_lignes.get(_etape.m_index);
            case COLONNE:
                return m_colonnes.get(_etape.m_index);
        }
        return null;
    }

    private ArrayList<Integer> lectureValeur(Etape _etape){
        ArrayList<Integer> result = new ArrayList<>();
        int idxDebut = 0;
        boolean debut = false;
        int max = 0;
        switch (_etape.m_sens) {
            case LIGNE:
                max = m_largeur;
                break;
            case COLONNE:
                max = m_hauteur;
                break;
        }
        for (int comp = 0; comp < max; comp++){
            int valeur =  0;
            switch (_etape.m_sens) {
                case LIGNE:
                    valeur = get(_etape.m_index, comp);
                    break;
                case COLONNE:
                    valeur = get(comp, _etape.m_index);
                    break;
            }
            if(!debut && valeur == s_PLEIN){
                debut = true;
                idxDebut = comp;
            }else if(debut && valeur != s_PLEIN){
                debut = false;
                result.add(comp-idxDebut);
            }
        }
        if(debut){
            result.add(max-idxDebut);
        }
        return result;
    }

    private ArrayList<Segment> recupEspace(Etape _etape, int _valeur, boolean _invers){
        ArrayList<Segment> result = new ArrayList<>();
        boolean debut = false;
        int idxDebut = 0;
        int max = 0;
        switch (_etape.m_sens) {
            case LIGNE:
                max = m_largeur;
                break;
            case COLONNE:
                max = m_hauteur;
                break;
        }
        for (int comp = 0; comp < max; comp++){
            int valeur =  0;
            switch (_etape.m_sens) {
                case LIGNE:
                    valeur = get(_etape.m_index, comp);
                    break;
                case COLONNE:
                    valeur = get(comp, _etape.m_index);
                    break;
            }
            if(!debut && (!_invers && valeur == _valeur || _invers && valeur != _valeur )){
                debut = true;
                idxDebut = comp;
            }else if(debut && (!_invers && valeur != _valeur || _invers && valeur == _valeur )){
                debut = false;
                result.add(new Segment(idxDebut, comp-1));
            }
        }
        if(debut){
            result.add(new Segment(idxDebut, max-1));
        }
        return result;
    }

    private void remplir(Etape _etape, ArrayList<Segment> _liste, int _valeur) {
        if (_liste != null) {
            for (Segment seg : _liste) {
                for (int comp = seg.m_debut; comp <= seg.m_fin; comp++) {
                    switch (_etape.m_sens) {
                        case LIGNE:
                            set(_etape.m_index, comp, _valeur);
                            break;
                        case COLONNE:
                            set(comp, _etape.m_index, _valeur);
                            break;
                    }
                }
            }
        }
    }

    private void saveTable(Etape _etape){
        int [][] save_table = new int[m_hauteur][m_largeur];
        for (int i = 0; i < m_hauteur; i++)
            for (int j = 0; j < m_largeur; j++)
                save_table[i][j] = get(i, j);
        m_savesTable.add(save_table);

        m_savesEtape.add(_etape);
    }

    private Etape resetTable(){
        int [][] save_table = m_savesTable.get(m_savesTable.size()-1);
        for (int i = 0; i < m_hauteur; i++)
            for (int j = 0; j < m_largeur; j++)
                set(i, j, save_table[i][j]);
        m_savesTable.remove(m_savesTable.size()-1);

        Etape result = m_savesEtape.get(m_savesEtape.size()-1);
        m_savesEtape.remove(m_savesEtape.size()-1);
        return result;
    }

    private void afficherCaracteristique() {
        System.out.println("m_hauteur : " + m_hauteur);
        System.out.println("m_nbLigne : " + m_lignes.size());
        System.out.println("m_largeur : " + m_largeur);
        System.out.println("m_nbColonne : " + m_colonnes.size());

        System.out.println("Tableau des lignes : ");
        System.out.println(m_lignes);
        System.out.println("Tableau des colonnes : ");
        System.out.println(m_colonnes);
    }

}
