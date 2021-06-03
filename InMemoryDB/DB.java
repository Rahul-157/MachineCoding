package InMemoryDB;

import java.util.*;


class Row{
    UUID id;
    HashMap<String,Object> values;
    Row(UUID id){
        this.id = id;
        this.values = new HashMap<>();
    }

    Row(LinkedHashMap<String,Class> validator, List<String> values) throws Exception{
        if(validator.size() != values.size())
            throw new Exception("All column values not passed");
        this.id = UUID.randomUUID();
        this.values = new HashMap<>();
        _validateValues(validator, values);
    }

    private void _validateValues(LinkedHashMap<String,Class> validator, List<String> values) throws Exception{
        int i=0;
        for(String key : validator.keySet()){
            if(validator.get(key) == Integer.class){
                try{
                    Integer parsed_value = Integer.parseInt(values.get(i).trim());
                    this.values.put(key,parsed_value);
                } catch (NumberFormatException e){
                    throw new Exception("Datatype Mismatch for key : "+ key);
                }
            }
            else    
                this.values.put(key,values.get(i).trim());
            i++;
        }
    }
}


class Table{
    String name;
    LinkedHashMap<String,String> columns;
    LinkedHashMap<String ,Class> validator;
    TreeMap<UUID,Row> rows;
    Table(String name,LinkedHashMap<String,String> columns) throws Exception{
        this.columns = columns;
        this.name = name;
        this.validator = new LinkedHashMap<>();
        this.rows = new TreeMap<>();
        for(String key : columns.keySet()){
            String type = columns.get(key);
            if(type.equals("int"))
                this.validator.put(key, Integer.class);
            else if(type.equals("string"))
                this.validator.put(key, String.class);
            else
                throw new Exception("Invalid datatype for Key "+ key);
        }
    }

    public boolean addRow(List<String> values){
        try{
        Row new_row = new Row(this.validator,values);
        this.rows.put(new_row.id,new_row);
        return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Row _removeExtraColumns(Row row,Set<String> columns){
        Row new_row = new Row(row.id);
        for(String column :columns){
            new_row.values.put(column, row.values.get(column));
        }
        return new_row;
    }

    private List<Row> _filterRows(List<Row> input,DBFilter filter,Set<String> columns) throws Exception{
        List<Row> result = new ArrayList<>();
        for(Row row : input){
            String column_to_check = filter.columne_name; 
            Class column_type = row.values.get(column_to_check).getClass();           
            if(column_type == this.validator.get(column_to_check)){
                if(column_type == String.class){
                    String col_value = (String)row.values.get(column_to_check);
                    if(filter.operator.equals("=") && col_value.equals(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator.equals("!=") && ! col_value.equals(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } 
                }
                else if(column_type == Integer.class){
                    Integer col_value = (Integer)row.values.get(column_to_check);
                    if(filter.operator=="<" && col_value < Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator==">" && col_value > Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator=="<=" && col_value <= Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator==">=" && col_value >= Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator=="=" && col_value == Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    } else if(filter.operator=="!=" && col_value != Integer.valueOf(filter.value)){
                        result.add(_removeExtraColumns(row,columns));
                    }
                }
            }
            else throw new Exception("Invalid Datatype for filtering on column "+column_to_check);
        }
        return result;
    }

    public List<Row> getRows(Set<String> columns,List<DBFilter> filters){
        List<Row> result = new ArrayList<Row>();
        boolean flag = false;
        for(DBFilter filter:filters){ //Fetch rows if there is any id based filter there : Reduced Searching time
            if(filter.isAggregate){
                for(DBFilter a_filter : filter.filters){
                    if(a_filter.columne_name=="id"){
                        result.add(this.getRowById(columns, a_filter.value));
                        flag=true;
                    }
                }
            } else{
                if(filter.columne_name=="id"){
                    flag = true;
                    result.add(this.getRowById(columns, filter.value));
                }
            }
        }
        if(!flag) result = new ArrayList<>(this.rows.values());

        for(DBFilter filter:filters){
            if(filter.isAggregate){
                for(DBFilter a_filter : filter.filters){
                    try{
                        result = _filterRows(result, a_filter,columns);
                    }catch (Exception e){
                        System.err.println(e.getMessage());
                    }
                }
                    
            }
            else{
                try{
                    result = _filterRows(result, filter,columns);
                }catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        }
        return result;
    }

    public Row getRowById(Set<String> columns,String id){
        Row data =  this.rows.get(UUID.fromString(id));
        if(columns.contains("*"))
            return data;
        
        return _removeExtraColumns(data, columns);

    }

    @Override
    public String toString(){
        return this.name;
    }

}

class DB{
    String name;
    private HashMap<String,Table> tables;
    static HashMap<String,DB> databases = new HashMap<String,DB>();

    private DB(String name){
        this.name = name;
        this.tables = new HashMap<>();
    }

    static public DB getDB(String name) throws Exception{
        if(databases.containsKey(name))
            return databases.get(name);
        throw new Exception("DB "+ name+ " not found");
    }

    synchronized static public DB createDB(String name) throws Exception{
        if(databases.containsKey(name))
            throw new Exception("DB "+ name+ " already exists");
        DB new_db = new DB(name);
        DB.databases.put(name,new_db);
        return new_db;
    }

    public Set<String> getTableNames(){
        return this.tables.keySet();
    }

    public Table getTable(String name) throws Exception{
        if(this.tables.containsKey(name))
            return this.tables.get(name);
        throw new Exception("Table does not exist");
    }
    public boolean createTable(String name,LinkedHashMap<String,String> columns){
        try{
            Table new_table = new Table(name,columns);
            this.tables.put(name,new_table);
            return true;
        } catch( Exception e){
            System.err.println(e.getMessage());
            return false;
        }
    }


    @Override
    public String toString(){
        return this.name;
    }
}