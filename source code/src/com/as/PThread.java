package com.as;

public abstract class PThread extends Thread{
    private int id1;
    private int id2;
    private int kwota1;
    private int kwota2;
    private int j=0;
    private boolean stopped=false;

    protected PThread(int id1, int id2,int kwota1, int kwota2) {
        this.id1 = id1;
        this.id2 = id2;
        this.kwota1=kwota1;
        this.kwota2=kwota2;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted() && !this.isStopped()) {
                execute(this.id1,this.id2,this.kwota1,this.kwota2);
                Thread.sleep(10);
            }
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    public abstract void execute(int id1, int id2, int kwota1, int kwota2);

    public void setKwota1(int kwota1) {
        this.kwota1 = kwota1;
    }

    public void setKwota2(int kwota2) {
        this.kwota2 = kwota2;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getJ() {
        return j;
    }
}
