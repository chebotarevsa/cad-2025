import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

/** Общие цвета и настройка внешнего вида (в тон веб-версии приложения). */
final class UiStyle {
    static final Color PINK = new Color(0xFF69B4);
    static final Color PINK_DARK = new Color(0xC71585);
    static final Color PINK_LIGHT = new Color(0xFFE4EC);
    static final Color ROW_ALT = new Color(0xFFF0F5);
    static final Color TEXT = new Color(0x4A2C3A);
    static final Color GREEN = new Color(0x2E7D32);
    static final Color ORANGE = new Color(0xE65100);
    static final Color GRAY = new Color(0x9E9E9E);

    static final Font BASE = new Font("SansSerif", Font.PLAIN, 14);
    static final Font BOLD = BASE.deriveFont(Font.BOLD);

    private UiStyle() {
    }

    static void install() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // останется стандартный внешний вид
        }
        UIManager.put("nimbusBase", PINK_DARK);
        UIManager.put("nimbusSelectionBackground", PINK);
        UIManager.put("control", PINK_LIGHT);
        UIManager.put("defaultFont", BASE);
        // подписи кнопок стандартных диалогов не зависят от языка системы
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
    }
}
