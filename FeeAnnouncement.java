/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sabinka
 */
public class FeeAnnouncement 
{
    private int readerId;    
    private int copyId;
    private int rentalId;
    private int amount;

    public FeeAnnouncement(int readerId, int copyId, int rentalId, int amount) {
        this.readerId = readerId;
        this.copyId = copyId;
        this.rentalId = rentalId;
        this.amount = amount;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FeeAnnouncement for reader" + readerId + ", copy" + copyId + " hadnt been returned in time. You have new unpayed fee:" + amount + "euros";
    }

    
    
    
    
    
    
}
