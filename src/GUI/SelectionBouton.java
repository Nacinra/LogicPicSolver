package GUI;

import javax.swing.*;
import Data.LogicPicGrid;
import sun.rmi.runtime.Log;

public class SelectionBouton extends JFrame {

    public static SelectionBouton s_singleton = null;
    private JPanel mainPanel = null;

    private SelectionBouton(){}

    public static void init(){
        SelectionBouton result = new SelectionBouton();
        result.mainPanel = new JPanel();
        result.setTitle("Boutons de selections");
        result.setSize(100, 600);
        result.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        result.setContentPane(result.mainPanel);

        //Boutons des différentes phases
        result.mainPanel.add(new Boutton("Phase Bordure",() -> LogicPicGrid.getSingleton().phaseBordure()));
        result.mainPanel.add(new Boutton("Phase Possible",() -> LogicPicGrid.getSingleton().phasePossible()));
        result.mainPanel.add(new Boutton("Phase Comptage",() -> LogicPicGrid.getSingleton().phaseComptage()));
        result.mainPanel.add(new Boutton("Magie", () -> LogicPicGrid.getSingleton().magie()));

        //Boutons des gestion
        result.mainPanel.add(new Boutton("Vérification",() -> LogicPicGrid.getSingleton().verification()));
        result.mainPanel.add(new Boutton("Reset",() -> LogicPicGrid.getSingleton().reset()));
        result.mainPanel.add(new Boutton("Changement Grille",() -> {
                    MainFrame.getSingleton().dispose();
                    LogicPicGrid.destroy();
                    LogicPicGrid.init();
                    MainFrame.init();
        }));

        //Boutons débiles
        result.mainPanel.add(new Boutton("Debile",() -> LogicPicGrid.getSingleton().debile(0,0)));
        result.mainPanel.add(new Boutton("Moin Debile",() -> LogicPicGrid.getSingleton().moindebile(0,0)));

        result.setVisible(true);
        s_singleton = result;
    }

    public static SelectionBouton getSingleton() {
        return s_singleton;
    }
}
