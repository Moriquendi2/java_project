/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.ui;

import java.awt.Color;
import java.awt.Font;

/** Shared palette and typography for the Swing control-center shell.
 * 中文：集中定义 Swing 界面的颜色和字体样式。
 */
final class UiTheme {

    static final Color BACKGROUND = new Color(241, 245, 249);
    static final Color SURFACE = Color.WHITE;
    static final Color INK = new Color(23, 43, 69);
    static final Color MUTED = new Color(115, 132, 155);
    static final Color LINE = new Color(220, 229, 239);
    static final Color BLUE = new Color(36, 104, 220);
    static final Color GREEN = new Color(21, 156, 113);
    static final Color CYAN = new Color(18, 164, 179);
    static final Color AMBER = new Color(216, 144, 36);

    static final String FONT_FAMILY = "Microsoft YaHei UI";
    static final Font SMALL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 11);

    /**
     * 中文：阻止创建主题常量工具类的实例。
     */
    private UiTheme() {
    }
}
