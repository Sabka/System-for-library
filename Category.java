package RDG;

/**
 *
 * @author sabinka
 */

public class Category 
{
    private int id;
    private String catName;
    private int period;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    
    
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", catName=" + catName + ", period=" + period + '}';
    }
}
