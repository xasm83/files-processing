package api;

import java.math.BigDecimal;

public class Site {
    private String id;
    private String name;
    private boolean mobile;
    private BigDecimal score;
    private String keywords;

    public Site() {
    }

    public Site(String id, String name, boolean mobile, BigDecimal score) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (mobile != site.mobile) return false;
        if (id != null ? !id.equals(site.id) : site.id != null) return false;
        if (name != null ? !name.equals(site.name) : site.name != null) return false;
        if (score != null ? !score.equals(site.score) : site.score != null) return false;
        return keywords != null ? keywords.equals(site.keywords) : site.keywords == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mobile ? 1 : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Site{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile=" + mobile +
                ", score=" + score +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
