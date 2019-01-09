package GUI;

import javax.swing.*;
import Data.LogicPicGrid;

import java.awt.*;

import static Data.LogicPicGrid.s_BLOQUE;
import static Data.LogicPicGrid.s_FINAL;
import static Data.LogicPicGrid.s_PLEIN;

public class MainFrame extends JFrame{

    public static MainFrame s_singleton = null;

    private MainFrame(){}

    public static void init(){
        MainFrame result = new MainFrame();
        int largeur = LogicPicGrid.getSingleton().m_largeur;
        int hauteur = LogicPicGrid.getSingleton().m_hauteur;

        result.setTitle("LogicPicSolver");
        result.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        result.setLayout(new GridLayout(hauteur, largeur));
        for (int i = 0; i < hauteur * largeur; i++) {
            JPanel panel = new JPanel();
            panel.setSize(60, 60);
            panel.setBorder(BorderFactory.createLineBorder(Color.black));
            panel.setBackground(Color.WHITE);
            result.getContentPane().add(panel);
        }

        result.pack();
        result.setVisible(true);
        result.setLocationRelativeTo(null);


        s_singleton = result;
    }

    public static MainFrame getSingleton(){
        return s_singleton;
    }

    public void refresh(){
        int largeur = LogicPicGrid.getSingleton().m_largeur;
        int hauteur = LogicPicGrid.getSingleton().m_hauteur;
        int tableau [][] = LogicPicGrid.getSingleton().m_tableau;

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                JPanel panel = (JPanel)getContentPane().getComponent(i*largeur+j);
                switch (tableau[i][j]){
                    case s_PLEIN :
                        panel.setBackground(Color.BLACK);
                        break;
                    case s_BLOQUE :
                        panel.setBackground(Color.CYAN);
                        break;
                    case s_FINAL :
                        panel.setBackground(Color.GREEN);
                        break;
                    default :
                        panel.setBackground(Color.WHITE);
                        break;
                }
            }
        }
        validate();
    }

}
