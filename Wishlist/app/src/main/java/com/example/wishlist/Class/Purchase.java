public class Purchase {
    private static long compteur = 0; // Variable de Classe  > Permet de donner un numéro de commande unique
    private String acheteur;
    private String beneficiare;
    private int quantity;
    private String date;
    private Produit product;
    private long PurchaseId;

    public Purchase(String acheteurID, String beneficiaireID, String date, int quantity, Produit product) {
        this.acheteur = acheteurID;
        this.beneficiare = beneficiaireID;
        this.quantity = quantity;
        this.date = date;
        this.product = product;
        compteur++;
        this.PurchaseId = compteur;
    }

    public String getReceiver() {return this.beneficiare;}

    public String getSender() {return this.acheteur;}

    public Produit getProduct() {return this.product;}

    public int getQuantity() {return this.quantity;}

    public String getDate() {return this.date;} // Ou autre manière de stocker la date ?

    public long getPurchaseId() {return this.PurchaseId;}

    public String description() {
        return "On the " + getDate() +
                " " + getSender() + " bought " +
                String.valueOf(getQuantity()) +
                " " + getProduct().name +
                " for " + getReceiver() + ".";
    }
}