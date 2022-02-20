import javax.swing.*;

public class MyTimer implements Runnable {

    long startTime;
    long endTime;
    JLabel jLabel;
    boolean end = false;

    public MyTimer(JLabel jLabel) {

        this.jLabel = jLabel;
    }

    public void run() {
        try {
            startTime = System.currentTimeMillis();
            while (!end) {
                //Thread.sleep(500);
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
            endTime = System.currentTimeMillis();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Timer thread ended " + Thread.currentThread().getId());
    }


    public String getTimeElapsed() {
        long timeInMillis = endTime - startTime;
        double timeInSeconds = (double) timeInMillis / 1000;
        String str ;
        if (timeInSeconds < 60.0) {
            str = "0:" + (int) timeInSeconds;
        } else {
            int n = (int) timeInSeconds / 60;

            int minutes = (int) timeInSeconds - 60 * n;
            int seconds = (int) timeInSeconds % 60;

            str =  minutes + ":" + seconds;
        }
        return str;
    }
}
