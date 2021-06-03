package InMemoryDB;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * insert into test where  k1 and k2 or k3 or k4 and k5 
 * split on basis of or
 * k1 & k2 , k3 , k4 & k5
 */

abstract class Matchers{
    static final Pattern create_database = Pattern.compile("^[ ]?(?i)create database ");
    static final Pattern use_database = Pattern.compile("[ ]?^(?i)use ");
    static final Pattern create_table = Pattern.compile("^[ ]?(?i)create table ");
    static final Pattern insert_into_table = Pattern.compile("^[ ]?(?i)insert into [\\w]+ values[ ]?\\Q(\\E.*\\Q)\\E");
    static final Pattern select_from_table = Pattern.compile("^[ ]?(?i)select (\\*|([\\w])+([,\\w])*(?<!,)) from .*");
}


class DBFilter{
    String columne_name,value; 
    String operator;
    boolean isAggregate;
    List<DBFilter> filters ;
    DBFilter(String filter) throws Exception{
        if(filter.toLowerCase().contains(" and ")){
            this.isAggregate = true;
            this.filters = new ArrayList<>();
            String []s_filters = filter.split(" (?i)and ");
            for(String f:s_filters){
                this.filters.add(new DBFilter(f));
            }
        }
        else{
            String []fragments = filter.split("([\\w\\s])!?=(?=[\\w\\s])|([\\w\\s])>=?(?=[\\w\\s])|([\\w\\s])<=?(?=[\\w\\s])");
            if(fragments.length !=2)
                throw new Exception("Invalid Query : Check "+ filter);
            this.columne_name = fragments[0].trim();
            this.value = fragments[1].trim();
            if(filter.contains("!="))
                this.operator = "!=";
            else if(filter.contains(">="))
                this.operator = ">=";
            else if(filter.contains("<="))
                this.operator = "<=";
            else if(filter.contains("="))
                this.operator = "=";
            else if(filter.contains("<"))
                this.operator = "<";
            else if(filter.contains(">"))
                this.operator = ">";
        }
    }
}


class Executor {
    String query;
    DB currentDB;
    
    private String _normalizeQuery(String query){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<query.length();i++){
            if(query.charAt(i)==' '){
                
                while(query.charAt(i)==' ')
                    i++;
                if(sb.charAt(sb.length()-1)!=',')
                    sb.append(" ");
                i--;
            }
            else if(query.charAt(i)==','|| query.charAt(i)=='=' || query.charAt(i)=='!' || query.charAt(i)=='<' || query.charAt(i)=='>'){
                if(sb.charAt(sb.length()-1)!=' ' && query.charAt(i-1)!='!' && query.charAt(i-1)!='<' && query.charAt(i-1)!='>')
                    sb.append(" ");
                sb.append(query.charAt(i));
            }
            else   
                sb.append(query.charAt(i));
        }
        return sb.toString();
    }

    Executor(){
       
    }

    public boolean execute(String raw_query){
        this.query = _normalizeQuery(raw_query);
        if(currentDB==null){
            if(Matchers.create_database.matcher(this.query).find()) {
                String fragments[] = this.query.split(Matchers.create_database.pattern());
                if(fragments[1].contains(" "))
                    {
                        System.err.println("Invalid Query : "+ this.query);
                        return false;  
                    }
                else{
                    try{
                        this.currentDB = DB.createDB(fragments[1].trim());
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                        return false;
                    }
                }
            }
            else{
                System.err.println("No Database Selected");
                return false;
            }
        }
        else if(Matchers.use_database.matcher(this.query).find()){
            String fragments[] = this.query.split(Matchers.use_database.pattern());
                if(fragments[1].contains(" "))
                    {
                        System.err.println("Invalid Query : "+ this.query);
                        return false;  
                    }
                else{
                    try{
                        this.currentDB = DB.getDB(fragments[1].trim());
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                        return false;
                    }
                }
        }
        else if(Matchers.create_table.matcher(this.query).find()){
            String fragments[] = this.query.split(Matchers.create_table.pattern());
            if(! fragments[1].contains("(")|| (fragments[1].contains("(") && !fragments[1].contains(")")))
            {
                System.err.println("Malformed Query : "+ this.query);
                return false;  
            }
            else{
                String table = this.query.split("^[ ]?(?i)create table ")[1].split("\\(")[0].trim();
                if(! table.matches("[\\w]+")){
                    System.err.println("Table Name Can not contain space and punctuation charachters");
                    return false;
                }
                fragments = fragments[1].split("\\(")[1].split("\\)")[0].split("([\\w ]),(?=[\\w ])");
                LinkedHashMap<String,String> columns = new LinkedHashMap<>();
                for(String column : fragments){
                    String []name_type = column.split(" ");
                    columns.put(name_type[0],name_type[1]);
                }
                return currentDB.createTable(table,columns);
            }
        }
        else if(Matchers.insert_into_table.matcher(this.query).find()){
            String table = this.query.split("^[ ]?(?i)insert into ")[1].split("[\\w ]values.*")[0].trim();
            String []values = this.query.split("^[ ]?(?i)insert into ")[1].split("\\(")[1].split("\\)")[0].split(",");
            try{
                return this.currentDB.getTable(table).addRow(Arrays.asList(values));
            }catch (Exception e){
                System.err.println(e.getMessage());
                return false;
            }
        }
        else if(Matchers.select_from_table.matcher(this.query).find()){
            String []selection_and_filters = this.query.split("(?i)where");
            String []columns_and_table = selection_and_filters[0].split("from");
            String []filters =  new String[0];
            if(selection_and_filters.length==2)
                filters = selection_and_filters[1].split("(?i)or");
            String table = columns_and_table[1].trim();
            Set<String> columns = new HashSet<>();
            if(columns_and_table[0].contains("*"))
                columns.add("*");
            else
                columns = Stream.of(columns_and_table[0].split("(?i)select ")[1].split(",")).map(col -> col.trim()).collect(Collectors.toSet());
            List<DBFilter> dbFilters = new ArrayList<>();
            for(String filter:filters){
                try{
                dbFilters.add(new DBFilter(filter));
                }catch (Exception e){
                    System.err.println(e.getMessage());
                    return false;
                }
            }
            try{
                if(columns.contains("*"))
                    columns = this.currentDB.getTable(table).columns.keySet();
                List<Row> result = this.currentDB.getTable(table).getRows(columns, dbFilters);
            
                for(String column : columns){
                    System.out.print(column+"\t");
                }
                System.out.println();
                for(Row row:result){
                    for(String column : columns)
                        System.out.print(row.values.get(column)+"\t");
                    System.out.println();   
                }
                return true;
            }catch (Exception e){
                System.err.println(e.getMessage());
                return false;
            }
        }
        else{
            System.err.println("Query not supported !");
            return false;
        }
        return false;
    }
}
