MIA AIRPORT PARKING MANAGEMENT — SWING STARTER
Team 7: Hao Yang, Zexuan Li, Haolin Chen.

Purpose
-------
This is the native Swing starter for the MIA parking project. The dashboard is
connected to a small in-memory development service. Other pages remain navigation
placeholders. The demo service uses simple allocation and fee examples; the
module owners should replace them with the requirements in docs/description.md.

Requirements
------------
- JDK 21. The current project module is configured for Java 21.
- MySQL Server 8.0 or newer when database features are enabled.
- IntelliJ IDEA (optional; command-line compilation also works).
- Swing and JDBC API (`java.sql`) are included in the JDK; no UI framework is used.
- MySQL Connector/J 9.7.0 is in lib/mysql-connector-j-9.7.0.jar.
- No application framework or build tool is required.

Run from IntelliJ IDEA
----------------------
1. Open this project folder. IDEA registers java_project.iml as the single module.
2. Set the project and module SDK to JDK 21.
3. Create an Application run configuration:
   Main class: mia.parking.app.ParkingApplication
   Working directory: the project root
4. Run the configuration.

Run from PowerShell
-------------------
Open PowerShell in the project root and run:

  .\build.ps1
  .\run.ps1

The scripts compile all sources with the JDK 21 compiler into the ignored out\
directory. The run script launches the Swing application using the in-memory
repository, snapshot store, and simulated device adapter. MySQL is not needed
for this development mode.

Run the MySQL connection demo
-----------------------------
1. Start MySQL Server 8.0 or newer and import db.sql. From a MySQL command-line
   client, run `source db.sql`, or open db.sql in MySQL Workbench and execute it.
2. Copy the example settings and enter your local account details:

   Copy-Item .\files\database.properties.example .\files\database.properties

3. From the project root, compile and run the standalone database demo:

   .\build.ps1
   java -cp ".\out;.\lib\mysql-connector-j-9.7.0.jar" mia.parking.infrastructure.persistence.DatabaseDemo

The demo lists rows from PARKING_ZONE. It reads connection settings from
files/database.properties. That local file is ignored by Git; do not put real
credentials in the tracked example file. The main Swing app continues to use its
in-memory development repository.

Project layout
--------------
- src/mia/parking/app: Swing application entry point.
- src/mia/parking/ui: control-center shell, shared theme, and page placeholders.
- src/mia/parking/core: parking-control service contract and temporary demo implementation.
- src/mia/parking/core/domain: shared records and enums used across modules.
- src/mia/parking/core/port: device, repository, snapshot, and time contracts.
- src/mia/parking/infrastructure: in-memory adapters, a standalone JDBC zone demo, and recovery/time adapters.
- src/mia/parking/simulation: simulated device and future scenario adapters.
- build.ps1 / run.ps1: compile and launch the development application.
- files: reserved for runtime configuration, data, and resources as needed.
- lib: validated third-party JAR files, when required by implementation.
- docs: requirements, course instructions, and the interactive HTML design reference.

Database and delivery status
----------------------------
The one-table MySQL connection demo is separate from the application's in-memory
repository. It demonstrates the JDBC driver, local connection settings, and basic
zone queries; it is not the final parking database. The final db.sql must still
cover the complete model and demo data required by docs/AGENTS.md. The final
project.jar is also a later delivery item.

Team source-file headers
------------------------
Keep the full team roster at the top of every source/resource file. Do not remove
or replace the confirmed Team 7 names without the team's agreement.
