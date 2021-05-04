/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RDG;

/**
 *
 * @author sabinka
 */
public class Author
    {
        private int id;

        public Author(int id) {
            this.id = id;
        } 

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        
    @Override
    public String toString() {
        return "Author{" + "id=" + id + '}';
    }
        
        
        
    }