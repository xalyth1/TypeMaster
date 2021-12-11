import javax.swing.*;
import java.util.TimerTask;

public class MyTimer implements Runnable {

    long startTime;
    JLabel jLabel;
    boolean end = false;

    public MyTimer(JLabel jLabel) {
        startTime = System.currentTimeMillis();
        this.jLabel = jLabel;
    }

    public void run() {
        try {
            while (!end) {
                Thread.sleep(500);
                long time = System.currentTimeMillis() - startTime;
                long seconds = time / 1000;
                if (seconds < 60) {
                    jLabel.setText("    Time:  0:" + (seconds < 10 ? "0" + seconds : seconds));
                } else {
                    long minutes = seconds / 60;
                    seconds = seconds % 60;
                    jLabel.setText("    Time:  " + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds));
                }

                // make similar background task with WPMs
                //System.out.println("myTimer Time: " + jLabel.getText());
                jLabel.repaint();
                jLabel.updateUI();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Timer thread ended " + Thread.currentThread().getId());
    }
}
