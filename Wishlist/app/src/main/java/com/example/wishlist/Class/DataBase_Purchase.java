import android.database.sqlite.SQLiteDatabase;

public class DataBase_Purchase {

    private static final String PURCHASE_TABLE_NAME = "Purchase";
    private static final String PURCHASE_COL0 = "num acheteur";
    private static final String PURCHASE_COL1 = "num receveur";
    private static final String PURCHASE_COL2 = "num produit";
    private static final String PURCHASE_COL3 = "date";
    private static final String PURCHASE_COL4 = "quantité";
    private static final String PURCHASE_COL5 = "num achat";

    public DataBase_Purchase(@Nullable Context context) {  // Ctrl-c  Ctrl-v
        super(context, DATABASE_NAME, null, 1);
    }

    public void CreateDB(SQLiteDatabase db){    // Faut peut-etre "onCreate" ?
        String sqlCommand = "CREATE TABLE " +
                PURCHASE_TABLE_NAME + " ( " +
                PURCHASE_COL0 + " INT NOT NULL REFERENCES utilisateur ('user id') "+
                PURCHASE_COL1 + " INT NOT NULL REFERENCES utilisateur ('user id') " +
                PURCHASE_COL2 + "  INT NOT NULL REFERENCES produit ('num produit') " +
                PURCHASE_COL3 + " DATE NOT NULL " +
                PURCHASE_COL4 + " INT NOT NULL " +
                PURCHASE_COL5 + " INT NOT NULL )";
        db.execSQlL(sqlCommand);
    }

    public Boolean AddPurchase(Purchase achat) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(PURCHASE_COL0, achat.getSender());
        contentvalues.put(PURCHASE_COL1, achat.getReceiver());
        // contentvalues.put(PURCHASE_COL2,achat.getProduct().number?); dépendra de la classe Produit
        contentvalues.put(PURCHASE_COL3, achat.getDate());
        contentvalues.put(PURCHASE_COL4, achat.getQuantity());
        contentvalues.put(PURCHASE_COL5, achat.getPurchaseId()); // A moins que la bdd génère elle même un numéro aléatoire ?
        long err = db.insert(PURCHASE_TABLE_NAME, null, contentvalues);
        return err != -1;
    }

}
