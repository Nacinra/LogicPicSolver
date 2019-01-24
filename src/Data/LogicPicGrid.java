package Data;
import GUI.MainFrame;
import GUI.MySwingWorker;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public class LogicPicGrid implements Serializable {

    private static LogicPicGrid s_singleton = null;
    private ArrayList<ArrayList<Integer>> m_colonnes = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> m_lignes = new ArrayList<>();
    transient private ArrayList<ArrayList<Integer>> m_Tcolonnes = new ArrayList<>();
    transient private ArrayList<ArrayList<Integer>> m_Tlignes = new ArrayList<>();
    private static final long serialVersionUID = 2L;
    private static final String s_savePath = "save/";

    public int m_largeur = 0;
    public int m_hauteur = 0;
    transient public int[][] m_tableau = null;
    transient public static final int s_VIDE = 0;
    transient public static final int s_PLEIN = 1;
    transient public static final int s_BLOQUE = 2;
    transient public static final int s_FINAL = 3;

    public MySwingWorker m_swingWorker = null;

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
                    System.out.print("nom du fichier : ");
                    String file_name = sc.nextLine();
                    File file = new File(s_savePath + file_name);
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
        for (int i = 0; i < m_hauteur; i++)
            for (int j = 0; j < m_largeur; j++)
                set(i, j, s_VIDE);

    }

    public int get(int _i, int _j){
        return m_tableau[_i][_j];
    }

    public void set(int _i, int _j, int _val){
        if(get(_i, _j) != _val) {
            m_tableau[_i][_j] = _val;
            if (m_swingWorker != null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m_swingWorker.myPublish(new Quadruplet(_i, _j, _val,m_largeur));
            }
        }
    }

    //--------------------------------------------------------------------------
    //------                     Phases de résolution                     ------
    //--------------------------------------------------------------------------
    //phase 0, mise en place des espaces par défaut
    public void phase0() {
        for (int i = 0; i < m_hauteur; i++) {
            ArrayList<Integer> ligne = m_lignes.get(i);
            int result[] = new int[m_largeur];
            int idx = 0;
            for (int val : ligne) {
                for (int j = 0; j < val; j++)
                    result[idx + j] = s_PLEIN;
                idx += val + 1;
            }
            idx--;
            int delta = m_largeur - idx;
            if (delta == 0) {
                for (int j = 0; j < m_largeur; j++) {
                    if (result[j] == s_VIDE) {
                        result[j] = s_BLOQUE;
                    }
                }
            } else {
                int idxDebut = 0;
                boolean debut = true;
                for (int j = 0; j < m_largeur; j++) {
                    if (debut && result[j] == s_PLEIN) {
                        idxDebut = j;
                        debut = false;
                    } else if (!debut && result[j] == s_VIDE) {
                        for (int k = idxDebut; k < delta + idxDebut && k < j; k++)
                            result[k] = s_VIDE;
                        debut = true;
                    }
                }
            }
            for (int j = 0; j < m_largeur; j++) {
                if (result[j] != s_VIDE) {
                    set(i, j, result[j]);
                }
            }
        }

        for (int j = 0; j < m_largeur; j++) {
            ArrayList<Integer> colonne = m_colonnes.get(j);
            int result[] = new int[m_hauteur];
            int idx = 0;
            for (int val : colonne) {
                for (int i = 0; i < val; i++)
                    result[idx + i] = s_PLEIN;
                idx += val + 1;
            }
            idx--;
            int delta = m_hauteur - idx;
            if (delta == 0) {
                for (int i = 0; i < m_hauteur; i++) {
                    if (result[i] == s_VIDE) {
                        result[i] = s_BLOQUE;
                    }
                }
            } else {
                int idxDebut = 0;
                boolean debut = true;
                for (int i = 0; i < m_hauteur; i++) {
                    if (debut && result[i] == s_PLEIN) {
                        idxDebut = i;
                        debut = false;
                    } else if (!debut && result[i] == s_VIDE) {
                        for (int k = idxDebut; k < delta + idxDebut && k < i; k++)
                            result[k] = s_VIDE;
                        debut = true;
                    }
                }
            }
            for (int i = 0; i < m_hauteur; i++) {
                if (result[i] != s_VIDE) {
                    set(i, j, result[i]);
                }
            }
        }
    }

    //phase de remplissage des bordures
    public void phaseBordure(){
        boolean modifie = false;
        int i,j,vide,debut,trouve;

        //pour chaque ligne
        for (i = 0; i < m_hauteur; i++) {

            //region debut de la ligne
            j = 0;
            debut = j;
            for (Integer num : m_lignes.get(i)) {
                trouve = 0;
                vide = 0;
                for (int idx = 0; idx < num; idx++) {
                    if(idx == 0)
                        debut = j;
                    switch (get(i, j)) {
                        case s_VIDE:
                            if (trouve != 0) {
                                set(i, j, s_PLEIN);
                                modifie = true;
                            }else {
                                vide++;
                            }
                            break;
                        case s_PLEIN:
                            trouve++;
                            break;
                        case s_BLOQUE:
                            //si bloqué, impossible de caser le segment
                            for(int k = debut; k < j; k++)
                                set(i, k, s_BLOQUE);
                            if(j!=debut)
                                modifie = true;
                            idx = -1;
                            vide = 0;
                            break;
                    }
                    j++;
                }
                while(j < m_largeur && get(i, j) == s_PLEIN){
                    set(i, debut, s_BLOQUE);
                    modifie = true;
                    j++;
                }
                if (j < m_largeur && vide == 0) {
                    if(get(i, j) != s_BLOQUE) {
                        set(i, j, s_BLOQUE);
                        modifie = true;
                    }
                    j++;

                } else {
                    break;
                }
            }
            //endregion

            //region fin de la ligne
            j = m_largeur-1;
            debut = j;
            for (Integer num : m_Tlignes.get(i)) {
                trouve = 0;
                vide = 0;
                for (int idx = 0; idx < num; idx++) {
                    if(idx == 0)
                        debut = j;
                    switch (get(i, j)) {
                        case s_VIDE:
                            if (trouve != 0) {
                                set(i, j, s_PLEIN);
                                modifie = true;
                            } else {
                                vide++;
                            }
                            break;
                        case s_PLEIN:
                            trouve++;
                            break;
                        case s_BLOQUE:
                            //si bloqué, impossible de caser le segment
                            for(int k = debut; k > j; k--)
                                set(i, k, s_BLOQUE);
                            if(j!=debut)
                                modifie = true;
                            idx = -1;
                            vide = 0;
                            break;
                    }
                    j--;
                }
                while(j >= 0 && get(i, j) == s_PLEIN){
                    set(i, debut, s_BLOQUE);
                    modifie = true;
                    j--;
                }
                if (j >= 0 && vide == 0) {
                    if(get(i, j) != s_BLOQUE) {
                        set(i, j, s_BLOQUE);
                        modifie = true;
                    }
                    j--;
                } else {
                    break;
                }
            }
            //endregion
        }

        //pour chaque colonne
        for (j = 0; j < m_largeur; j++) {

            //region debut de la colonne
            i = 0;
            debut = i;
            for (Integer num : m_colonnes.get(j)) {
                trouve = 0;
                vide = 0;
                for (int idx = 0; idx < num; idx++) {
                    if(idx == 0)
                        debut = i;
                    switch (get(i, j)) {
                        case s_VIDE:
                            if (trouve != 0) {
                                set(i, j, s_PLEIN);
                                modifie = true;
                            }else{
                                vide ++;
                            }
                            break;
                        case s_PLEIN:
                            trouve++;
                            break;
                        case s_BLOQUE:
                            //si bloqué, impossible de caser le segment
                            for(int k = debut; k < i; k++)
                                set(k, j, s_BLOQUE);
                            if(i!=debut)
                                modifie = true;
                            idx = -1;
                            vide = 0;
                            break;
                    }
                    i++;
                }
                while(i < m_hauteur && get(i, j) == s_PLEIN){
                    set(debut, j, s_BLOQUE);
                    modifie = true;
                    i++;
                }
                if (i < m_hauteur && vide == 0) {
                    if(get(i, j) != s_BLOQUE) {
                        set(i, j, s_BLOQUE);
                        modifie = true;
                    }
                    i++;
                } else {
                    break;
                }
            }
            //endregion

            //region fin de la colonne
            i = m_hauteur-1;
            debut = i;
            for (Integer num : m_Tcolonnes.get(j)) {
                trouve = 0;
                vide = 0;
                for (int idx = 0; idx < num; idx++) {
                    if(idx == 0)
                        debut = i;
                    switch (get(i, j)) {
                        case s_VIDE:
                            if (trouve != 0) {
                                set(i, j, s_PLEIN);
                                modifie = true;
                            }else {
                                vide++;
                            }
                            break;
                        case s_PLEIN:
                            trouve++;
                            break;
                        case s_BLOQUE:
                            //si bloqué, impossible de caser le segment
                            for(int k = debut; k > i; k--)
                                set(k, j, s_BLOQUE);
                            if(i!=debut)
                                modifie = true;
                            idx = -1;
                            vide = 0;
                            break;
                    }
                    i--;
                }
                while(i >= 0 && get(i, j) == s_PLEIN){
                    set(debut, j, s_BLOQUE);
                    modifie = true;
                    i--;
                }
                if (i >= 0 && vide == 0) {
                    if(get(i, j) != s_BLOQUE) {
                        set(i, j, s_BLOQUE);
                        modifie = true;
                    }
                    i--;
                } else {
                    break;
                }
            }
            //endregion
        }

        //Réentrance si modification durant la phase
        if(modifie)
            phaseBordure();
    }

    //phase de remplissage avec les combinaisons gagnantes
    public void phasePossible(){
        ArrayList<Segment> espacesLibre;
        ArrayList<Integer> resteAPlacer;
        boolean debut;
        int idxDebut = 0;

        //region Pour chaque ligne
        for (int i = 0; i < m_hauteur; i++) {
            ArrayList<Integer> ligne = m_lignes.get(i);
            espacesLibre = new ArrayList<>();
            debut = false;
            for (int j = 0; j < m_largeur; j++){
                if(!debut && get(i, j) != s_BLOQUE){
                    debut = true;
                    idxDebut = j;
                }else if(debut && get(i, j) == s_BLOQUE){
                    debut = false;
                    espacesLibre.add(new Segment(idxDebut,j-1));
                }
            }
            if(debut){
                espacesLibre.add(new Segment(idxDebut,m_largeur-1));
            }

            ArrayList<Segment> ligneARemplir = Outils.combinaisonGagnante(espacesLibre, ligne);
            remplirLigne(ligneARemplir, i);
        }
        //endregion

        //region Pour chaque colonne
        for (int j = 0; j < m_largeur; j++) {
            ArrayList<Integer> colonne = m_colonnes.get(j);
            espacesLibre = new ArrayList<>();
            debut = false;
            for (int i = 0; i < m_hauteur; i++){
                if(!debut && get(i, j) != s_BLOQUE){
                    debut = true;
                    idxDebut = i;
                }else if(debut && get(i, j) == s_BLOQUE){
                    debut = false;
                    espacesLibre.add(new Segment(idxDebut,i-1));
                }
            }
            if(debut){
                espacesLibre.add(new Segment(idxDebut,m_hauteur-1));
            }

            ArrayList<Segment> colonneARemplir = Outils.combinaisonGagnante(espacesLibre, colonne);
            remplirColonne(colonneARemplir, j);
        }
        //endregion
    }

    //phase de comptage
    public void phaseComptage(){
        boolean modifie = false;
        for(int i = 0; i < m_hauteur;i++){
            if(verifLigne(i)){
                for(int j = 0; j < m_largeur;j++){
                    if(get(i, j) == s_VIDE){
                        modifie = true;
                        set(i, j, s_BLOQUE);
                    }
                }
            }
        }
        for (int j = 0; j < m_largeur; j++) {
            if(verifColonne(j)){
                for(int i = 0; i < m_hauteur;i++){
                    if(get(i, j) == s_VIDE){
                        modifie = true;
                        set(i, j, s_BLOQUE);
                    }
                }
            }
        }

        if(modifie)
            phaseComptage();
    }

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

    public boolean moindebile(int _i, int _j){
        if(_i == (m_hauteur-1)){
            //System.out.println("fin de colonne : "+_i +" "+_j);
            set(_i, _j, s_PLEIN);
            if(!verifColonne(_j)){
                set(_i, _j, s_VIDE);
                if(!verifColonne(_j)) return false;
            }
        }
        if(_j == (m_largeur-1)){

            //System.out.println("fin de ligne : "+_i +" "+_j);
            set(_i, _j, s_PLEIN);
            if(!verifLigne(_i)){
                set(_i, _j, s_VIDE);
                if(!verifLigne(_i)) return false;
            }
            if(_i == (m_hauteur-1)) return true;
        }

        int i1 = (_j == (m_largeur-1))? _i+1 : _i;
        int j1 = (_j+1)%m_largeur;
        set(_i, _j, s_PLEIN);
        if(moindebile(i1,j1)) return true;
        set(_i, _j, s_VIDE);
        return moindebile(i1,j1);
    }

    //--------------------------------------------------------------------------
    //------                  Outils de gestion des Grid                  ------
    //--------------------------------------------------------------------------
    private boolean verifLigne(int _i){
        return Outils.compareArray(recupLigne(_i),m_lignes.get(_i));
    }

    private boolean verifColonne(int _j){
        return Outils.compareArray(recupColonne(_j),m_colonnes.get(_j));
    }

    public boolean verification(){
        for(int i = 0; i < m_hauteur;i++){
            if(!verifLigne(i))
                return false;
        }
        for (int j = 0; j < m_largeur; j++) {
            if(!verifColonne(j))
                return false;
        }
        for(int i = 0; i < m_hauteur;i++){
            for(int j = 0; j < m_largeur; j++){
                switch(get(i, j)){
                    case s_PLEIN :
                        set(i, j, s_FINAL);
                        break;
                    case s_BLOQUE :
                        set(i, j, s_VIDE);
                        break;
                }
            }
        }
        return true;
    }

    private ArrayList<Integer> recupLigne(int _i){
        int idxDebut = 0;
        boolean debut = true;
        ArrayList<Integer> ligne = new ArrayList<>();
        for (int j = 0; j < m_largeur; j++) {
            if (debut && get(_i, j) == s_PLEIN) {
                idxDebut = j;
                debut = false;
            } else if (!debut && get(_i, j) != s_PLEIN) {
                ligne.add(j-idxDebut);
                debut = true;
            }
        }
        if(!debut){
            ligne.add(m_largeur - idxDebut);
        }
        return ligne;
    }

    private ArrayList<Integer> recupColonne(int _j){
        int idxDebut = 0;
        boolean debut = true;
        ArrayList<Integer> colonne = new ArrayList<>();
        for(int i = 0; i < m_hauteur; i++){
            if(debut && get(i, _j) == s_PLEIN){
                idxDebut = i;
                debut = false;
            }else if(!debut && get(i, _j) != s_PLEIN){
                colonne.add(i-idxDebut);
                debut = true;
            }
        }
        if(!debut){
            colonne.add(m_hauteur - idxDebut);
        }
        return colonne;
    }

    private void remplirLigne(ArrayList<Segment> _liste, int _i) {
        if (_liste != null) {
            int j = 0;
            for (Segment seg : _liste) {
                for (j = seg.m_debut; j <= seg.m_fin; j++)
                    set(_i, j, s_PLEIN);
            }
        }
    }

    private void remplirColonne(ArrayList<Segment> _liste, int _j) {
        if (_liste != null) {
            int i = 0;
            for (Segment seg : _liste) {
                for (i = seg.m_debut; i <= seg.m_fin; i++)
                    set(i, _j, s_PLEIN);
            }
        }
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
