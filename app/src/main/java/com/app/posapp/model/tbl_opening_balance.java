package com.app.posapp.model;

import com.app.posapp.R;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.androidannotations.annotations.EActivity;

@Table(name = "tbl_opening_balance")
public class tbl_opening_balance extends Entity {

    @TableField(name = "amount",datatype = DATATYPE_DOUBLE)
    public double OpeningBalance=0;
}
