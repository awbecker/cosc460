package simpledb;
import java.util.*;


/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     *
     * @param tid        The transaction this scan is running as a part of.
     * @param tableid    the table to scan.
     * @param tableAlias the alias of this table (needed by the parser); the returned
     *                   tupleDesc should have fields with name tableAlias.fieldName
     *                   (note: this class is not responsible for handling a case where
     *                   tableAlias or fieldName are null. It shouldn't crash if they
     *                   are, but the resulting name can be null.fieldName,
     *                   tableAlias.null, or null.null).
     */
    
    private TransactionId tid = null;
    private int tableid;
    private String tableAlias = null;
    private DbFileIterator iter = null;
    
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        this.tid = tid;
        this.tableid = tableid;
        this.tableAlias = tableAlias;
        Catalog cat = Database.getCatalog();
        DbFile f = cat.getDatabaseFile(tableid);
        this.iter = f.iterator(tid);
    }

    /**
     * @return return the table name of the table the operator scans. This should
     * be the actual name of the table in the catalog of the database
     */
    public String getTableName() {
        Catalog cat = Database.getCatalog();
        return cat.getTableName(tableid);
    }

    /**
     * @return Return the alias of the table this operator scans.
     */
    public String getAlias() {
        return tableAlias;
    }

    public SeqScan(TransactionId tid, int tableid) {
        this(tid, tableid, Database.getCatalog().getTableName(tableid));
    }

    public void open() throws DbException, TransactionAbortedException {
        iter.open();
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
    	Catalog cat = Database.getCatalog();
        TupleDesc tdsc = cat.getTupleDesc(tableid);
        String[] newtdscf = new String[tdsc.numFields()];
        Type[] newtdsct = new Type[tdsc.numFields()];
        for(int i = 0; i < tdsc.numFields(); i++){
        	newtdscf[i] = tableAlias + "." + tdsc.getFieldName(i);
        	newtdsct[i] = tdsc.getFieldType(i);
        }
        return new TupleDesc(newtdsct, newtdscf);
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        return iter.hasNext();
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        return iter.next();
    }

    public void close() {
        iter.close();
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        iter.rewind();
    }
}
