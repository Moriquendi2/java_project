/* Team 7: Hao Yang, Zexuan Li, Haolin Chen. */
package mia.parking.app;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import mia.parking.core.DemoParkingControlService;
import mia.parking.core.ParkingControlService;
import mia.parking.infrastructure.persistence.InMemoryParkingRepository;
import mia.parking.infrastructure.recovery.InMemorySnapshotStore;
import mia.parking.infrastructure.time.SystemClock;
import mia.parking.simulation.device.SimulatedDeviceGateway;
import mia.parking.core.port.DeviceSpaceStatus;
import mia.parking.core.domain.ParkingSpace;
import mia.parking.ui.ControlCenterView;

/** Starts the native Swing parking control client.
 * 中文：启动 Swing 停车控制台并装配业务服务、数据仓库和模拟设备。
 */
public final class ParkingApplication {

    /**
     * 中文：阻止创建应用入口工具类的实例。
     */
    private ParkingApplication() {
    }

    /**
     * 中文：创建应用服务和模拟适配器，并显示主窗口。
     */
    public static void main(String[] args) {
        InMemoryParkingRepository repository = new InMemoryParkingRepository();
        SimulatedDeviceGateway devices = new SimulatedDeviceGateway();
        for (ParkingSpace space : repository.loadSpaces()) {
            devices.reportSpaceStatus(new DeviceSpaceStatus(space.id(), space.enabled(), false, null));
        }
        ParkingControlService service = new DemoParkingControlService(
                repository, new InMemorySnapshotStore(), new SystemClock(), devices);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MIA Parking · 停车运营中心");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ControlCenterView(service));
            frame.setMinimumSize(new Dimension(1080, 700));
            frame.setSize(1360, 850);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
