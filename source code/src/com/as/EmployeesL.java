package com.as;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeesL extends Employees implements Serializable {
    List<Employees> arrayList=new ArrayList<>();
    void add(Employees a)
    {
        arrayList.add(a);
    }
    Employees get(int idw)
    {
        return arrayList.get(idw);
    }
}
