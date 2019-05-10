package ImageViewer;

import java.awt.Component;
    import java.awt.Dimension;
    import java.awt.event.WindowAdapter;
    import java.awt.event.WindowEvent;
    import java.util.ArrayList;
    import java.util.List;
    import javax.swing.JFrame;
    import javax.swing.JScrollPane;
    import javax.swing.JTable;
    import javax.swing.JTextArea;
    import javax.swing.UIManager;
    import javax.swing.border.EmptyBorder;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.table.TableCellRenderer;
    import javax.swing.table.TableModel;
    import javax.swing.table.TableRowSorter;

    public class Main extends JFrame {
      public Main() {
        super("Multi-Line Cell Example");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        DefaultTableModel dm = new DefaultTableModel() {
          public Class<String> getColumnClass(int columnIndex) {
            return String.class;
          }
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
        dm.setDataVector(
            new Object[][]{
                {"A0, Line1\nA0, Line2\nA0, Line3", 
                 "B0, Line1\nB0, Line2", 
                 "C0, Line1"}, 
                {"A1, Line1", 
                 "B1, Line1\nB1, Line2", 
                 "C1, Line1"},
                {"A2, Line1", 
                 "B2, Line1", 
                 "C2, Line1"}
                }, 
                new Object[] {"A", "B", "C"});

        JTable table = new JTable(dm);
        table.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
        TableRowSorter<? extends TableModel> sort = new TableRowSorter<DefaultTableModel>(dm);
        table.setRowSorter(sort);
        JScrollPane scroll = new JScrollPane(table);
        getContentPane().add(scroll);
        setLocationByPlatform(true);
        setSize(400, 430);
        setVisible(true);
      }

      public static void main(String[] args) {
        Main frame = new Main();
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
      }
    }