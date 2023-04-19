class Product {
    public class InvalidFieldLength extends Exception {
        private String field;
        private int maxLength;
        public InvalidFieldLength(String field, int maxLength){
            this.field = field;
            this.maxLength = maxLength;
        }

        @Override
        public String getMessage() {
            return "Length of '" + field  +"' Field of Product Class Must Not exceed " + Integer.toString(maxLength);
        }
    }
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    public Product(String id, String name, String description, double unitPrice) throws InvalidFieldLength {
        this.id = fixTheNumberOfCharactersByAddingPadding(id, 6);
        this.name = fixTheNumberOfCharactersByAddingPadding(name, 35);
        this.description = fixTheNumberOfCharactersByAddingPadding(description, 75);
        this.unitPrice = unitPrice;

        if(id.length() > 6){
            throw new InvalidFieldLength("id", 6);
        }
        if(name.length() > 35){
            throw new InvalidFieldLength("name", 35);
        }
        if(description.length() > 75){
            throw new InvalidFieldLength("description", 75);
        }
    }
    public String fixTheNumberOfCharactersByAddingPadding(String field, int characters){
        return String.format("%1$"+characters+ "s", field);
    }
    public String getId(){return id;}
    public String getName() {
        return name;
    }
    public String getDescription(){return description;}
    public double getUnitPrice() {
        return unitPrice;
    }

    public String getIdTrim(){return id.trim();}
    public String getNameTrim() {
        return name.trim();
    }
    public String getDescriptionTrim(){return description.trim();}

    public static Product toProduct(String data) throws InvalidFieldLength{

        var id = data.substring(0, 6);
        var name = data.substring(6, 41);
        var description = data.substring(41, 116);
        var price = Double.parseDouble(data.substring(116));

        Product product = new Product(id, name, description, price);
        return product;
    }

    Product trim(){
        id = id.trim();
        name = name.trim();
        description = description.trim();
        return this;
    }

    @Override
    public String toString() {
        return id + name + description + fixTheNumberOfCharactersByAddingPadding(Double.toString(unitPrice), 10) + "\n";
    }
}
