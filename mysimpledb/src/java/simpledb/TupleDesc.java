package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

	private TDItem[] fields;
    /**
     * A help class to facilitate organizing the information of each field
     */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         */
        public final Type fieldType;

        /**
         * The name of the field
         */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr  array specifying the number of and types of fields in this
     *                TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may
     *                be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        this.fields = new TDItem[typeAr.length];
        for(int i = 0; i < this.fields.length; i++){
        	fields[i] = new TDItem(typeAr[i], fieldAr[i]);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in this
     *               TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
    	this.fields = new TDItem[typeAr.length];
    	for(int i = 0; i<typeAr.length; i++){
        	fields[i] = new TDItem(typeAr[i], null);
        }
        
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return this.fields.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
    	if(i < 0 || i >= this.fields.length){
    		throw new NoSuchElementException();
    	}
    	return this.fields[i].fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid
     *          index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
    	if(i < 0 || i >= this.fields.length){
    		throw new NoSuchElementException();
    	}
        return this.fields[i].fieldType;
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        int i = 0;
        while(i < this.fields.length){
        	if(this.fields[i].fieldName != null && this.fields[i].fieldName.equals(name)){
        		return i;
        	}
        	i++;
        }
        throw new NoSuchElementException(); 
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
    	int sum = 0;
        for(int i = 0; i < this.fields.length; i++){
        	sum += this.fields[i].fieldType.getLen();
        }
        return sum;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        int len = td1.numFields() + td2.numFields();
        Type[] typeAr = new Type[len];
		String[] fieldAr = new String[len];
		for(int i = 0; i < td1.numFields(); i++){
			typeAr[i] = td1.fields[i].fieldType;
			fieldAr[i] = td1.fields[i].fieldName;
		}
		for(int i = td1.numFields(); i < len; i++){
			typeAr[i] = td2.fields[i - td1.numFields()].fieldType;
			fieldAr[i] = td2.fields[i - td1.numFields()].fieldName;
		}
        return new TupleDesc(typeAr, fieldAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
    	if(this == null || o == null || !(o instanceof TupleDesc)){
    		return false;
    	}
        if(this.getSize() != ((TupleDesc)o).getSize()){
        	return false;
        }
        int len = this.numFields();
        for(int i = 0; i < len; i++){
        	if(this.getFieldType(i) != ((TupleDesc)o).getFieldType(i)){
        		return false;
        	}
        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldName[0](fieldType[0]), ..., fieldName[M](fieldType[M])"
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        String str = this.fields[0].toString();
        for(int i = 1; i < this.fields.length; i++){
        	str = str + ", " + this.fields[i].toString();
        }
        return str;
    }

    /**
     * @return An iterator which iterates over all the field TDItems
     * that are included in this TupleDesc
     */
    public Iterator<TDItem> iterator() {
        return (Arrays.asList(this.fields)).iterator();
    }

}
