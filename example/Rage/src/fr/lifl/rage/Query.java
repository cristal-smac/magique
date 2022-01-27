package fr.lifl.rage;

public class Query implements java.io.Serializable {
    public Class  fieldClass;
    public String fieldName ;
    public Object fieldValue;

    public Query() {
	this(null, null, null);
    }

    public Query(Class fieldClass, String fieldName, Object fieldValue) {
	this.fieldClass = fieldClass;
	this.fieldName  = fieldName;
	this.fieldValue = fieldValue;
    }
}
