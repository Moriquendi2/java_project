/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.ui;

import java.util.Map;
import mia.parking.core.domain.Incident;

/** Small built-in text catalog for the two supported interface languages.
 * 中文：提供控制中心使用的中英文界面文案和事件提示。
 */
final class UiText {
    private static final Map<String, String> CHINESE = Map.ofEntries(
            Map.entry("brand.title", "MIA 停车运营中心"),
            Map.entry("brand.subtitle", "机场停车控制系统"),
            Map.entry("status.demo", "演示模式 · 内存服务已连接"),
            Map.entry("nav.dashboard", "可视化总览"),
            Map.entry("nav.dashboard.description", "查看停车场分区、运行指标与最新事件。"),
            Map.entry("nav.spaces", "分区与车位"),
            Map.entry("nav.spaces.description", "按区域查看车位布局、类别和状态。"),
            Map.entry("nav.vehicles", "车辆出入场"),
            Map.entry("nav.vehicles.description", "查看入口识别、车位分配与停车会话。"),
            Map.entry("nav.security", "安全管理"),
            Map.entry("nav.security.description", "集中查看告警并跟进巡逻处理。"),
            Map.entry("nav.fees", "收费结算"),
            Map.entry("nav.fees.description", "查看停车费用、支付与结算记录。"),
            Map.entry("nav.devices", "摄像头与传感器"),
            Map.entry("nav.devices.description", "查看闸机、摄像头和车位传感器。"),
            Map.entry("nav.users", "用户与订阅"),
            Map.entry("nav.users.description", "管理不同用户类别与订阅信息。"),
            Map.entry("nav.logs", "日志与统计"),
            Map.entry("nav.logs.description", "查询业务事件和停车场运行统计。"),
            Map.entry("nav.simulation", "仿真配置"),
            Map.entry("nav.simulation.description", "配置统一时钟与演示场景。"),
            Map.entry("nav.backup", "备份与恢复"),
            Map.entry("nav.backup.description", "查看缓存快照和运行状态恢复。"),
            Map.entry("footer.note", "基础框架 · 示例数据不代表实时状态"),
            Map.entry("footer.brand", "MIA 停车管理"),
            Map.entry("page.eyebrow", "停车运营"),
            Map.entry("metric.occupancy", "全场占用率"),
            Map.entry("metric.occupancy.note", "内存演示数据"),
            Map.entry("metric.available", "可用车位"),
            Map.entry("metric.available.note", "样例布局"),
            Map.entry("metric.sessions", "进行中会话"),
            Map.entry("metric.sessions.note", "当前停车会话"),
            Map.entry("metric.alerts", "待处理告警"),
            Map.entry("metric.alerts.note", "当前未处理"),
            Map.entry("panel.zones", "分区运行概况"),
            Map.entry("panel.zones.subtitle", "分区占用率"),
            Map.entry("panel.events", "最近事件"),
            Map.entry("panel.events.subtitle", "最新告警与事件"),
            Map.entry("zone.prefix", "分区 "),
            Map.entry("zone.spaces", "%d 个车位"),
            Map.entry("legend.free", "空闲"),
            Map.entry("legend.allocated", "已分配 / 占用"),
            Map.entry("legend.warning", "需关注"),
            Map.entry("demo.started", "演示环境已启动"),
            Map.entry("demo.sample.data", "当前使用内存样例数据"),
            Map.entry("placeholder.kicker", "接口待接入"),
            Map.entry("placeholder.title", "%s模块已预留"),
            Map.entry("placeholder.detail", "该页面用于后续团队开发。当前基础框架不包含业务处理或实时数据。"),
            Map.entry("incident.capacity", "分区容量提醒"),
            Map.entry("incident.misparking", "车辆停放位置错误"),
            Map.entry("incident.accessible", "无障碍车位违规"),
            Map.entry("incident.drivingTimeout", "场内行驶超时"),
            Map.entry("incident.parkingTimeout", "停车超时"),
            Map.entry("incident.deviceFailure", "设备故障"),
            Map.entry("incident.patrol", "巡逻报告"),
            Map.entry("incident.message.capacity", "分区占用率达到提醒阈值。"),
            Map.entry("incident.message.misparking", "车辆停在未分配的车位。"),
            Map.entry("incident.message.accessible", "无资格车辆占用了无障碍车位。"),
            Map.entry("incident.message.drivingTimeout", "车辆未能在规定时间内到达目标位置。"),
            Map.entry("incident.message.parkingTimeout", "车辆停车时间超过允许时长。"),
            Map.entry("incident.message.deviceFailure", "车位设备报告故障。"));

