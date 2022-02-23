import javax.swing.*;

public class MyTimer implements Runnable {

    private static long nextId = 1;

    private final long id;
    private long startTime;
    private long endTime;
    private JLabel jLabel;
    private boolean finished = false;

    public MyTimer(JLabel jLabel) {
        this.id = nextId;
        nextId++;
        this.jLabel = jLabel;
    }

    public void run() {
        startTime = System.currentTimeMillis();
        while (!finished) {
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

        System.out.println("Timer thread ended. id= " + Thread.currentThread().getId());
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getId() {
        return id;
    }
}
