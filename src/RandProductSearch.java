import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RandProductSearch extends JFrame implements ActionListener {

    private JPanel panel = new JPanel();
    private JLabel name_label = new JLabel("Name");
    private JTextField name_field = new JTextField(35);
    private JButton button = new JButton("Search");
    private JButton saveBtn = new JButton("Save");
    private JLabel recordLabel = new JLabel("Records Entered");
    private JTextField recordField = new JTextField(20);
    private JTextArea area = new JTextArea("Search Results will appear here!");
    private JPanel searchResults = new JPanel();
    private long recordsEntered = 0;

    private static List<Product> records;
    public RandProductSearch(String title) throws IOException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        setSize(500, 500);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth()/2 - 250;
        int height = (int) screenSize.getHeight()/2 - 250;

        setLocation(width, height);

        panel.setLayout(new GridLayout(10, 6));
        panel.setSize(100, 100);
        panel.setLocation(50, 50);

        searchResults.setLayout(new GridLayout(1, 1));
        searchResults.setSize(100, 100);

        setRecordEntered();

        recordField.setText(Long.toString(recordsEntered));
        recordField.setEnabled(false);

        panel.add(recordLabel);
        panel.add(recordField);

        panel.add(name_label);
        panel.add(name_field);

        button.addActionListener(this);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigate();
            }
        });

        area.setBounds(10,30, 200,200);


        panel.add(button);
        panel.add(saveBtn);
        searchResults.add(area);

        this.add(panel);
        this.add(searchResults);


        records = readRecord();
    }

    private  void navigate(){
        try {
            this.dispose();
            RandProductMaker randProductMaker = new RandProductMaker("Random Product Maker");
            randProductMaker.setVisible(true);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(name_field.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Name field is required!");
            return;
        }
        area.setText("");
        try {
            var name = name_field.getText().trim();

            for(var record:records){
                if(record.getName().toLowerCase().contains(name.toLowerCase())){
                    System.out.println(record.getId() + ", " + record.getName() + ", " + record.getDescription() + ", " + record.getUnitPrice());
                    if(area.getText().trim().isEmpty()){
                        area.setText(record.getId() + ", " + record.getName() + ", " + record.getDescription() + ", " + record.getUnitPrice());
                    } else {
                        area.setText(area.getText() + "\n" + record.getId() + ", " + record.getName() + ", " + record.getDescription() + ", " + record.getUnitPrice());
                    }
                }
            }

            if(area.getText().trim().isEmpty()){
                area.setText("No Results Found!");
            }

        } catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage());
            area.setText("Search Results will appear here!");
            return;
        }
    }

    private void setRecordEntered() throws IOException{
        RandomAccessFile file = new RandomAccessFile("products.bin", "rw");
        recordsEntered = file.length() / 127;
        file.close();
    }
    private List<Product> readRecord() throws IOException {
        RandomAccessFile file = new RandomAccessFile("products.bin", "r");
        byte[] bytes = new byte[(int) file.length()];
        file.seek(0);
        file.read(bytes);
        file.close();
        var dumbRecords = new String(bytes).split("\n");
        List<Product> products = new ArrayList<>();
        for(var record: dumbRecords){
            try {
                products.add(Product.toProduct(record).trim());
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        return products;
    }
}