    private static final Map<String, String> ENGLISH = Map.ofEntries(
            Map.entry("brand.title", "MIA Parking Operations Center"),
            Map.entry("brand.subtitle", "AIRPORT PARKING CONTROL"),
            Map.entry("status.demo", "Demo mode · in-memory service connected"),
            Map.entry("nav.dashboard", "Dashboard"),
            Map.entry("nav.dashboard.description", "View zones, operating metrics, and recent events."),
            Map.entry("nav.spaces", "Zones & Spaces"),
            Map.entry("nav.spaces.description", "Browse space layout, category, and status by zone."),
            Map.entry("nav.vehicles", "Vehicle Flow"),
            Map.entry("nav.vehicles.description", "View entry recognition, space assignments, and sessions."),
            Map.entry("nav.security", "Security"),
            Map.entry("nav.security.description", "Review alerts and follow up on patrol reports."),
            Map.entry("nav.fees", "Fees & Settlement"),
            Map.entry("nav.fees.description", "View parking fees, payments, and settlements."),
            Map.entry("nav.devices", "Cameras & Sensors"),
            Map.entry("nav.devices.description", "View gates, cameras, and space sensors."),
            Map.entry("nav.users", "Users & Subscriptions"),
            Map.entry("nav.users.description", "Manage user categories and subscriptions."),
            Map.entry("nav.logs", "Logs & Statistics"),
            Map.entry("nav.logs.description", "Review events and parking operations statistics."),
            Map.entry("nav.simulation", "Simulation"),
            Map.entry("nav.simulation.description", "Configure the shared clock and demo scenarios."),
            Map.entry("nav.backup", "Backup & Recovery"),
            Map.entry("nav.backup.description", "Review snapshots and restored runtime state."),
            Map.entry("footer.note", "Starter interface · sample values are not live data"),
            Map.entry("footer.brand", "MIA PARKING"),
            Map.entry("page.eyebrow", "PARKING OPERATIONS"),
            Map.entry("metric.occupancy", "Overall occupancy"),
            Map.entry("metric.occupancy.note", "In-memory demo data"),
            Map.entry("metric.available", "Available spaces"),
            Map.entry("metric.available.note", "Sample layout"),
            Map.entry("metric.sessions", "Active sessions"),
            Map.entry("metric.sessions.note", "Current parking sessions"),
            Map.entry("metric.alerts", "Open alerts"),
            Map.entry("metric.alerts.note", "Unresolved"),
            Map.entry("panel.zones", "Zone overview"),
            Map.entry("panel.zones.subtitle", "ZONE OCCUPANCY"),
            Map.entry("panel.events", "Recent events"),
            Map.entry("panel.events.subtitle", "LATEST ALERTS AND EVENTS"),
            Map.entry("zone.prefix", "Zone "),
            Map.entry("zone.spaces", "%d spaces"),
            Map.entry("legend.free", "Free"),
            Map.entry("legend.allocated", "Assigned / occupied"),
            Map.entry("legend.warning", "Attention"),
            Map.entry("demo.started", "Demo environment ready"),
            Map.entry("demo.sample.data", "Using in-memory sample data"),
            Map.entry("placeholder.kicker", "Coming soon"),
            Map.entry("placeholder.title", "%s module reserved"),
            Map.entry("placeholder.detail", "This page is reserved for team development. Business logic and live data are not connected yet."),
            Map.entry("incident.capacity", "Zone capacity warning"),
            Map.entry("incident.misparking", "Vehicle parked in the wrong space"),
            Map.entry("incident.accessible", "Accessible-space violation"),
            Map.entry("incident.drivingTimeout", "Driving timeout"),
            Map.entry("incident.parkingTimeout", "Parking timeout"),
            Map.entry("incident.deviceFailure", "Device failure"),
            Map.entry("incident.patrol", "Patrol report"),
            Map.entry("incident.message.capacity", "Zone occupancy reached the warning threshold."),
            Map.entry("incident.message.misparking", "A vehicle parked in an unassigned space."),
            Map.entry("incident.message.accessible", "An unauthorized vehicle used an accessible space."),
            Map.entry("incident.message.drivingTimeout", "A vehicle did not reach its destination within the allowed time."),
            Map.entry("incident.message.parkingTimeout", "A vehicle exceeded its allowed parking duration."),
            Map.entry("incident.message.deviceFailure", "A parking-space device reported a failure."));

    /**
     * 中文：阻止创建界面文案工具类的实例。
     */
    private UiText() {
    }

    /**
     * 中文：按当前语言读取本地化界面文案。
     */
    static String text(UiLanguage language, String key) {
        return (language == UiLanguage.ENGLISH ? ENGLISH : CHINESE).getOrDefault(key, key);
    }

    /**
     * 中文：按界面语言返回事件类别对应的标题。
     */
    static String incidentTitle(UiLanguage language, Incident.Type type) {
        return text(language, switch (type) {
            case CAPACITY_WARNING -> "incident.capacity";
            case MISPARKING -> "incident.misparking";
            case ACCESSIBLE_SPACE_VIOLATION -> "incident.accessible";
            case DRIVING_TIMEOUT -> "incident.drivingTimeout";
            case PARKING_TIMEOUT -> "incident.parkingTimeout";
            case DEVICE_FAILURE -> "incident.deviceFailure";
            case PATROL_REPORT -> "incident.patrol";
        });
    }

    /**
     * 中文：按界面语言生成事件提示内容。
     */
    static String incidentMessage(UiLanguage language, Incident incident) {
        String key = switch (incident.type()) {
            case CAPACITY_WARNING -> "incident.message.capacity";
            case MISPARKING -> "incident.message.misparking";
            case ACCESSIBLE_SPACE_VIOLATION -> "incident.message.accessible";
            case DRIVING_TIMEOUT -> "incident.message.drivingTimeout";
            case PARKING_TIMEOUT -> "incident.message.parkingTimeout";
            case DEVICE_FAILURE -> "incident.message.deviceFailure";
            case PATROL_REPORT -> null;
        };
        return key == null ? incident.message() : text(language, key);
    }
}
