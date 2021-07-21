package com.as;

class Player{
    private String name;
    private int pieniadze;
    private boolean zablokowany;
    private int id;
    Player(String name1,int pieniadze1,int id1){
        name=name1;
        pieniadze=pieniadze1;
        zablokowany=false;
        id=id1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPieniadze() {
        return pieniadze;
    }
    public boolean getZablokowany()
    {
        return zablokowany;
    }

    public void setPieniadze(int pieniadze) {
        this.pieniadze = pieniadze;
    }

    public void setZablokowany(boolean zablokowany) {
        this.zablokowany = zablokowany;
    }
}