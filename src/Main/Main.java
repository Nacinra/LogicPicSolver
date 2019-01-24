package Main;

import Data.LogicPicGrid;
import GUI.MainFrame;
import GUI.SelectionBouton;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogicPicGrid.init();
            SelectionBouton.init();
            MainFrame.init();
        });
    }
}
