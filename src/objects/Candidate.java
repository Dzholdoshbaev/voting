package objects;

public class Candidate {
    private String candidate_id;
    private String name;
    private String photo;
    private int totalVotes;
    private int percent;

    public Candidate(String name, String candidate_id, int totalVotes, String photo, int percent) {
        this.name = name;
        this.candidate_id = candidate_id;
        this.totalVotes = totalVotes;
        this.photo = photo;
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public String getCandidate_id() {
        return candidate_id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}
