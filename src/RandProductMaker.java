import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame implements ActionListener {
    private String[] placeholders = {"ID", "Name", "Description", "Unit Price"};
    private JPanel panel = new JPanel();
    private List<JLabel> labels = new ArrayList<>();
    private List<JTextField> fields = new ArrayList<>();
    private JButton button = new JButton("Save");
    private JButton srchBtn  = new JButton("Search");
    private JLabel recordLabel = new JLabel("Records Entered");
    private JTextField recordField = new JTextField(20);
    private long recordsEntered = 0;
    public RandProductMaker(String title) throws IOException{
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500, 500);
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth()/2 - 250;
        int height = (int) screenSize.getHeight()/2 - 250;


        setLocation(width, height);

        panel.setLayout(new GridLayout(6, 6));
        panel.setSize(200, 200);

        panel.setLocation(50, 50);

        setRecordEntered();

        recordField.setText(Long.toString(recordsEntered));
        recordField.setEnabled(false);

        panel.add(recordLabel);
        panel.add(recordField);

        for(var placeholder:placeholders){
            labels.add(new JLabel(placeholder));
            fields.add(new JTextField(20));
        }

        for(var i=0;i<labels.size();i++){
            panel.add(labels.get(i));
            panel.add(fields.get(i));
        }

        button.addActionListener(this);
        srchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigate();
            }
        });

        this.add(panel);
        this.add(button);
        this.add(srchBtn);
    }

    private  void navigate(){
        try {
            this.dispose();
            RandProductSearch randProductSearch = new RandProductSearch("Random Product Maker");
            randProductSearch.setVisible(true);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(var i=0;i<fields.size();i++){
            if(fields.get(i).getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(null, "All Fields are required!");
                return;
            }
        }

        try {
            var id = fields.get(0).getText();
            var name = fields.get(1).getText();
            var description = fields.get(2).getText();
            var price = Double.parseDouble(fields.get(3).getText());

            saveRecord(new Product(id, name, description, price));
        } catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage());
            return;
        }

        recordsEntered++;
        recordField.setText(Long.toString(recordsEntered));

        for(var i=0;i<fields.size();i++){
            fields.get(i).setText("");
        }
    }

    private void setRecordEntered() throws IOException{
        RandomAccessFile file = new RandomAccessFile("products.bin", "rw");
        recordsEntered = file.length() / 127;
        file.close();
    }

    private void saveRecord(Product product) throws IOException{
        RandomAccessFile file = new RandomAccessFile("products.bin", "rw");
        file.seek(file.length());
        file.write(product.toString().getBytes());
        file.close();
    }
    private void readRecord() throws IOException {
        RandomAccessFile file = new RandomAccessFile("products.bin", "r");
        byte[] bytes = new byte[(int) file.getFilePointer()];
        file.seek(0);
        file.read(bytes);
        file.close();
        System.out.println(new String(bytes));
    }
    public static void main(String[] args){
        try {
            RandProductMaker randProductMaker = new RandProductMaker("Random Product Maker");
            randProductMaker.setVisible(true);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

}
