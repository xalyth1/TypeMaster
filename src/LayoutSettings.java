import java.awt.*;

public class LayoutSettings {
    private static Font font = new Font("Segoe UI Semibold", Font.PLAIN, 24);
    private static Color defaultFontColor = new Color(187,187,187);
    private static Color backgroundColor = new Color(43,43,43);

    private static Color wrongLetterColor = new Color(240, 55,73);
    private static Color goodLetterColor = new Color(153,204,0);

    private static Color progressBarUnfinishedColor = wrongLetterColor;
    private static Color progressBarCorrectColor = goodLetterColor;

    public static final Color BUTTON_COLOR = new Color(80,80,80);
    public static final Color INPUT_PANEL_COLOR = new Color(60,60,60);
    public static final Color BUTTON_FOCUS_COLOR = new Color(100,100,100);


    public static final Font LABELS_PANEL_FONT = new Font("Segoe UI Semibold", Font.BOLD, 24);;

    public static Font getFont() {
        return font;
    }

    public static void setFont(Font font) {
        LayoutSettings.font = font;
    }

    public static Color getDefaultFontColor() {
        return defaultFontColor;
    }

    public static void setDefaultFontColor(Color defaultFontColor) {
        LayoutSettings.defaultFontColor = defaultFontColor;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Color backgroundColor) {
        LayoutSettings.backgroundColor = backgroundColor;
    }

    public static Color getWrongLetterColor() {
        return wrongLetterColor;
    }

    public static void setWrongLetterColor(Color wrongLetterColor) {
        LayoutSettings.wrongLetterColor = wrongLetterColor;
    }

    public static Color getGoodLetterColor() {
        return goodLetterColor;
    }

    public static void setGoodLetterColor(Color goodLetterColor) {
        LayoutSettings.goodLetterColor = goodLetterColor;
    }

    public static Color getProgressBarUnfinishedColor() {
        return progressBarUnfinishedColor;
    }

    public static void setProgressBarUnfinishedColor(Color progressBarUnfinishedColor) {
        LayoutSettings.progressBarUnfinishedColor = progressBarUnfinishedColor;
    }

    public static Color getProgressBarCorrectColor() {
        return progressBarCorrectColor;
    }

    public static void setProgressBarCorrectColor(Color progressBarCorrectColor) {
        LayoutSettings.progressBarCorrectColor = progressBarCorrectColor;
    }
}
