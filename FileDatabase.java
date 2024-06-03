import java.sql.*;
//Hello

public class FileDatabase {
    private String db_name;

    public FileDatabase(String db_name) {
        this.db_name = db_name;
        createDatabase();
        populateWithDummyData();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + db_name);
    }

    private void createDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS files ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "type TEXT NOT NULL,"
                    + "size INTEGER NOT NULL,"
                    + "path TEXT NOT NULL)");
            System.out.println("Database and table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateWithDummyData() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM files");
            if (rs.getInt(1) == 0) {
                String[] dummyData = {
                        "('Inventory Report', 'text/plain', 2048, '/path/to/report.txt'),",
                        "('Stock Image', 'image/jpeg', 102400, '/path/to/stock_image.jpg'),",
                        "('RFID Tag List', 'application/vnd.ms-excel', 51200, '/path/to/rfid_list.xlsx'),",
                        "('Tracking Software', 'application/x-msdownload', 204800, '/path/to/tracking_software.exe'),",
                        "('User Manual', 'application/pdf', 102400, '/path/to/user_manual.pdf')"
                };
                for (String data : dummyData) {
                    stmt.execute("INSERT INTO files (name, type, size, path) VALUES " + data);
                }
                System.out.println("Dummy data inserted successfully.");
            } else {
                System.out.println("Dummy data already exists.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFile(String name, String fileType, int size, String path) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO files (name, type, size, path) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, fileType);
            pstmt.setInt(3, size);
            pstmt.setString(4, path);
            pstmt.executeUpdate();
            System.out.println("File added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateFile(int fileId, String name, String fileType, Integer size, String path) {
        StringBuilder query = new StringBuilder("UPDATE files SET ");
        if (name != null) query.append("name = ?, ");
        if (fileType != null) query.append("type = ?, ");
        if (size != null) query.append("size = ?, ");
        if (path != null) query.append("path = ? ");
        query.append("WHERE id = ?");

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            int i = 1;
            if (name != null) pstmt.setString(i++, name);
            if (fileType != null) pstmt.setString(i++, fileType);
            if (size != null) pstmt.setInt(i++, size);
            if (path != null) pstmt.setString(i, path);
            pstmt.setInt(i, fileId);

            pstmt.executeUpdate();
            System.out.println("File updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteFile(int fileId) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM files WHERE id = ?")) {
            pstmt.setInt(1, fileId);
            pstmt.executeUpdate();
            System.out.println("File deleted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void searchFiles(String name, String fileType) {
        StringBuilder query = new StringBuilder("SELECT * FROM files WHERE 1=1");
        if (name != null) query.append(" AND name LIKE ?");
        if (fileType != null) query.append(" AND type = ?");

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            int i = 1;
            if (name != null) pstmt.setString(i++, "%" + name + "%");
            if (fileType != null) pstmt.setString(i, fileType);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id")
                        + ", Name: " + rs.getString("name")
                        + ", Type: " + rs.getString("type")
                        + ", Size: " + rs.getInt("size")
                        + ", Path: " + rs.getString("path"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllFiles() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM files")) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id")
                        + ", Name: " + rs.getString("name")
                        + ", Type: " + rs.getString("type")
                        + ", Size: " + rs.getInt("size")
                        + ", Path: " + rs.getString("path"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        FileDatabase db = new FileDatabase("dummy.db");

        while (true) {
            System.out.println("\nFile Database Menu");
            System.out.println("1. Add a file");
            System.out.println("2. Update a file");
            System.out.println("3. Delete a file");
            System.out.println("4. Search for files");
            System.out.println("5. Print all files");
            System.out.println("6. Exit");

            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter file name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter file type: ");
                    String fileType = scanner.nextLine();
                    System.out.print("Enter file size: ");
                    int size = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter file path: ");
                    String path = scanner.nextLine();
                    db.addFile(name, fileType, size, path);
                    System.out.println("File added successfully.");
                    break;
                case "2":
                    System.out.print("Enter file ID to update: ");
                    int fileId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new file name (leave blank to keep current): ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new file type (leave blank to keep current): ");
                    String newType = scanner.nextLine();
                    System.out.print("Enter new file size (leave blank to keep current): ");
                    String newSize = scanner.nextLine();
                    System.out.print("Enter new file path (leave blank to keep current): ");
                    String newPath = scanner.nextLine();
                    db.updateFile(fileId, newName.isEmpty() ? null : newName, 
                                   newType.isEmpty() ? null : newType, 
                                   newSize.isEmpty() ? null : Integer.parseInt(newSize), 
                                   newPath.isEmpty() ? null : newPath);
                    System.out.println("File updated successfully.");
                    break;
                case "3":
                    System.out.print("Enter file ID to delete: ");
                    int deleteId = Integer.parseInt(scanner.nextLine());
                    db.deleteFile(deleteId);
                    System.out.println("File deleted successfully.");
                    break;
                case "4":
                    System.out.print("Enter file name to search (leave blank to ignore): ");
                    String searchName = scanner.nextLine();
                    System.out.print("Enter file type to search (leave blank to ignore): ");
                    String searchType = scanner.nextLine();
                    db.searchFiles(searchName.isEmpty() ? null : searchName, 
                                   searchType.isEmpty() ? null : searchType);
                    break;
                case "5":
                    db.getAllFiles();
                    break;
                case "6":
                    System.out.println("Exiting the menu.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
