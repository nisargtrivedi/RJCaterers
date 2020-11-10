package com.app.posapp.model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

@Table(name = "tbl_item")
public class Items extends Entity implements Serializable {

    @TableField(name = "itemname",datatype = DATATYPE_STRING)
    public String ItemName="";

    @TableField(name = "price",datatype = DATATYPE_DOUBLE)
    public double ItemPrice=0;

    @TableField(name = "unit",datatype = DATATYPE_STRING)
    public String Unit="";


    public Items(String itemName, double itemPrice, String unit) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        Unit = unit;
    }
    public Items(){

    }
}
