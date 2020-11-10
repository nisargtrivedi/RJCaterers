package com.app.posapp.model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.util.Date;

@Table(name = "tbl_cart")
public class tbl_cart extends Entity {

    @TableField(name = "item_name",datatype = DATATYPE_STRING)
    public String ItemName="";
    @TableField(name = "item_qty",datatype = DATATYPE_INTEGER)
    public int ItemQty=0;
    @TableField(name = "item_price",datatype = DATATYPE_DOUBLE)
    public double ItemPrice=0;
    @TableField(name = "item_unit",datatype = DATATYPE_STRING)
    public String ItemUnit="";
    @TableField(name = "sell_date",datatype = DATATYPE_STRING)
    public String sellDate;
}
