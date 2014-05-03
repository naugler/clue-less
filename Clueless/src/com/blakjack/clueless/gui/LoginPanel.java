/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blakjack.clueless.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author nauglrj1
 */
public class LoginPanel extends JPanel {

    private static final int MAX_USERNAME_LEN = 12;

    private final JTextField username = new JTextField();
    private final JTextField address = new JTextField();
    private final JTextField port;
    
    static final Pattern pat = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");

    public LoginPanel(boolean startServer) {
        DecimalFormat df = new DecimalFormat("#####");
        NumberFormatter nf = new NumberFormatter(df) {
            @Override
            public String valueToString(Object iv) {
                if ((iv == null) || (((Integer) iv) == -1)) {
                    return "";
                } else {
                    try {
                        return super.valueToString(iv);
                    } catch (ParseException ex) {
                        return "";
                    }
                }
            }

            @Override
            public Object stringToValue(String text) {
                if ("".equals(text)) {
                    return null;
                }
                try {
                    return super.stringToValue(text);
                } catch (ParseException ex) {
                    return null;
                }
            }
        };

        nf.setMinimum(0);
        nf.setMaximum(65534);
        nf.setValueClass(Integer.class);
        port = new JFormattedTextField(nf);
        port.setColumns(5);
        
        initComponents();
        if (startServer) {
            address.setEnabled(false);
            try {
                address.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    private void initComponents() {
        username.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (getLength() + str.length() <= MAX_USERNAME_LEN) {
                    super.insertString(offs, str, a);
                }
            }
        });
        address.getDocument().addDocumentListener(new DocumentListener() {
            void checkDocument(DocumentEvent e) {
                try {
                    String text = e.getDocument().getText(0, e.getDocument().getLength());
                    boolean isValid = isValidIP(text);
                    if (isValid) {
                        address.setForeground(Color.GREEN.darker().darker());
                    } else {
                        address.setForeground(Color.red);
                    }
                } catch (BadLocationException ex) {
                    //Do something, OK?
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {checkDocument(e);}
            @Override
            public void removeUpdate(DocumentEvent e) {checkDocument(e);}
            @Override
            public void changedUpdate(DocumentEvent e) {checkDocument(e);}
        });

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Username: "), c);
        c.gridy = 1;
        add(new JLabel("Address: "), c);
        c.gridy = 2;
        add(new JLabel("Port: "), c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2;
        add(username, c);
        c.gridy = 1;
        add(address, c);
        c.gridy = 2;
        add(port, c);
    }
    
    static boolean isValidIP(String s) {
        Matcher m = pat.matcher(s);
        return m.matches();
    }

    public String getUsername() {
        return username.getText();
    }

    public String getAddress() {
        return address.getText();
    }

    public int getPort() {
        return Integer.parseInt(port.getText());
    }

}
