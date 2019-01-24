package GUI;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Boutton extends JButton{


        public Boutton(String _name, InterfaceAction _int){
            super(_name);
            addMouseListener(new java.awt.event.MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    MySwingWorker task = new MySwingWorker(_int);
                    task.execute();
                }
            });
        }
}
