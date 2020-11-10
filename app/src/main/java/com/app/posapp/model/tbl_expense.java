package com.app.posapp.model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

@Table(name = "tbl_expense")
public class tbl_expense extends Entity {

    @TableField(name = "e_date",datatype = DATATYPE_STRING)
    public String ExpenseDate="";

    @TableField(name = "ExpenseName",datatype = DATATYPE_STRING)
    public String ExpenseName="";

    @TableField(name = "e_amount",datatype = DATATYPE_DOUBLE)
    public double ExpenseAmount=0;


}
