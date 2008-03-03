/*
 * Copyright 2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.pms.mql.dialect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SQLQueryModel is a generic model structure used to generate a SQL Select Statement.
 * The actual rendering of the SQL Select Statement is done by the various dialects.
 *
 * @author Will Gorman (wgorman@pentaho.org)
 */
public class SQLQueryModel {
  
    /**
     * defines the type of joins availabe in a select statement 
     */
    public enum JoinType { JOIN, LEFT_OUTER_JOIN, RIGHT_OUTER_JOIN };

    /**
     * defines the type of ordering available in a select statement
     */
    public enum OrderType { ASCENDING, DESCENDING };
    
    /**
     * inner class that defines the SELECT portion of an SQL query
     */
    public static class SQLSelection {
      
      private String formula;
      private String alias;
      
      public SQLSelection(String formula, String alias) {
        this.formula = formula;
        this.alias = alias;
      }
      
      /**
       * return the column or formula for the selection
       * note that these should already be in dialect specific
       * form.
       * 
       * @return formula
       */
      public String getFormula() {
        return formula;
      }

      /**
       * return the alias of the selection
       * 
       * @return alias
       */
      public String getAlias() {
        return alias;
      }
    }
    
    /**
     * inner class that defines the FROM portion of a SQL query
     * 
     * note that this is a basic version, eventually add support
     * for syntax such as A JOIN B ON C.
     */
    public static class SQLTable {
      private String tableName;
      private String alias;
      
      public SQLTable(String tableName, String alias) {
        this.tableName = tableName;
        this.alias = alias;
      }
      
      /**
       * returns the table name of the sql table
       * 
       * @return table name
       */
      public String getTableName() {
        return tableName;
      }
      
      /**
       * returns the alias of the sql table
       * @return
       */
      public String getAlias() {
        return alias;
      }
    }
    
    /**
     * inner class that defines the WHERE portion of a SQL query
     */ 
    public static class SQLWhereFormula {
      String formula;
      String operator;
      
      public SQLWhereFormula(String formula, String operator) {
        this.formula = formula;
        this.operator = operator;
        if (this.operator == null) {
          this.operator = "AND";
        }
      }
      
      /**
       * return the where formula. note that these should already be in dialect
       * specific form. 
       * 
       * @return formula
       */
      public String getFormula() {
        return formula;
      }
      
      /**
       * return the operator, used to combine where formulas
       * @return operator
       */
      public String getOperator() {
        return operator;
      }
    }

    /**
     * inner class that defines the ORDER BY portion of a SQL query
     */ 
    public static class SQLOrderBy {
      SQLSelection selection;
      OrderType order;
      
      public SQLOrderBy(SQLSelection selection, OrderType order) {
        this.selection = selection;
        this.order = order;
      }
      
      /**
       * the selection to order
       * 
       * @return selection
       */
      public SQLSelection getSelection() {
        return selection;
      }
      
      /**
       * the type of ordering
       * 
       * @return order
       */
      public OrderType getOrder() {
        return order;  
      }
    }
    
    private boolean distinct = true;
    private List<SQLSelection> selections = new ArrayList<SQLSelection>();
    private List<SQLSelection> ulSelections = Collections.unmodifiableList(selections);

    private List<SQLTable> tables = new ArrayList<SQLTable>();
    private List<SQLTable> ulTables = Collections.unmodifiableList(tables);
    
    private List<SQLWhereFormula> whereFormulas = new ArrayList<SQLWhereFormula>();
    private List<SQLWhereFormula> ulWhereFormulas = Collections.unmodifiableList(whereFormulas);
    
    private List<SQLSelection> groupbys = new ArrayList<SQLSelection>();
    private List<SQLSelection> ulGroupbys = Collections.unmodifiableList(groupbys);
    
    private List<SQLWhereFormula> havings = new ArrayList<SQLWhereFormula>();
    private List<SQLWhereFormula> ulHavings = Collections.unmodifiableList(havings);
    
    private List<SQLOrderBy> orderbys = new ArrayList<SQLOrderBy>();
    private List<SQLOrderBy> ulOrderbys = Collections.unmodifiableList(orderbys);
    
    /**
     * true if DISTINCT should appear at the beginning of the select statement
     * 
     * @return distinct
     */
    public boolean getDistinct() {
      return distinct;
    }
    
    /**
     * sets whether or not the DISTINCT keyword should appear at the beginning
     * of the select statement
     * 
     * @param distinct
     */
    public void setDistinct(boolean distinct) {
      this.distinct = distinct;
    }
    
    /**
     * returns an uneditable list of selections
     * 
     * @return selections
     */
    public List<SQLSelection> getSelections() {
      return ulSelections;
    }
    
    /**
     * adds a selection to the select statement. Note that the formula should
     * already be in dialect specific form.
     * 
     * @param formula a database column or formula
     * @param alias the alias of the selection
     */
    public void addSelection(String formula, String alias) {
      selections.add(new SQLSelection(formula, alias));
    }
    
    /**
     * returns an uneditable list of tables
     *  
     * @return tables
     */
    public List<SQLTable> getTables() {
      return ulTables;
    }

    /**
     * adds a table to the select statement
     *  
     * @param tableName the table name
     * @param alias the table alias to use
     */
    public void addTable(String tableName, String alias) {
      tables.add(new SQLTable(tableName, alias));
    }
    
    /**
     * returns an uneditable list of where formulas
     * 
     * @return where formulas
     */
    public List<SQLWhereFormula> getWhereFormulas() {
      return ulWhereFormulas;
    }

    /**
     * adds a where formula to the select statement. Note that the formula 
     * should already be in dialect specific form.
     * 
     * @param formula where formula
     * @param operation operator that combines where formulas
     */
    public void addWhereFormula(String formula, String operation) {
      whereFormulas.add(new SQLWhereFormula(formula, operation));
    }

    /**
     * returns an uneditable list of group bys
     * 
     * @return group bys
     */
    public List<SQLSelection> getGroupBys() {
      return ulGroupbys;
    }

    /**
     * adds a group by to the select statement. note that the formula 
     * should already be in dialect specific form.
     * 
     * @param formula the group by formula
     * @param alias the optional group by alias
     */
    public void addGroupBy(String formula, String alias) {
      groupbys.add(new SQLSelection(formula, alias));
    }
    
    /**
     * returns an uneditable list of having formulas
     * 
     * @return havings
     */
    public List<SQLWhereFormula> getHavings() {
      return ulHavings;
    }
 
    /**
     * adds a having formula to the select statement
     * 
     * @param formula the having formula
     * 
     * @param operation the operation to combine having formulas
     */
    public void addHavingFormula(String formula, String operation) {
      havings.add(new SQLWhereFormula(formula, operation));
    }
    
    /**
     * returns an uneditable list of order by statements
     * 
     * @return order bys
     */
    public List<SQLOrderBy> getOrderBys() {
      return ulOrderbys;
    }
   
    /**
     * adds an order by formula to the select statement. Note that the formula
     * should already be in dialect specific form.
     * 
     * @param formula formula for order by
     * @param alias alias for order by
     * @param order the order string 
     */
    public void addOrderBy(String formula, String alias, OrderType order) {
      orderbys.add(new SQLOrderBy(new SQLSelection(formula, alias), order));
    }
}
