/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sabinka
 */
class Stats2Row 
{
    int x;
    int numFees;
    double amount;

    public Stats2Row(int x, int numFees, double amount) 
    {
        this.x = x;
        this.numFees = numFees;
        this.amount = amount;
    }

    Stats2Row() {}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getNumFees() {
        return numFees;
    }

    public void setNumFees(int numFees) {
        this.numFees = numFees;
    }
   

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "added days=" + x + ", number of fees=" + numFees + ", amount=" + amount;
    }
    
    
    
    
    
    
    
}
