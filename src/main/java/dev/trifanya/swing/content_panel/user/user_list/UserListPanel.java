package dev.trifanya.swing.content_panel.user.user_list;

import dev.trifanya.service.UserService;
import dev.trifanya.swing.MainFrame;
import dev.trifanya.swing.content_panel.ContentLayeredPane;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserListPanel extends JPanel implements Runnable {
    private UserService userService;
    private ContentLayeredPane contentLayeredPane;

    private JLabel userListLabel;

    private UserTableModel userTableModel;
    private UserTable userTable;
    private UserListScrollPane userListScrollPane;

    private JButton userDetailsButton;

    private String sortByColumn = "id";
    private String sortDir = "ASC";

    public UserListPanel() {
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(600, 400));
        MainFrame.setBasicInterface(this);
    }

    public void init(ContentLayeredPane contentLayeredPane) {
        userService = new UserService();

        this.contentLayeredPane = contentLayeredPane;

        initUserListLabel();
        add(userListLabel, new GridBagConstraints(
                0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(25, 25, 25, 25), 30, 20));

        userTableModel = new UserTableModel();
        userTable = new UserTable(userTableModel);
        userListScrollPane = new UserListScrollPane(userTable);

        add(userListScrollPane, new GridBagConstraints(
                0, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 25, 25, 25), 0, 0));

        initUserDetailsButton();
        add(userDetailsButton, new GridBagConstraints(
                0, 2, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 25, 25, 25), 50, 20));

        (new Thread(this)).start();
    }

    public void initUserListLabel() {
        userListLabel = new JLabel("СПИСОК УЧАСТНИКОВ");
        userListLabel.setBackground(MainFrame.firstColor);
        userListLabel.setForeground(MainFrame.secondColor);
        userListLabel.setFont(MainFrame.font);
        userListLabel.setBorder(new LineBorder(MainFrame.secondColor, 3, true));
        userListLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void initUserDetailsButton() {
        userDetailsButton = new JButton("Подробнее");
        userDetailsButton.setBackground(MainFrame.firstColor);
        userDetailsButton.setForeground(MainFrame.secondColor);
        userDetailsButton.setFont(MainFrame.font);
        userDetailsButton.setBorder(new LineBorder(MainFrame.secondColor, 3, true));
        userDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(UserListPanel.this, "Вы не выбрали строку", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int userId = Integer.valueOf(userTableModel.getValueAt(selectedRow, 0));
                contentLayeredPane.getUserDetailsPanel().fill(userService.getUserById(userId));
                contentLayeredPane.putPanelOnTop("USER DETAILS");
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                userTableModel.fillTable(sortByColumn, sortDir);
                this.repaint();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}