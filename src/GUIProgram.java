import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javafx.application.Application;
import java.lang.*;

import java.util.ArrayList;

public class GUIProgram
{
    public static void main(String[] args) throws Exception
    {
        final ArrayList<File> selectedFile = new ArrayList<>();
        final AmazonStorage db = new AmazonStorage();

        JFrame app = new JFrame();

        final Container pane = app.getContentPane();

        app.setTitle("My Search Engine");
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(Box.createRigidArea(new Dimension(5, 175)));

        final JLabel textbox = new JLabel("Load My Engine");
        textbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(textbox);

        pane.add(Box.createRigidArea(new Dimension(5, 50)));

        final JButton chooseButton = new JButton("Choose Files");
        chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(chooseButton);

        final JButton goToSearch = new JButton("Search");
        goToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(goToSearch);
        goToSearch.setVisible(false);

        final JLabel textbox2 = new JLabel("");
        textbox2.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(textbox2);


        pane.add(Box.createRigidArea(new Dimension(5, 50)));

        final JButton constructButton = new JButton("Construct Inverted Indicies");
        constructButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(constructButton);

        final JButton goToTopN = new JButton("Top N");
        goToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(goToTopN);
        goToTopN.setVisible(false);

        final JTextField field = new JTextField("");
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(field);
        field.setVisible(false);

        final JButton searchTerm = new JButton("Search");
        goToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(searchTerm);
        searchTerm.setVisible(false);

       final JButton searchN = new JButton("Search");
        goToSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(searchN);
        searchN.setVisible(false);

        app.setSize(600,600);
        app.setVisible(true);

        chooseButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter()
                {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory())
                        {
                            return true;
                        }
                        else
                        {
                            return f.getName().toLowerCase().endsWith(".tar.gz");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "GZ Files (*.tar.gz)";
                    }
                });

                fileChooser.showOpenDialog(null);

                File selectedFiles[] = fileChooser.getSelectedFiles();
                if (selectedFiles != null)
                {
                    selectedFile.clear();

                    for(int x = 0; x < selectedFiles.length; x++)
                    {
                        selectedFile.add(selectedFiles[x]);
                        textbox2.setText(textbox.getText() + "\n" + selectedFiles[x].toString());
                    }
                    constructButton.setText("Load Engine");
                }
            }
        });

        constructButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(constructButton.getText().equals("Load Engine"))
                {
                    for(int x = 0; x < selectedFile.size(); x++)
                    {
                        try
                        {
                            db.putFile(selectedFile.get(x));
                        }
                        catch (Exception f)
                        {
                            System.out.println("Failed to put file");
                        }
                    }
                }

                textbox2.setVisible(false);
                textbox.setText("Engine was loaded & Inverted indicies were constructed successfully! \nPlease Select an Action");
                chooseButton.setVisible(false);
                constructButton.setVisible(false);
                goToSearch.setVisible(true);
                goToTopN.setVisible(true);
            }
        });

        goToSearch.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textbox.setText("Enter Your Search Term");
                goToSearch.setVisible(false);
                goToTopN.setVisible(false);
                field.setVisible(true);
                field.setText("");
                searchTerm.setVisible(true);

                System.out.println(pane.getComponentCount());
                for(int x = 0; x < pane.getComponentCount(); x++)
                {
                    if(pane.getComponent(x).getName() != null)
                    {
                        System.out.println(pane.getComponent(x).getName());
                        if (pane.getComponent(x).getName().equals("table"))
                        {
                            pane.remove(x);
                        }
                    }
                }
            }
        });

        goToTopN.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textbox.setText("Enter Your N Value");
                goToSearch.setVisible(false);
                goToTopN.setVisible(false);
                field.setVisible(true);
                field.setText("");
                searchN.setVisible(true);

                System.out.println(pane.getComponentCount());
                for(int x = 0; x < pane.getComponentCount(); x++)
                {
                    if(pane.getComponent(x).getName() != null)
                    {
                        System.out.println(pane.getComponent(x).getName());
                        if (pane.getComponent(x).getName().equals("table"))
                        {
                            pane.remove(x);
                        }
                    }
                }
            }
        });

        searchTerm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textbox.setText("You searched the term: " + field.getText());
                goToSearch.setVisible(true);
                field.setVisible(false);
                searchTerm.setVisible(false);
                goToSearch.setText("Go to Term Search");

                String column[] = {"Doc ID", "Doc Folder", "Doc Name", "Frequencies"};
                //Object data[][] = db.fetchFiles(field.getText());
                Object data[][] = {
                        {1,"Kathy", "Name", 3},
                        {2,"John", "Name", 4},
                        {3,"Sue", "Name", 3},
                        {4,"Jane", "Name", 2},
                        {5,"Joe", "Name", 1}
                };


                JTable table = new JTable(data,column);
                table.setName("table");
                pane.add(table);
            }
        });

        searchN.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int num = Integer.parseInt(field.getText());

                textbox.setText("Top-N Frequent Terms" + field.getText());
                goToTopN.setVisible(true);
                field.setVisible(false);
                searchN.setVisible(false);
                goToTopN.setText("Go to N Choice");

                // Object[][] data = db.fetchTerms(num);

                String column[] = {"Term", "Total Frequencies"};
                Object[][] data = {
                        {"Kathy", 3},
                        {"John", 4},
                        {"Sue", 3},
                        {"Jane", 2},
                        {"Joe", 1}
                };

                JTable table = new JTable(data,column);
                table.setName("table");
                pane.add(table);
            }
        });
    }
}

