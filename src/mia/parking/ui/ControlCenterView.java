/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.Border;
import mia.parking.core.ParkingControlService;
import mia.parking.core.domain.DashboardSnapshot;
import mia.parking.core.domain.Incident;
import mia.parking.core.domain.ZoneSnapshot;

/** Main navigation shell and lightweight, non-functional dashboard preview.
 * 中文：构建控制中心 Swing 导航、仪表盘和模块占位页面。
 */
public final class ControlCenterView extends JPanel {

    /**
     * 中文：保存控制中心页面的导航标识和本地化文案键。
     */
    private record Module(String id, String titleKey, String descriptionKey) { }

    /**
     * 中文：将负责成员标签与其连续导航模块组合在一起。
     */
    private record ModuleGroup(List<Module> modules) { }

    private static final List<ModuleGroup> MODULE_GROUPS = List.of(
            new ModuleGroup(List.of(
                    new Module("dashboard", "nav.dashboard", "nav.dashboard.description"),
                    new Module("spaces", "nav.spaces", "nav.spaces.description"),
                    new Module("devices", "nav.devices", "nav.devices.description"),
                    new Module("simulation", "nav.simulation", "nav.simulation.description"))),
            new ModuleGroup(List.of(
                    new Module("vehicles", "nav.vehicles", "nav.vehicles.description"),
                    new Module("security", "nav.security", "nav.security.description"),
                    new Module("backup", "nav.backup", "nav.backup.description"))),
            new ModuleGroup(List.of(
                    new Module("fees", "nav.fees", "nav.fees.description"),
                    new Module("users", "nav.users", "nav.users.description"),
                    new Module("logs", "nav.logs", "nav.logs.description"))));

    private static final List<Module> MODULES = MODULE_GROUPS.stream()
            .flatMap(group -> group.modules().stream())
            .toList();

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pages = new CardHost(pageLayout);
    private final Map<String, JButton> navigationButtons = new HashMap<>();
    private final ParkingControlService service;
    private DashboardSnapshot snapshot;
    private JPanel dashboardPanel;
    private String selectedModuleId = "dashboard";
    private UiLanguage language = UiLanguage.CHINESE;

    /**
     * 中文：初始化仪表盘、导航、语言控件和刷新计时器。
     */
    public ControlCenterView(ParkingControlService service) {
        this.service = service;
        snapshot = service.getDashboardSnapshot();
        setLayout(new BorderLayout());
        setBackground(UiTheme.BACKGROUND);
        rebuildInterface();
        new Timer(1000, event -> refreshDashboard()).start();
    }

