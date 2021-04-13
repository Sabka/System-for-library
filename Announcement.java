/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sabinka
 */
public class Announcement 
{
    private int readerId;
    private int copyId;

    public Announcement(int readerId, int copyId) {
        this.readerId = readerId;
        this.copyId = copyId;
    }
    
    public Announcement() {}
    
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

    @Override
    public String toString() {
        return "[[Announcement for reader " + getReaderId() + ": Copy with id " + getCopyId() + " has been delivered to library.]]";
    }
    
    
    
}
