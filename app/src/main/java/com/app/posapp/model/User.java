package com.app.posapp.model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

@Table(name = "tbl_users")
public class User extends Entity {
    @TableField(name = "firstname",datatype = DATATYPE_STRING)
    public String FirstName;

    @TableField(name = "lastname",datatype = DATATYPE_STRING)
    public String LastName;

    @TableField(name = "password",datatype = DATATYPE_STRING)
    public String Password;

    @TableField(name = "shopname",datatype = DATATYPE_STRING)
    public String ShopName;

    @TableField(name = "shopaddress",datatype = DATATYPE_STRING)
    public String Address;

    @TableField(name = "mobile",datatype = DATATYPE_STRING)
    public String Mobile;
}