    /**
     * 中文：按当前语言重新构建控制中心界面。
     */
    private void rebuildInterface() {
        removeAll();
        navigationButtons.clear();
        pages.removeAll();
        add(buildTop(), BorderLayout.NORTH);
        for (Module module : MODULES) {
            if ("dashboard".equals(module.id())) {
                dashboardPanel = buildDashboard(module);
                pages.add(dashboardPanel, module.id());
            } else {
                pages.add(buildPlaceholder(module), module.id());
            }
        }

        JScrollPane content = new JScrollPane(pages);
        content.setBorder(BorderFactory.createEmptyBorder());
        content.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        content.getViewport().setBackground(UiTheme.BACKGROUND);
        add(content, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        showModule(selectedModuleId);
        revalidate();
        repaint();
    }

    /**
     * 中文：组合页面主体、顶部区域、导航栏和页脚。
     */
    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UiTheme.SURFACE);
        top.add(buildHeader(), BorderLayout.NORTH);
        top.add(buildNavigation(), BorderLayout.CENTER);
        return top;
    }

    /**
     * 中文：构建品牌标识、语言切换和页面标题区域。
     */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setBackground(UiTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.LINE),
                BorderFactory.createEmptyBorder(12, 28, 12, 28)));

        JLabel mark = new JLabel("M", SwingConstants.CENTER);
        mark.setOpaque(true);
        mark.setBackground(UiTheme.BLUE);
        mark.setForeground(Color.WHITE);
        mark.setFont(new java.awt.Font(UiTheme.FONT_FAMILY, java.awt.Font.BOLD, 19));
        mark.setPreferredSize(new Dimension(38, 38));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel brandTitle = label(text("brand.title"), 15, UiTheme.INK, true);
        JLabel brandSubtitle = label(text("brand.subtitle"), 9, UiTheme.MUTED, false);
        brand.add(brandTitle);
        brand.add(Box.createVerticalStrut(2));
        brand.add(brandSubtitle);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(mark);
        left.add(brand);

        JLabel statusDot = label("●", 11, UiTheme.GREEN, true);
        JLabel status = label(text("status.demo"), 11, UiTheme.MUTED, false);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        right.setOpaque(false);
        right.add(statusDot);
        right.add(status);
        ButtonGroup languageGroup = new ButtonGroup();
        right.add(languageToggle(UiLanguage.CHINESE, "中文", languageGroup));
        right.add(languageToggle(UiLanguage.ENGLISH, "English", languageGroup));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    /**
     * 中文：创建一个语言选项按钮并加入按钮组。
     */
    private JToggleButton languageToggle(UiLanguage choice, String caption, ButtonGroup group) {
        JToggleButton button = new JToggleButton(caption, language == choice);
        button.setFont(UiTheme.SMALL_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        button.setForeground(language == choice ? UiTheme.BLUE : UiTheme.MUTED);
        button.setBackground(language == choice ? new Color(246, 249, 255) : UiTheme.SURFACE);
        group.add(button);
        button.addActionListener(event -> {
            if (button.isSelected() && language != choice) {
                language = choice;
                rebuildInterface();
            }
        });
        return button;
    }

    /**
     * 中文：构建可横向滚动的模块导航栏。
     */
    private JScrollPane buildNavigation() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(UiTheme.SURFACE);
        row.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));
        row.setPreferredSize(new Dimension(1160, 52));

        for (int index = 0; index < MODULE_GROUPS.size(); index++) {
            ModuleGroup group = MODULE_GROUPS.get(index);
            JPanel groupPanel = new JPanel(new BorderLayout());
            groupPanel.setBackground(UiTheme.SURFACE);
            if (index > 0) {
                groupPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 1, 0, 0, UiTheme.LINE),
                        BorderFactory.createEmptyBorder(0, 12, 0, 0)));
            }

            JPanel buttons = new JPanel(new GridLayout(1, group.modules().size(), 2, 0));
            buttons.setBackground(UiTheme.SURFACE);
            for (Module module : group.modules()) {
                JButton button = new JButton(text(module.titleKey()));
                button.setFont(UiTheme.SMALL_BOLD);
                button.setFocusPainted(false);
                button.setContentAreaFilled(true);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UiTheme.SURFACE));
                button.addActionListener(event -> showModule(module.id()));
                navigationButtons.put(module.id(), button);
                buttons.add(button);
            }
            groupPanel.add(buttons, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = index;
            constraints.gridy = 0;
            constraints.weightx = (double) group.modules().size() / MODULES.size();
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            row.add(groupPanel, constraints);
        }

        JScrollPane navigation = new JScrollPane(row,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        navigation.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.LINE));
        navigation.getViewport().setBackground(UiTheme.SURFACE);
        navigation.getHorizontalScrollBar().setUnitIncrement(24);
        return navigation;
    }

    /**
     * 中文：构建控制中心底部说明区域。
     */
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 9));
        footer.setBackground(UiTheme.SURFACE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.LINE));
        footer.add(label(text("footer.brand"), 10, UiTheme.MUTED, true));
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 14));
        footer.add(separator);
        footer.add(label(text("footer.note"), 10, UiTheme.MUTED, false));
        return footer;
    }

    /**
     * 中文：切换当前页面并更新导航按钮样式。
     */
    private void showModule(String moduleId) {
        selectedModuleId = moduleId;
        pageLayout.show(pages, moduleId);
        navigationButtons.forEach((id, button) -> styleNavigationButton(button, id.equals(moduleId)));
    }

    /**
     * 中文：刷新业务快照并重建仪表盘页面。
     */
    private void refreshDashboard() {
        snapshot = service.getDashboardSnapshot();
        pages.remove(dashboardPanel);
        dashboardPanel = buildDashboard(MODULES.getFirst());
        pages.add(dashboardPanel, MODULES.getFirst().id());
        pageLayout.show(pages, selectedModuleId);
        pages.revalidate();
        pages.repaint();
    }

    /**
     * 中文：根据按钮选中状态应用对应颜色和边框。
     */
    private void styleNavigationButton(JButton button, boolean selected) {
        button.setBackground(selected ? new Color(246, 249, 255) : UiTheme.SURFACE);
        button.setForeground(selected ? UiTheme.BLUE : UiTheme.MUTED);
        button.setBorder(BorderFactory.createMatteBorder(
                0, 0, 3, 0, selected ? UiTheme.BLUE : UiTheme.SURFACE));
    }

    /**
     * 中文：组合页面标题、指标卡片和运营面板。
     */
    private JPanel buildDashboard(Module module) {
        JPanel page = pageContainer();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(buildPageHeading(module));
        page.add(Box.createVerticalStrut(22));
        page.add(buildMetrics());
        page.add(Box.createVerticalStrut(16));
        page.add(buildOperationsPanels());
        return page;
    }

    /**
     * 中文：创建控制中心页面的共用容器。
     */
    private JPanel pageContainer() {
        JPanel page = new JPanel();
        page.setOpaque(true);
        page.setBackground(UiTheme.BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(26, 34, 32, 34));
        return page;
    }

    /**
     * 中文：构建模块标题及说明区域。
     */
    private JPanel buildPageHeading(Module module) {
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(label(text("page.eyebrow"), 10, new Color(111, 131, 160), true));
        heading.add(Box.createVerticalStrut(7));
        heading.add(label(text(module.titleKey()), 25, UiTheme.INK, true));
        heading.add(Box.createVerticalStrut(5));
        heading.add(label(text(module.descriptionKey()), 12, UiTheme.MUTED, false));
        return heading;
    }

    /**
     * 中文：构建占用、空闲、会话和告警摘要卡片。
     */
    private JPanel buildMetrics() {
        JPanel metrics = new JPanel(new GridLayout(1, 4, 14, 0));
        metrics.setOpaque(false);
        int totalSpaces = snapshot.totalSpaces();
        int availableSpaces = snapshot.availableSpaces();
        double occupancy = totalSpaces == 0 ? 0 : (totalSpaces - availableSpaces) * 100.0 / totalSpaces;
        long openIncidents = snapshot.recentIncidents().stream().filter(incident -> !incident.resolved()).count();
        metrics.add(metric(text("metric.occupancy"), String.format(java.util.Locale.ROOT, "%.1f%%", occupancy),
                text("metric.occupancy.note"), UiTheme.BLUE));
        metrics.add(metric(text("metric.available"), Integer.toString(availableSpaces),
                text("metric.available.note"), UiTheme.GREEN));
        metrics.add(metric(text("metric.sessions"), Integer.toString(snapshot.activeSessions()),
                text("metric.sessions.note"), UiTheme.CYAN));
        metrics.add(metric(text("metric.alerts"), Long.toString(openIncidents),
                text("metric.alerts.note"), UiTheme.AMBER));
        return metrics;
    }

    /**
     * 中文：构建展示标题、数值和说明的指标卡片。
     */
    private JPanel metric(String title, String value, String note, Color accent) {
        JPanel card = new JPanel();
        card.setBackground(UiTheme.SURFACE);
        Border accentBorder = BorderFactory.createMatteBorder(0, 3, 0, 0, accent);
        Border contentBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(13, 15, 13, 15));
        card.setBorder(BorderFactory.createCompoundBorder(accentBorder, contentBorder));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(label(title, 11, UiTheme.MUTED, true));
        card.add(Box.createVerticalStrut(8));
        card.add(label(value, 25, UiTheme.INK, true));
        card.add(Box.createVerticalStrut(5));
        card.add(label(note, 10, new Color(139, 153, 172), false));
        return card;
    }

    /**
     * 中文：组合分区概览面板和事件面板。
     */
    private JPanel buildOperationsPanels() {
        JPanel panels = new JPanel(new GridLayout(1, 2, 16, 0));
        panels.setOpaque(false);
        panels.add(buildZonePanel());
        panels.add(buildEventsPanel());
        return panels;
    }

    /**
     * 中文：构建分区占用情况及状态图例。
     */
    private JPanel buildZonePanel() {
        JPanel panel = panelColumn();
        panel.add(panelHeading(text("panel.zones"), text("panel.zones.subtitle")));
        panel.add(Box.createVerticalStrut(18));
        for (int index = 0; index < snapshot.zones().size(); index++) {
            ZoneSnapshot zone = snapshot.zones().get(index);
            int occupancy = zone.occupancyPercent();
            Color color = occupancy >= 80 ? UiTheme.AMBER : occupancy >= 50 ? UiTheme.BLUE : UiTheme.GREEN;
            panel.add(zoneRow(text("zone.prefix") + zone.zoneId(),
                    text("zone.spaces").formatted(zone.totalSpaces()),
                    occupancy, color));
            if (index + 1 < snapshot.zones().size()) {
                panel.add(Box.createVerticalStrut(15));
            }
        }
        panel.add(Box.createVerticalStrut(20));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        legend.setOpaque(false);
        legend.add(legendItem(UiTheme.GREEN, text("legend.free")));
        legend.add(legendItem(UiTheme.BLUE, text("legend.allocated")));
        legend.add(legendItem(UiTheme.AMBER, text("legend.warning")));
        panel.add(legend);
        return panel;
    }

    /**
     * 中文：构建显示分区信息和占用进度的行组件。
     */
    private JPanel zoneRow(String name, String detail, int occupancy, Color color) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

        JPanel labels = new JPanel(new BorderLayout());
        labels.setOpaque(false);
        JLabel zoneName = label(name, 12, new Color(36, 59, 88), true);
        JLabel zoneDetail = label(detail, 10, new Color(135, 149, 168), false);
        JPanel zoneNameGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        zoneNameGroup.setOpaque(false);
        zoneNameGroup.add(zoneName);
        zoneNameGroup.add(Box.createHorizontalStrut(9));
        zoneNameGroup.add(zoneDetail);
        labels.add(zoneNameGroup, BorderLayout.WEST);
        labels.add(label(occupancy + "%", 12, new Color(36, 59, 88), true), BorderLayout.EAST);

        JProgressBar progress = new JProgressBar(0, 100);
        progress.setValue(occupancy);
        progress.setStringPainted(false);
        progress.setBorderPainted(false);
        progress.setForeground(color);
        progress.setBackground(new Color(237, 241, 246));
        progress.setPreferredSize(new Dimension(120, 8));
        progress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        row.add(labels);
        row.add(Box.createVerticalStrut(8));
        row.add(progress);
        return row;
    }

    /**
     * 中文：构建状态图例中的颜色标识项。
     */
    private JPanel legendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        JLabel dot = label("●", 10, color, true);
        item.add(dot);
        item.add(label(text, 10, UiTheme.MUTED, false));
        return item;
    }

    /**
     * 中文：构建近期事件列表或无事件时的示例内容。
     */
    private JPanel buildEventsPanel() {
        JPanel panel = panelColumn();
        panel.add(panelHeading(text("panel.events"), text("panel.events.subtitle")));
        panel.add(Box.createVerticalStrut(18));
        if (snapshot.recentIncidents().isEmpty()) {
            panel.add(eventRow("--:--", text("demo.started"), text("demo.sample.data"), UiTheme.GREEN));
        } else {
            for (int index = 0; index < snapshot.recentIncidents().size(); index++) {
                Incident incident = snapshot.recentIncidents().get(index);
                String time = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        .withZone(java.time.ZoneId.systemDefault()).format(incident.occurredAt());
                panel.add(eventRow(time, UiText.incidentTitle(language, incident.type()),
                        UiText.incidentMessage(language, incident), UiTheme.AMBER));
                if (index + 1 < snapshot.recentIncidents().size()) {
                    panel.add(new JSeparator());
                }
            }
        }
        return panel;
    }

    /**
     * 中文：构建包含时间、标题和详情的事件行。
     */
    private JPanel eventRow(String time, String title, String detail, Color color) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.add(label("●", 10, color, true), BorderLayout.WEST);

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(label(title, 11, new Color(52, 75, 103), true));
        copy.add(Box.createVerticalStrut(5));
        copy.add(label(detail, 10, new Color(132, 147, 167), false));
        row.add(copy, BorderLayout.CENTER);
        row.add(label(time, 10, new Color(135, 149, 168), false), BorderLayout.EAST);
        return row;
    }

    /**
     * 中文：创建采用共用样式的纵向面板。
     */
    private JPanel panelColumn() {
        JPanel panel = new JPanel();
        panel.setBackground(UiTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.LINE),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    /**
     * 中文：构建面板标题和副标题。
     */
    private JPanel panelHeading(String title, String subtitle) {
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(label(title, 14, new Color(36, 59, 88), true));
        heading.add(Box.createVerticalStrut(4));
        heading.add(label(subtitle, 9, new Color(134, 149, 169), true));
        return heading;
    }

    /**
     * 中文：构建尚未接入业务功能的占位页面。
     */
    private JPanel buildPlaceholder(Module module) {
        JPanel page = pageContainer();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(buildPageHeading(module));
        page.add(Box.createVerticalStrut(17));

        JPanel message = panelColumn();
        message.add(label(text("placeholder.kicker"), 10, UiTheme.BLUE, true));
        message.add(Box.createVerticalStrut(10));
        message.add(label(text("placeholder.title").formatted(text(module.titleKey())),
                15, new Color(36, 59, 88), true));
        message.add(Box.createVerticalStrut(7));
        message.add(label(text("placeholder.detail"),
                12, UiTheme.MUTED, false));
        page.add(message);
        return page;
    }

    /**
     * 中文：按指定大小、颜色和粗细创建文本标签。
     */
    private JLabel label(String text, int size, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new java.awt.Font(UiTheme.FONT_FAMILY, bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    /**
     * 中文：按当前语言读取本地化界面文案。
     */
    private String text(String key) {
        return UiText.text(language, key);
    }

    /**
     * 中文：为控制中心卡片页面提供滚动容器行为。
     */
    private static final class CardHost extends JPanel implements Scrollable {

        /**
         * 中文：创建采用统一背景样式的可滚动卡片容器。
         */
        private CardHost(CardLayout layout) {
            super(layout);
            setBackground(UiTheme.BACKGROUND);
        }

        /**
         * 中文：返回滚动容器首选可视区域尺寸。
         */
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(1100, 620);
        }

        /**
         * 中文：返回单步滚动距离。
         */
        @Override
        public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return 24;
        }

        /**
         * 中文：返回整页滚动距离。
         */
        @Override
        public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 24, 24);
        }

        /**
         * 中文：使卡片内容宽度跟随视口。
         */
        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        /**
         * 中文：使卡片内容高度保持首选尺寸。
         */
        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
