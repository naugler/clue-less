package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Card;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class EvidenceLocker extends JPanel {

    private final TableCellRenderer cardRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, 
                Object value, boolean isSelected, boolean hasFocus, 
                int row, int column) {
            return super.getTableCellRendererComponent(table,
                    Card.values()[row], isSelected, hasFocus, row, column);
        }
    };
    private final TableModel model = new DefaultTableModel(
            new String[]{"Card", "", "Notes"}, 
            Card.values().length) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };
    private final JTable table = new JTable(model) {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row,
                int column) {
            Component component = super.prepareRenderer(renderer, row, column);
            int rendererWidth = component.getPreferredSize().width;
            TableColumn tableColumn = getColumnModel().getColumn(column);
            tableColumn.setPreferredWidth(Math.max(rendererWidth
                    + getIntercellSpacing().width,
                    tableColumn.getPreferredWidth()));
            return component;
        }
    };

    public EvidenceLocker() {
        TitledBorder border = new TitledBorder("Evidence");
        setBorder(border);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn cardColumn = table.getColumnModel().getColumn(0);
        cardColumn.setCellRenderer(cardRenderer);
        setLayout(new BorderLayout());
//        JScrollPane scrollPane = new JScrollPane(table);
        add(table);
    }
}
