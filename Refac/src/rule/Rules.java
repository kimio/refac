/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rule;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author felipekn
 */
public abstract class Rules extends MessageErrors{
    protected String row; 
    protected int rowNumber;
    protected final HashMap<String,String> vars;
    protected final TreeMap<String,String> errors;
    protected StringBuilder code = new StringBuilder();
    
    
    public Rules(){
        this.vars = new HashMap<>();
        this.errors = new TreeMap<>();
    }
    public String getLogErrors(){
        StringBuilder error = new StringBuilder();
        Set<String> keys = errors.keySet();
        keys.forEach((key) -> {
            error.append(key);
            error.append("\n");
            error.append(errors.get(key));
            error.append("\n");
            error.append("\n");
        });
        return error.toString();
    }
    public void setRow(String row,int rowNumber){
        this.row = row;
        this.rowNumber = rowNumber;
        verify();
        setError();
    }
    public abstract void verify();
    public abstract void setError();
}
