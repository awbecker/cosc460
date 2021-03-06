package simpledb;

import java.io.Serializable;

/**
 * A RecordId is a reference to a specific tuple on a specific page of a
 * specific table.
 */
public class RecordId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new RecordId referring to the specified PageId and tuple
     * number.
     *
     * @param pid     the pageid of the page on which the tuple resides
     * @param tupleno the tuple number within the page.
     */
    
    private PageId pid = null;
    private int tpno;
    
    public RecordId(PageId pid, int tupleno) {
        this.pid = pid;
        this.tpno = tupleno;
    }

    /**
     * @return the tuple number this RecordId references.
     */
    public int tupleno() {
        return this.tpno;
    }

    /**
     * @return the page id this RecordId references.
     */
    public PageId getPageId() {
        return this.pid;
    }

    /**
     * Two RecordId objects are considered equal if they represent the same
     * tuple.
     *
     * @return True if this and o represent the same tuple
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof RecordId)){
        	return false;
        }
        RecordId rid = (RecordId)o;
        if(rid.getPageId().equals(this.getPageId()) && rid.tupleno() == this.tupleno()){
        	return true;
        }
        else{
        	return false;
        }
    }

    /**
     * You should implement the hashCode() so that two equal RecordId instances
     * (with respect to equals()) have the same hashCode().
     *
     * @return An int that is the same for equal RecordId objects.
     */
    @Override
    public int hashCode() {
        String pidhash = Integer.toString(this.getPageId().hashCode());
        String tpnohash = Integer.toString(this.tupleno());
        return (pidhash + "/" + tpnohash).hashCode();

    }

}
