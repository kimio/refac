/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rule;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author felipekn
 */
public class RulesObjectiveC {
    private String row;
    private int rowNumber;
    private final HashMap<String,String> vars;
    private int positionEndRowImplementation = 0;
    private StringBuilder code = new StringBuilder();
    
    public RulesObjectiveC(){
        this.vars = new HashMap<>();
    }
    public void setRow(String row,int rowNumber){
        this.row = row;
        this.rowNumber = rowNumber;
        verify();
    }
    private void verify(){
        currentImplementationRow();
        verifyString();
        spaceBetweenMethodSignature();
        movePointerOn();
        StringBuilder append = code.append(row).append("\n");
    }
    public String getNewStringVarsWithImplementation(){
        StringBuilder varsBuilder = new StringBuilder();
        Set<String> keys = vars.keySet();
        keys.forEach((key) -> {
            varsBuilder.append(vars.get(key));
        });
        varsBuilder.append("\n");
        return 
                code.replace(
                        positionEndRowImplementation,
                        positionEndRowImplementation+1, 
                        varsBuilder.toString()).toString();
    }
    //Validation
    private void currentImplementationRow(){
        if(positionEndRowImplementation<1){
            if(row.contains("@implementation")){
                positionEndRowImplementation = code.toString().length()+row.length();
            }
        }
    }
    private void verifyString(){
        if(row.contains("@\"")&&!row.contains("NSLocalizedString")&&!row.contains("static NSString")&&!row.contains("@\"\"")){
            
            Pattern pattern = Pattern.compile("@\"(.+?)\"");
            Matcher matcher = pattern.matcher(row);
            matcher.find();

            String valueString = matcher.group(1);
            String replaceWithVar = "@\""+valueString+"\"";
            String var = "_VAR"+rowNumber;
            this.vars.put(var, "\nstatic NSString *const "+var+" = "+replaceWithVar+";");
            
            row = row.replaceAll(replaceWithVar, var);
        }
    }
    private void spaceBetweenMethodSignature(){
        if(row.contains("-(")){
            row = row.replace("-(", "- (");
        }
    }
    private void movePointerOn(){
        if(row.contains("*)")&&!row.contains(" *)")){
            row = row.replace("*)", " *)");
        }
    }
}
