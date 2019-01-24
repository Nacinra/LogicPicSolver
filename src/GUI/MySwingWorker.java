package GUI;

import Data.LogicPicGrid;
import Data.Quadruplet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static Data.LogicPicGrid.s_BLOQUE;
import static Data.LogicPicGrid.s_FINAL;
import static Data.LogicPicGrid.s_PLEIN;

/**
 * Same as SwingWorker but providing a public publish function
 */
public class MySwingWorker extends SwingWorker<Void, Quadruplet> {

    InterfaceAction m_interface;

    private  MySwingWorker(){};
    public  MySwingWorker(InterfaceAction _int){
        m_interface = _int;
    }

    @Override
    protected Void doInBackground() throws Exception {
        LogicPicGrid.getSingleton().m_swingWorker = this;
        m_interface.methode();
        LogicPicGrid.getSingleton().m_swingWorker = null;
        return null;
    }

    @Override
    protected void process(List<Quadruplet> chunks) {
        for(Quadruplet quad : chunks) {
            JPanel panel = (JPanel)MainFrame.getSingleton().getContentPane().getComponent(quad.i * quad.lar + quad.j);
            switch (quad.val) {
                case s_PLEIN:
                    panel.setBackground(Color.BLACK);
                    break;
                case s_BLOQUE:
                    panel.setBackground(Color.CYAN);
                    break;
                case s_FINAL:
                    panel.setBackground(Color.GREEN);
                    break;
                default:
                    panel.setBackground(Color.WHITE);
                    break;
            }
        }
    }

    public final void myPublish(Quadruplet _args) {
        publish(_args);
    }
}
