package api;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Объявление
 */
public class Advert{

    private int id;
    private String title;
    private long date;
    private float sum;
    private String url;
    private Project project;
    private String desc;
    private String img;

    public Advert(int id, String title, long date, float sum, String url, String desc, String img, Project project) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.sum = sum;
        this.url = url;
        this.desc = desc;
        this.project = project;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Advert advert = (Advert) o;

        if (date != advert.date){
            return false;
        }
        if (Float.compare(advert.sum, sum) != 0){
            return false;
        }
        if (title != null ? !title.equals(advert.title) : advert.title != null){
            return false;
        }
        if (!url.equals(advert.url)){
            return false;
        }
        return desc != null ? desc.equals(advert.desc) : advert.desc == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (int) (date ^ (date >>> 32));
        result = 31 * result + (sum != +0.0f ? Float.floatToIntBits(sum) : 0);
        result = 31 * result + url.hashCode();
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return title +"\n" +sum+" руб.";
    }

    
}
